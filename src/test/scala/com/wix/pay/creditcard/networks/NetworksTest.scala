/*      __ __ _____  __                                              *\
**     / // // /_/ |/ /          Wix                                 **
**    / // // / /|   /           (c) 2006-2015, Wix LTD.             **
**   / // // / //   |            http://www.wix.com/                 **
**   \__/|__/_//_/| |                                                **
\*                |/                                                 */
package com.wix.pay.creditcard.networks


import scala.io.Source
import org.specs2.execute.Result
import org.specs2.mutable.SpecWithJUnit


/** Unit-Test for the [[Networks]] object.
  *
  * @author <a href="mailto:ohadr@wix.com">Raz, Ohad</a>
  */
class NetworksTest extends SpecWithJUnit {
  def generateNumber(prefix: String, length: Int): String = {
    s"$prefix${Array.fill(length - prefix.length)("0").mkString}"
  }


  "apply" should {
    "identify networks according to rules described at http://en.wikipedia.org/wiki/Bank_card_number and " +
      "according to results of https://www.bindb.com/bin-database.html" in {
      val lengths = Map("amex" -> 15, "diners" -> 14)
      val networksResourceInputStream = getClass.getClassLoader.getResourceAsStream("networks.txt")

      Result.foreach(Source.fromInputStream(networksResourceInputStream).getLines().toSeq) { line =>
        val arr = line.split(',')
        val (bin, network) = (arr(0).trim, arr(1).trim)
        val length = lengths.getOrElse(network, 16)
        val number = generateNumber(bin, length)

        Networks(number) must beSome(network)
      }
    }
  }

  "all" should {
    "return all supported Networks (and nothing but it)" in {
      Networks.all must contain(exactly(
        "amex", "dankort", "diners","discover", "isracard", "jcb", "maestro", "mastercard", "unionpay", "visa", "rupay"))
    }
  }
}
