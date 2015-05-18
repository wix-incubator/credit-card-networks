/*      __ __ _____  __                                              *\
**     / // // /_/ |/ /          Wix                                 **
**    / // // / /|   /           (c) 2006-2015, Wix LTD.             **
**   / // // / //   |            http://www.wix.com/                 **
**   \__/|__/_//_/| |                                                **
\*                |/                                                 */
package com.wix.pay.creditcard.networks


import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context


/** Credit Card Network.
  * Identifies a credit card's Network based on its (full) number, and according to
  * [[http://en.wikipedia.org/wiki/Bank_card_number]] and [[https://www.bindb.com/bin-database.html]].
  * In addition, exposes all supported Networks (via the [[Networks.all()]] method).
  *
  * @author <a href="mailto:ohadr@wix.com">Raz, Ohad</a>
  */
object Networks {
  val amex = "amex"
  val dankort = "dankort"
  val diners = "diners"
  val discover = "discover"
  val isracard = "isracard"
  val jcb = "jcb"
  val maestro = "maestro"
  val masterCard = "mastercard"
  val unionPay = "unionpay"
  val visa = "visa"


  val amexRegex = """^3([47]\d{3}|569[04]|[23]\d{3})\d{10}$""".r
  val dankortRegex = """^5019(34)\d{10}$""".r
  val dinersRegex = """^36(409\d|5\d{3})\d{8}$""".r
  val discoverRegex = """^(6011\d{2}|65\d{4}|64[4-9]\d{3}|601300|3[^547]\d{4})\d{10}$""".r
  val isracardRegex = """^\d{8,9}$""".r
  val jcbRegex = """^35(?:2[89]|[3-8]\d)\d{12}$""".r
  val maestroRegex =
    """^(50[^1]\d{3}|501[^9]\d{2}|5019[^3]\d|50193[^4]|5[6789]\d{4}|6[^0245]\d{4}|62[79]\d{3}|628[01]\d{2}|626257|62292[78]|62293[^4]|62294[^14]|62299\d|62298[02369]|62297[^0]|62296[^4569]|62295[^68]|62212[^6789]|62211\d|6220\d{2}|621[^4]\d{2}|6214[^8]\d|62148[^3]|620\d{3}|64[0-3]\d{3}|60[2-7]\d{3}|601[^13]\d{2}|6013[^0]\d|60130[^0]|600\d{3})\d{6,13}$""".r
  val masterCardRegex = """^5[1-5]\d{14}$""".r
  val unionPayRegex = """^(62[345]\d{3}|628[2-8]\d{2}|626[^2]\d{2}|6262[^5]\d|62625[^7]|622[^019]|6229[01]\d|62298[14578]|622970|62296[4569]|62295[68]|62294[14]|622934|62292[^78]|622[2-8]\d{2}|6221[^12]|62212[6-9]|621483)\d{10,13}$""".r
  val visaRegex = """^4\d{12}(\d{3})?$""".r


  def all(): Seq[String] = macro generateAll

  def generateAll(ctx: Context)(): ctx.Expr[Seq[String]] = {
    import ctx.universe._

    val cls = Networks.getClass
    val fields = typeOf[Networks.type].termSymbol.typeSignature.decls.collect {
      case f: MethodSymbol
        if f.isPublic && f.isStatic && f.isAccessor && f.returnType.typeSymbol == weakTypeOf[String].typeSymbol =>
          cls.getMethod(f.name.decodedName.toString).invoke(Networks).toString
    }

    ctx.Expr(q"""Seq(..$fields)""")
  }


  /** According to [[http://en.wikipedia.org/wiki/Bank_card_number]] and [[https://www.bindb.com/bin-database.html]].
    *
    * @param creditCardNumber
    *                         the credit number for which a Network will be resolved.
    * @return
    *         The resolved Network, or {{{None}}} if failed to resolve one.
    */
  def apply(creditCardNumber: String): Option[String] = {
    creditCardNumber match {
      case amexRegex(_*) => Some(Networks.amex)
      case dankortRegex(_*) => Some(Networks.dankort)
      case dinersRegex(_*) => Some(Networks.diners)
      case discoverRegex(_*) => Some(Networks.discover)
      case isracardRegex(_*) => Some(Networks.isracard)
      case jcbRegex(_*) => Some(Networks.jcb)
      case maestroRegex(_*) => Some(Networks.maestro)
      case masterCardRegex(_*) => Some(Networks.masterCard)
      case unionPayRegex(_*) => Some(Networks.unionPay)
      case visaRegex(_*) => Some(Networks.visa)
      case _ => None
    }
  }
}
