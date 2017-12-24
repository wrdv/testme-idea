package com.example.services.impl;

/**
  * Created by Admin on 24/12/2017.
  */
trait FooTrait {

}
class Foo extends FooTrait {
    //  <caret>
    def example(someTuple:(Option[Int],Option[List[List[Double]]]))={
        "something"
    }

}