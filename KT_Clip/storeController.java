package com.kt.clip2.web.storebenefit.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kt.clip2.common.http.RestCallGeoFencing;
import com.kt.clip2.common.vo.BranchVO;
import com.kt.clip2.frw.exception.MessageException;
import com.kt.clip2.web.storebenefit.service.WebStoreService;
import com.kt.clip2.web.storebenefit.vo.GeoPoint;
import com.kt.clip2.web.storebenefit.vo.ShopVO;

@Controller("v2StoreController")
@RequestMapping(value="/cv2/wv/storeBenefit/store")
public class StoreController {
	
	protected static final Logger LOG = LoggerFactory.getLogger(StoreController.class);
	
	@Autowired 
	private RestCallGeoFencing restCallGeoFencing;
	@Autowired
	private WebStoreService webStoreService;
	
	
	public static JSONObject getJsonFromBranchVO(BranchVO vo){
	
		JSONObject data = new JSONObject();
		
		data.put("branchName", vo.getBranchName());
		data.put("shopId", vo.getShopId());
		data.put("coord", vo.getCoord());
		data.put("addr", vo.getAddr());
		data.put("tel", vo.getTel());
		data.put("branchCd", vo.getBranchCd());
		
		return data;
	}
	
	@RequestMapping(value = "/detail.do", method = RequestMethod.POST)
	public String detailMapInfo(HttpServletRequest request, Model model,
			HttpServletResponse response,
			@RequestParam(value="latitude", required=true) String latitude,
			@RequestParam(value="longitude", required=true) String longitude,
			@RequestParam(value="shopId", required=true) String shopId,
			
			@RequestParam(value="branchCnt", required=false) Integer limit,
			@RequestParam(value="custId", required=false) String custId,
			@RequestParam(value="branchCd", required=false) String branchCd
			) throws Exception {

		if(longitude == null || "".equals(longitude)) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"longitude"} );
		}
		if(latitude == null || "".equals(latitude)) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"latitude"} );
		}
		if(shopId == null  || "".equals(shopId)){
			throw new MessageException("SVC.COM_4001.400", new Object[]{"shopId"} );
		}
		model.addAttribute("latitude" , latitude);
		model.addAttribute("longitude", longitude);
		model.addAttribute("shopId", shopId);		// 위도,경도,shopId저장.
		
		
		// (1) shopId로 일단 네임을 찾은 후, 혹시 name을 준 경우 name을 덮어씌운다.
		ShopVO shop = webStoreService.getShopInfoByShopId(shopId);
		String shopName = shop.getShopName();
		if(request.getParameter("shopNm")!=null && !"".equals(request.getParameter("shopNm"))){
			shopName = URLDecoder.decode(request.getParameter("shopNm"),"UTF-8");
		}
		model.addAttribute("shopName", shopName);	// shopName저장.
		
		
		
		// (2) 소호인지 아닌지 결정. 	>> custId가 있다면 반드시 branchCd가 있어야함. (반대는 상관없음.)
		Map<String, Object> clipPartnerShopMap = new HashMap<String, Object>();
		if("".equals(custId) || custId == null) {
			
			clipPartnerShopMap.put("menuYn", "n");
		} else {	  
			//BranchLog => Kailos 지점 코드 길이 초과로 branch_log insert 생략처리.
			String kShopId = shopId.substring(0,3);
			
			if(LOG.isDebugEnabled()){
				LOG.debug("kShopId : {}", kShopId);
			}
			
			if(!"KAI".equals(kShopId) && branchCd != null && !"".equals(branchCd)){		 
				webStoreService.insertBranchLog(branchCd, shopId, custId, "N");	// 저장한다.
			}
			/* 클립파트너인지 확인 - infoUrl menuUrl이 담긴다. 없으면 {menuYn=n} 게 담김.*/
			clipPartnerShopMap = webStoreService.getClipPartnerShopInfo(custId, shopId);
		}
		model.addAttribute("sohoInfo", clipPartnerShopMap);	// 소호인지 아닌지 정보저장.
		
		
		
		
		
		// (3) menuYn/limit을 고려해 마커갯수 count 정하기.
		int count = 0;
		if ("y".equals(clipPartnerShopMap.get("menuYn"))){	// 소호면 마커는 한개.
			count = 1;
		} else {											// 소호가 아니면
			if (limit == null || limit.equals("")){			
				count = 20;			// limit이 없으면 기본값 20개
			} else {
				count = limit;		// limit이 있으면 그 limit값을 따라감.
			}
		}
		model.addAttribute("limit", count);
		
		
		
		
		
		
		// (4) branchCd가 없으면 - lat/lang/shopName을 넣어 limit만큼 뽑는다.
		List<BranchVO> branchs = new ArrayList<BranchVO>();
		List<BranchVO> list = new ArrayList<BranchVO>();
		
		if (branchCd == null || "".equals(branchCd)){
			branchs = restCallGeoFencing.getShopListByShopName(latitude, longitude, shopName, count);
			
			// return 1번 >> branchCd가 없고 지점이 2개 이상인 경우 
			if (branchs.size() > 1){
				list = new ArrayList<BranchVO>();
				
				for (BranchVO vo : branchs){
					BranchVO branch = restCallGeoFencing.getBranchByBranchCd(vo.getBranchCd());
					list.add(branch);
				}
				
				JSONArray arr = new JSONArray();
				for (int i = 0 ; i<list.size() ; i++){
					JSONObject data = new JSONObject();
					BranchVO vo = list.get(i);

					data = getJsonFromBranchVO(vo);
					if (i == 0){
						data.put("center", "y");
					}
					arr.add(data);
				}
				
				String encodedArr = URLEncoder.encode(arr.toString(), "UTF-8");
				model.addAttribute("branchs", encodedArr);
				
				return "/store/detail/mapMultiStore";

				
				
			// return 2번 >> branchCd가 없고 지점이 1개인 경우.
			} else if (branchs.size() == 1){
				
				BranchVO branch = restCallGeoFencing.getBranchByBranchCd(branchs.get(0).getBranchCd());

				JSONObject data = new JSONObject();
				data = getJsonFromBranchVO(branch);
				
				String encodedData = URLEncoder.encode(data.toString(), "UTF-8");
				model.addAttribute("branch", encodedData);
			
				return "/store/detail/mapOneStore";
				
			// return 3번 >> branchCd가 없고 결과값도 없는 경우.
			} else if (branchs.size() < 1 || branchs.isEmpty()){
				return "/store/detail/noData";				
			}
			
			
		} else {
			model.addAttribute("branchCd", branchCd);
			
			// return 4번 >> branchCd가 있고 지점이 1개
			if(count == 1){
				BranchVO branch = restCallGeoFencing.getBranchByBranchCd(branchCd);
				 
				JSONObject data = new JSONObject();
				data = getJsonFromBranchVO(branch);
				
				String encodedData = URLEncoder.encode(data.toString(), "UTF-8");
				model.addAttribute("branch", encodedData);
			
				return "/store/detail/mapOneStore";
				
				
			// return 5번 >> branchCd가 있고 지점이 2개 이상.	
			} else if (count > 1){
				//(5-0)  branchCd를 기준으로 출력한다.
				// 해당 branchCd 지점 출력.
				BranchVO branch = restCallGeoFencing.getBranchByBranchCd(branchCd);
				JSONObject centerObj = getJsonFromBranchVO(branch);
				centerObj.put("center","y");
				
				// 나머지 limit-1 branchCd 출력.
				String utmk = branch.getCoord().substring(branch.getCoord().indexOf("(") +1 , branch.getCoord().indexOf(")"));
				String[] utmkCoords = utmk.split(" ");
				double x = Double.parseDouble(utmkCoords[0]);
				double y = Double.parseDouble(utmkCoords[1]);
				GeoPoint latlng = webStoreService.convertUtmkToLatLng(x, y);
				String lat = Double.toString(latlng.getY());
				String lng = Double.toString(latlng.getX());
				branchs = restCallGeoFencing.getShopListByShopName(lat, lng, shopName, count);
				
				for (BranchVO vo : branchs){
					BranchVO eachVo = restCallGeoFencing.getBranchByBranchCd(vo.getBranchCd());
					list.add(eachVo);		// list에 limit-1의 매장들 저장.
				}
				
				list.add(branch);			// list에 최초 branch 저장.
				
				JSONArray arr = new JSONArray();
				for (int i = 0 ; i<list.size() ; i++){
					
					JSONObject data = new JSONObject();
					BranchVO vo = list.get(i);
					if (!branchCd.equals(vo.getBranchCd())){
						data = getJsonFromBranchVO(vo);
						arr.add(data);
					}
				}
				arr.add(centerObj);
				
				if(LOG.isDebugEnabled()){
					LOG.debug("매장 지도 값 : {}, limit : {}", arr.toString(), limit);
				}
				
				String encodedArr = URLEncoder.encode(arr.toString(), "UTF-8");
				model.addAttribute("branchs", encodedArr);
				return "/store/detail/mapMultiStore";
			}
		}
		
		return "/store/detail/noData";
	}
	 
	
	@RequestMapping(value = "/storeInfos.do", method = RequestMethod.GET)
	public ModelAndView detailShopInfo (String longitude, String latitude, String shop_nm, Integer limit) throws UnsupportedEncodingException{
		
		if("".equals(longitude) || longitude == null) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"longitude"} );
		}
		if("".equals(latitude) || latitude == null) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"latitude"} );
		}
		if("".equals(shop_nm) || shop_nm == null) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"shop_nm"} );
		}
		if("".equals(limit) || limit == null) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"limit"} );
		}
		
		ModelAndView mav = new ModelAndView();
		shop_nm = URLDecoder.decode(shop_nm, "UTF-8");
		List<BranchVO> branchs = restCallGeoFencing.getShopListByShopName(latitude, longitude, shop_nm, limit);
		mav.addObject("branchs", branchs);
		return mav;
	}
	
	@RequestMapping(value="/branchInfo.do", method= RequestMethod.GET)
	public ModelAndView detailBranchInfo (String branchCd){

		if("".equals(branchCd) || branchCd == null) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"branchCd"} );
		}
		
		ModelAndView mav = new ModelAndView();
		
		BranchVO branch = restCallGeoFencing.getBranchByBranchCd(branchCd);
		mav.addObject("branch", branch);
		return mav;
	}
	
	@RequestMapping(value="/menuInfo.do", method= RequestMethod.GET)
	public ModelAndView menuInfoByShopId (String shopId){
		
		if("".equals(shopId) || shopId == null) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"shopId"} );
		}
		
		ModelAndView mav = new ModelAndView();
		
		List<HashMap<String, Object>> allMenus = webStoreService.getMenuListByShopId(shopId);
		
		mav.addObject("allMenus", allMenus);
		return mav;
	}
	
	@RequestMapping(value="/multiBranchInfo.do", method= RequestMethod.GET)
	public ModelAndView detailMultiBranchInfo (String longitude, String latitude, String shop_nm, Integer limit){

		if("".equals(longitude) || longitude == null) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"longitude"} );
		}
		if("".equals(latitude) || latitude == null) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"latitude"} );
		}
		if("".equals(shop_nm) || shop_nm == null) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"shop_nm"} );
		}
		if("".equals(limit) || limit == null) {
			throw new MessageException("SVC.COM_4001.400", new Object[]{"limit"} );
		}
		
		ModelAndView mav = new ModelAndView();
		try {
			shop_nm = URLDecoder.decode(shop_nm, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		List<BranchVO> branchs = restCallGeoFencing.getShopListByShopName(latitude, longitude, shop_nm, limit);
		List<BranchVO> list = new ArrayList<>();
		
		for (BranchVO vo : branchs){
			BranchVO branch = restCallGeoFencing.getBranchByBranchCd(vo.getBranchCd());
			branch.setDistance(vo.getDistance());
			list.add(branch);
		}
		
		mav.addObject("branch", list );
		return mav;
	}
	
	
	
}
