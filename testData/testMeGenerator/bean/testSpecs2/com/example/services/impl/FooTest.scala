package com.example.services.impl

import com.example.beans.JavaBean
import com.example.foes.Fire
import org.specs2.mutable.Specification

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {
  val foo = new Foo()

  "Foo" should {

    "fight" in {
      val result = foo.fight(new Fire(), new com.example.foes.FireBall(new JavaBean()), new JavaBean())
      result === new JavaBean()
    }

    "fight 2" in {
      val result = foo.fight(new com.example.dependencies.ChildWithSetters())
      result === new com.example.dependencies.ChildWithSetters()
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme