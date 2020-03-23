package org.example

import org.apache.spark.sql.SparkSession
import org.json4s._
import org.json4s.jackson.JsonMethods._

case class Wine(id:Option[Int], country:Option[String], points:Option[Int], price:Option[Double], title:Option[String], variety:Option[String], winery:Option[String] ){

  def print_row(): Unit ={
    var row = "wine_row: " + this.id.getOrElse(None).toString + ", " + this.country.getOrElse(None).toString + ", " + this.points.getOrElse(None).toString + ", " + this.price.getOrElse(None).toString + ", " + this.title.getOrElse(None).toString + ", " + this.variety.getOrElse(None).toString + ", " + this.winery.getOrElse(None).toString
    println(row)
  }
}
// {"id":4,"country":"US","points":87,"price":65.0,"title":"Sweet Cheeks 2012 ","variety":"Pinot Noir","winery":"Sweet Cheeks"}
object JsonReader extends App {
//создать сессию спарка
  val spark = SparkSession.builder().master("local").getOrCreate()
  val filename = args(0)
 // val filename = "/home/mary/HardDisk/git_my_projects/de_hw4_scala_json_reader/winemag-data-130k-v2.json"
  //получить доступ к распред.кластеру и прочитать файл
  val file_text = spark.sparkContext.textFile(filename)

  file_text.foreach( f = row => {
    implicit val formats = DefaultFormats
    val json_row = parse(row)
    val id_s:Option[String] = Some(pretty(json_row \\ "id"))
    var country:Option[String] = Some(pretty(json_row \\ "country"))
    val points_s:Option[String] = Some(pretty(json_row \\ "points"))
    val price_s:Option[String] = Some(pretty(json_row \\ "price"))
    var title:Option[String] = Some(pretty(json_row \\ "title"))
    var variety:Option[String] = Some(pretty(json_row \\ "variety"))
    var winery:Option[String] = Some(pretty(json_row \\ "winery"))

    val id:Option[Int] = if (id_s.get == "{ }") None else Some(id_s.get.toInt)
    val points:Option[Int] = if (points_s.get == "{ }") None else Some(points_s.get.toInt)
    val price:Option[Double] = if (price_s.get == "{ }") None else Some(price_s.get.toDouble)
    country = if (country.get == "{ }") None else  Some(country.get)
    variety = if (variety.get == "{ }") None else  Some(variety.get)
    title = if (title.get == "{ }") None else  Some(title.get)
    winery = if (winery.get == "{ }") None else  Some(winery.get)
    // выводим на экран через класс Wine
    Wine(id,country, points, price, title, variety, winery).print_row()
  })
}
