# Aiddoru Backend

## 프로젝트 개요

**Aiddoru**는 태그 기반 크리에이터 추천 및 커뮤니티 플랫폼의 백엔드 서비스입니다. YouTube API를 활용한 인플루언서 데이터 수집, 실시간 랭킹 시스템, 커뮤니티 기능을 제공합니다.

## 기술 스택

### Backend Framework
- **Spring Boot 3.3.4** (Java 17)
- **Spring Security** (JWT 기반 인증)
- **Spring Data JPA** (Hibernate)
- **Spring Kafka** (스트림 처리)

### Database & Cache
- **MySQL 8.2.0** (메인 데이터베이스)
- **Redis** (캐싱)

### External APIs & Services
- **YouTube Data API v3** (인플루언서 데이터 수집)
- **Gmail SMTP** (이메일 인증)
- **gRPC** (마이크로서비스 통신)

### Infrastructure & Monitoring
- **Docker & Docker Compose** (컨테이너화)
- **Prometheus** (메트릭 수집)
- **Grafana** (모니터링 대시보드)
- **AlertManager** (알림 관리)

### Message Queue & Stream Processing
- **Apache Kafka** (이벤트 스트리밍)
- **Kafka Streams** (실시간 데이터 처리)

## 주요 기능

### 1. 인플루언서 관리 시스템
- YouTube 채널 데이터 자동 수집
- 실시간 구독자 수, 조회수, 좋아요 수 추적
- 인플루언서 랭킹 시스템 (일간/주간/월간)

### 2. 커뮤니티 기능
- 자유게시판 및 특정 인플루언서별 커뮤니티
- 게시글 작성, 수정, 삭제
- 댓글 시스템
- 좋아요 및 추천 시스템
- 이미지 업로드 지원

### 3. 사용자 관리
- JWT 기반 인증/인가
- 이메일 인증 시스템
- 사용자 프로필 관리
- 관리자 기능 (사용자 차단, 커뮤니티 관리)

### 4. 통계 및 분석
- 커뮤니티 활동 통계
- 인기 게시글 분석
- 인플루언서 성과 지표
- 실시간 모니터링

## 프로젝트 구조

```
src/main/java/com/aiddoru/dev/
├── Config/                 # 설정 클래스
│   ├── AppConfig.java
│   ├── WebMvcConfig.java
│   └── KafkaStreamsConfig.java
├── Controller/             # REST API 컨트롤러
│   ├── Admin/             # 관리자 API
│   ├── Auth/              # 인증 API
│   ├── Community/         # 커뮤니티 API
│   ├── Rank/              # 랭킹 API
│   ├── Statistic/         # 통계 API
│   └── User/              # 사용자 API
├── Domain/                # 도메인 모델
│   ├── Entity/            # JPA 엔티티
│   │   ├── Community/     # 커뮤니티 관련
│   │   ├── Rank/          # 랭킹 관련
│   │   ├── Recommend/     # 추천 관련
│   │   └── User/          # 사용자 관련
│   ├── Enum/              # 열거형
│   └── Helper/            # 유틸리티 클래스
├── Service/               # 비즈니스 로직
│   ├── Community/         # 커뮤니티 서비스
│   ├── Rank/              # 랭킹 서비스
│   ├── User/              # 사용자 서비스
│   └── kafka/             # Kafka 관련 서비스
├── Persistence/           # 데이터 접근 계층
├── DTO/                   # 데이터 전송 객체
├── Cache/                 # 캐싱 관련
└── Utility/               # 유틸리티 클래스
```

## 주요 엔티티

### User (사용자)
- 기본 사용자 정보 (이메일, 사용자명, 비밀번호)
- 권한 관리 (USER, ADMIN, BLOCK)
- 프로필 이미지 및 차단 상태 관리

### Idol (인플루언서)
- YouTube 채널 정보
- 구독자 수, 조회수, 좋아요 수 등 통계
- 태그 및 카테고리 분류
- 실시간 데이터 추적

### Thread (게시글)
- 커뮤니티 게시글
- 제목, 내용, 작성자 정보
- 조회수, 좋아요, 댓글 수
- 추천 시스템 연동

### Community (커뮤니티)
- 자유게시판 및 특정 인플루언서별 커뮤니티
- 커뮤니티별 설정 및 규칙 관리

## API 엔드포인트

### 인증 관련 (`/api/auth`)
- `POST /signup` - 회원가입
- `POST /login` - 로그인
- `POST /logout` - 로그아웃
- `POST /emailConfirm` - 이메일 인증

### 커뮤니티 관련 (`/api/community`)
- `GET /list` - 커뮤니티 목록 조회
- `GET /{communityName}` - 특정 커뮤니티 조회
- `POST /add` - 커뮤니티 생성

### 게시글 관련 (`/api/thread`)
- `GET /list` - 게시글 목록 조회
- `POST /write` - 게시글 작성
- `GET /{threadId}` - 게시글 상세 조회
- `PUT /{threadId}` - 게시글 수정
- `DELETE /{threadId}` - 게시글 삭제

### 랭킹 관련 (`/api/idol`)
- `GET /ranking` - 인플루언서 랭킹 조회
- `GET /recommend` - AI 추천 인플루언서
- `GET /statistic` - 통계 데이터

### 관리자 관련 (`/api/admin`)
- `GET /getUserList` - 사용자 목록 조회
- `POST /block` - 사용자 차단
- `POST /init` - 시스템 초기화

## 환경 설정

### 개발 환경 실행
```bash
# Docker Compose로 전체 스택 실행
docker-compose up -d

# 또는 개별 서비스 실행
./gradlew bootRun
```

### 환경별 프로파일
- `application.properties` - 기본 설정
- `application-test.properties` - 테스트 환경
- `application-job.properties` - 배치 작업 환경
- `application-production_tmp.properties` - 프로덕션 환경

### 주요 설정값
```properties
# 데이터베이스
spring.datasource.url=jdbc:mysql://mysql:3306/aiddoru
spring.datasource.username=root
spring.datasource.password=

# Redis
spring.data.redis.host=redis
spring.data.redis.port=6379
spring.data.redis.password=

# JWT
jwt.secret=your-jwt-secret

# YouTube API
youtube.api.key=your-youtube-api-key

# Kafka
spring.kafka.bootstrap-servers=
```

## 모니터링 및 로깅

### Prometheus 메트릭
- 애플리케이션 성능 지표
- JVM 메모리 사용량
- HTTP 요청 통계
- 데이터베이스 연결 풀 상태

### Grafana 대시보드
- 실시간 시스템 모니터링
- 성능 지표 시각화
- 알림 설정

### 로깅
- Logback 기반 구조화된 로깅
- 일별 로그 파일 롤링
- 에러 로그 별도 관리

## 배포 및 운영

### Docker 배포
```bash
# 이미지 빌드
docker build -t praisebak/aiddoru_backend .

# 컨테이너 실행
docker run -p 8080:8080 praisebak/aiddoru_backend
```

### 스케줄링 작업
- 인플루언서 데이터 업데이트 (일간)
- 랭킹 시스템 갱신 (실시간)
- 사용자 차단 해제 (일간)
- 통계 데이터 정리 (주간)

## 개발 가이드

### 코드 컨벤션
- Java 17 문법 사용
- Lombok 활용 (Getter, Setter, Builder 등)
- Spring Boot 표준 어노테이션 사용
- RESTful API 설계 원칙 준수

