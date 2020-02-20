package com.powple.backend.config

import com.powple.backend.service.GreetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ResourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.toMono

@Configuration
class RoutesConfiguration(
    @Autowired val resourceProperties: ResourceProperties,
    @Autowired val resourceLoader: ResourceLoader,
    @Autowired val greetingService: GreetingService) {

    @Bean
    fun routes() =
        apiRoutes()
            .and(indexRoutes())
            //.and(resourcesRoutes())

    fun apiRoutes() = router {
        "/api".nest {
            GET("/greetings") {req ->
                greetingService.greeting(
                    req.queryParam("name")
                        .filter(String::isNotBlank) // this looks like a bug to me.
                        .orElse("World"))
                    .flatMap { resp -> ok().syncBody(resp) }
            }
        }
    }

    //TODO: review when https://github.com/spring-projects/spring-boot/issues/9785
    fun indexRoutes() = router {
        GET("/") {
            resourceLoader.getResource("classpath:/static/index.html")
                .toMono()
                .flatMap { r -> ok().syncBody(r) }
        }
    }
}