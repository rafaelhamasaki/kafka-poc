package io.github.yukiohama.publisher;

import io.github.yukiohama.domain.OrderPlaced;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class PublisherApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublisherApplication.class, args);
    }

    @RestController
    public static class OrderController {

        @Autowired
        private KafkaTemplate<String, OrderPlaced> template;

        @PostMapping
        @ResponseStatus(HttpStatus.ACCEPTED)
        public void placeOrder(@RequestBody Order order) {
            template.send("orders", new OrderPlaced(order.getMerchantId(), order.getProductId()));
        }
    }

    @NoArgsConstructor
    @Getter
    static class Order {
        private String merchantId;
        private String productId;
    }
}
