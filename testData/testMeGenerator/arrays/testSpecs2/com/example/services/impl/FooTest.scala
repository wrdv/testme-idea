package com.example.services.impl

import com.example.foes.Fire
import org.specs2.mutable.Specification
/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {
    val foo = new Foo()

  "Foo" should {

      "fight" in {
        val result = foo.fight(Array(new Fire()), Array("foeName"), Array(1))
        result === Array("replaceMeWithExpectedResult")
      }

      "fire Starter" in {
        val result = foo.fireStarter(Array("foeName"), Array(1))
        result === Array(new Fire())
      }

      "fire Counter" in {
        val result = foo.fireCounter(Array(new Fire()))
        result === Array(1)
      }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme