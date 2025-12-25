package com.example.AirbnbSpring.repository.reads;

import com.example.AirbnbSpring.model.Airbnb;
import com.example.AirbnbSpring.model.readModels.AirbnbReadModel;
import com.example.AirbnbSpring.model.readModels.AvailabilityReadModel;
import com.example.AirbnbSpring.model.readModels.BookingReadModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisReadRepository {

    private static final String AIRBNB_KEY_PREFIX="airbnb:";
    private static final String BOOKING_KEY_PREFIX="booking:";
    private static final String AVAILABILITY_KEY_PREFIX="availability:";

    private final RedisTemplate<String,String> redisTemplate;
    private final ObjectMapper objectMapper;

    public AirbnbReadModel findAirbnbById(Long id){
        String key=AIRBNB_KEY_PREFIX+id;
        String value=redisTemplate.opsForValue().get(key);

        if(value==null)return null;

        try{
            return objectMapper.readValue(value,AirbnbReadModel.class);
        }catch (JacksonException e){
            throw new RuntimeException("failed to parse Airbnb read model from redis",e);
        }
    }


    public BookingReadModel findBookingById(Long id){
        String key=BOOKING_KEY_PREFIX+id;
        String value=redisTemplate.opsForValue().get(key);

        if(value==null)return null;

        try{
            return objectMapper.readValue(value,BookingReadModel.class);
        }catch (JacksonException e){
            throw new RuntimeException("failed to parse Booking read model from redis",e);
        }
    }

    public AvailabilityReadModel findAvailabilityById(Long id){
        String key=AVAILABILITY_KEY_PREFIX+id;
        String value=redisTemplate.opsForValue().get(key);

        if(value==null)return null;

        try{
            return objectMapper.readValue(value,AvailabilityReadModel.class);
        }catch (JacksonException e){
            throw new RuntimeException("failed to parse Availability read model from redis",e);
        }
    }

    public List<AirbnbReadModel> findAllAirbnbs(){

        Set<String> keys=redisTemplate.keys(AIRBNB_KEY_PREFIX+"*");

        if(keys.isEmpty()||keys==null)return List.of();
        return keys.stream()
                .map(
                        key->{String value=redisTemplate.opsForValue().get(key);

                            if(value==null)return  null;
                            try {
                                return objectMapper.readValue(value, AirbnbReadModel.class);
                            } catch (JacksonException e) {
                                throw new RuntimeException("Failed to parse Airbnb read model from Redis", e);
                            }

                        }
                )
                .filter(airbnb->airbnb!=null)
                .collect(Collectors.toList());
        }









}
