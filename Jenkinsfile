pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        disableConcurrentBuilds()
    }

    agent { label 'gradle-nodejs' }

    stages {
        stage('Build') {
            steps {
                checkout scm
                container('nodejs') {
                    echo "Build frontend..."
                    dir('frontend') {
                        sh "yarn install && yarn build"
                    }
                }
                container('gradle') {
                    echo "Build backend"
                    sh "gradle build -p backend -Pprod -x:frontend:assemble"
                }
            }
        }
    }
}