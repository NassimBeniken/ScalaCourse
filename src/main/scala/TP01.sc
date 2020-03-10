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