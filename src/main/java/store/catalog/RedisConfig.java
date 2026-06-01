package store.catalog;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfig {

    // Configures Spring Cache to serialize Redis values as JSON instead of the
    // default Java serialization. JSON values are inspectable from redis-cli and
    // don't require cached classes to implement Serializable.

    @SuppressWarnings("removal")
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()
                )
            )
            .entryTtl(Duration.ofMinutes(5))
            .disableCachingNullValues();
    }

}
