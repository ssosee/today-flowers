# 🪻 [오늘의 꽃](https://2dayflower.com/)
## 목차
* 오늘의 꽃 소개
* 개발 목적
* 적용 기술
* 기능
* DB

## 오늘의 꽃 소개
사람들은 😻 특별한 날 **💐꽃**으로 마음을 표현하기도 합니다.<br />
그런데 꽃의 꽃말을 함께 고려하는 경우는 드문것 같습니다. 😅<br />
그 이유는 일일이 꽃에 대한 정보를 함께 찾아보는 것이 **귀찮기 때문**이라고 생각합니다.<br /><br />
**오늘의 꽃**은 **꽃 이름**, **꽃말**, 그리고 꽃에 대한 간단한 **이야기**를 함께 제공하여<br />
**상대방에게 나의 마음을 더 확실하게 전달**할 수 있게 도와줍니다.

## 개발 목적
* 꽃 선물할 때 **꽃말을 찾아보는것에 불편함을 느껴 개발**을 진행
* 공공 데이터(농촌진흥청 국립원예특작과학원)를 이용하여 **꽃 이름, 꽃말 등으로 꽃을 검색할 수 있는 웹 서비스**
* 학습한 기술을 **연습**하고, **개발/배포/운영 경험**을 목적으로 개인프로젝트를 진행함

## 적용 기술
<div align="center">
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/maridaDB-1F305F?style=for-the-badge&logo=mariadb&logoColor=white">
<br /><br />
<img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">
<img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
<br /><br />
<img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
<img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
</div>
<br />

## 도메인
[오늘의 꽃](https://2dayflower.com)

## 화면
![img.png](img.png)

![img_1.png](img_1.png)

![img_3.png](img_3.png)

![img_2.png](img_2.png)

## 기능
### 회원
`FormLogin` 방식과 `OAuth2Login` 방식을 병행함
* 일반 회원가입
* 소셜 회원가입
  * 카카오톡
* 아이디 찾기
* 비밀번호 찾기
  * `smtp` 활용하여 임시 비밀번호 회원 이메일로 전송
  * 비동기 방식으로 구현함
* 내 정보
* 회원탈퇴
* 이메일 변경
* 비밀번호 변경
  * 일반 회원만 가능
* 로그인
* 로그아웃

### 꽃
* 오늘의 꽃
  * 오늘 날짜를 기준으로 꽃 조회
* 생일의 꽃
  * 생일로 꽃 조회
* 꽃말의 꽃
  * 꽃말로 꽃 조회
* 이름의 꽃
  * 이름으로 꽃 조회
* 좋아요 랭킹
  * 좋아요 내림차순으로 꽃 조회
* 좋아요
  * `Ajax`를 활용하여 구현
* 좋아한 꽃
  * 회원이 좋아요 누른 꽃 조회

## DB
![DB.png](DB.png)
