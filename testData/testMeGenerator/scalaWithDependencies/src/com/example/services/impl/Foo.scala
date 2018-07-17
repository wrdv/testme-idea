package com.example.services.impl
import com.example.common.LogSupport
import com.example.foes.Fire
import com.example.warriers.FooFighter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class Foo(fooFighter:FooFighter) extends LogSupport{
  //  <caret>
  val withFire: Fire = new Fire()

  def fight(optFire:Option[String]): Future[String] ={
    logger.info("invoking method of dependency directly...")
    Future{
      fooFighter.fight(withFire)
    }
  }
  def fightWithMap(optFire:Option[String]): Future[String] ={
    logger.info("using method of dependency as a passed refences...")
    Future{
      Some(withFire) map {
        fooFighter.fight
      }
    }
  }
}