package enum

sealed trait TwoLevels

object TwoLevels {
  implicit val twoLevelsEnum: Enum[TwoLevels] = Enum.derived
}

case object FirstValue extends TwoLevels
sealed trait SecondLevel extends TwoLevels
case object SecondValue extends SecondLevel
case object ThirdValue extends SecondLevel