file://<WORKSPACE>/src/main/scala/BroadcastingSpark.scala
### file%3A%2F%2F%2FUsers%2Fsmukka%2FDownloads%2FOptionalTasksSpark-main%2Fsrc%2Fmain%2Fscala%2FBroadcastingSpark.scala:34: error: `;` expected but `val` found
/    val broadcastedUsers = broadcast(usersData)
     ^

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.12.19
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.12.19/scala-library-2.12.19.jar [exists ]
Options:



action parameters:
uri: file://<WORKSPACE>/src/main/scala/BroadcastingSpark.scala
text:
```scala
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
    .csv("<WORKSPACE>/src/main/resources/transaction_data.csv")
    
    val usersData = spark.read
    .option("header","true")
    .option("inferSchema", "true")
    .csv("<WORKSPACE>/src/main/resources/user_data.csv")

    // using groupBy
    // val startTimeWithoutBroadcast = System.nanoTime()
     val joinedDF = transactionsData.join(usersData, "user_id")
    // val groupByResult = joinedDF.groupBy("name").sum("amount")
    // val endTimeWithoutBroadcast = System.nanoTime()
    // println(s"Time without broadcast: ${(endTimeWithoutBroadcast - startTimeWithoutBroadcast) / 1e9d} seconds")
    // groupByResult.write
    // .option("header","true")
    // .csv("<WORKSPACE>/src/main/resources/GroupedData")

    //use groupby using broadcast
//     val startTimeWithBroadcast = System.nanoTime()
/    val broadcastedUsers = broadcast(usersData)
//     val joinWithBroadcast = transactionsData.join(broadcastedUsers, "user_id")
//     val groupByBroadcastResult = joinWithBroadcast.groupBy("name").sum("amount")
//     groupByBroadcastResult.explain()
//     val endTimeWithBroadcast = System.nanoTime()
//     println(s"Time with broadcast: ${(endTimeWithBroadcast - startTimeWithBroadcast) / 1e9d} seconds")
//     groupByBroadcastResult.write
//     .option("header","true")
//     .csv("<WORKSPACE>/src/main/resources/GroupedByBroadcast")
    
    //use WindowPartiton
    // val startTimeWithoutWindow = System.nanoTime()
    // val windowSpec = Window.partitionBy("name")
    // val groupByWithoutWindow = joinedDF.withColumn("total_amount", sum("amount").over(windowSpec)).select("name", "total_amount").distinct()
    // val endTimeWithoutWindow = System.nanoTime()
    // println(s"Time with window partitioning and no broadcast: ${(endTimeWithoutWindow - startTimeWithoutWindow) / 1e9d} seconds")
    // groupByWithoutWindow.write.option("header", "true").csv("<WORKSPACE>/src/main/resources/windowPartition")

    //use WindowPartiton using broadcast
    val startTimeWithWindow = System.nanoTime()
    val windowSpec = Window.partitionBy("name")
    val groupByWithWindow = joinWithBroadcast.withColumn("total_amount", sum("amount").over(windowSpec)).select("name", "total_amount").distinct()
    val endTimeWithWindow = System.nanoTime()
    println(s"Time with window partitioning with broadcast: ${(endTimeWithWindow - startTimeWithWindow) / 1e9d} seconds")
    groupByWithWindow.write.option("header", "true").csv("<WORKSPACE>/src/main/resources/windowPartitionByPartition")

    Thread.sleep(100000)
    spark.stop()
}
```



#### Error stacktrace:

