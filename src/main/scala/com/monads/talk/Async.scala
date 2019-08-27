package com.monads.talk

class Async[A](f: => A) {
  var value: A = _
  val th: Thread = Async.createThread {
    value = f
    Thread.sleep(3000)
  }

  th.start()

  def get = {
    synchronized {
      th.join()
      value
    }
  }

}

object Async {
  def apply[A](f: => A): Async[A] = new Async(f)

  def createThread(fn: => Unit): Thread = {
    new Thread {
      override def run(): Unit = fn
    }
  }
}
