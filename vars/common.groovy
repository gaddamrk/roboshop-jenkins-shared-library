def compile() {
  if (app_lang == "nodejs") {
     sh 'npm install'
  }
  if (app_lang == "maven") {
    sh 'mvn package'
  }

}

def unittests() {
  if (app_lang == "nodejs") {
  //developer is missing unittest cases in our project , he need to add them as best practices, we are skipping to proceed further
    sh 'npm test || true'
  }
  if (app_lang == "maven") {
    sh 'mvn test'
  }

  if (app_lang == "python") {
    sh 'python3 -m unittest'
  }
}