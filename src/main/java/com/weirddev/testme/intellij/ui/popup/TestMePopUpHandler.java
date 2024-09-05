
package com.weirddev.testme.intellij.ui.popup;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.ide.util.EditSourceUtil;
import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNamedElement;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.popup.HintUpdateSupply;
import com.weirddev.testme.intellij.icon.IconTokensReplacerImpl;
import com.weirddev.testme.intellij.icon.TemplateNameFormatter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class TestMePopUpHandler implements CodeInsightActionHandler {
  private static final PsiElementListCellRenderer<PsiElement> ourDefaultTargetElementRenderer = new DefaultPsiElementListCellRenderer();
  private final DefaultListCellRenderer myActionElementRenderer = new TestMeActionCellRenderer(new TemplateNameFormatter(), new IconTokensReplacerImpl()); // todo DI

  @Override
  public boolean startInWriteAction() {
    return false;
  }

  /**
   * invoke create test for missed selected method only
   * @param project the project where action is invoked.
   * @param editor  the editor where action is invoked.
   * @param selectMethod    none null if create test only for select method
   */
  public void createSelectMethodTest(@NotNull Project project, @NotNull Editor editor, @NotNull PsiMethod selectMethod) {
    invoke(project, editor, selectMethod.getContainingFile(), selectMethod);
  }

  /**
   * invoke create test for class file
   * @param project the project where action is invoked.
   * @param editor  the editor where action is invoked.
   * @param file    the file open in the editor.
   */
  @Override
  public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
    invoke(project, editor, file, null);
  }

  /**
   *
   * @param project the project where action is invoked.
   * @param editor  the editor where action is invoked.
   * @param file    the file open in the editor.
   * @param selectMethod    none null if create test only for select method
   */
  public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file, PsiMethod selectMethod) {
//    FeatureUsageTracker.FileTemplateConfig().triggerFeatureUsed(getFeatureUsedKey()); //todo register a ProductivityFeaturesProvider extension

    try {
      GotoData gotoData = getSourceAndTargetElements(editor, file, selectMethod);
      if (gotoData != null) {
        show(project, editor, file, gotoData);
      }
    }
    catch (IndexNotReadyException e) {
      DumbService.getInstance(project).showDumbModeNotification("Test Generation is not available here during index update");
    }
  }

  @NonNls
  protected abstract String getFeatureUsedKey();

  @Nullable
  protected abstract GotoData getSourceAndTargetElements(Editor editor, PsiFile file, PsiMethod selectMethod);

  private void show(@NotNull final Project project,
                    @NotNull Editor editor,
                    @NotNull PsiFile file,
                    @NotNull final GotoData gotoData) {
    final List<AdditionalAction> additionalActions = gotoData.additionalActions;

    if (additionalActions.isEmpty()) {
      HintManager.getInstance().showErrorHint(editor, getNotFoundMessage(project, editor, file));
      return;
    }
    final String title = getChooserTitle(editor, file, gotoData.source);
    final JBList<Object> list = new JBList<>(new CollectionListModel<>(additionalActions));
    HintUpdateSupply.installHintUpdateSupply(list, o -> o instanceof PsiElement ? (PsiElement) o : null);//todo - consider removing. probably not needed

    list.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof AdditionalAction) {
          return myActionElementRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
        PsiElementListCellRenderer<?> renderer = getRenderer(value, gotoData.renderers, gotoData);
        return renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      }
    });

    final Runnable runnable = () -> {
      int[] ids = list.getSelectedIndices();
      if (ids == null || ids.length == 0) return;
      List<?> selectedElements = list.getSelectedValuesList();
      for (Object element : selectedElements) {
        if (element instanceof AdditionalAction) {
          ((AdditionalAction)element).execute(editor.getProject());
        }
        else {
          Navigatable nav = element instanceof Navigatable ? (Navigatable)element : EditSourceUtil.getDescriptor((PsiElement)element);
          try {
            if (nav != null && nav.canNavigate()) {
              navigateToElement(nav);
            }
          }
          catch (IndexNotReadyException e) {
            DumbService.getInstance(project).showDumbModeNotification("Test Generation is not available while indexing");
          }
        }
      }
    };

    final PopupChooserBuilder<Object> builder = new PopupChooserBuilder<>(list);
    builder.setFilteringEnabled(o -> {
      if (o instanceof AdditionalAction) {
        return ((AdditionalAction)o).getText();
      }
      return getRenderer(o, gotoData.renderers, gotoData).getElementText((PsiElement)o);
    });
    final JBPopup popup = builder.
      setTitle(title).
      setItemChoosenCallback(runnable).
      setMovable(true).
      setCancelCallback(() -> {
        HintUpdateSupply.hideHint(list);
        return true;
      }).
      setAdText(getAdText(gotoData.source, 0)).
      createPopup();
