FROM apache/spark:4.0.1-scala2.13-java17-python3-r-ubuntu

USER root
RUN apt-get update && apt-get install -y cron
RUN pip install psycopg2-binary
RUN pip install pika
COPY . /opt/spark/work-dir
