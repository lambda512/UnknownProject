package com.unknown.paldak.admin.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FAQVO {
	   private Long faqId;
	   private String faqTitle;
	   private String faqDescription;
	   private String faqCategory;
	   private String faqImageURL;
	   private Date faqRegdate;
	   private Date faqUpdateDate;
}
