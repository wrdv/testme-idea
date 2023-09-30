package com.example.services.impl

import com.example.scala.CaseClass

class UseCase(){
  def use(caseClass:CaseClass):Unit={
    println(caseClass.`case`)
  }

  def makeDefault():CaseClass = {
    new CaseClass(1,true,2.5,"default", Some("val"))
  }
}