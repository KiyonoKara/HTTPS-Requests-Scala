package util

/**
 * Created by KaNguy - 05/07/2021
 * File util.Verify.scala
 */

// URL
import java.net.{URLEncoder, URL, IDN, URI}
import java.nio.charset.StandardCharsets

// SSL & Security
import javax.net.ssl.{SSLSession, HostnameVerifier, SSLContext, TrustManager, X509TrustManager}
import java.security.cert.X509Certificate
import java.security.SecureRandom
import javax.net.ssl.HttpsURLConnection

object Verify {
  def encodeURLParameters(str: Iterable[(String, String)]): String = {
    str.map({
      case (k, v) =>
        s"""${URLEncoder.encode(k, "UTF-8")}=${URLEncoder.encode(v, "UTF-8")}"""
    }).mkString("&")
  }

  def createURL(url: String, urlParameters: Iterable[(String, String)] = Nil): String = {
    val newURL: URL = new URL(new URI(url).toASCIIString)
    val separator: String = if (newURL.getQuery != null) "&" else "?"
    val encodedURLParameters: String = Verify.encodeURLParameters(urlParameters)
    s"""${newURL}${separator}${encodedURLParameters}"""
  }
}

