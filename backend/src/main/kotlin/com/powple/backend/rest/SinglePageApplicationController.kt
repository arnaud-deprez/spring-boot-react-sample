package com.powple.backend.rest

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SinglePageApplicationController {

    /**
     * Use to forward unknown url to index.html.
     * Behaviour:
     *  1. this regex path match any unknown url that you don't have implemented.
     *  2. then it forwards it to the static content handler/servlet setup by default in spring-boot @see https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-static-content
     *      - if the url contains a known extension, it will send the resources
     *      - otherwise if it does not match a resources, it loads the index.html with all its resources
     *  3. Then the router of the SPA (angular, react, vuejs, etc.) get its state from the url
     */
    @GetMapping(value = "/**/{[path:[^\\.]*}")
    fun home(): String {
        return "forward:/"
    }

}