package com.example.services.impl

import com.example.foes.Fire
import org.specs2.mutable.Specification

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {
  val foo = new Foo()

  "Foo" should {

    "fight" in {
      val result = foo.fight(new Fire(), "foeName")
      result === "replaceMeWithExpectedResult"
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme