package julienrf.enum

import org.scalatest.FunSuite

class LabelsSuite extends FunSuite {
  import LabelsSuite.Foo

  test("Labels[Foo]") {
    assert(Foo.enum.labels == Set("Bar", "Baz"))
  }

}

object LabelsSuite {
  sealed trait Foo
  object Foo {
    case object Bar extends Foo
    case object Baz extends Foo

    val enum: Labels[Foo] = Labels[Foo]
  }
}