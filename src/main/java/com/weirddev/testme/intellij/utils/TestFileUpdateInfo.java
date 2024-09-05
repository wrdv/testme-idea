package com.weirddev.testme.intellij.utils;

import com.intellij.psi.PsiElement;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * info to update exist test file
 *
 * @author huangliang
 */
@Data
@AllArgsConstructor
public class TestFileUpdateInfo {

    /**
     * parent element of add element
     */
    private PsiElement parentElement;

    /**
     * element add to exist test file
     */
    private PsiElement addElement;

    /**
     * content type of add element; import, field , method
     */
    private String contentType;

}
