pipeline {
    agent any
    stages {
        stage("Build Modules & Build Docker Images") {
            steps {
                script {
                    def modules = ['Main', 'ApiGateway','EurekaServer','AuthService','EmailNotification','Report']
                    for (def module in modules) {
                        dir("${module}") {
                            echo "Building ${module}..."
                            bat "mvn clean install"
                        }
                    }
                }
            }
        }
    }
}