#!/bin/sh
export JAVA_HOME=/opt/java/openjdk
export SPARK_HOME=/opt/spark
export PATH=$JAVA_HOME/bin:$SPARK_HOME/bin:$PATH

spark-submit --master local[2] /opt/spark/work-dir/word_count.py