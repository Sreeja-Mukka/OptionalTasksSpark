import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.broadcast
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

object BroadcastingSpark extends App {
    val spark = SparkSession.builder
        .appName("BroadCast-Spark")
        .master("local[*]")
        .getOrCreate()

    val transactionsData = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("/Users/smukka/Downloads/OptionalTasksSpark-main/src/main/resources/transaction_data.csv")
    
    val usersData = spark.read
    .option("header","true")
    .option("inferSchema", "true")
    .csv("/Users/smukka/Downloads/OptionalTasksSpark-main/src/main/resources/user_data.csv")

    // using groupBy
    // // val startTimeWithoutBroadcast = System.nanoTime()
      val joinedDF = transactionsData.join(usersData, "user_id")
    // // val groupByResult = joinedDF.groupBy("name").sum("amount")
    // // val endTimeWithoutBroadcast = System.nanoTime()
    // // println(s"Time without broadcast: ${(endTimeWithoutBroadcast - startTimeWithoutBroadcast) / 1e9d} seconds")
    // // groupByResult.write
    // // .option("header","true")
    // // .csv("/Users/smukka/Downloads/OptionalTasksSpark-main/src/main/resources/GroupedData")

    //use groupby using broadcast
    // val startTimeWithBroadcast = System.nanoTime()

      val broadcastedUsers = broadcast(usersData)
      val joinWithBroadcast = transactionsData.join(broadcastedUsers, "user_id")

    // val groupByBroadcastResult = joinWithBroadcast.groupBy("name").sum("amount")

    // groupByBroadcastResult.explain()
    // val endTimeWithBroadcast = System.nanoTime()
    // println(s"Time with broadcast: ${(endTimeWithBroadcast - startTimeWithBroadcast) / 1e9d} seconds")
    // groupByBroadcastResult.write
    // .option("header","true")
    // .csv("/Users/smukka/Downloads/OptionalTasksSpark-main/src/main/resources/GroupedByBroadcast")
    
    //use WindowPartiton
    // val startTimeWithoutWindow = System.nanoTime()
    // val windowSpec = Window.partitionBy("name")
    // val groupByWithoutWindow = joinedDF.withColumn("total_amount", sum("amount").over(windowSpec)).select("name", "total_amount").distinct()
    // val endTimeWithoutWindow = System.nanoTime()
    // println(s"Time with window partitioning and no broadcast: ${(endTimeWithoutWindow - startTimeWithoutWindow) / 1e9d} seconds")
    // groupByWithoutWindow.write.option("header", "true").csv("/Users/smukka/Downloads/OptionalTasksSpark-main/src/main/resources/windowPartition")

    //use WindowPartiton using broadcast
    val startTimeWithWindow = System.nanoTime()
    val windowSpec = Window.partitionBy("name")
    val groupByWithWindow = joinWithBroadcast.withColumn("total_amount", sum("amount").over(windowSpec)).select("name", "total_amount").distinct()
    val endTimeWithWindow = System.nanoTime()
    println(s"Time with window partitioning with broadcast: ${(endTimeWithWindow - startTimeWithWindow) / 1e9d} seconds")
    groupByWithWindow.write.option("header", "true").csv("/Users/smukka/Downloads/OptionalTasksSpark-main/src/main/resources/windowPartitionByPartition")
    Thread.sleep(100000)
    spark.stop()
}