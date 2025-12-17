pipeline {
    agent any  // Utilise l'agent Jenkins principal (qui a Git)

    environment {
        SONAR_HOST_URL = 'http://172.17.0.1:9000'
    }

    tools {
        // Configure Maven à installer automatiquement
        maven 'maven-3.9'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Tests') {
            steps {
                sh 'mvn test jacoco:report'
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube-local') {
                    sh '''
                        mvn sonar:sonar \
                          -Dsonar.host.url=http://172.17.0.1:9000 \
                          -Dsonar.projectKey=tp3-integration-continue \
                          -Dsonar.projectName="TP3 Integration Continue" \
                          -Dsonar.java.binaries=target/classes \
                          -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
    
    post {
        always {
            publishHTML(target: [
                reportName: 'JaCoCo Coverage Report',
                reportDir: 'target/site/jacoco',
                reportFiles: 'index.html',
                keepAll: true
            ])
            cleanWs()
        }
        success {
            echo 'O Pipeline réussi ! O'
        }
        failure {
            echo 'X Pipeline échoué ! X'
        }
    }
}