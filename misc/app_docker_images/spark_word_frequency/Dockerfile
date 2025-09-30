FROM apache/spark:4.0.1-scala2.13-java17-python3-r-ubuntu

USER root
RUN apt-get update && apt-get install -y cron
COPY . /opt/spark/work-dir
