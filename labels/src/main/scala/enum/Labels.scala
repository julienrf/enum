package enum

import shapeless.labelled.FieldType
import shapeless.{:+:, Witness, CNil, LabelledGeneric, Coproduct}

/** Gives the names of all the subtypes of `A`. Note that it is not limited to case objects: it works with any sum types. */
trait Labels[A] {
  /** The names of all subtypes of `A` */
  val labels: Set[String]
}

object Labels {

  @inline def apply[A](implicit labels: Labels[A]): Labels[A] = labels

  trait Derived[A] extends Labels[A]

  @inline implicit def derived[A](implicit derived: Derived[A]): Labels[A] = derived

  object Derived extends Derived1 {

    implicit val cnil: Derived[CNil] =
      new Derived[CNil] {
        val labels = Set.empty[String]
      }

    implicit def ccons[K <: Symbol, L, R <: Coproduct](implicit
      label: Witness.Aux[K],
      tailLabels: Derived[R]
    ): Derived[FieldType[K, L] :+: R] =
      new Derived[FieldType[K, L] :+: R] {
        val labels = tailLabels.labels + label.value.name
      }
  }

  trait Derived1 {
    implicit def generic[A, Repr <: Coproduct](implicit
      gen: LabelledGeneric.Aux[A, Repr],
      reprLabels: Derived[Repr]
    ): Derived[A] =
      new Derived[A] {
        val labels = reprLabels.labels
      }
  }

}