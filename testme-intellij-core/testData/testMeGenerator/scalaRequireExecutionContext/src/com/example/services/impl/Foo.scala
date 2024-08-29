package com.example.services.impl
import com.example.common.LogSupport
import com.example.foes.Fire
import com.example.warriers.FooFighter

import scala.concurrent.{ExecutionContext, Future}


class Foo(fooFighter: FooFighter, implicit val ec:ExecutionContext) extends LogSupport{
  //  <caret>
  val withFire: Fire = new Fire()

  def fight(optFire:Option[String]) ={
    logger.info("it's started...")
    Future{
      fooFighter.fight(withFire)
    }
  }
}