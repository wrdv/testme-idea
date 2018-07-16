package com.example.services.impl
import com.example.common.LogSupport
import com.example.foes.Fire
import com.example.dependencies.TimeMachine

import scala.concurrent.Future


class Foo(timeMachine: TimeMachine) extends LogSupport{
  //  <caret>
  val withFire: Fire = new Fire()

  def fight(optFire:Option[String]): Future[Double] ={
    logger.info("it's started...")
    println(withFire.getJavaBean.getMyString)
    timeMachine.lookInto()
  }
}