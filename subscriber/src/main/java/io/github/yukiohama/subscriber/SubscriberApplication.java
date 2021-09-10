package io.github.yukiohama.subscriber;

import io.github.yukiohama.domain.OrderPlaced;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class SubscriberApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriberApplication.class, args);
	}

	@Bean
	public Consumer<OrderPlaced> receive() {
		return System.out::println;
	}
}
