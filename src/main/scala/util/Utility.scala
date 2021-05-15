package util

/**
 * Created by KaNguy - 05/07/2021
 * File util.Utility.scala
 */

// URL
import java.net.{URLEncoder, URL, URI}

// JSON

object Utility {
  def encodeURLParameters(str: Iterable[(String, String)]): String = {
    str.map({
      case (k, v) =>
        s"""${URLEncoder.encode(k, "UTF-8")}=${URLEncoder.encode(v, "UTF-8")}"""
    }).mkString("&")
  }

  def createURL(url: String, urlParameters: Iterable[(String, String)] = Nil): String = {
    val newURL: URL = new URL(new URI(url).toASCIIString)
    if (urlParameters == Nil) return s"""$newURL"""
    val separator: String = if (newURL.getQuery != null) "&" else "?"
    val encodedURLParameters: String = Utility.encodeURLParameters(urlParameters)
    s"""$newURL$separator$encodedURLParameters"""
  }

  def getKeyByValue(map: Map[String, String] = Map.empty[String, String], value: String): String = {
    map.find(_._2.contains(value)).map(_._1.toString).getOrElse("")
  }

  def lowerCaseSingleKV(map: Map[String, String] = Map.empty[String, String], key: String): Map[String, String] = {
    val fin: Map[String, String] = Map.empty[String, String]
    map.foreach(item => {
      if (item._1 == key) {
        return fin updated (item._1.toLowerCase, item._2.toLowerCase)
      }
    })
    fin
  }

  def MapToJSON(map: Map[String, String]): String = {
    var json: String = ""

    var iterator: Int = 0
    map.foreach(i => {
      if (iterator == map.size - 1) {
        json += f"${'"'}${i._1}${'"'}" + ": " + f"${'"'}${i._2}${'"'}"
      } else {
        json += f"${'"'}${i._1}${'"'}" + ": " + f"${'"'}${i._2}${'"'}${','}" + " "
      }
      iterator += 1
    })
    json = "{" + json + "}"
    json
  }
}

