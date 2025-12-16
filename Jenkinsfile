pipeline {
    agent {
        docker {
            image 'maven:3.9.6-eclipse-temurin-21-alpine' 
            args '-v $HOME/.m2:/root/.m2'
            reuseNode true
        }
    }
    
    environment {
        // Variables pour SonarQube
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonarqube-token')
    }
    
    stages {
        stage('Setup Git') {
            steps {
                sh 'apk add --no-cache git'  
            }
        }
        stage('Checkout') {
            steps {
                checkout scm  
            }
        }
        // Stage 1: Checkout (sera automatique avec GitHub webhook)
        stage('Checkout') {
            steps {
                echo "Code checkout via GitHub webhook"
            }
        }
        
        // Stage 2: Build
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        // Stage 3: Tests avec JaCoCo
        stage('Tests') {
            steps {
                sh 'mvn test jacoco:report'
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                    jacoco(
                        execPattern: 'target/jacoco.exec',
                        classPattern: 'target/classes',
                        sourcePattern: 'src/main/java',
                        exclusionPattern: '**/test/**'
                    )
                }
            }
        }
        
        // Stage 4: Analyse SonarQube
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube-local') {
                    sh '''
                        mvn sonar:sonar \
                          -Dsonar.projectKey=tp3-integration-continue \
                          -Dsonar.projectName="TP3 Integration Continue" \
                          -Dsonar.java.binaries=target/classes \
                          -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
        }
        
        // Stage 5: Quality Gate
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        // Stage 6: Packaging
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
    
    post {
        always {
            // Publier les rapports JaCoCo
            publishHTML(target: [
                reportName: 'JaCoCo Coverage Report',
                reportDir: 'target/site/jacoco',
                reportFiles: 'index.html',
                keepAll: true
            ])
            
            // Nettoyer
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