package com.monads.talk

import org.scalatest.FunSuite

class AsyncSetSuite extends FunSuite {

  import Monad._

  val readNum: String => Async[Int] = (a: String) => Monad[Async].pure(a.toInt)
  // A => F[B]
  val queryServer: Int => Async[Double] = (a: Int) => Monad[Async].pure((a * a).toDouble)
  // B => F[C]


  test("left identity") {

    val pureBind = Monad[Async].pure(5).bind(num => queryServer(num))

    assert(pureBind.get == queryServer(5).get)
  }

  test("right identity") {

    val monad = Monad[Async].pure(5)

    assert(monad.bind(Monad[Async].pure(_)).get == monad.get)

  }

  test("associativity") {

    val monad1 = Monad[Async].pure("5").bind(readNum(_)).bind(queryServer(_))

    val monad2 = Monad[Async].pure("5").bind(num => readNum(num).bind(queryServer(_)))

    val allAsync = for {
      v1 <- monad1
      v2 <- monad2
    } yield v1 == v2

    assert(allAsync.get)
    assert(monad1.get == monad2.get)

    println(readNum("5").map(queryServer).flatten)
  }

}


