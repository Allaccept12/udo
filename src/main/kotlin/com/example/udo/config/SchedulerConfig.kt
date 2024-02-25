package com.example.udo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
class SchedulerConfig {

    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        return ThreadPoolTaskScheduler().apply {
            poolSize = 10
            setThreadNamePrefix("task-scheduler-")
            initialize()
        }
    }
}