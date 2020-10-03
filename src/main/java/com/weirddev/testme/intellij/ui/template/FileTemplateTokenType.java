
package com.weirddev.testme.intellij.ui.template;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;

interface FileTemplateTokenType {
  IElementType ESCAPE = new IElementType("ESCAPE", Language.ANY);
  IElementType TEXT = new IElementType("TEXT", Language.ANY);
  IElementType MACRO = new IElementType("MACRO", Language.ANY);
  IElementType DIRECTIVE = new IElementType("DIRECTIVE", Language.ANY);
}