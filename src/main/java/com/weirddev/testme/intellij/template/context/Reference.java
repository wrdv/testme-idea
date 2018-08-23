package com.weirddev.testme.intellij.template.context;

import com.intellij.psi.PsiType;
import com.weirddev.testme.intellij.template.TypeDictionary;

/**
 * Date: 06/05/2017
 *
 * @author Yaron Yamin
 *
 * A referene to a non method langauge construct (i.e. field/variable)
 */
public class Reference {
    private final String referenceName;
    private final Type referenceType;
    private final Type ownerType;
    private final String referenceId;

    public Reference(String referenceName, PsiType refType, PsiType psiOwnerType, TypeDictionary typeDictionary) {
        this.referenceName = referenceName;
        referenceType = new Type(refType, null, typeDictionary, 1, false);
        ownerType = new Type(psiOwnerType, null, typeDictionary, 1, false);
        referenceId = ownerType.getCanonicalName() + referenceName + referenceType.getCanonicalName();
    }

    public String getReferenceName() {
        return referenceName;
    }

    public Type getReferenceType() {
        return referenceType;
    }

    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reference)) return false;

        Reference reference = (Reference) o;

        return referenceId.equals(reference.referenceId);
    }

    @Override
    public int hashCode() {
        return referenceId.hashCode();
    }

    @Override
    public String toString() {
        return "Reference{" +
                "referenceName='" + referenceName + '\'' +
                ", referenceType=" + referenceType +
                ", ownerType=" + ownerType +
                '}';
    }
}
