package enum

import org.scalatest.funsuite.AnyFunSuite

class ValuesSuite extends AnyFunSuite {
  test("Values[Foo]") {
    assert(Values[Foo].values == Set(Foo.Bar, Foo.Baz))
  }
}