package com.unknown.paldak.admin.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.unknown.paldak.admin.common.domain.Criteria;
import com.unknown.paldak.admin.domain.BrandVO;
import com.unknown.paldak.admin.mapper.AdminBrandMapper;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminBrandServiceImpl implements BaseService<BrandVO>{
    
	@Autowired
	private AdminBrandMapper mapper;

	@Override
	public void register(BrandVO brandVO) {
		mapper.insertSelectKey(brandVO);
		
	}

	@Override
	public BrandVO get(Long brandId) {
		return mapper.read(brandId);
	}

	
	@Override
	public boolean modify(BrandVO brandVO) {
		LocalDateTime now = LocalDateTime.now();
		Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		brandVO.setUpdateDate(date);
		return mapper.update(brandVO)==1;
	}

	@Override
	public boolean remove(Long brandId) {
		return mapper.delete(brandId)==1;
	}

	@Override
	public List<BrandVO> getList(Criteria cri) {
		return mapper.getListWithPaging(cri);
	}
	
	@Override
	public List<BrandVO> getDescList(Criteria cri) {
		return mapper.getDescListWithPaging(cri);
	}
	
	@Override
	public int getTotal(Criteria cri) {
		return mapper.getTotalCount(cri);
	}

	
}