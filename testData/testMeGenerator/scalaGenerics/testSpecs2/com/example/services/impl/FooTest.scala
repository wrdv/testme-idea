package com.example.services.impl

import org.specs2.mutable.Specification

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {
  val foo = new Foo()

  "Foo" should {

    "tuple Maker" in {
      val result = foo.tupleMaker(Some(1))
      result === ("Object", 11223344.53, 1)
    }

    "example" in {
      val result = foo.example((Some(1), Some(List(List(11223344.53)))))
      result === "replaceMeWithExpectedResult"
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme