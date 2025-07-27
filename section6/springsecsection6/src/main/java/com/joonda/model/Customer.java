package com.joonda.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/*
* JPA 에서 사용하는 Annotation
* Customer 클래스는 DB Table에 대응된다
* 필드들은 테이블의 컬럼으로 매핑된다.
* JPA가 이 객체를 관리하며, DB CRUD 작업을 쉽게 할 수 있다.
* 반드시 Primary Key가 존재해야함 (@Id)
* */
@Entity

/*
* 이미 Customer 클래스와 DB Table인 customer 명칭이 일치하지만
* 편의를 위해 customer 라는 테이블에 대응함을 명시
* */
@Table(name = "customer")
@Getter @Setter
public class Customer {

  /*
  * Primary Key 임을 명시
  * */
  @Id
  /*
  * Primary Key인 id를 insert 쿼리를 날릴 때 값을 명시하지 않음
  * DB가 새로운 PK 값을 생성한다.
  * 즉 IDENTITY 전략은 DB가 기본 키 값을 자동으로 생성하도록 한다. (auto_increment)로 동작한다.
  * */
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String email;
  private String pwd;
  /*
  * role 필드가 테이블의 컬럼과 일치하지 않을때는 @Column Annotation을 사용
  * 현재는 같지만 편의를 위해 명시를 해놓은 상태!
  * */
  @Column(name = "role")
  private String role;
}
