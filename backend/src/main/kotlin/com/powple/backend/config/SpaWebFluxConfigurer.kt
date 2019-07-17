package com.powple.backend.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.web.ResourceProperties
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.CacheControl
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.resource.ResourceResolver
import org.springframework.web.reactive.resource.ResourceResolverChain
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import java.util.concurrent.TimeUnit

@Configuration
@ConditionalOnWebApplication
class SpaWebFluxConfigurer(
    @Autowired val webFuxProperties: WebFluxProperties,
    @Autowired val resourceProperties: ResourceProperties) : WebFluxConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        super.addResourceHandlers(registry)
        registry.addResourceHandler(webFuxProperties.staticPathPattern)
            .addResourceLocations(*resourceProperties.staticLocations)
            .setCacheControl(CacheControl.maxAge(resourceProperties.cache.period.seconds, TimeUnit.SECONDS))
            .resourceChain(true)
            .addResolver(SpaResolver())
    }
}

/**
 * Use to forward unknown url to index.html.
 * This allows usage of any kind of router in SPA application (angular, reactjs, vuejs, etc.)
 */
class SpaResolver : ResourceResolver {
    override fun resolveUrlPath(resourcePath: String, locations: MutableList<out Resource>, chain: ResourceResolverChain): Mono<String> {
        return chain.resolveUrlPath(resourcePath, locations)
            .switchIfEmpty(
                resolve(resourcePath, locations)
                    .map { r -> r.url.toString() }
            )
    }

    override fun resolveResource(exchange: ServerWebExchange?, requestPath: String, locations: MutableList<out Resource>, chain: ResourceResolverChain): Mono<Resource> {
        return chain.resolveResource(exchange, requestPath, locations)
            .switchIfEmpty(resolve(requestPath, locations))
    }

    private fun resolve(requestPath: String, locations: MutableList<out Resource>): Mono<Resource> {
        return locations.toFlux()
            .map { loc -> loc.createRelative(requestPath) }
            .filter(Resource::exists)
            .filter(Resource::isReadable)
            .defaultIfEmpty(ClassPathResource("static/index.html"))
            .next()
    }
}