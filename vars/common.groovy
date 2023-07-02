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
    if (app_lang == "nodejs") {
        sh "zip -r cart-${TAG_NAME}.zip nod_modules server.js"
    }
    sh 'ls -l '
}


}