# Messaging with Spring Boot — Learning Path (Basic to Advanced)
> Study these projects in order.
> Folder: `5. Messaging with Spring Boot`
> Prerequisite: comfortable with REST + JPA (sections 2–3).

---

## Why This Section?

Real systems don't make every caller wait. When a user registers, you don't want the HTTP request to hang while an email is sent. Messaging lets one service hand off work to another asynchronously. RabbitMQ and Kafka are the two you'll see in job postings — learn the mental model once and both make sense.

---

## Learning Order

```
Step 1 → rabbitmq-message-with-spring-boot-3   (Easier mental model: queues) ← START HERE
Step 2 → kafka-message-with-spring-boot-3       (Streaming log, multi-service)
```

> Do RabbitMQ first — it's a single module and the queue model is simpler. Kafka is split into producer + consumer services, closer to microservices.

---

## Core Mental Model (read before both)

```
Producer → [ Broker ] → Consumer
   |           |            |
 sends      stores       receives & processes
 a message  the message  asynchronously

The producer does NOT wait for the consumer.
If the consumer is down, the broker holds the message.
```

---

## Step 1 — `rabbitmq-message-with-spring-boot-3` ← START HERE

### What to learn
- The three RabbitMQ pieces: **Exchange → Binding → Queue** (`RabbitMqConfig`)
- Producing a message (`RabbitMQProducer` / `NotificationProducer`) from a REST call
- Consuming with `@RabbitListener` (`NotificationConsumer`) and saving to the DB
- DTO (`NotificationRequest`) as the message payload, JPA entity (`Notification`) for storage

### Key concept
```
You publish to an EXCHANGE, not directly to a queue.
A BINDING (with a routing key) decides which QUEUE the message lands in.
A @RabbitListener on that queue processes it.
This indirection is what makes routing flexible.
```

### Run it
- Start RabbitMQ (Docker: `docker run -p 5672:5672 -p 15672:15672 rabbitmq:management`)
- `POST` a notification to the controller → watch the consumer log + DB row appear
- Open the management UI at `http://localhost:15672` (guest/guest) and watch the queue

### Questions to answer
1. What is the difference between an exchange, a binding, and a queue?
2. What does the routing key do?
3. What happens to a message if the consumer is offline when it's sent?
4. What is a dead-letter queue and when do you need one?

### Practice exercise
Add a second queue + listener (e.g. an "audit" consumer) bound to the same exchange, so one published message fans out to BOTH consumers.

---

## Step 2 — `kafka-message-with-spring-boot-3`

### What to learn
- Two separate Spring Boot apps: `producer-service` and `consumer-services`
- Topic config (`KafkaTopConfig`), producer config (`ProducerKafkaConfig`), consumer config (`ConsumerKafkaConfig`)
- Sending with `KafkaTemplate`, receiving with `@KafkaListener`
- Consumer persists the notification via JPA

### Key concept
```
Kafka is a distributed, append-only LOG (not a queue you drain).
Messages live in a TOPIC, split into PARTITIONS, and are kept even after being read.
Consumers track their own OFFSET → they can replay history.
Great for high throughput, event sourcing, and many independent consumers.
```

### Run it
- Start Kafka (Docker compose, or Confluent/Bitnami image)
- Run `producer-service`, then `consumer-services`
- `POST` to the producer's `NotificationController` → see the consumer receive + persist it

### Questions to answer
1. How is a Kafka topic different from a RabbitMQ queue? (Hint: messages stay vs. get consumed.)
2. What is a partition, and how does it relate to parallelism?
3. What is a consumer group and a consumer offset?
4. When would you choose Kafka over RabbitMQ, and vice versa?

### Practice exercise
Run a second instance of `consumer-services` in the SAME consumer group and observe partitions being split between them. Then change the group id and see both instances receive every message.

---

## RabbitMQ vs. Kafka — Cheat Sheet

| | RabbitMQ | Kafka |
|---|---|---|
| Model | Message queue (broker) | Distributed log |
| After read | Message removed | Message retained (replayable) |
| Best for | Task queues, routing, RPC | High-throughput streams, event sourcing |
| Ordering | Per-queue | Per-partition |
| Mental difficulty | Lower | Higher |

---

## Summary Table

| Step | Project | Core Concept | Priority |
|---|---|---|---|
| 1 | rabbitmq | Exchange/binding/queue, @RabbitListener | 🔥 START |
| 2 | kafka | Topics/partitions/offsets, @KafkaListener | ✅ |

---

## After This Section → Testing

You can now build async, multi-service systems. Next (`6. Testing with Spring Boot`) you prove they actually work — unit, slice, and integration tests.