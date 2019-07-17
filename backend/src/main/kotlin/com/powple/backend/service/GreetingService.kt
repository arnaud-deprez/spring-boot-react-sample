package com.powple.backend.service

import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicLong

data class Greeting(val id: Long, val content: String)


@Component
class GreetingService {
    val counter = AtomicLong()

    fun greeting(name: String) =
            Mono.just(Greeting(counter.incrementAndGet(), "Hello, $name!"))
}