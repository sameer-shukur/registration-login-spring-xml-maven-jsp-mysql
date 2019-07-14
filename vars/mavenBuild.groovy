def call (def artifactory, def mvnHome,def pom, def goal, def releaseRepo, def snapshotRepo){
  rtMavenDeployer (
      id: 'deployer-unique-id',
      serverId: "${artifactory}",
      releaseRepo: "${releaseRepo}/${BUILD_NUMBER}",
      snapshotRepo: "${snapshotRepo}/${BUILD_NUMBER}"
  )

  rtMavenRun (
      tool: "${mvnHome}",
      pom: "${pom}",
      goals: "${goal}",
      opts: '-Xms1024m -Xmx4096m',
      deployerId: 'deployer-unique-id',
  )
}
