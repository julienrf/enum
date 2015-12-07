package julienrf.enum

import org.scalatest.FunSuite

class ValuesSuite extends FunSuite {
  import ValuesSuite.Foo

  test("Values[Foo]") {
    assert(Foo.enum.values == Set(Foo.Bar, Foo.Baz))
  }

}

object ValuesSuite {
  sealed trait Foo
  object Foo {
    case object Bar extends Foo
    case object Baz extends Foo

    val enum: Values[Foo] = Values[Foo]
  }
}