# 前置
本机3kafka节点

# 命令
## 创建topic
kafka-topics.sh --zookeeper localhost:2181 --create --topic test32 --partitions 3 - -replication-factor 2

## 生产者
./kafka-console-producer.sh --broker-list localhost:9093,localhost:9092,localhost:9094 --topic test32

## 消费者
./kafka-console-consumer.sh --bootstrap-server localhost:9093,localhost:9092,localhost:9094 --topic test32