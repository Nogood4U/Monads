import scala.util.Try

val readNum = (a: String) => Try(a.toInt)
// A => F[B]
val queryServer = (a: Int) => Try((a * a).toDouble)
// B => F[C]

// Necesitamos
// A => F[C]

// A => F[B] >>= [ B => F[C] ] => F[C]

// def bind[A, B](a: M[A], f: A => M[B]): M[B] // flatMap

readNum("5").flatMap(queryServer)
// Success(25.0)

