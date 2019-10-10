import org.apache.spark.sql.SparkSession
import org.scalatest.FunSuite

class Test extends FunSuite {

  test("Redshift adapter can use common module") {
    Entity("id", NestedEntity("value"))
  }

  test("Redshift adapter can create spark session") {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("Simple Application")
      .getOrCreate()

    spark.stop()
  }
}
