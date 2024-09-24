# 사용할 베이스 이미지
FROM openjdk:17-jdk-slim

# 컨테이너 내부의 작업 디렉토리 설정
WORKDIR /app

COPY build/libs/aiddoru-backend-0.0.1-SNAPSHOT.jar /app/app.jar
COPY keystore.jks /app/keystore.jks

RUN mkdir -p /app/logs

# 컨테이너에서 사용할 포트 설정
EXPOSE 8080

# 애플리케이션을 실행할 명령어 설정
ENTRYPOINT ["nohup", "java", "-jar","-Dspring.profiles.active=production", "app.jar", ">", "/app/logs/output.log", "2>&1", "&"]