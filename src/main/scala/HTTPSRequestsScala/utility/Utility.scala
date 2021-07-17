package HTTPSRequestsScala.utility

/**
 * Created by KaNguy - 05/07/2021
 * File HTTPSRequestsScala.utility.Utility.scala
 */

// URL
import java.net.{HttpURLConnection, URI, URL, URLEncoder}

// Collections
import scala.collection.mutable.ListBuffer

object Utility {
  /** Encodes URL parameters for queries
   *
   * @param str URL parameters as an iterable collection
   * @return String with the URL parameters in the URL format
   */
  def encodeURLParameters(str: Iterable[(String, String)]): String = {
    str.map({
      case (k, v) =>
        s"""${URLEncoder.encode(k, "UTF-8")}=${URLEncoder.encode(v, "UTF-8")}"""
    }).mkString("&")
  }

  /** Creates an URL with a method-based approach by using an URL and taking parameters in the form of an iterable collection
   *
   * @param url String with the URL
   * @param urlParameters URL parameters
   * @return Completed URL with the parameters
   */
  def createURL(url: String, urlParameters: Iterable[(String, String)] = Nil): String = {
    val newURL: URL = new URL(new URI(url).toASCIIString)
    if (urlParameters == Nil) return s"""$newURL"""
    val separator: String = if (newURL.getQuery != null) "&" else "?"
    val encodedURLParameters: String = Utility.encodeURLParameters(urlParameters)
    s"""$newURL$separator$encodedURLParameters"""
  }

  /** Gets a map key by its value
   *
   * @param map Collection map
   * @param value Value of the key
   * @return Key name
   */
  def getKeyByValue(map: Map[String, String] = Map.empty[String, String], value: String): String = {
    map.find(_._2.contains(value)).map(_._1).getOrElse("")
  }

  /** Manipulates the key and value, and returns it all in lower-case (this is not recommended for API key headers)
   *
   * @param map Map collection
   * @param key A valid key from the map
   * @return A new map that just has the lower-cased data
   */
  def lowerCaseSingleKV(map: Map[String, String] = Map.empty[String, String], key: String): Map[String, String] = {
    val fin: Map[String, String] = Map.empty[String, String]
    map.foreach(item => {
      if (item._1 == key) {
        return fin updated (item._1.toLowerCase, item._2.toLowerCase)
      }
    })
    fin
  }

  /** Creates a valid and parsable JSON string from a provided collection
   *
   * @param collections Map, List, Int, Boolean, and String are valid types if the collection is started off with a Map
   * @return JSON string
   */
  def CollectionsToJSON(collections: Any): String = {
    val JSON = new ListBuffer[String]()
    collections match {
      case map: Map[_, _] =>
        for ((k, v) <- map) {
          val key = k.asInstanceOf[String].replaceAll("\"" , "\\\\\"")
          v match {
            case map: Map[_, _] => JSON += s""""$key": ${CollectionsToJSON(map)}""";
            case list: List[_] => JSON += s""""$key": ${CollectionsToJSON(list)}""";
            case int: Int => JSON += s""""$key": $int""";
            case boolean: Boolean => JSON += s""""$key": $boolean""";
            case string: String => JSON += s""""$key": "${string.replaceAll("\"" , "\\\\\"")}""""
            case _ => ();
          }
        };

      case theList: List[_] =>
        val list = new ListBuffer[String]()
        for (listing <- theList) {
          listing match {
            case map: Map[_, _] => list += CollectionsToJSON(map);
            case caseList: List[_] => list += CollectionsToJSON(caseList);
            case int: Int => list += int.toString;
            case boolean: Boolean => list += boolean.toString;
            case string: String => list += s""""${string.replaceAll("\"" , "\\\\\"")}"""";
            case _ => ();
          }
        }

        return "[" + list.mkString(",") + "]"

      case _ => ();
    }

    val JSONString: String = "{" + JSON.mkString(",") + "}"
    JSONString
  }

  /** Parses JSON into a collection, primarily an immutable Map; this method is a medium for the JSON parser that is in this library
   *
   * @param json The JSON string
   * @return Typically a map
   */
  def JSONToCollections(json: String): Any = {
    JSON.parse(json)
  }

  def setHeaders(connection: HttpURLConnection, headers: Iterable[(String, String)] = Nil): Unit = {
    if (headers.nonEmpty) {
      headers foreach {
        case (key, value) =>
          try {
            connection.setRequestProperty(key, value)
          } catch {
            case _: Any => ()
          }
      }
    }
  }
}

