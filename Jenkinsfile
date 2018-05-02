pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        disableConcurrentBuilds()
    }

    agent any

    stages {
        stage('Build') {
            steps {
                echo "Build branch: $BRANCH_NAME"
                script {
                    if (env.TAG_NAME) {
                        echo "TAG: Build tag: $TAG_NAME"
                    }
                }
            }
        }
    }
}