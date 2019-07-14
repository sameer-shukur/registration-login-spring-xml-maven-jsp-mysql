@Library('shared-library')_
node(label: 'master'){
    //Variables
    def gitURL = "https://github.com/sameer-shukur/registration-login-spring-xml-maven-jsp-mysql.git"
    def repoBranch = "master"
    def applicationName = "registration-form"
    def sonarqubeServer = "sonar"
    def sonarqubeGoal = "clean verify sonar:sonar"
    def mvnHome = "maven"
    def pom = "pom.xml"
    def goal = "clean install"
    def artifactory = "Artifactory"
    def releaseRepo = "libs-release-local"
    def snapshotRepo = "libs-snapshot-local"
    def variable = credentials("tomcat")
	def dockerRegistry = "https://registry.hub.docker.com"
	def dockerCredentialID = "dockerID" 
    def lastSuccessfulBuildID = 0
    try {
        
        stage('Get Last Successful Build Number'){
        def build = currentBuild.previousBuild
        while (build != null) {
            if (build.result == "SUCCESS")
            {
                lastSuccessfulBuildID = build.id as Integer
                break
            }
            build = build.previousBuild
        }
        echo "${lastSuccessfulBuildID}"
    }
    stage('Git-Checkout'){
        gitClone "${gitURL}","${repoBranch}"
    }
    
    //Sonarqube Analysis
    stage('Sonarqube-scan'){
        sonarqubeScan "${mvnHome}","${sonarqubeGoal}","${pom}", "${sonarqubeServer}"
    }
    
    //Quality-gate
    stage('Quality-Gate'){
        qualityGate "${sonarqubeServer}"
    }
    
    //MVN Build
    stage('Maven Build and Push to Artifactory'){
        mavenBuild "${artifactory}","${mvnHome}","${pom}", "${goal}", "${releaseRepo}", "${snapshotRepo}"
    }
    //SCP to tomcat instance provisioned using Terraform
    stage('Copy'){
        withCredentials([string(credentialsId:'tomcat', variable: 'host')]) {
            echo "${variable}"
            sh "scp ./target/*.war ${host}:/opt/tomcat/webapps/"
            
        }
        
    }
	
	//docker-image-build and Push
    stage('Build Docker image and Push'){
        dockerBuildAndPush "${dockerRegistry}","${dockerCredentialID}","${dockerImageName}"
    }
    
}
catch(err)
	{
		currentBuild.result = 'FAILURE'
		//Mail on failure
		mail bcc: '', body:"${err}", cc: '', from: '', replyTo: '', subject: 'Job failed', to: 'sameer.shukur.m@gmail.com'
	}
}
