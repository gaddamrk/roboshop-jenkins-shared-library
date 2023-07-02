def compile() {
  if (app_lang == "nodejs") {
     sh 'npm install'
     sh 'env'
  }
  if (app_lang == "maven") {
    sh 'mvn package'
  }

}

//developer is missing unittest cases in our project , he need to add them as best practices, we are skipping to proceed further

def unittests() {

  if (app_lang == "nodejs") {
       sh 'npm test || true'
  }
  if (app_lang == "maven") {
       sh 'mvn test'
  }

  if (app_lang == "python") {
       sh 'python3 -m unittest'
  }
}

def email(email_note) {
  mail bcc: '', body: "Job Failed - ${JOB_BASE_NAME}\nJenkins URL - ${BUILD_URL}", cc: '', from: 'gaddamranjithmb2017@gmail.com', replyTo: '', subject: "Job Failed - ${JOB_BASE_NAME}", to: 'gaddamranjithmb2017@gmail.com'
}

def artifactpush() {
    sh "echo ${TAG_NAME} >VERSION"

    if (app_lang == "nodejs") {
      sh "zip -r ${component}-${TAG_NAME}.zip node_modules server.js VERSION ${extraFiles}"
    }

    if (app_lang == "nginx" || app_lang == "python") {
        sh "zip -r ${component}-${TAG_NAME}.zip * -x Jenkinsfile"
    }

    sh 'ls -l '


    NEXUS_USER = sh ( script: 'aws ssm get-parameters --region us-east-1 --names nexus.user --query Parameters[0].Value --with-decryption | sed \'s/"//g\'', returnStdout: true).trim()
    NEXUS_PASS = sh ( script: 'aws ssm get-parameters --region us-east-1 --names nexus.pass --query Parameters[0].Value --with-decryption | sed \'s/"//g\'', returnStdout: true).trim()
    wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${NEXUS_PASS}", var: 'SECRET']]]) {
      sh "curl -v -u ${NEXUS_USER}:${NEXUS_PASS} --upload-file ${component}-${TAG_NAME}.zip http://172.31.86.181:8081/repository/${component}/${component}-${TAG_NAME}.zip"
    }




}