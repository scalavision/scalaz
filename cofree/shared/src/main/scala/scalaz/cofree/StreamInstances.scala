package scalaz.cofree

import scalaz._
import scalaz.ct._
import scalaz.types.IsCovariantClass
import scalaz.cofree.StreamTransducer.Process

trait StreamInstances {
  implicit def monad[E]: Monad[Process[E, ?]] =
    instanceOf(new MonadClass[Process[E, ?]] with BindClass.DeriveFlatten[Process[E, ?]] {
      override final def map[A, B](ma: Process[E, A])(f: A => B): Process[E, B] =
        ma.map(f)

      override final def ap[A, B](ma: Process[E, A])(mf: Process[E, A => B]): Process[E, B] =
        ma.flatMap(a => mf.map(f => f(a)))

      override final def pure[A](a: A): Process[E, A] = Process.now(a)

      override final def flatMap[A, B](ma: Process[E, A])(f: A => Process[E, B]): Process[E, B] =
        ma.flatMap(f)
    })

  implicit final val bifunctor: Bifunctor[Process] =
    instanceOf(new BifunctorClass.DeriveBimap[Process] {
      override def lmap[A, B, S](fab: Process[A, B])(as: A => S): Process[S, B] = fab.leftMap(as)
      override def rmap[A, B, T](fab: Process[A, B])(bt: B => T): Process[A, T] = fab.map(bt)
    })

  implicit def isCovariant[E]: IsCovariant[Process[E, ?]] = IsCovariantClass.unsafeForce[Process[E, ?]]

}
