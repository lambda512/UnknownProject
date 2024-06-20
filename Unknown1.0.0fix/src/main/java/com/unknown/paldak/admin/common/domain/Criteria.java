package com.unknown.paldak.admin.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class Criteria {
   private Integer pageNum; // 페이지 번호
   private Integer amount; // 페이지 당 항목 수
   private String type; // 검색 조건
   private String keyword; // 검색 키워드
   private String sortColumn;
   private String groupColumn;

   public Criteria() {
       this(1,10); // 기본값 설정
   }

   public Criteria(int pageNum, int amount) {
       this.pageNum = pageNum;
       this.amount = amount;
   }

   public String[] getTypeArr() {
       return type == null ? new String[] {} : type.split("");
   }
}






