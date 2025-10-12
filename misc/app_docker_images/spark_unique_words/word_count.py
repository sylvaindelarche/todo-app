
from pyspark.sql import SparkSession
from pyspark.sql.functions import explode, split, col
import psycopg2
import pika

# Fetch titles and names from database, data used for words counting
conn = psycopg2.connect(
    dbname="postgres",
    user="postgres",
    password="postgres",
    host="dev-db-1",
    port="5432"
)
data = []
try:
    with conn.cursor() as cur:
        cur = conn.cursor()
        cur.execute("SELECT title FROM list")
        lists = cur.fetchall()
        lists = [x[0] for x in lists]
        cur.execute("SELECT name FROM task")
        tasks = cur.fetchall()
        tasks = [x[0] for x in tasks]
        data = lists + tasks
finally:
    conn.close()

# Use Spark engine to count unique words
spark = SparkSession.builder \
    .appName("WordCountFromList") \
    .getOrCreate()
df = spark.createDataFrame(data, "string").toDF("value")
words_df = df.select(explode(split(col("value"), r"\W+")).alias("word"))
cleaned_df = words_df.filter(col("word") != "").selectExpr("lower(word) as word")
word_counts = cleaned_df.groupBy("word").count()
word_counts.show(truncate=False)
unique_words = word_counts.count()
spark.stop()

# Publish to RabbitMQ the count of unique words for main app to consume
connection = pika.BlockingConnection(pika.ConnectionParameters('dev-rabbitmq-1'))
channel = connection.channel()
channel.queue_declare(queue='queue.unique_words')
channel.basic_publish(exchange='metrics-exchange', routing_key='metrics', body=str(unique_words))
