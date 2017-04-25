package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
import com.example.warriers.impl.FooFighterImpl;

public class Foo{

    private FooFighter fooFighter;

    protected FooFighter fooFighterProtected;


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
        String propOfInner;

        public InnerClass(String propOfInner) {
            this.propOfInner = propOfInner;
        }
        class InnerOfInnerClass {
            String propOfInnerOfInner;

            public InnerOfInnerClass(String propOfInnerOfInner) {
                this.propOfInnerOfInner = propOfInnerOfInner;
            }

            public void methodOfInnerClass() {<caret>
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
    public static class InnerStaticClassWithMember{
        FooFighter innerFooFighter;
        public String methodOfInnerClass(Fire fire) {
            return innerFooFighter.fight(fire);
        }
    }

    public String fight(Fire withFire,String foeName) {
        publicInnerClass.methodOfInnerClass();
        anonymousPublicInnerClass.methodOfInnerClass();
        innerStaticClass.methodOfInnerClass();
        innerClass.methodOfInnerClass();
        innerOfPublicInnerClass.methodOfInnerClass();
        fooFighterProtected.fight(withFire);
        return fooFighter.fight(withFire);
    }

}
