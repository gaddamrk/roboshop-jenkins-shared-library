def call() {
  if(!env.SONAR_EXTRA_OPTS) {
      env.SONAR_EXTRA_OPTS = " "
  }
//  if(!env.TAG_NAME) {
//      env.PUSH_CODE = "false"
//  } else {
//      env.PUSH_CODE = "true"
//  }
  try {

    node('workstation') {

        stage('check out') {
            cleanWs()
            git branch: 'main', url: "https://github.com/gaddamrk/${component}.git"
            sh 'env'
        }
        stage('compile/build') {
            common.compile()
        }
        stage('unit test') {
            common.unittests()
        }

        stage('quality control') {

            SONAR_USER = sh ( script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.user --query Parameters[0].Value --with-decryption | sed \'s/"//g\'', returnStdout: true).trim()
            SONAR_PASS = sh ( script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.password --query Parameters[0].Value --with-decryption | sed \'s/"//g\'', returnStdout: true).trim()
            wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_PASS}", var: 'SECRET']]]) {
               // sh "sonar-scanner -Dsonar.host.url=http://172.31.85.30:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectKey=${component} -Dsonar.qualitygate.wait=true ${SONAR_EXTRA_OPTS}"
               sh "echo sonar scan"
            }
        }

//        if(env.PUSH_CODE == "true") {
          stage('upload code to centralized place') {
            echo 'upload file'
          }
//        }
    }









  } catch(Exception e) {
    common.email('failed')
  }

}










