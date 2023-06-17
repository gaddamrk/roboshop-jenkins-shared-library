def call() {

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
            echo 'unit test'
          }
        }

        stage('quality control') {
          steps {
            echo 'quality control'
          }
        }

        stage('upload code to centralized place') {
          steps {
            echo 'upload'
          }
        }
     }
  }
}