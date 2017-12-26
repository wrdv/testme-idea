package com.example.services.impl

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {
  val foo = new com.example.services.impl.Foo()

  "Foo" should {

    "example" in {
      val result = foo.example((Some(1), Some(List(List(11223344)))))
      result === "replaceMeWithExpectedResult"
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme