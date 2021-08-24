# buy-something
쇼핑몰 상품 주문 서비스 입니다. 

## 테이블 구조

#### 회원 보유금액 정보 테이블

| 순서 | 한글항목명 | 영문항목명 | 속성 | 길이 | NULL | KEY | DEFAULT | Extra |
| :--: | :--------: | :-------: | :--: | :--: | :--: | :-: | :-----: | :---: |
| 1 | ID | id | BIGINT | | NOTNULL | PK | | auto_increment |
| 2 | 사용자이름 | name | VARCHAR | 255 | NOTNULL | | | | 
| 3 | 적립금 | savings | INT | | NOTNULL | | 0 | |

#### 포인트 유효기간 관리 테이블

| 순서 | 한글항목명 | 영문항목명 | 속성 | 길이 | NULL | KEY | DEFAULT | Extra |
| :--: | :--------: | :-------: | :--: | :--: | :--: | :-: | :-----: | :---: |
| 1 | ID | id | BIGINT | | NOTNULL | PK | | auto_increment |
| 2 | 사용자ID | user_id | BIGINT | | NOTNULL | FK | | | 
| 3 | 포인트 | amount | INT | | NOTNULL | | | |
| 4 | 최종수정일 | modified_date | DATETIME | 6 | NOTNULL | | |
| 5 | 만료일 | expiration_date | DATETIME | 6 | | NULL | | 

#### 회원 할인 쿠폰 보유 정보 테이블

| 순서 | 한글항목명 | 영문항목명 | 속성 | 길이 | NULL | KEY | DEFAULT | Extra |
| :--: | :--------: | :-------: | :--: | :--: | :--: | :-: | :-----: | :---: |
| 1 | ID | id | BIGINT | | NOTNULL | PK | | auto_increment |
| 2 | 사용자ID | user_id | BIGINT | | NOTNULL | FK | | | 
| 3 | 쿠폰명 | name | VARCHAR | 255 | | | | |
| 4 | 할인율 | discount_rate | INT | | NOTNULL | | |
| 5 | 최소주문금액 | min_amount | INT | | | | | 
