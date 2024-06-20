package com.unknown.paldak.admin.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class Criteria {
   private Integer pageNum; // ������ ��ȣ
   private Integer amount; // ������ �� �׸� ��
   private String type; // �˻� ����
   private String keyword; // �˻� Ű����
   private String sortColumn;
   private String groupColumn;

   public Criteria() {
       this(1,10); // �⺻�� ����
   }

   public Criteria(int pageNum, int amount) {
       this.pageNum = pageNum;
       this.amount = amount;
   }

   public String[] getTypeArr() {
       return type == null ? new String[] {} : type.split("");
   }
}






