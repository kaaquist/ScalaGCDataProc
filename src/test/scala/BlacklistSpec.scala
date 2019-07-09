import com.typesafe.scalalogging.LazyLogging
import org.scalatest.{FlatSpec, Matchers}

class BlacklistSpec extends FlatSpec with Matchers with LazyLogging {
  val blacklistCreater = new BlacklistCreater()
  it should "get a List[Strings] containing all blacklisted domains" in {
    val testUrls = List(
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
    val content = blacklistCreater.getBlacklist(testUrls)
    content.length > 2000000 should be(true)
  }

  it should "return true" in {
    val fruit = Set("apple", "orange", "peach", "banana")

    fruit("apple") should be(true)
  }
}
