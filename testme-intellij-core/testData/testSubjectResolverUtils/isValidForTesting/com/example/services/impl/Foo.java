package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
import com.example.warriers.impl.FooFighterImpl;

public class Foo{

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
    private static class PrivateInnerStaticClass{
        public void methodOfInnerClass(){

        }
    }
    public static class InnerStaticClassWithMember{
        FooFighter innerFooFighter;
        public String methodOfInnerClass(Fire fire) {
            return innerFooFighter.fight(fire);
        }
    }
    public class InnerClassWithMember{
        FooFighter innerFooFighter;
        public String methodOfInnerClass(Fire fire) {
            return innerFooFighter.fight(fire);
        }
    }

}
