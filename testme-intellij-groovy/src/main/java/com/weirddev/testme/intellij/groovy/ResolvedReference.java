package com.weirddev.testme.intellij.groovy;

import com.intellij.psi.PsiType;

/**
 * Date: 10/05/2017
 *
 * @author Yaron Yamin
 */
public class ResolvedReference { //todo move to a common module
    private final String referenceName;
    private final PsiType refType;
    private final PsiType psiOwnerType;

    public ResolvedReference(String referenceName, PsiType refType, PsiType psiOwnerType) {

        this.referenceName = referenceName;
        this.refType = refType;
        this.psiOwnerType = psiOwnerType;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public PsiType getRefType() {
        return refType;
    }

    public PsiType getPsiOwnerType() {
        return psiOwnerType;
    }
}
