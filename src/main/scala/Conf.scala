import com.typesafe.scalalogging.LazyLogging
import pureconfig.generic.ProductHint
import pureconfig.{CamelCase, ConfigFieldMapping, SnakeCase}
import pureconfig.generic.auto._

class Conf() extends LazyLogging {
  import Conf._
  implicit def hint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, SnakeCase))

  lazy val prefixRoot = "top"

  lazy val prefixSub = "top.sub"

  lazy val gcConf: GcConf = pureconfig.loadConfigOrThrow[GcConf](s"$prefixSub.gc")
  lazy val sparkConf: SparkConf = pureconfig.loadConfigOrThrow[SparkConf](s"$prefixSub.spark_setup")

  lazy val hmacKey:String = pureconfig.loadConfigOrThrow[String](s"$prefixRoot.hmac_key")
}

object Conf {
  val theConf: Conf = new Conf()

  case class GcConf (
                      pubsub_topic: String,
                      subscription: String
                    )

  case class SparkConf (
                         master: String,
                         appName: String,
                         executorCores: String,
                         minExecutors: String,
                         maxExecutors: String,
                         savePath: String,
                         filePath: String
                       )
}