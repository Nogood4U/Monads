package com.monads.talk

import org.scalatest.FunSuite

import scala.util.Try


class AppSetSuite extends FunSuite {

  import Monad._

  val readNum: String => Try[Int] = (a: String) => Try(a.toInt)
  // A => F[B]
  val queryServer: Int => Try[Double] = (a: Int) => Try((a * a).toDouble)
  // B => F[C]


  test("left identity") {

    val pureBind = Monad[Try].pure(5).bind(num => queryServer(num))

    assert(pureBind == queryServer(5))
  }

  test("right identity") {

    val monad = Monad[Try].pure(5)

    assert(monad.bind(Monad[Try].pure(_)) == monad)

  }

  test("associativity") {

    val monad1 = Monad[Try].pure("5").bind(readNum(_)).bind(queryServer(_))

    val monad2 = Monad[Try].pure("5").bind(num => readNum(num).bind(queryServer(_)))

    assert(monad1 == monad2)
  }

}


