FROM gradle:7.2-jdk11 AS backed_builder
WORKDIR mancala_backend
ADD src src
ADD build.gradle .
ADD settings.gradle .
RUN gradle build

FROM openjdk:11 as backend
WORKDIR mancala_backend
COPY --from=backed_builder /home/gradle/mancala_backend/build/libs/mancala-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

FROM node:14-alpine as frontend
WORKDIR /frontend
COPY ./frontend ./
RUN npm i
CMD ["npm", "run", "start"]