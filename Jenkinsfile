pipeline {
    agent {
        docker {
            image 'maven:3.9.6-eclipse-temurin-21-alpine'
            args '-v $HOME/.m2:/root/.m2'
            reuseNode true
        }
    }
    stages {
        stage('Setup Git') {
            steps {
                sh 'apk add --no-cache git'  // Installe Git
            }
        }
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
        
        // Tests avec JaCoCo
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
        
        //Analyse SonarQube
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
        
        //Quality Gate
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        //Packaging
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