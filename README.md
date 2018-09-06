# TwitterDemoProject
This is Demo Project to explore Twitter4j APIs Publishing a tweet and retrieving Twitter Timelines.
Instructions to use the application are as following:
1. Download the JAR file from the below link: 
   https://github.com/HimalayGoswami/TwitterDemoProject/raw/AddUnitTests/target/TwitterDemoProject-1.0-SNAPSHOT.jar
2. Open terminal, go to the Directory containing the JAR downloaded
3. Create a ".yml" file, copy the content from below given link:
   https://github.com/HimalayGoswami/TwitterDemoProject/blob/AddUnitTests/src/main/resources/TwitterDemoConf.yml
   and paste it in the ".yml" file and put the file in the same directory as JAR.
4. Execute the command: java -jar TwitterDemoProject.jar <Consumer_Key> <Consumer_Secret> server <XXX.yml>
5. Follow the instructions given there to provide access keys.
6. After getting the Authentication keys from twitter application will be started
7. Rest APIs available are as following:
  a) http://localhost:8080/TimeLine
  b) http://localhost:8080/Tweet

To view Code Coverage report for Tests please open Target/site/jacoco/index.html with chrome after mvn install.

