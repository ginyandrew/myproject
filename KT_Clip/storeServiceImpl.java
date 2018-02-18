package com.kt.clip2.web.storebenefit.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kt.clip2.common.http.RestCallGeoFencing;
import com.kt.clip2.common.vo.BranchVO;
import com.kt.clip2.frw.transaction.TxMain;
import com.kt.clip2.frw.transaction.TxRequired;
import com.kt.clip2.web.storebenefit.dao.WebStoreDao;
import com.kt.clip2.web.storebenefit.vo.GeoPoint;
import com.kt.clip2.web.storebenefit.vo.GeoTrans;
import com.kt.clip2.web.storebenefit.vo.ShopVO;

@Service
@TxMain
public class WebStoreServiceImpl implements WebStoreService {

	protected static final Logger LOG = LoggerFactory.getLogger(WebStoreServiceImpl.class);
	
	@Autowired
	private WebStoreDao webStoreDao;
	@Autowired 
	private RestCallGeoFencing restCallGeoFencing;
	
	// clippartner web view host
	@Value("${clippartner.stamp.webview}")
	String clippartnerWebviewHost;
	
	@Override
	public GeoPoint convertUtmkToLatLng(double x, double y) {
		GeoPoint gp1 = new GeoPoint(x, y);
		System.out.println(" getX : "+gp1.getX()+" getY : "+gp1.getY());
		GeoPoint tm_pt1 = GeoTrans.convert( GeoTrans.UTMK,GeoTrans.GEO, gp1);
		System.out.println(tm_pt1.getY() + "," + tm_pt1.getX());
		return tm_pt1;
	}

	@Override
	public JSONObject getJson(String url) throws Exception {
		
		InputStream is = new URL(url).openStream();
		JSONObject json = new JSONObject();
		try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	     
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	        sb.append((char) cp);
	      }
	      
	      String jsonText = sb.toString();
	      JSONParser jsonParser = new JSONParser();
		json = (JSONObject) jsonParser.parse(jsonText);
			
	    } finally {
	      is.close();
	    }
		
		return json;
	}
	
	@Override
	public List<HashMap<String, Object>> getMenuListByShopId(String shopId) {

		List<String> menuCategories = webStoreDao.getMenuListByShopId(shopId);
		
		List<HashMap<String, Object>> allMenu = new ArrayList<>();
		for (String eachCategory : menuCategories){
			
			HashMap<String, Object> map = new HashMap<>();
			map.put("category", eachCategory);
			map.put("shopId", shopId);
			
			List<HashMap<String, Object>> menus = webStoreDao.getMenusByMenuCategory(map);
		
			HashMap<String, Object> menuMap = new HashMap<>();
			menuMap.put("menuGrpName", eachCategory);
			menuMap.put("menusByGrp", menus);
			
			allMenu.add(menuMap);
		}
		return allMenu;
	}
	
	@Override
	public ShopVO getShopInfoByShopId(String shopId) {
		return webStoreDao.getShopInfoByShopId(shopId);
	}

	@Override
	public Map<String, Object> getStoreInfo(String latitude, String longitude, String shop_nm) {
		
		Map<String, Object> shopInfoMap= null;
		
		if(StringUtils.hasText(latitude) && StringUtils.hasText(longitude)){
			shopInfoMap = new HashMap<String, Object>();
			List<BranchVO> retBranchList = restCallGeoFencing.getShopListByShopName(latitude, longitude, shop_nm, 1);
			
			if(!retBranchList.isEmpty()){  	
				shopInfoMap.put("branchCd", retBranchList.get(0).getBranchCd());
				shopInfoMap.put("shopId", retBranchList.get(0).getShopId());
				
				if(LOG.isDebugEnabled()){
					LOG.debug("geofencing branch params >> [branchCd : {}, shopId : {}]", shopInfoMap.get("branchCd"), shopInfoMap.get("shopId"));
				}   
			}
		}
		
		return shopInfoMap;
	}

	@Override
	public Map<String, Object> getClipPartnerShopInfo(String custId, String shopId) {
		
		String clipPartnerShop = webStoreDao.getClipPartnerShopInfo(shopId);
		
		StringBuilder infoUrl = null;
		StringBuilder menuUrl = null;
		if (clipPartnerShop != null && !"".equals(clipPartnerShop)){
			
			infoUrl = new StringBuilder();
			infoUrl.append(clippartnerWebviewHost);
			infoUrl.append("/wvclip/shopdesc?clip_user_id=");
			infoUrl.append(custId);
			infoUrl.append("&shop_id=");
			infoUrl.append(shopId);
			
			menuUrl = new StringBuilder();
			menuUrl.append(clippartnerWebviewHost);
			menuUrl.append("/wvclip/shopmenu?clip_user_id=");
			menuUrl.append(custId);
			menuUrl.append("&shop_id=");
			menuUrl.append(shopId);

		}
		
		
		
		Map<String, Object> clipPartnerShopMap = new HashMap<String, Object>();
		
		if(infoUrl != null || menuUrl != null){
			
			if(LOG.isDebugEnabled()){
				LOG.debug("clippartner WebView URL >> infoUrl : {}, menuUrl : {}", infoUrl.toString(), menuUrl.toString());
			}
			
			clipPartnerShopMap.put("info_url", infoUrl.toString());
			clipPartnerShopMap.put("menu_url", menuUrl.toString());
		}
		
		clipPartnerShopMap.put("menuYn", infoUrl != null?"y":"n");
		
		if(LOG.isDebugEnabled()){
			LOG.debug("clippartner WebView URL >> menuYn : {}", clipPartnerShopMap.get("menuYn"));
		}
		
		return clipPartnerShopMap;
	}

	@Override
	@TxRequired
	public void insertBranchLog(String branchCd, String shopId, String custId, String clipuseYN) {
		webStoreDao.insertBranchLog(branchCd, shopId, custId, clipuseYN);
	}

}
