/**
 * Created by KaNguy - 04/16/2021
 * File Convert.scala
 */

package util

// Util
import java.util

// Collections
import scala.collection.mutable

/** Class - Convert
 *
 * @param hashMap - HashMap with any value
 * @param array2d - 2D Array
 */
class Convert(var hashMap: mutable.HashMap[Any, Any] = mutable.HashMap.empty[Any, Any], var array2d: Array[Array[Any]] = Array.empty[Array[Any]]) {
  /** Method which converts 2D Array into HashMap
   *
   * @param array2d - 2D Array must only have an array of 2 values per index for this algorithm to work
   * @return HashMap[Any, Any]
   */
  def From2DtoHashMapMAX2(array2d: Array[Array[Any]] = this.array2d): mutable.HashMap[Any, Any] = {
    // Declare an empty HashMap
    val hashMap = mutable.HashMap.empty[Any, Any]
    // If it is empty, return it
    if (array2d.isEmpty) mutable.HashMap.empty[Any, Any]
    else {
      // First iteration of the 2D Array
      for (i <- array2d) {
        // ArrayList that can be added upon in the second iteration
        val regularArrayList: util.ArrayList[Any] = new util.ArrayList[Any]()
        // Iteration over an array in the 2D array
        for (j <- i) {
          // Adds the elements of it until it reaches the limit
          regularArrayList.add(j)
          if (regularArrayList.size() == i.length) {
            // Puts the elements in the HashMap
            hashMap.put(regularArrayList.get(0), regularArrayList.get(1))
          }
        }
      }
    }
    // Return
    hashMap
  }
}

// Testing for class, Convert
object Convert extends App {
  val convert: Convert = new Convert()
  val hash = convert.From2DtoHashMapMAX2(Array(Array("First-1", "Second-1"), Array("First-2", "Second-2")))
  println(hash)
}
