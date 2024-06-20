package com.unknown.paldak.admin.common.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageDTO {
	private int startPage;
	private int endPage;
	private boolean prev, next;

	private Criteria cri; 
	private int total; //// 전체 항목 수

	public PageDTO(Criteria cri, int total) {
		this.cri = cri;
	    this.total = total; // 전체 항목 수

	    this.endPage = (int) (Math.ceil(cri.getPageNum() / 10.0)) * 10; // 끝 페이지 번호
	    this.startPage = this.endPage - 9; // 시작 페이지 번호

	    int realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount())); // 실제 끝 페이지 번호

	    if (realEnd <= this.endPage) {
	        this.endPage = realEnd;
	    }

	    this.prev = this.startPage > 1;
	    this.next = this.endPage < realEnd;

	    System.out.println("Page Number: " + cri.getPageNum());
	    System.out.println("Total: " + total);
	    System.out.println("End Page: " + endPage);
	    System.out.println("Start Page: " + startPage);
	    System.out.println("Real End: " + realEnd);
	    System.out.println("Prev: " + prev);
	    System.out.println("Next: " + next);
	}
}
