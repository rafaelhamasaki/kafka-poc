# Kafka POC

A very simple Spring Boot + Kafka proof of concept with a publisher, a simple subscriber and a stream processor.


## Projects

 - **domain** - Holds common domain events (only one in this PoC, `OrderPlaced`)
 -  **publisher** - Contains a REST endpoint where one can place an order, publishing an `OrderPlaced` event to the `orders` topic
 -  **subscriber** - Subscribes to the `orders` topic and prints `OrderPlaced` events to `System.out`
 -  **processor** - Subscribes to the `orders` topic and computes the number of orders placed by merchant

## Running

 1.  Run `docker-compose up` on the root folder
 2. Create the topic:
```
docker-compose exec broker kafka-topics \
   --create \
   --bootstrap-server localhost:9092 \
   --replication-factor 1 \
   --partitions 1 \
   --topic orders
```
3.  `mvn clean package` all projects
4. Execute the generated jars with `java -jar <jar-name>`

## Usage

Place orders with:
```
curl --location --request POST 'localhost:8080' \
--header 'Content-Type: application/json' \
--data-raw '{
    "merchantId": "string",
    "productId": "string"
}'
```
Orders will be printed on the terminal in which `subscriber` is running.

Number of orders per `merchantId` can be queried with `curl --location --request GET 'localhost:8090'`
