#!/bin/bash

# --- create topics **********************
kafka-topics --zookeeper localhost:2181 --list --topic "it21-streaming" | grep "it21-streaming"
if [ $? != 0 ]; then
  kafka-topics --zookeeper localhost:2181 --create --topic it21-streaming --partitions 1 --replication-factor 1
fi
kafka-topics --zookeeper localhost:2181 --list --topic "stock-streaming-source" | grep "stock-streaming-source"
if [ $? != 0 ]; then
  kafka-topics --zookeeper localhost:2181 --create --topic stock-streaming-source --partitions 1 --replication-factor 1
fi
kafka-topics --zookeeper localhost:2181 --list --topic stock-streaming-sink | grep "stock-streaming-sink"
if [ $? != 0 ]; then
  kafka-topics --zookeeper localhost:2181 --create --topic stock-streaming-sink --partitions 1 --replication-factor 1
fi

# --- list all topics
kafka-topics --zookeeper localhost:2181 --list


# --- register employee schema *****************************
# register key schema
curl -X GET "http://localhost:8081/subjects" | grep "it21-streaming-key"
if [ $? == 0 ]; then
  curl -X DELETE "http://localhost:8081/subjects/it21-streaming-key"
fi
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  --data '{"schema": "{\"type\": \"string\"}"}' \
  "http://localhost:8081/subjects/it21-streaming-key/versions"
# register value schema
curl -X GET "http://localhost:8081/subjects" | grep "it21-streaming-value"
if [ $? == 0 ]; then
  curl -X DELETE "http://localhost:8081/subjects/it21-streaming-value"
fi
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  --data '{"schema": "{\"type\": \"record\", \"namespace\": \"com.it21learning.streaming.model\", \"name\": \"Employee\", \"fields\": [{\"name\": \"name\", \"type\": \"string\"}, {\"name\": \"age\", \"type\": \"int\"}, {\"name\": \"gender\", \"type\": \"string\"}, {\"name\": \"title\", \"type\": \"string\"}, {\"name\": \"department\", \"type\": \"string\"}, {\"name\": \"webUrl\", \"type\": \"string\"}]}"}' \
  "http://localhost:8081/subjects/it21-streaming-value/versions"


# --- register stock schema *****************************
curl -X GET "http://localhost:8081/subjects" | grep "stock-streaming-source-key"
if [ $? == 0 ]; then
  curl -X DELETE "http://localhost:8081/subjects/stock-streaming-source-key"
fi
# register key schema
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  --data '{"schema": "{\"type\": \"string\"}"}' \
  "http://localhost:8081/subjects/stock-streaming-source-key/versions"
# register value schema
curl -X GET "http://localhost:8081/subjects" | grep "stock-streaming-source-value"
if [ $? == 0 ]; then
  curl -X DELETE "http://localhost:8081/subjects/stock-streaming-source-value"
fi
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  --data '{"schema": "{\"namespace\": \"com.datafibers.kafka.streams.avro\", \"type\": \"record\", \"name\": \"Stock\", \"fields\": [{\"name\": \"refresh_time\", \"type\": \"string\"}, {\"name\": \"symbol\", \"type\": \"string\"}, {\"name\": \"company_name\", \"type\": \"string\"}, {\"name\": \"exchange\", \"type\": \"string\"}, {\"name\": \"open_price\", \"type\": \"double\"}, {\"name\": \"ask_price\", \"type\": \"double\"}, {\"name\": \"ask_size\", \"type\": \"int\"}, {\"name\": \"bid_price\", \"type\": \"double\"}, {\"name\": \"bid_size\", \"type\": \"int\"}, {\"name\": \"price\", \"type\": \"double\"}]}"}' \
  "http://localhost:8081/subjects/stock-streaming-source-value/versions"


# --- register summary schema *****************************
# register key schema
curl -X GET "http://localhost:8081/subjects" | grep "stock-streaming-sink-key"
if [ $? == 0 ]; then
  curl -X DELETE "http://localhost:8081/subjects/stock-streaming-sink-key"
fi
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  --data '{"schema": "{\"type\": \"string\"}"}' \
  "http://localhost:8081/subjects/stock-streaming-sink-key/versions"
# register value schema
curl -X GET "http://localhost:8081/subjects" | grep "stock-streaming-sink-value"
if [ $? == 0 ]; then
  curl -X DELETE "http://localhost:8081/subjects/stock-streaming-sink-value"
fi
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  --data '{"schema": "{\"namespace\": \"com.it21learning.streaming.stock\", \"type\": \"record\", \"name\": \"Summary\", \"fields\": [{\"name\": \"start_time\", \"type\": \"string\"}, {\"name\": \"end_time\", \"type\": \"string\"}, {\"name\": \"symbol\", \"type\": \"string\"}, {\"name\": \"company_name\", \"type\": \"string\"}, {\"name\": \"open_price\", \"type\": \"double\"}, {\"name\": \"ask_price_mean\", \"type\": \"double\"}, {\"name\": \"ask_price_error\", \"type\": \"double\"}, {\"name\": \"bid_price_mean\", \"type\": \"double\"}, {\"name\": \"bid_price_error\", \"type\": \"double\"}, {\"name\": \"price_mean\", \"type\": \"double\"}, {\"name\": \"price_change\", \"type\": \"int\"}]}"}' \
  "http://localhost:8081/subjects/stock-streaming-sink-value/versions"


# list all schemas
curl -X GET "http://localhost:8081/subjects"
# check all versions
curl -X GET "http://localhost:8081/subjects/it21-streaming-key/versions"
curl -X GET "http://localhost:8081/subjects/it21-streaming-value/versions"
# by version id
curl -X GET "http://localhost:8081/subjects/it21-streaming-key/versions/1"
curl -X GET "http://localhost:8081/subjects/it21-streaming-value/versions/1"


# delete schema
curl -X DELETE "http://localhost:8081/subjects/it21-streaming-key"
