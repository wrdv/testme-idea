package com.example.services.impl

import com.example.common.LogSupport
import com.example.foes.Fire
import com.example.warriers.FooFighter
import com.example.warriers.impl.FooFighterImpl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait Foo extends LogSupport{
  val fooFighter:FooFighter
  val withFire: Fire = new Fire()

  def fight(optFire:Option[String]): Future[String] ={
    logger.info("invoking method of dependency directly...")
    Future{
      fooFighter.fight(withFire)
    }
  }
  def fightWithMap(optFire:Option[String]): Future[Option[String]] ={
    logger.info("using method of dependency as a passed refences...")
    Future{
      Some(withFire) map {
        fooFighter.fight
      }
    }
  }
}

object Foo extends Foo{
  val fooFighter =  new FooFighterImpl()
  //  <caret>
}