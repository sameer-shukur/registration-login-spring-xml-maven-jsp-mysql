def call(def sonarqubeServer){
  withSonarQubeEnv("${sonarqubeServer}") {
    timeout(time: 1, unit: 'MINUTES') { 
      def qg = waitForQualityGate() 
      if (qg.status != 'OK') {
        error "Pipeline aborted due to quality gate failure: ${qg.status}"
        currentBuild.status='FAILURE'
      }
    }
  }
}
