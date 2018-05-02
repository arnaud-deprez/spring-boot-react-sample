pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        disableConcurrentBuilds()
    }

    agent { label 'gradle' }

    stages {
        stage('Build') {
            steps {
                echo "Build $BRANCH_NAME"
                script {
                    if (env.TAG_NAME) {
                        echo "TAG: Build tag: $TAG_NAME"
                    }
                }
            }
        }
    }
}