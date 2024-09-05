package com.example.services.impl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class Foo {
  //  <caret>
  def findMeABetterFuture(hopes:Option[String]): Future[String] ={
    Future{
      "utopia"
    }
  }
  def lookIntoTheFuture(hopes:Future[Int]): Future[Float] ={
    Future(1.1f)
  }
}