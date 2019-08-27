package com.monads.talk


case class State[S, A](run: S => (A, S)) {

  def flatMap[B](f: A => State[S, B]): State[S, B] =
    State(s => {
      val (a, t) = run(s)
      f(a) run t
    })

  def map[B](f: A => B): State[S, B] =
    State(s => {
      val (a, t) = run(s)
      (f(a), t)
    })

  def eval(s: S): S =
    run(s)._2
}

object State {
  def apply[S, A](run: S => (A, S)): State[S, A] = new State(run)
}
