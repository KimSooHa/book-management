# book-management - 도서 관리 시스템

## 개발 환경
IntelliJ IDE 2022.2.4(Community Edition), SpringBoot 2.7.7, JDK 11, Mariadb 3.0.9, Gradle, Lombok 1.18.24, Spring Data JPA 2.7.7, AWS(EC2)

## ERD
![image](https://github.com/KimSooHa/book-management/assets/81688625/5ad7980d-42f5-4e5f-a11f-88058c587157)

### 테이블 정의서
> User 테이블
> - userId : 회원 고유 식별자
> - name : 회원 이름
> - loginId : 회원 로그인 아이디
> - pwd : 회원 비밀번호
> - email : 회원 이메일

> Book 테이블
> - bookId : 도서 고유 식별자
> - title : 도서 제목
> - author : 도서 작가
> - isbn : 도서 ISBN
> - publicDate : 도서 출판일
> - borrowed : 도서 대출 여부
> - borrowedId : 대출 중인 경우 대출자의 사용자 ID
> - borrowedDate : 대출중인 경우 대출일

> Loan 테이블
> - loanId : 대출 이력 고유 식별자
> - userId : 대출을 한 사용자 ID(User 테이블과 연결)
> - bookId : 대출한 도서의 ID(Book 테이블과 연결)
> - borrowedDate : 대출한 날짜
> - returnedDate : 반납한 날짜

## 아키텍처
<img width="969" alt="image" src="https://github.com/KimSooHa/book-management/assets/81688625/3d4b8cdc-e1bd-4cf2-8a54-64f9590f0d0f">

> http://ec2-52-79-220-234.ap-northeast-2.compute.amazonaws.com:8083 을 통하여 각 기능별로 시연해볼 수 있습니다.


## 기능 정의서

### 회원가입 API
#### 기능 설명 : 회원을 등록하는 API로, 새로운 회원 정보를 추가합니다.
- 입력:
  - HTTP Method : POST
  - uri : 'api/users/signup'
  - requestBody :
  ```
  {
  "name": "test",
  "loginId" : "test123",
  "pwd": "test123*",
  "email": "test@example.com"
  }
  ```
 - 출력
 - 성공 시 : Http Status 200 OK 및 등록된 회원의 ID 반환
 - 실패 시 : Http Status 400 Bad Request 및 에러 메시지 반환

### 도서 등록 API
#### 기능 설명 : 도서를 등록하는 API로, 새로운 도서 정보를 추가합니다.
- 입력:
    - HTTP Method : POST
    - uri : 'api/books'
    - requestBody :
  ```
  {
  "title": "Example book",
  "author": "example author",
  "isbn": "9780134685991",
  "publicDate": "2008-05-08"
  }
  ```
- 출력:
- 성공 시 : Http Status 200 OK 및 등록된 도서의 ID 반환
- 실패 시 : Http Status 400 Bad Request 및 에러 메시지 반환

### 도서 수정 API
#### 기능 설명 : 도서를 수정하는 API로, 새로운 도서 정보를 추가합니다.
- 입력:
    - HTTP Method : PUT
    - uri : 'api/books/{bookId}'
    - requestBody :
  ```
  {
  "title": "book"
  }
  ```
- 출력:
- 성공 시 : Http Status 200 OK 및 성공 메시지 반환
- 실패 시 : Http Status 400 Bad Request 및 에러 메시지 반환

### 대출 처리 API
#### 기능 설명 : 사용자가 도서를 대출할 때 호출되는 API로, 대출 이력을 생성하고 도서의 대출 상태를 변경합니다.
- 입력:
    - HTTP Method : POST
    - uri : 'api/books/{bookId}/loan'
    - Request Parameter: userId (대출자의 사용자 ID)
- 출력:
- 성공 시 : Http Status 200 OK 및 성공 메시지 반환
- 실패 시 : Http Status 400 Bad Request 및 에러 메시지 반환

### 반납 처리 API
#### 기능 설명 : 사용자가 도서를 대출할 때 호출되는 API로, 대출 이력을 생성하고 도서의 대출 상태를 변경합니다.
- 입력:
    - HTTP Method : POST
    - uri : 'api/books/{bookId}/return'
- 출력:
- 성공 시 : Http Status 200 OK 및 성공 메시지 반환
- 실패 시 : Http Status 400 Bad Request 및 에러 메시지 반환

### 대출 이력 확인 API
#### 기능 설명 : 사용자가 도서를 대출할 때 호출되는 API로, 대출 이력을 생성하고 도서의 대출 상태를 변경합니다.
- 입력:
    - HTTP Method : GET
    - uri : 'api/books/{bookId}/loan-history'
- 출력:
- 성공 시 : Http Status 200 OK 및 대출 이력 반환
- 실패 시 : Http Status 400 Bad Request 및 에러 메시지 반환