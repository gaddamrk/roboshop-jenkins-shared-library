def compile() {
  if (app_lang == "nodejs") {
     sh 'npm install'
  }
  if (app_lang == "maven") {
    sh 'mvn package'
    sh 'env'
  }

}

def unittests() {
  if (app_lang == "nodejs") {
  //developer is missing unittest cases in our project , he need to add them as best practices, we are skipping to proceed further
    sh 'npm test'
  }
  if (app_lang == "maven") {
    sh 'mvn test'
  }

  if (app_lang == "python") {
    sh 'python3 -m unittest'
  }
}

def email(email_note) {
  mail bcc: '', body: 'test', cc: '', from: 'gaddamranjithmb2017@gmail.com', replyTo: '', subject: 'test from jenkins', to: 'gaddamranjithmb2017@gmail.com'
}

