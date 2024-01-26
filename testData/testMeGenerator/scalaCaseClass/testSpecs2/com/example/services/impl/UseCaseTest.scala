package com.example.services.impl

import com.example.scala.{CaseClass, Nested}
import org.specs2.mutable.Specification

/** created by TestMe integration test on MMXVI */
class UseCaseTest extends Specification {
  val useCase = new UseCase()

  "UseCase" should {

    "make Default" in {
      val result = useCase.makeDefault
      result === new CaseClass(1, true, 100d, "case", Some("myName"), Some(new Nested(Some("arg"))))
    }

    "use" in {
      val result = useCase.use(new CaseClass(1, true, 100d, "case", Some("myName"), Some(new Nested(Some("arg")))))
      false //todo - validate something
    }

  }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme