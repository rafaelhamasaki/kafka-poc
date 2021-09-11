package io.github.yukiohama.processor;

import io.github.yukiohama.domain.OrderPlaced;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SpringBootApplication
public class ProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessorApplication.class, args);
    }

    public static class OrdersPlacedProcessor {

        @Bean
        public Consumer<KStream<String, OrderPlaced>> process() {
            return input -> input
                    .map((key, value) -> new KeyValue<>(value.getMerchantId(), value.getMerchantId()))
                    .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                    .count(Materialized.as("orders-placed"));
        }
    }

    @RestController
    public static class OrdersPlacedController {

        @Autowired
        private InteractiveQueryService interactiveQueryService;

        @GetMapping
        public List<OrderCount> list() {
            ReadOnlyKeyValueStore<String, Long> ordersPlacedStore =
                    interactiveQueryService.getQueryableStore("orders-placed", QueryableStoreTypes.keyValueStore());
            KeyValueIterator<String, Long> iterator = ordersPlacedStore.all();

            List<OrderCount> orderCountList = new ArrayList<>();

            while (iterator.hasNext()) {
                KeyValue<String, Long> next = iterator.next();
                orderCountList.add(new OrderCount(next.key, next.value));
            }

            return orderCountList;
        }
    }

    @Getter
    @AllArgsConstructor
    static class OrderCount {
        private String merchantId;
        private Long ordersPlaced;
    }
}
