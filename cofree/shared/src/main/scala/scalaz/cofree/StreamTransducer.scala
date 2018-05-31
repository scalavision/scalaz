package scalaz.cofree

import scalaz.effect.{ExitResult}

object StreamTransducer {

  sealed abstract class Process[E, A] { self =>

    def tag: Int

    final def map[B](f: A => B): Process[E,B] = ???

    final def flatMap[B](f: A => Process[E,B]): Process[E, B] = ???

    final def leftMap[E2](f: E => E2): Process[E2, A] = ???

  }

  object Process extends StreamInstances {
    final object Tags {
      final val Await = 0
      final val Emit = 1
      final val Halt = 2
      final val End = 3
      final val Kill = 4
    }

    final def now[E, A](a: A) : Process[E, A] = ???

    final class Await[E, A0, A] private[Process](
      req: Process[E,A0],
      recv: ExitResult[E,A] => Process[E,A]
    ) extends Process[E,A] {
      override def tag: Int = Tags.Await
    }

    final case class Emit[E, A] private [Process](
      head: A,
      tail: Process[E,A]
    ) extends Process[E,A] {
      override def tag: Int = Tags.Emit
    }

    final case class Halt[E,A] private [Process](
      err: ExitResult[E,A]
    ) extends Process[E,A] {
      override def tag: Int = Tags.Halt
    }

    final case object End extends Process {
      override def tag: Int = Tags.End
    }

    final case object Kill extends Process {
      override def tag: Int = Tags.Kill
    }



  }

}
