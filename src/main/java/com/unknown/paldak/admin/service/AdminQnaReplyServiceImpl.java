package com.unknown.paldak.admin.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unknown.paldak.admin.common.domain.Criteria;
import com.unknown.paldak.admin.domain.QNAReplyVO;
import com.unknown.paldak.admin.mapper.AdminQnaReplyMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
@AllArgsConstructor
public class AdminQnaReplyServiceImpl implements BaseService<QNAReplyVO>{
    
	@Autowired
	private AdminQnaReplyMapper mapper;

	@Override
	public void register(QNAReplyVO replyVO) {
		System.out.println("111111111111111klsfkldajflkjsalkfsdlkfhsldfkhl");
		if(replyVO.getReply() != null && replyVO.getReply().length()!=0) {
			replyVO.setAnswer('Y');
		}
		log.info("register... " + replyVO);
		mapper.insertSelectKey(replyVO);
		
	}

	@Override
	public QNAReplyVO get(Long replyId) {
		log.info("get..." + replyId);	
		return mapper.read(replyId);
	}

	@Override
	public boolean modify(QNAReplyVO replyVO) {
		LocalDateTime now = LocalDateTime.now();
		Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		replyVO.setReplyUpdateDate(date);
		return mapper.update(replyVO)==1;
	}

	@Override
	public boolean remove(Long replyId) {
		log.info("remove ... " + replyId);
		return mapper.delete(replyId)==1;
	}

	@Override
	public List<QNAReplyVO> getList(Criteria cri) {
		System.out.println(cri+"reply");
		return mapper.getList();
	}
	
	@Override
	public List<QNAReplyVO> getDescList(Criteria cri) {
		System.out.println(cri);
		return mapper.getDescListWithPaging(cri);
	}
	
	@Override
	public int getTotal(Criteria cri) {
		return mapper.getTotalCount(cri);
	}

	public QNAReplyVO getByQnaId(Long qnaId) {
		return mapper.readByQnaId(qnaId);
	}
	
	
	
}