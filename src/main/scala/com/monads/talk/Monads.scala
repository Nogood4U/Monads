package com.monads.talk

import scala.language.higherKinds
import scala.util.Try


trait Functor[F[_]] {

  def map[A, B](a: F[A], f: A => B): F[B]

}

trait Monad[M[_]] extends Functor[M] {

  def pure[A](a: A): M[A]

  def bind[A, B](a: M[A])(f: A => M[B]): M[B]

}

object Monad {

  def apply[A[_]](implicit m: Monad[A]) = m

  implicit val tryMonad: Monad[Try] = new Monad[Try] {
    override def pure[A](a: A): Try[A] = Try(a)

    override def bind[A, B](a: Try[A])(f: A => Try[B]): Try[B] = a.flatMap(f)

    override def map[A, B](a: Try[A], f: A => B): Try[B] = a.map(f)
  }

  implicit class MonadOps[A](val m: Try[A]) {

    def bind[B](f: A => Try[B]): Try[B] = Monad[Try].bind[A, B](m)(f)
  }


  implicit val asyncMonad: Monad[Async] = new Monad[Async] {
    override def pure[A](a: A) = Async(a)

    override def bind[A, B](a: Async[A])(f: A => Async[B]) = {
      Async {
        var value: B = null.asInstanceOf[B]
        val t = Async.createThread {
          value = f(a.get).get
        }
        t.start()
        t.join()
        value
      }
    }

    override def map[A, B](a: Async[A], f: A => B) = {
      //      Async {
      //        f(a.get)
      //      }
      bind(a)(a => Async(f(a)))
    }
  }

  implicit class AsyncMonadOps[A](val m: Async[A]) {

    def bind[B](f: A => Async[B]): Async[B] = Monad[Async].bind[A, B](m)(f)

    def flatMap[B](f: A => Async[B]): Async[B] = bind(f)

    def map[B](f: A => B): Async[B] = Monad[Async].map(m, f)

    def flatten[B] = Monad[Async].bind[A, B](m)(x => x.asInstanceOf[Async[B]])
  }

}
