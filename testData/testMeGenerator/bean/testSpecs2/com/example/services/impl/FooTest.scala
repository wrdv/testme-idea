package com.example.services.impl

import org.specs2.mutable.Specification

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {
  val foo = new Foo()

  "Foo" should {

    "fight" in {
      val result = foo.fight(new com.example.foes.Fire(), new com.example.foes.FireBall(new com.example.beans.JavaBean()), new com.example.beans.JavaBean())
      result === new com.example.beans.JavaBean()
    }

    "fight 2" in {
      val result = foo.fight(new com.example.dependencies.ChildWithSetters())
      result === new com.example.dependencies.ChildWithSetters()
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme