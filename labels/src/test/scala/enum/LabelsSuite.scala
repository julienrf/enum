package enum

import org.scalatest.funsuite.AnyFunSuite

class LabelsSuite extends AnyFunSuite {
  test("Labels[Foo]") {
    assert(Labels[Foo].labels == Set("Bar", "Baz"))
  }
}