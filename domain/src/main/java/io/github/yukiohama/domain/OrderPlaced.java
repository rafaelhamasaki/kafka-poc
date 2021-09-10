package io.github.yukiohama.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderPlaced {
    private String merchantId;
    private String productId;
}
