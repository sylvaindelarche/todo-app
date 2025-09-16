from pyspark.sql import SparkSession
from pyspark.sql.functions import explode, split, col

# Create Spark session
spark = SparkSession.builder \
    .appName("WordCountFromList") \
    .master("spark://dev-network:7077") \
    .getOrCreate()

data = [
    "hello world",
    "spark is fun",
    "hello spark"
]

# Convert list into DataFrame
df = spark.createDataFrame(data, "string").toDF("value")

# Split lines into words
words_df = df.select(explode(split(col("value"), r"\W+")).alias("word"))

# Clean and lowercase words
cleaned_df = words_df.filter(col("word") != "").selectExpr("lower(word) as word")

# Count words
word_counts = cleaned_df.groupBy("word").count()

# Show only words with frequency > 1
word_counts.filter("count > 1").show(truncate=False)

spark.stop()
