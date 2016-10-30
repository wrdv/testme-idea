package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
<caret>
public class Foo{

    private FooFighter fooFighter;

    protected FooFighter fooFighterProtected;
    FooFighter fooFighterDefault;
    public FooFighter fooFighterPublic;
    final FooFighter fooFighterFinal=new FooFighterImpl();
    private static FooFighter fooFighterStatic=new FooFighterImpl();

    byte byteField=2;
    short shortField;
    int intField=1;
    long longField=1111111;
    float floatField=1.1f;
    double doubleField=1.2;
    char charField;
    boolean booleanField=true;

    Byte byteFieldWrapper=2;
    Short shortFieldWrapper;
    Integer intFieldWrapper=1;
    Long longFieldWrapper=1111111l;
    Float floatFieldWrapper=1.1f;
    Double doubleFieldWrapper=1.2;
    Character charFieldWrapper;
    Boolean booleanFieldWrapper=true;

    PublicInnerClass publicInnerClass;
    InnerStaticClass innerStaticClass;
    PublicInnerClass.InnerOfPublicInnerClass innerOfPublicInnerClass;
    InnerClass innerClass;

    public class PublicInnerClass {
        public class InnerOfPublicInnerClass {
            public void methodOfInnerClass() {
            }
        }
        public void methodOfInnerClass(){
        }
    }
    class InnerClass{
        class InnerOfInnerClass {
            public void methodOfInnerClass() {
            }
        }
        public void methodOfInnerClass(){
        }
    }

    PublicInnerClass anonymousPublicInnerClass =new PublicInnerClass(){
        public void methodOfInnerClass(){}
    };

    public static class InnerStaticClass{
        public void methodOfInnerClass(){

        }
    }

    public String fight(Fire withFire,String foeName) {
        System.out.println("Running and using primitives:"+byteField+shortField+intField+longField+floatField+doubleField+charField+booleanField);
        System.out.println("Running and using primitive Wrappers:" + byteFieldWrapper + shortFieldWrapper + intFieldWrapper + longFieldWrapper + floatFieldWrapper + doubleFieldWrapper + charFieldWrapper + booleanFieldWrapper);
        publicInnerClass.methodOfInnerClass();
        anonymousPublicInnerClass.methodOfInnerClass();
        innerStaticClass.methodOfInnerClass();
        innerClass.methodOfInnerClass();
        innerOfPublicInnerClass.methodOfInnerClass();
        fooFighterDefault.fight(withFire);
        fooFighterFinal.fight(withFire);
        fooFighterProtected.fight(withFire);
        fooFighterPublic.fight(withFire);
        fooFighterStatic.fight(withFire);

        return fooFighter.fight(withFire);
    }

}
