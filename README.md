# Example spring-boot backend and react frontend

Usually frontend and java backend developers are used to their tools: 

* frontend developers often uses [npm](https://www.npmjs.com/)/[yarn](https://yarnpkg.com/) + [webpack](https://webpack.js.org/) and develop in javascript/typescript.
* java backend developers often uses [maven](https://maven.apache.org/)/[gradle](https://gradle.org/) and develop in any JVM languages.

In this scenario we will explore a typical use case where frontend developers will develop a web application that will use 
some REST services exposed by a JVM backend application.

In a cloud environment like [Kubernetes](https://kubernetes.io), it's more likely that the frontend server will be 
developed in nodejs and acts as a proxy by forwarding the API calls to the JVM backend.

But if you need to integrate with some [spring-cloud](http://projects.spring.io/spring-cloud/) and [spring-cloud-netflix](https://cloud.spring.io/spring-cloud-netflix/) 
technologies such as Eureka and Spring cloud config, it's easier to deploy your react web application in a [Spring Boot](https://projects.spring.io/spring-boot/) application 
as this integration comes for free.

This example will show you how to integrate a [Reactjs](https://reactjs.org/) web application and a [Spring Boot](https://projects.spring.io/spring-boot/) application 
(based on [Spring Boot Web Reactive](https://spring.io/guides/gs/reactive-rest-service/)) 
while using both the tools that frontend and backend developers are used to.

---
*NOTE*

> Do not hesitate to have a look at [JHipster](http://www.jhipster.tech/) project which comes with an awesome and more advanced integration 
> between [Angular](https://angular.io/) or [React](https://reactjs.org/) and [Spring Boot](https://projects.spring.io/spring-boot/).
---

## Root project

Initialize a gradle project with `gradle wrapper init` command

## Backend

Go to [spring-boot Initializr](https://start.spring.io/) and fill the form to create your project.

Here I choose:
* gradle project
* kotlin
* group id: com.powple
* artifact id: backend
* dependencies: _Spring Reactive Web_, _Spring Boot DevTools_

Then we will create a web service that will be used by the frontend.
Here I decided to use the Webflux functional endpoints. See [RoutesConfiguration](./backend/src/main/kotlin/com/powple/backend/config/RoutesConfiguration.kt).

In order to use SPA application router (eg. angular, reactjs or vuejs router), we have to resolve unknown path to the `index.html` page.
This is achieved thanks to [SpaWebFluxConfigurer](./backend/src/main/kotlin/com/powple/backend/config/SpaWebFluxConfigurer.kt) 
and the [`/` route](./backend/src/main/kotlin/com/powple/backend/config/RoutesConfiguration.kt)

Then add in [settings.gradle.kts](./settings.gradle.kts): 

```kotlin
include('backend')
```

### Options

#### Caching static resources

Add `spring.resources.cache.period: 126227704` in [application.yml](./backend/src/main/resources/application.yml) 
to enable caching headers for 4 years.

## Frontend

We will create a new react application based on [create-react-app](https://github.com/facebook/create-react-app#creating-an-app) utility: 

```sh
npx create-react-app frontend
```

Then optionally rename extension of file containing JSX from .js to .jsx to avoid IDE conflict with javascript code style.

Then change the react application to use that greeting service, see: 
* [App.jsx](./frontend/src/App.jsx)
* [GreetingService.js](./frontend/src/GreetingService.js)

---
*NOTE*

> The greeting service try to reach the API as if it was exposed locally: `/api/greetings`.
> In development, we will request the development web server to act as a proxy by forwarding all the API call to our java backend.
> In production, the react application will be served by [Spring Boot](https://projects.spring.io/spring-boot/) so the API is actually exposed locally.
---

Add in [settings.gradle.kts](./settings.gradle.kts): 

```kotlin
include("backend")
```

### Development

In development, we will to our Webpack development server to proxy all unhandled request to the backend API. 

We can do so by adding in [package.json](./frontend/package.json): 

```json
{
  //...
  "proxy": "http://localhost:8080"
}
```

You can find more information [here](https://facebook.github.io/create-react-app/docs/proxying-api-requests-in-development)

Once it is done, you will be able to run your backend server and frontend web server separately and while developing, 
you will see the changes directly.

#### Backend

You will need to configure your IDE to auto-rebuild your project when it detects a change so that [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html) 
can reload the changes. Check the [doc](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html) for more info.

Alternatively, you can run in a terminal `./gradlew :backend:bootRun` but you won't have auto-rebuild functionality.

#### Frontend

Then in another terminal, you can run: 

```sh
cd frontend
yarn install
yarn start
```

Then it should pop up your default browser with the [Reactjs](https://reactjs.org/) application at http://localhost:3000.
Make some changes and see how it impacts the web app.

### Production

We need to package the result of the build made by `yarn build` command to the [Spring Boot](https://projects.spring.io/spring-boot/) application.

In order to do that, we will use the [gradle-node-plugin](https://github.com/node-gradle/gradle-node-plugin) to allow us to run yarn/npm tasks from gradle 
and at the end, make `processResources` task from the backend to depends on frontend `yarn build`.

1. First, add in [settings.gradle.kts](./settings.gradle.kts) before backend:          

```kotlin
include("frontend")
```

2. Make yarn/npm tasks depends on gradle tasks in [build.gradle.kts](./frontend/build.gradle.kts) in frontend.

3. Make backend gradle `processResources` task depending on frontend gradle `yarn_build` tasks in [build.gradle](./backend/build.gradle.kts) in backend 
and copy the frontend build output into `/static` folder in the jar file. More information on [how spring-boot can serve static content](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-static-content).

---
**NOTE:** 

> In this project, the backend will depend on frontend build only when the gradle property `prod` is set. 
> It is to avoid that we systematically rebuild the frontend when we are only developing in the backend.
--- 

That's it!
You can now serve your react application from a [Spring Boot](https://projects.spring.io/spring-boot/) application using REST services in kotlin or any JVM languages.

Let's try this:

```sh
./gradlew clean build -Pprod
 java -jar backend/build/libs/*.jar
```

Then open your browser at http://localhost:8080. Hooray!
