package com.kt.clip2.web.common.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.clip2.frw.cache.ReadCache;
import com.kt.clip2.frw.transaction.TxMain;
import com.kt.clip2.web.common.dao.WebEventDao;
import com.kt.clip2.web.common.vo.EventVO;

/**
 * 멤버십 Service
 * 
 * @author		: Blucean
 * @version		: v2.0
 * @since		: 2016.08.02
 */
@TxMain
@Service
public class WebEventServiceImpl implements WebEventService{
	
	protected static final Logger LOG = LoggerFactory.getLogger(WebEventServiceImpl.class);
	
	@Autowired
	private WebEventDao eventDao;
	
	@Override
	public int getCountCommonEventList(HashMap<String, Object> map) {
		
		return eventDao.getCountCommonEventList(map);
	}

	@Override
	@ReadCache(namespace = "WebEventServiceImpl_getCommonEventList", keyIndex = {1,2,3})
	public List<EventVO> getCommonEventList(int beginPage, int endPage, String recommend, String os) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("beginPage", beginPage);
		map.put("endPage", endPage);
		map.put("recommend", recommend);
		map.put("os", os);
		
		List<EventVO> events = eventDao.getCommonEventList(map);
		
		return events;
	}
	
	@Override
	@ReadCache(namespace = "WebEventServiceImpl_getEventDetail", keyIndex = {1})
	public String getEventDetail(int idx) {
		
		String imgUrl = eventDao.getEventDetail(idx);
		
		return imgUrl;
	}
	 
}
