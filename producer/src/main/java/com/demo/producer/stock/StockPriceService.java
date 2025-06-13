package com.demo.producer.stock;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class StockPriceService {

    private final Random random = new Random();

    /**
     * Genera un stream infinito de precios para un símbolo de acción dado.
     * Emite un nuevo precio cada segundo.
     * @param symbol El símbolo de la acción (ej. "AAPL").
     * @return un Flux de StockPrice.
     */
    public Flux<StockPrice> getPriceStream(String symbol) {
        return Flux.interval(Duration.ofSeconds(1))
                .map(i -> new StockPrice(
                        symbol.toUpperCase(),
                        generateRandomPrice(),
                        LocalDateTime.now()
                ));
    }

    private double generateRandomPrice() {
        // Genera un precio base y le suma una pequeña variación.
        return 100 + random.nextDouble() * 10 - 5;
    }
}