//    final Ref<UsageView> usageView = new Ref<>();
//    if (gotoData.listUpdaterTask != null) {
//      gotoData.listUpdaterTask.init((AbstractPopup)popup, list, usageView);
//      ProgressManager.getInstance().run(gotoData.listUpdaterTask);
//    }

    if (ApplicationManager.getApplication().isHeadlessEnvironment()) {
       //for UT support - otherwise there's a swing error when popup set relative to fake test editor
      popup.showCenteredInCurrentWindow(project);
    }else{
      popup.showInBestPositionFor(editor);
    }
  }

  private static PsiElementListCellRenderer<PsiElement> getRenderer(Object value,
                                                        Map<Object, PsiElementListCellRenderer<PsiElement>> targetsWithRenderers,
                                                        GotoData gotoData) {
    PsiElementListCellRenderer<PsiElement> renderer = targetsWithRenderers.get(value);
    if (renderer == null) {
      renderer = gotoData.getRenderer(value);
    }
    return Objects.requireNonNullElse(renderer, ourDefaultTargetElementRenderer);
  }

  protected void navigateToElement(Navigatable descriptor) {
    descriptor.navigate(true);
  }

  @NotNull
  protected abstract String getChooserTitle(Editor editor, PsiFile file, PsiElement sourceElement);

  @NotNull
  protected abstract String getNotFoundMessage(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file);

  @Nullable
  protected String getAdText(PsiElement source, int length) {
    return null;
  }

  public interface AdditionalAction {
    @NotNull
    String getText();

    Icon getIcon();

    void execute(Project project);
  }

  public static class GotoData {
    @NotNull public final PsiElement source;
    public final List<AdditionalAction> additionalActions;

//    public ListBackgroundUpdaterTask listUpdaterTask; //todo un-used. check relevancy for future features
    public Map<Object, PsiElementListCellRenderer<PsiElement>> renderers = new HashMap<>();//todo un-used. consider using these renders instead of ourDefaultTargetElementRenderer = new DefaultPsiElementListCellRenderer()

    public GotoData(@NotNull PsiElement source, @NotNull List<AdditionalAction> additionalActions) {
      this.source = source;
      this.additionalActions = additionalActions;
    }

    public PsiElementListCellRenderer<PsiElement> getRenderer(Object value) {
      return renderers.get(value);
    }
  }

  private static class DefaultPsiElementListCellRenderer extends PsiElementListCellRenderer<PsiElement> {
    @Override
    public String getElementText(final PsiElement element) {
      if (element instanceof PsiNamedElement) {
        String name = ((PsiNamedElement)element).getName();
        if (name != null) {
          return name;
        }
      }
      return element.getContainingFile().getName();
    }

    @Override
    protected String getContainerText(final PsiElement element, final String name) {
      if (element instanceof NavigationItem) {
        final ItemPresentation presentation = ((NavigationItem)element).getPresentation();
        return presentation != null ? presentation.getLocationString():null;
      }

      return null;
    }

    @Override
    protected int getIconFlags() {
      return 0;
    }
  }
}