```
scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$expectAt(ScalametaParser.scala:389)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$expectAt(ScalametaParser.scala:393)
	scala.meta.internal.parsers.ScalametaParser.expect(ScalametaParser.scala:395)
	scala.meta.internal.parsers.ScalametaParser.accept(ScalametaParser.scala:411)
	scala.meta.internal.parsers.ScalametaParser.acceptStatSep(ScalametaParser.scala:443)
	scala.meta.internal.parsers.ScalametaParser.acceptStatSepOpt(ScalametaParser.scala:445)
	scala.meta.internal.parsers.ScalametaParser.statSeqBuf(ScalametaParser.scala:4094)
	scala.meta.internal.parsers.ScalametaParser.getStats$2(ScalametaParser.scala:4124)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$scala$meta$internal$parsers$ScalametaParser$$templateStatSeq$3(ScalametaParser.scala:4125)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$scala$meta$internal$parsers$ScalametaParser$$templateStatSeq$3$adapted(ScalametaParser.scala:4123)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$listBy(ScalametaParser.scala:555)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$templateStatSeq(ScalametaParser.scala:4123)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$templateStatSeq(ScalametaParser.scala:4115)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$templateBody$1(ScalametaParser.scala:3993)
	scala.meta.internal.parsers.ScalametaParser.inBracesOr(ScalametaParser.scala:254)
	scala.meta.internal.parsers.ScalametaParser.inBraces(ScalametaParser.scala:250)
	scala.meta.internal.parsers.ScalametaParser.templateBody(ScalametaParser.scala:3993)
	scala.meta.internal.parsers.ScalametaParser.templateBodyOpt(ScalametaParser.scala:3996)
	scala.meta.internal.parsers.ScalametaParser.templateAfterExtends(ScalametaParser.scala:3947)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$template$1(ScalametaParser.scala:3963)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:319)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:363)
	scala.meta.internal.parsers.ScalametaParser.template(ScalametaParser.scala:3952)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$templateOpt$1(ScalametaParser.scala:3987)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:319)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:363)
	scala.meta.internal.parsers.ScalametaParser.templateOpt(ScalametaParser.scala:3980)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$objectDef$1(ScalametaParser.scala:3706)
	scala.meta.internal.parsers.ScalametaParser.autoEndPos(ScalametaParser.scala:366)
	scala.meta.internal.parsers.ScalametaParser.autoEndPos(ScalametaParser.scala:371)
	scala.meta.internal.parsers.ScalametaParser.objectDef(ScalametaParser.scala:3698)
	scala.meta.internal.parsers.ScalametaParser.tmplDef(ScalametaParser.scala:3585)
	scala.meta.internal.parsers.ScalametaParser.topLevelTmplDef(ScalametaParser.scala:3573)
	scala.meta.internal.parsers.ScalametaParser$$anonfun$2.applyOrElse(ScalametaParser.scala:4108)
	scala.meta.internal.parsers.ScalametaParser$$anonfun$2.applyOrElse(ScalametaParser.scala:4102)
	scala.PartialFunction.$anonfun$runWith$1$adapted(PartialFunction.scala:145)
	scala.meta.internal.parsers.ScalametaParser.statSeqBuf(ScalametaParser.scala:4094)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$batchSource$13(ScalametaParser.scala:4304)
	scala.Option.getOrElse(Option.scala:189)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$batchSource$1(ScalametaParser.scala:4304)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:319)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:363)
	scala.meta.internal.parsers.ScalametaParser.batchSource(ScalametaParser.scala:4261)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$source$1(ScalametaParser.scala:4255)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:319)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:363)
	scala.meta.internal.parsers.ScalametaParser.source(ScalametaParser.scala:4255)
	scala.meta.internal.parsers.ScalametaParser.entrypointSource(ScalametaParser.scala:4259)
	scala.meta.internal.parsers.ScalametaParser.parseSourceImpl(ScalametaParser.scala:119)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$parseSource$1(ScalametaParser.scala:116)
	scala.meta.internal.parsers.ScalametaParser.parseRuleAfterBOF(ScalametaParser.scala:58)
	scala.meta.internal.parsers.ScalametaParser.parseRule(ScalametaParser.scala:53)
	scala.meta.internal.parsers.ScalametaParser.parseSource(ScalametaParser.scala:116)
	scala.meta.parsers.Parse$.$anonfun$parseSource$1(Parse.scala:30)
	scala.meta.parsers.Parse$$anon$1.apply(Parse.scala:37)
	scala.meta.parsers.Api$XtensionParseDialectInput.parse(Api.scala:22)
	scala.meta.internal.semanticdb.scalac.ParseOps$XtensionCompilationUnitSource.toSource(ParseOps.scala:15)
	scala.meta.internal.semanticdb.scalac.TextDocumentOps$XtensionCompilationUnitDocument.toTextDocument(TextDocumentOps.scala:179)
	scala.meta.internal.pc.SemanticdbTextDocumentProvider.textDocument(SemanticdbTextDocumentProvider.scala:54)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$semanticdbTextDocument$1(ScalaPresentationCompiler.scala:462)
```
#### Short summary: 

file%3A%2F%2F%2FUsers%2Fsmukka%2FDownloads%2FOptionalTasksSpark-main%2Fsrc%2Fmain%2Fscala%2FBroadcastingSpark.scala:34: error: `;` expected but `val` found
/    val broadcastedUsers = broadcast(usersData)
     ^