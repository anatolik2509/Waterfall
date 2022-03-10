FROM maven:3.8.4-jdk-8 as maven
WORKDIR /waterfall
COPY . .
RUN mvn package --no-transfer-progress

FROM tomcat:9.0
COPY --from=maven /waterfall/target/Waterfall.war $CATALINA_HOME/webapps/

 
