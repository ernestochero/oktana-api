package commons

import scala.concurrent.ExecutionContext

object Implicits {
  implicit val ec: ExecutionContext = ExecutionContext.global
}
