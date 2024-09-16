package com.weirddev.testme.intellij.common.utils

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiParameterList
import com.intellij.psi.PsiType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.mockito.Mockito.*

class PsiMethodUtilsTest {
    @Mock
    private PsiMethod psiMethod
    @Mock
    private PsiParameter psiParameter
    @Mock
    private PsiType psiType
    @Mock
    private PsiParameterList psiParameterList
    @Mock
    private PsiClass psiClass

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    void testFormatMethodId() {
        when(psiParameter.getType()).thenReturn(psiType)
        when(psiType.getCanonicalText()).thenReturn("my.Type")
        when(psiClass.getQualifiedName()).thenReturn("my.Class")
        when(psiMethod.getParameterList()).thenReturn(psiParameterList)
        when(psiMethod.getContainingClass()).thenReturn(psiClass)
        when(psiParameterList.getParameters()).thenReturn(new PsiParameter[]{psiParameter})
        when(psiMethod.getName()).thenReturn("methodName")

        assert PsiMethodUtils.formatMethodId(psiMethod).equals("my.Class#methodName(my.Type)")
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme