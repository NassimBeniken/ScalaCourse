import java.text.SimpleDateFormat
import scala.collection.mutable.ListBuffer
import scala.io.Source

def convertTimestampToDate(ts:BigInt):String = {
  val tst = ts * 1000L
  val df:SimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
  df.format(tst.toLong)
}

def main() = {
  val filename = "/Users/beni/Developments/GridTools/src/main/scala/file.txt"
  /******* By hours, all the connected hosts, host which get the most connections and the most recent host connection *******/
  val lines: Seq[String] = Source.fromFile(filename)
      .mkString
      .split("\n")
  val groupLinesByHour: Map[Int, Seq[String]] = lines
      .groupBy(x => convertTimestampToDate(x.split(" ")(0).toInt)
      .split(" ")(1)
      .split(":")(0).toInt)
  for((k,v) <- groupLinesByHour) {
    var hostsList = ListBuffer[String]()
    var hostsMap = Map[Int, String]()
    println("Hosts connected at " + k + "h :")
    for(hosts <- v ) {
      hostsList += hosts.split(" ")(1)
      hostsList += hosts.split(" ")(2)
      hostsMap += hosts.split(" ")(0).toInt -> (hosts.split(" ")(1) + " (or " + hosts.split(" ")(2) + ")")
    }
    val mostRecentHosts = hostsMap.maxBy(_._1)._2
    val maxHost = hostsList.groupBy(identity)
        .mapValues(_.size)
        .maxBy(_._2)._1
    hostsList = hostsList.distinct
    for(host <- hostsList)
      println("- " + host)
    println("Host with the most connections at this hour       : " + maxHost)
    println("Host with the most recent connection at this hour : " + mostRecentHosts + "\n")
  }
}

main()


//def main2() = {
//  val filename = "/Users/beni/Developments/GridTools/src/main/scala/file.txt"
//  var actualHour = convertTimestampToDate(Source.fromFile(filename)
//    .getLines()
//    .take(1)
//    .mkString
//    .split(" ")(0).toInt)
//    .split(" ")(1)
//    .split(":")(0).toInt
//  var hostsList = ListBuffer[String]()
//  for(line <- Source.fromFile(filename).getLines()) {
//    val hour = convertTimestampToDate(line
//      .split(" ")(0).toInt)
//      .split(" ")(1)
//      .split(":")(0).toInt
//      if(hour == actualHour) {
//        hostsList += line.split(" ")(1)
//        hostsList += line.split(" ")(2)
//      }
//      else {
//        hostsList = hostsList.distinct
//        print("Hôtes connectés à " + actualHour + "h :\n")
//        for(host <- hostsList) {
//          print("- " + host + "\n")
//        }
//        actualHour = hour
//        hostsList.remove(0, hostsList.size)
//        hostsList += line.split(" ")(1)
//        hostsList += line.split(" ")(2)
//      }
//  }
//  hostsList = hostsList.distinct
//  print("Hôtes connectés à " + actualHour + "h :\n")
//  for(host <- hostsList) {
//    print("- " + host + "\n")
//  }
//  print("Hôtes ayant la connexion la plus récente :\n")
//  print("- " + hostsList.takeRight(2)(0) + "\n")
//  print("- " + hostsList.takeRight(2)(1) + "\n")
//}
//
//main2()