package scalaz
package syntax

final class ContTOps[M[_], A](self: M[A]) {
  final def cps[R](implicit M: Bind[M]): ContT[M, R, A] =
    ContT((f: A => M[R]) => M.bind(self)(f))

  final def cps_(implicit M: Bind[M]): ContT[M, Unit, A] =
    cps[Unit]
}

trait ToContTOps {
  implicit def ToContTOps[M[_], A](ma: M[A]) = new ContTOps(ma)
}
