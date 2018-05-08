#!/bin/bash

set -o nounset
set -e


###############################
#### MAIN
###############################
CORES=40


WORKING_DIR=/home/spark/test-sqs_playground/hansolyi
ASSEMBLY="$WORKING_DIR/sqs_playground-core-assembly.jar"
CONF=$WORKING_DIR/application.conf

echo "LAUNCHING sqs_playground with args $@"

jar uf $(readlink -f $ASSEMBLY) -C $WORKING_DIR $(basename $CONF) && \

spark-submit \
  --master spark://spark-dev-master-001.prod.dc3:7077 \
  --class com.videoamp.sqs_playground.SQSApp \
  --total-executor-cores $CORES \
  --conf spark.eventLog.enabled=false \
  --conf spark.ui.showConsoleProgress=false \
  --conf spark.sql.tungsten.enabled=true \
  --conf spark.executor.cores=5 \
  --conf spark.memory.offHeap.enabled=true \
  --conf spark.memory.offHeap.size=5g \
  --driver-memory 10g \
  --executor-memory 15g \
   $ASSEMBLY "$@"