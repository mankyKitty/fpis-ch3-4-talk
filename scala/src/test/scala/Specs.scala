
import org.scalacheck.Properties
import org.scalacheck.Prop.{forAll, BooleanOperators}

object WatSpecification extends Properties("Wat") {

  // Simple ScalaCheck property, as an example. :)
  property("list reverse") = forAll {
    l: List[String] => l.reverse.reverse == l
  }

  def failingFn1( xs: List[Int] ): Int = {
    val y: Int = throw new Exception("wup-wah")
    try {
      val x = 42 + 5
      x + y
    }
    catch { case e: Exception => 43 }
  }
  // demonstrating Referencial Transparency failsauce
  property("Failing Fn #1") = forAll {
    li: List[Int] => failingFn1(li) match {
      // We should always get an Int, right? ;)
      case _:Int => true
    }
  }

  def failingFn2( xs: List[Int] ): Int =
    try {
      val x = 42 + 5
      x + ((throw new Exception("wup-wah")): Int)
    }
    catch { case e:Exception => 43 }

  // No no no no no, this shouldn't work... but it does ;)
  property("Failing Fn #2 only ever returns 43 *sadface*") = forAll {
    li: List[Int] => (failingFn2(li) == 43)
  }

  // Mean function examples
  def mean1( xs: Seq[Double]): Double =
    if (xs.isEmpty)
      throw new ArithmeticException("mean of empty list")
    else xs.sum / xs.length

  property("mean 1 for all list of doubles") = forAll {
    ld: List[Double] => mean1(ld) match {
      case _:Double => true
    }
  }

  def mean2( xs: Seq[Double], onEmpty: Double): Double =
    if (xs.isEmpty) onEmpty
    else xs.sum / xs.length

  property("mean 2 for all non-empty list of doubles") = forAll {
    (ld: List[Double], d: Double) => (!ld.isEmpty) ==>
      (mean2(ld,d) == (ld.sum / ld.length))
  }
  // Here for information really, not a very useful property to show.
  // property("mean 2 for all empty list of doubles we get default") = forAll {
  //   (ld: List[Double], d: Double) => (ld.isEmpty) ==>
  //     (mean2(ld,d) == d)
  // }

  def mean3( xs: Seq[Double] ): Option[Double] =
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)

  property("mean 3 provides total function with sensible error handling") = forAll {
    ld: List[Double] => (ld, mean3(ld)) match {
      // Empty list won't throw an exception, and we don't have a weird useless Double
      // value that we have to check against here or "somewhere" in the codebase.
      case (Nil,None) => true
      // The ideal situation, we have a non-empty list and a result!
      case (_::_, Some(r)) => r == (ld.sum / ld.length)
      // The unwanted failing situation, can we even get here??
      case _ => false
    }
  }
}
