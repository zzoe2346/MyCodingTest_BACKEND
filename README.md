
<h1 align="center">
  <br>
  <a href="http://www.amitmerchant.com/electron-markdownify"><img src="https://github.com/user-attachments/assets/2cfa5d66-5018-49d2-bcc5-6c7ae81a0a6f" alt="Markdownify" width="200"></a>
  <br>
  MY CODING TEST
  <br>
</h1>

<h4 align="center">백준에서 푼 문제 쉽게 파악하고 복습하기</h4>

<p align="center">
     <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white"
         alt="Gitter">
     <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white"
         alt="3">
     <img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white"
         alt="323">
     <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white"
         alt="33k">
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/8b1b513b-3bfa-4f61-82d1-36c298ae60a5"
         alt="33k">
</p>

## 귀찮은 복사/붙여넣기 그만! 문제 풀이와 복습에 집중해주세요.
<p align="center">
  <img src="https://github.com/user-attachments/assets/988566c4-cd75-41c8-bbe6-096cea20fb8b" alt="image">
</p>
<p align="center">
  <img width="3310" height="1388" alt="image" src="https://github.com/user-attachments/assets/fb2057b5-8f52-424b-a084-a79d3db48b57" />
<p>

> [MyCodingTest_FE - Github](https://github.com/zzoe2346/MyCodingTest_FE)

> [MyCodingTest_ChromeExtention - Github](https://github.com/zzoe2346/MyCodingTest_Connector)  

> [MyCodingTest_ChromeExtention - Chrome Web Store](https://chromewebstore.google.com/detail/my-coding-test-connector/ekmnmpgdcpflanopjcopleffealdeifj)

## 비즈니스 핵심 흐름
<img width="3686" height="1489" alt="image" src="https://github.com/user-attachments/assets/e2dbbbfd-d58f-465c-81c4-107064a46d76" />

## CI/CD Pipeline
![cicddia](https://github.com/user-attachments/assets/f2e25372-b12b-4692-b9ed-fe5055d145ee)
    
## Deploy Architecture
![image](https://github.com/user-attachments/assets/49960a4f-a6f9-42a3-8bba-41fb015b90cb)

## ERD
<p align="center">
<img width="910" height="457" alt="image" src="https://github.com/user-attachments/assets/d4eaa855-99de-421d-8efa-d43c09ceaf28" />
</p>

## 개발 과정에서 마주친 문제와 해결과정
### 초기 서비스 운영 비용을 절감: AWS EC2 대신 AWS Lightsail 채택
- 서비스 트래픽 예측이 어려운 초기 단계에서 과도한 인프라 비용을 지출하는 대신, LightSail을 활용하여 최소한의 
비용으로 안정적인 운영 환경을 구축했습니다.
- 그 결과, 동등한 성능인 EC2(t3.micro) 대비 26.16% 비용 절감을 달성했습니다.
<p align="center">
  <img width="822" height="283" alt="image" src="https://github.com/user-attachments/assets/3c99c25b-67d0-44f4-9883-b7dc2e1aa9d0" />
<p>

### JPA 최적화를 수행: 메모리 사용량이 64.1% 감소
- JPQL을 활용하여 쿼리 결과를 DTO에 바로 매핑하였습니다.
- 그 결과, VisualVM의 프로파일기능을 활용하여 DTO직접 매핑을 한 경우와 안 한 경우를 비교해보았고 
실제 메모리 사용량이 64.1% 감소(1560B → 560B)한 것을 확인하였습니다.
<p align="center">
<img width="805" height="254" alt="image" src="https://github.com/user-attachments/assets/5c31d0ea-f74d-4be4-a734-f6ad4dea7f78" />
<p>

### AWS S3 도입: Request Body 크기가 71.1% 감소
- 비정형 데이터는 클라이언트와 AWS S3가 직접 Upload/Download 하도록 설계하였습니다.
- 이를 위해 S3의 signed URL 기능을 활용했으며, 스프링은 비정형 데이터에 직접 관여하지 않고, signed URL만 생성하여 제공합니다.
- 그 결과, 데이터 전송은 클라이언트와 S3가 처리하며, 스프링에 도달하는 특정 Request의 크기가 71.1% 감소했습니다. 이로 인해 스프링의 메모리 부하도 줄어드는 효과도 얻을 수 있었습니다.

<p align="center">
<img width="806" height="298" alt="image" src="https://github.com/user-attachments/assets/8892343c-682e-4417-9c46-d94e5827ea03" />
<p>
  
### 버그 방지, 유지보수성 위한 단위 테스트 도입: Test Coverage 100% 달성
- 단위 테스트 커버리지 100%를 달성하고, JaCoCo를 활용하여 지속적으로 유지하고 있습니다.
- 이를 통해
    - 코드 변경에 따른 잠재적 오류를 조기에 발견하고 수정하여, 코드 안정성을 향상시켰습니다.
    - 코드에 대한 확신을 갖고, 더욱 빠르고 효율적으로 개발을 진행할 수 있게 되었습니다.
 
<p align="center">
<img width="859" height="285" alt="image" src="https://github.com/user-attachments/assets/2f92efe0-c62f-4f14-b5a3-35d6fce416dc" />
<p>

### 통합 테스트 결과를 기반으로한 API 문서화
- REST Assured와 Spring REST Docs를 활용하여 API 통합 테스트와 문서화를 자동화하였습니다.
- API 호출을 통해 통합 테스트를 진행하고, 그 결과를 바탕으로 Spring REST Docs가 최신 API 문서를 자동으로 생성하여 개발 효율성과 문서 정확성을 동시에 높였습니다.
- 이를 통해 API 변경 사항이 문서에 즉시 반영되므로, 항상 최신 정보가 보장되는  API 문서가 제공됩니다.

<p align="center">
<img width="760" height="273" alt="image" src="https://github.com/user-attachments/assets/1a9e9da0-2518-4473-aa44-46e8be83b70f" />
<p>
