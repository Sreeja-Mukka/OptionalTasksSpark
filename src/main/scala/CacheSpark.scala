import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

object CacheSpark extends App {

val spark = SparkSession.builder
    .appName("Factorial Without Caching")
    .master("local[*]")
    .config("spark.ui.port", "4061")
    .getOrCreate()

val data = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("/Users/smukka/Downloads/OptionalTasksSpark-main/src/main/resources/SalesData.csv")

val salesDF = data.toDF("id", "date", "amount", "category")

salesDF.cache()

import spark.implicits._

val highValueSales = salesDF.filter($"amount" > 100)

val averageSalesPerDay = salesDF.groupBy("date").avg("amount")

val salesByCategory = salesDF.groupBy("category").count()

val sortedSales = salesDF.orderBy($"amount".desc)

val selectedColumns = salesDF.select($"id", $"amount")

val withTax = salesDF.withColumn("amountWithTax", $"amount" * 1.1)

val renamedDF = salesDF.withColumnRenamed("amount", "transactionAmount")

val distinctCategories = salesDF.select("category").distinct()

val withoutDate = salesDF.drop("date")

val salesWithStatus = salesDF.withColumn("status", when($"amount" > 150, "High").otherwise("Low"))

val detailedAgg = salesDF.groupBy("date").agg(avg($"amount").alias("average"), sum($"amount").alias("total"))

val usersDF = Seq((1, "John"), (2, "Jane")).toDF("user_id", "name")
val joinedDF = salesDF.join(usersDF, salesDF("id") === usersDF("user_id"))

val pivotTable = salesDF.groupBy("date").pivot("category").sum("amount")

val windowSpec = Window.partitionBy("category").orderBy("date")
val cumulativeSum = salesDF.withColumn("cumulativeSum", sum($"amount").over(windowSpec))

val filteredByCategory = salesDF.filter($"category".rlike("Electr.*"))


val startTimeWithoutBroadcast = System.nanoTime()

highValueSales.show()
averageSalesPerDay.show()
salesByCategory.show()
sortedSales.show()
selectedColumns.show()
withTax.show()
renamedDF.show()
distinctCategories.show()
withoutDate.show()
salesWithStatus.show()
detailedAgg.show()
joinedDF.show()
pivotTable.show()
cumulativeSum.show()
filteredByCategory.show()

val endTimeWithoutBroadcast = System.nanoTime()
println(s"Time without cache : ${(endTimeWithoutBroadcast - startTimeWithoutBroadcast) / 1e9d} seconds")

spark.stop()
}