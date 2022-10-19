FROM bellsoft/liberica-openjdk-alpine:18
COPY ./build/libs/telegram-bot-0.0.1-SNAPSHOT.jar ./
ENV TZ="Europe/Moscow"
CMD ["java","-jar", "telegram-bot-0.0.1-SNAPSHOT.jar"]