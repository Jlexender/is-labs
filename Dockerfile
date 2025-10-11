FROM quay.io/wildfly/wildfly:latest-jdk21

COPY app/build/libs/*.war /opt/jboss/wildfly/standalone/deployments/app.war
EXPOSE 8080
