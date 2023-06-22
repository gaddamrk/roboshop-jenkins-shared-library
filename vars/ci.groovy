
def call() {
  try {
    pipeline {
      agent {
        label 'workstation'
      }
      stages {

        stage('compile/build') {
          steps {
            script {
              common.compile()
            }
          }
        }

        stage('unit test') {
          steps {
            script {
              common.unittests()
            }
          }
        }

        stage('quality control') {
           environment {
             sonarqube.user = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.user --query Parameters[0].Value --with-decryption | sed \'s/"//g\')'
             sonarqube.pass = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.pass --query Parameters[0].Value --with-decryption | sed \'s/"//g\')'
           }
           steps {
             sh "sonar-scanner -Dsonar.host.url=http://172.31.85.30:9000 -Dsonar.login=${sonarqube.user} -Dsonar.password=${sonarqube.pass} -Dsonar.projectKey=cart"


           }
        }

        stage('upload code to centralized place') {
          steps {
            echo 'upload'
          }
        }


      }

    }
  } catch(Exception e) {
    common.email('failed')
  }
}




