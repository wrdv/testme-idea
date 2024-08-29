package com.example.services.impl
import collection.JavaConverters._
import com.example.foes.{Fear, Fire, Ice}
import org.specs2.mutable.Specification
/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {
    val foo = new Foo()

    "Foo" should {

        "fight" in {
            val result = foo.fight(List(new Fire()).asJava, Set(new Fire()).asJava, Map("String" -> new Ice()).asJava, List("String").asJava, List(List("String").asJava).asJava, new java.util.LinkedList(List(List(new Fear()).asJava).asJava), new java.util.LinkedList(List(new Fear()).asJava))
            result === List(new Fire()).asJava
        }

        "fight Concrete Types" in {
            val result = foo.fightConcreteTypes(new java.util.ArrayList(List(new Fire()).asJava), new java.util.HashSet(Set(new Fire()).asJava), new java.util.HashMap(Map("String" -> new Ice()).asJava), new java.util.Vector(Vector(List("String").asJava).asJava), new java.util.TreeSet(List(new Fear()).asJava), null)
            result === List(new Fire()).asJava
        }

        "typeless" in {
            val result = foo.typeless(List("String").asJava, Set("String").asJava, Map("String" -> "String").asJava, List("String").asJava, List(List("String").asJava).asJava, new java.util.LinkedList(List(List("String").asJava).asJava))
            result === List("String").asJava
        }

        "misc Colls" in {
            val result = foo.miscColls(Map("String" -> new Fire()).asJava, Set(new Fire())).asJava, Vector("String").asJava, Set(new Fire()).asJava)
            result === Map("String" -> new Fire()).asJava
        }

        "object Maps" in {
            val result = foo.objectMaps(Map(true -> "String").asJava, Map(1 -> true).asJava, new java.util.HashMap(Map(new Fire() -> new Fire()).asJava))
            result === new java.util.HashMap(Map(new Fire() -> new Fire()).asJava)
        }

    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme