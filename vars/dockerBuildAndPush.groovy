def call(def registry, def dockerCredentialID ,def imageName){
  docker.withRegistry('https://registry.hub.docker.com', "${dockerCredentialID}") {
    
    echo "${dockerCredentialID}"
 
    //build docker image
    image = docker.build("${imageName}:${BUILD_NUMBER}")
    
    //push image to hub
    image.push()
    image.push("latest")
    
   }
}
