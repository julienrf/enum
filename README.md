# Enum

Yet another enumeration toolbox for Scala, powered by [shapeless](https://github.com/milessabin/shapeless).

## Artifacts

 - [![](https://index.scala-lang.org/julienrf/enum/enum/latest.svg)](https://index.scala-lang.org/julienrf/enum/enum) finds the values and labels of an enumeration and a mapping to go from a value to its label and _vice versa_.
 - [![](https://index.scala-lang.org/julienrf/enum/enum-values/latest.svg)](https://index.scala-lang.org/julienrf/enum/enum-values) finds the set of values of an enumeration ;
 - [![](https://index.scala-lang.org/julienrf/enum/enum-labels/latest.svg)](https://index.scala-lang.org/julienrf/enum/enum-labels) finds the set of value names of an enumeration ;

The artifacts are built for Scala 2.11, 2.12, 2.13 and Scala.js 0.6.

## Usage

Just define your enumeration as a sealed trait (or a sealed abstract class) extended by case objects.

For instance:

~~~ scala
sealed trait Foo
object Foo {
  case object Bar extends Foo
  case object Baz extends Foo
}
~~~

### `enum`

Use `Enum.derived[A]` to derive an `Enum[A]` value that provides several useful methods:

~~~ scala
import enum.Enum

val enum: Enum[Foo] = Enum.derived[Foo]

enum.values == Set(Foo.Bar, Foo.Baz)
enum.labels == Set("Bar", "Baz")
enum.encode(Foo.Bar) == "Bar"
enum.decode("Baz") == Right(Foo.Baz)
enum.decode("invalid") == Left(DecodingFailure[Foo](Set("Bar", "Baz")))
enum.decodeOpt("Baz") == Some(Foo.Baz)
~~~

See the [API documentation](https://www.javadoc.io/doc/org.julienrf/enum_2.13/3.1) for more details.

### `enum-values`

Use `Values.derived[A]` to automatically gather all the `A` enumeration values as a `Set[A]`:

~~~ scala
import enum.Values

val fooValues: Values[Foo] = Values.derived[Foo]

fooValues.values == Set(Foo.Bar, Foo.Baz)
~~~

See the [API documentation](https://www.javadoc.io/doc/org.julienrf/enum-values_2.13/3.1) for more details.

### `enum-labels`

Use `Labels.derived[A]` to automatically gather all the `A` enumeration labels as a `Set[String]`:

~~~ scala
import enum.Labels

val fooLabels: Labels[Foo] = Labels.derived[Foo]

fooLabels.labels == Set("Bar", "Baz")
~~~

See the [API documentation](https://www.javadoc.io/doc/org.julienrf/enum-labels_2.13/3.1) for more details.

## Changelog

- 3.1
    - Scala 2.12 and 2.13 support
- 3.0
    - **Breaking change**: the companion object’s `apply` method that was used to derive enumerations
      has been renamed to `derived`. The `apply` method still exists but it now returns the
      implicitly available instance (your old code may still compile with the new version but might fail at runtime).
    - Remove package `julienrf`
- 2.2
    - Update shapeless to 2.3.0 and Scala.js to 0.6.7
- 2.1
    - Lessen derivation implicit priority
- 2.0
    - Added support for implicit definitions in enumerations companion objects
- 1.1
    - Added `Enum[A]`
- 1.0
    - First release

## License

This content is released under the [MIT License](http://opensource.org/licenses/mit-license.php).
