
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
             sonar_user = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.user --query Parameters[0].Value --with-decryption | sed \'s/"//g\')'
             sonar_pass = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.password --query Parameters[0].Value --with-decryption | sed \'s/"//g\')'
           }
           steps {
             sh "sonar-scanner -Dsonar.host.url=http://172.31.85.30:9000 -Dsonar.login=${sonar_user} -Dsonar.password=${sonar_pass} -Dsonar.projectKey=cart"


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




