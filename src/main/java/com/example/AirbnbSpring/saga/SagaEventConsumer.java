package com.example.AirbnbSpring.saga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class SagaEventConsumer {
    private static final String SAGA_QUEUE="saga:events";

    private final RedisTemplate<String,String> redisTemplate;
    private final SagaEventProcessor sagaEventProcessor;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 500)
    public void consumeEvents(){
        log.info("👂 SagaEventConsumer polling Redis...");


        try {
            String eventJson=redisTemplate.opsForList().leftPop(SAGA_QUEUE,1, TimeUnit.SECONDS);
            log.info("📥 Event popped from Redis: {}", eventJson);

            if(eventJson!=null && !eventJson.isEmpty()){

                SagaEvent sagaEvent=objectMapper.readValue(eventJson, SagaEvent.class);
                log.info("processing saga event: {}",sagaEvent.getSagaId());
                sagaEventProcessor.processEvent(sagaEvent);
                log.info("Processed saga event succesfully {}",sagaEvent.getSagaId());
            }

        } catch (Exception e) {
            log.info("error processing saga event:{}",e.getMessage());
            throw new RuntimeException("Failed to process saga event");
        }

    }

}
