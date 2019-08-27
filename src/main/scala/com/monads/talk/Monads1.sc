
val readNum = (a: String) => a.toInt
// A => B

val queryServer = (a: Int) => (a * a).toDouble
// B => C

val composed = readNum andThen queryServer

val composed2 = queryServer compose readNum
// A => C

composed("5")
composed2("5")
queryServer(readNum("5"))
// f( g( x ) )
// 25.0

// no error handling
// no null validation
