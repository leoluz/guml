FROM openjdk:8u92-jre-alpine

RUN apk update && apk add graphviz git ttf-dejavu

COPY build/libs/guml-0.0.1.war /guml.war

ENTRYPOINT ["java", "-jar", "/guml.war"]
