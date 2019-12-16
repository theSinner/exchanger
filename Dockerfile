FROM maven:3.6.3-jdk-11-slim
RUN mkdir /code
WORKDIR /code
COPY pom.xml /code/pom.xml
RUN mvn compile
COPY . .
RUN mvn test
CMD ["mvn", "exec:java"]