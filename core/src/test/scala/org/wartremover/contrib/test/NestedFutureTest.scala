package org.wartremover.contrib.test

import org.scalatest.funsuite.AnyFunSuite
import org.wartremover.contrib.warts.NestedFuture
import org.wartremover.test.WartTestTraverser

import scala.concurrent.Future

class NestedFutureTest extends AnyFunSuite with ResultAssertions {
  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  test("single future doesn't error") {
    val result = WartTestTraverser(NestedFuture) {
      val f: Future[Unit] = Future.successful(())
    }
    assertEmpty(result)
  }

  test("error if nested Future[Future[Unit]]") {
    val result = WartTestTraverser(NestedFuture) {
      val f: Future[Future[Unit]] = Future.successful(Future.successful(()))
    }
    assertError(result)(NestedFuture.message)
  }
}
