package enum

import org.scalatest.FunSuite

class LabelsSuite extends FunSuite {
  test("Labels[Foo]") {
    assert(Labels[Foo].labels == Set("Bar", "Baz"))
  }
}