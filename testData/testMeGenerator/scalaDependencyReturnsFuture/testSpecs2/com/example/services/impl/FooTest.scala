package com.example.services.impl

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import com.example.dependencies.TimeMachine

import scala.concurrent.duration._
import scala.concurrent.Await

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification with Mockito {
  isolated
  val timeMachine: TimeMachine = mock[TimeMachine]
  val foo = new Foo(timeMachine)

  "Foo" should {

    "fight" in {
      timeMachine.lookInto() returns scala.concurrent.Future.successful(100d)
      val result = foo.fight(Some("optFire"))
      Await.result(result, 10.seconds) === 100d
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme