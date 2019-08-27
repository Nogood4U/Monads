package com.monads.talk

import org.scalatest.FunSuite

class StateSetSuite extends FunSuite {


  val readNum: String => State[Int, Int] = (a: String) => State(state => (a.toInt, 1 + state))
  // A => F[B]
  val queryServer: Int => State[Int, Double] = (a: Int) => State(state => ((a * a).toDouble, 1 + state))
  // B => F[C]


  test("left identity") {

    val pureBind = State[Int, Int](state => (5, state)).flatMap(num => queryServer(num))
    1
    assert(pureBind.run(0) == queryServer(5).run(0))
  }

  test("right identity") {

    val monad = State[Int, Int](state => (5, 1 + state))

    assert(monad.flatMap(num => State(state => (num, state))).eval(0) == monad.eval(0))

  }

  test("associativity") {

    val monad1 = State[Int, String](state => ("5", 1 + state)).flatMap(readNum(_)).flatMap(queryServer(_))

    val monad2 = State[Int, String](state => ("5", 1 + state)).flatMap(num => readNum(num).flatMap(queryServer(_)))

    val allAsync = for {
      v1 <- monad1
      v2 <- monad2
    } yield v1 == v2

    assert(monad1.run(0) == monad2.run(0))
  }

}


