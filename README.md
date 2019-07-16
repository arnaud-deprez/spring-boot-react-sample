# Example spring-boot backend and react frontend

Usually frontend and java backend developers are used to their tools: 

* frontend developers often uses [npm](https://www.npmjs.com/)/[yarn](https://yarnpkg.com/) + [webpack](https://webpack.js.org/) and developer in javascript/typescript.
* java backend developers often uses [maven](https://maven.apache.org/)/[gradle](https://gradle.org/) and develop in any JVM languages.

In this scenario we will explore a typical use case where frontend developers will develop a web application that will use 
some REST services exposed by a JVM backend application.

In a microservice world in a cloud environment like [Kubernetes](https://kubernetes.io), it's more likely that the frontend server will be 
developed in nodejs and proxy the API calls to the JVM backend.

But if you need to integrate with some [spring-cloud](http://projects.spring.io/spring-cloud/) and [spring-cloud-netflix](https://cloud.spring.io/spring-cloud-netflix/) 
technologies such as Eureka and Spring cloud config, it's easier to deploy your react web application in a [Spring Boot](https://projects.spring.io/spring-boot/) application 
as this integration comes for free.

This example will show you how to integrate a [React](https://reactjs.org/) web application and JVM backend application while using both the tools 
that frontend and backend developers used to.

---
*NOTE*

> Do not hesitate to have a look at [JHipster](http://www.jhipster.tech/) project which comes a awesome and more advanced integration 
> between [Angular](https://angular.io/) (and soon [React](https://reactjs.org/)) and [Spring Boot](https://projects.spring.io/spring-boot/).
---

## Root project

Initialize a gradle project with `gradle wrapper init` command

## Backend

Go to [spring-boot starter](https://start.spring.io/) and fill the form to create your project.

Here I choose:
* gradle project
* kotlin
* group id: com.powple
* artifact id: backend
* dependencies: _Web_

Then create a web service that will be used by the frontend: [GreetingController](./backend/src/main/kotlin/com/powple/backend/rest/GreetingController.kt)

Add in [settings.gradle](./settings.gradle): 

```groovy
include 'backend'
```

### Options

#### Caching static resources

Add `spring.resources.cache-period: 126227704` in [application.yml](./backend/src/main/resources/application.yml) 
to enable caching headers for 4 years.

## Frontend

We will create a new react application based on [create-react-app](https://github.com/facebook/create-react-app#creating-an-app) utility: 

```sh
npx create-react-app frontend
```

Then rename extension of file containing JSX from .js to .jsx to avoid IDE conflict with javascript code style.

Then change the react application to use that greeting service, see: 
* [App.jsx](./frontend/src/App.jsx)
* [GreetingService.js](./frontend/src/GreetingService.js)

---
*NOTE*

> The greeting service try to reach the API as it was exposed locally: `/api/greetings`.
> In development, we will request the development web server to act as a proxy in front our backend.
> In production, the react application will be served by [Spring Boot](https://projects.spring.io/spring-boot/) so the API is actually exposed locally.
---

Add in [settings.gradle](./settings.gradle): 

```groovy
include 'backend'
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

You can find more information [here](https://github.com/facebook/create-react-app/blob/master/packages/react-scripts/template/README.md#proxying-api-requests-in-development)

### Production

We need to package the build result of `yarn build` command to the [Spring Boot](https://projects.spring.io/spring-boot/) application.

In order to do that, we will use the [gradle-node-plugin](https://github.com/srs/gradle-node-plugin) to allow us to run yarn/npm tasks from gradle 
so we can make build tasks from the backend to depends on frontend gradle assemble task.

1. First, add in [settings.gradle](./settings.gradle) before backend:          
```groovy
include 'frontend'
```

2. Map yarn/npm tasks to gradle tasks in [build.gradle](./frontend/build.gradle) in frontend.

3. Make backend gradle build tasks depending on frontend gradle assemble tasks in [build.gradle](./backend/build.gradle) in backend 
and copy the frontend build output into `/static` folder in the jar file. More information on [how spring-boot can serve static content](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-static-content) 

That's it!
You can now serve your react application from a [Spring Boot](https://projects.spring.io/spring-boot/) application using REST services in kotlin or any JVM languages. 
