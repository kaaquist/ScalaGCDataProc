import java.io.{PrintWriter, StringWriter}

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.{SparkConf, SparkContext}
import io.lemonlabs.uri.Url
import io.lemonlabs.uri.parsing.UriParsingException

object ScalaApp extends App with LazyLogging {
  val blacklistUrls = List(
    "https://blocklist.site/app/dl/malware",
    "https://blocklist.site/app/dl/phishing",
    "https://blocklist.site/app/dl/piracy",
    "https://blocklist.site/app/dl/porn",
    "https://blocklist.site/app/dl/proxy",
    "https://blocklist.site/app/dl/ransomware",
    "https://blocklist.site/app/dl/redirect",
    "https://blocklist.site/app/dl/scam",
    "https://blocklist.site/app/dl/spam",
    "https://blocklist.site/app/dl/torrent",
    "https://blocklist.site/app/dl/facebook",
    "https://blocklist.site/app/dl/youtube"
  )
  val blacklist = new BlacklistCreater().getBlacklist(blacklistUrls).toSet
  val config = new Conf()
  val sparkConfig = new SparkConf()
    .setMaster(config.sparkConf.master)
    .setAppName(config.sparkConf.appName)
    //.set("spark.executor.cores", config.sparkConf.executorCores)
    //.set("spark.dynamicAllocation.minExecutors",config.sparkConf.minExecutors)
    //.set("spark.dynamicAllocation.maxExecutors", config.sparkConf.maxExecutors)
  val sc = new SparkContext(config.sparkConf.master, config.sparkConf.appName)
  val data = sc.textFile(config.sparkConf.filePath).flatMap(line=> {
    try {
      val domain = line.split('"').lift(7).getOrElse("kasper.dk")
      val urlStr = domain.split("://") match {
        case parts if parts.length > 1 =>
          s"http://${parts(1)}"
        case parts if parts.length == 1 =>
          s"http://${parts(0)}"
      }
      val theTopDomain = Url.parse(urlStr).apexDomain
      logger.debug(s"$theTopDomain  ::: $urlStr :: $domain ")
      theTopDomain
    } catch {
      case upe: UriParsingException =>
        val sw = new StringWriter
        upe.printStackTrace(new PrintWriter(sw))
        logger.warn(s"upe: URL parse error" +
            s"upe: ${sw.toString}")
        Url.parse("http://kasper.dk").apexDomain
      case ex: Exception =>
        val sw = new StringWriter
        ex.printStackTrace(new PrintWriter(sw))
        logger.warn(s"ex: URL parse error" +
          s"ex: ${sw.toString}")
        Url.parse("http://kasper.dk").apexDomain
    }
  })

  //val d1 = data.filter(!blacklist.contains(_)).distinct()
  //val d1 = data.distinct().filter(domainName => !blacklist(domainName))
  val d1 = data.distinct()
  d1.coalesce(1).saveAsTextFile(config.sparkConf.savePath)
  sc.stop
}