package julienrf.enum

import shapeless.labelled.FieldType
import shapeless.{:+:, Witness, CNil, LabelledGeneric, Coproduct}

/** Gives the names of all the subtypes of `A` */
class Labels[A](val labels: Set[String])

object Labels {

  @inline def apply[A](implicit labels: Labels[A]): Labels[A] = labels

  implicit def derived[A, Repr <: Coproduct](implicit
    gen: LabelledGeneric.Aux[A, Repr],
    reprLabels: Labels[Repr]
  ): Labels[A] =
    new Labels(reprLabels.labels)

  implicit val cnil: Labels[CNil] =
    new Labels(Set.empty)

  implicit def ccons[K <: Symbol, L, R <: Coproduct](implicit
    label: Witness.Aux[K],
    tailLabels: Labels[R]
  ): Labels[FieldType[K, L] :+: R] =
    new Labels(tailLabels.labels + label.value.name)

}