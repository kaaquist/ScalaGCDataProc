import com.typesafe.scalalogging.LazyLogging

import scala.io
import scala.io.Codec
class BlacklistCreater extends LazyLogging {
  def getListFromURL(url: String,
    connectTimeout: Int = 5000,
  readTimeout: Int = 5000,
  requestMethod: String = "GET") = {
    import java.net.{URL, HttpURLConnection}
    val connection = (new URL(url)).openConnection.asInstanceOf[HttpURLConnection]
    connection.setConnectTimeout(connectTimeout)
    connection.setReadTimeout(readTimeout)
    connection.setRequestMethod(requestMethod)
    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
    val inputStream = connection.getInputStream
    // Charset needed to be set else there are errors.
    val content = io.Source.fromInputStream(inputStream)(Codec.ISO8859).mkString
    if (inputStream != null) inputStream.close
    content
  }

  def getBlacklist(lists: List[String]): List[String] = {
    val blacklist = for( url <- lists) yield getListFromURL(url).split("\n").toList
    blacklist.flatten
  }
}
