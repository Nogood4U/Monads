import scala.util.Try

val readNum = (a: String) => Try(a.toInt)
// A => F[B]
val queryServer = (a: Int) => Try((a * a).toDouble)
// B => F[C]

//val composed = readNum andThen queryServer
// no compila
/*
Tenemos manejo de errores ,
pero los tipos no concuerdan ,
no podemos hacer composicion funcional

*/

readNum("5").map(num => queryServer(num))
//Success(Success(25.0))
/*
   A => F[F[C]] , :(

   A => F[C]    , :)
*/


