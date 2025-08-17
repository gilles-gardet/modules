package com.ggardet.modulith.core.event

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.modulith.events.core.EventSerializer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@EnableAsync
@EnableScheduling
@Configuration
class EventConfig {

    @Bean
    fun eventSerializer(objectMapper: ObjectMapper): EventSerializer {
        return object : EventSerializer {
            override fun serialize(event: Any): String {
                return objectMapper.writeValueAsString(event)
            }
            override fun <T : Any> deserialize(serialized: Any, type: Class<T>): T {
                return when (serialized) {
                    is String -> objectMapper.readValue(serialized, type)
                    else -> objectMapper.convertValue(serialized, type)
                }
            }
        }
    }
}
