package enum

import org.scalatest.FunSuite

class ValuesSuite extends FunSuite {
  test("Values[Foo]") {
    assert(Values[Foo].values == Set(Foo.Bar, Foo.Baz))
  }
}