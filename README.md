<h1 align="center">
  <br>
  <a href="https://github.com/zzoe2346/MyCodingTest_BACKEND"><img src="https://github.com/user-attachments/assets/2cfa5d66-5018-49d2-bcc5-6c7ae81a0a6f" alt="MyCodingTest" width="200"></a>
  <br>
  MY CODING TEST
  <br>
</h1>

<h4 align="center">백준 문제 풀이 기록을 자동 수집하고 복습을 도와주는 서비스</h4>

<p align="center">
     <img src="https://img.shields.io/badge/java_21-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
     <img src="https://img.shields.io/badge/spring_boot_3-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white" alt="Spring">
     <img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
     <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white" alt="AWS">
</p>

<p align="center">
  <a href="#architecture">Architecture</a> •
  <a href="#package-structure">Package Structure</a> •
  <a href="#related-repositories">Related Repositories</a> •
  <a href="#getting-started">Getting Started</a>
</p>

---

## Architecture

### System Overview
<p align="center">
  <img src="https://github.com/user-attachments/assets/e2dbbbfd-d58f-465c-81c4-107064a46d76" alt="Business Flow" width="800">
</p>

### Deployment
<p align="center">
  <img src="https://github.com/user-attachments/assets/49960a4f-a6f9-42a3-8bba-41fb015b90cb" alt="Deploy Architecture" width="600">
</p>

---

## Package Structure

프로젝트는 **DDD(Domain-Driven Design)** 기반으로 설계되었습니다.

```
com.mycodingtest/
├── judgment/        # 채점 도메인 - 플랫폼별 채점 결과 관리
├── problem/         # 문제 도메인 - 알고리즘 문제 정보 관리
├── review/          # 리뷰 도메인 - 오답노트 및 복습 기능
├── user/            # 사용자 도메인 - OAuth 인증 및 프로필
├── collector/       # 수집 도메인 - 외부 플랫폼 데이터 통합
├── query/           # CQRS Query Side - 조회 전용 API
└── common/          # 공통 모듈 - 보안, 설정, 예외처리
```

### Domain Module Structure

각 도메인 모듈은 **Layered Architecture**를 따릅니다:

```
[domain]/
├── api/                 # Presentation Layer
│   ├── *Controller.java     # REST API 엔드포인트
│   └── dto/                 # Request/Response DTO
├── application/         # Application Layer
│   ├── *Service.java        # 유스케이스 구현
│   └── dto/                 # Command 객체
├── domain/              # Domain Layer
│   ├── *.java               # Entity, Value Object
│   └── *Repository.java     # Repository 인터페이스
└── infrastructure/      # Infrastructure Layer
    └── *RepositoryImpl.java # Repository 구현체
```

### Layer Responsibilities

| Layer | 역할 | 의존 방향 |
|-------|------|----------|
| **API** | HTTP 요청/응답 처리, 인증 | → Application |
| **Application** | 비즈니스 유스케이스 조율 | → Domain |
| **Domain** | 핵심 비즈니스 로직, 엔티티 | 없음 (최하위) |
| **Infrastructure** | 기술 구현 (JPA, 외부 API) | → Domain |

> **DIP(Dependency Inversion Principle)**: Repository 인터페이스는 Domain Layer에, 구현체는 Infrastructure Layer에 위치하여 도메인이 기술에 의존하지 않습니다.

---

## ERD

<p align="center">
  <img src="https://github.com/user-attachments/assets/d4eaa855-99de-421d-8efa-d43c09ceaf28" alt="ERD" width="700">
</p>

---

## Related Repositories

| Repository | Description |
|------------|-------------|
| [MyCodingTest_FE](https://github.com/zzoe2346/MyCodingTest_FE) | React 프론트엔드 |
| [MyCodingTest_Connector](https://github.com/zzoe2346/MyCodingTest_Connector) | Chrome Extension |

---

## Getting Started

### Prerequisites
- Java 21
- MySQL 8.0+
- Gradle 8.x

### Run Locally
```bash
./gradlew bootRun
```

### Run Tests
```bash
./gradlew test
```

---

## CI/CD

<p align="center">
  <img src="https://github.com/user-attachments/assets/f2e25372-b12b-4692-b9ed-fe5055d145ee" alt="CI/CD Pipeline" width="700">
</p>
