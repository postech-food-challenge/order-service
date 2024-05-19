FROM gradle:8.3.0-jdk17 AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build

RUN mkdir /app && cp /home/gradle/src/build/libs/com.fiap.postech.order-0.0.1.jar /app/com.fiap.postech.order.jar