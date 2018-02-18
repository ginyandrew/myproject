/**
 * 
 */
package com.kt.clip2.web.coupon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kt.clip2.common.http.RestCallGeoFencing;
import com.kt.clip2.web.coupon.service.WebCouponService;
import com.kt.clip2.web.coupon.vo.CouponVO;
import com.kt.clip2.web.storebenefit.service.WebStoreService;
import com.kt.clip2.web.storebenefit.vo.ShopVO;

/**
 * @author blucean
 *
 */
@Controller("v2CouponController")
@RequestMapping(value = "/cv2/wv/coupon")
public class CouponController {

	@Autowired 
	private RestCallGeoFencing restCallGeoFencing;
	@Autowired
	private WebCouponService webCouponService;
	@Autowired
	private WebStoreService webStoreService;
	@RequestMapping(value = "/externalCoupon.do", method = RequestMethod.GET)
	public String externalCoupon() {
		
		return "/coupon/externalCoupon";
	}
	
	@RequestMapping(value = "/detail/info.do", method = RequestMethod.GET)
	public String detailInfo(Model model, @RequestParam(value="couponId", required=true) String cpnId) {
		
		CouponVO coupon = webCouponService.getCouponInfoByCpnId(cpnId);
		String shopId = webCouponService.getShopIdByCpnId(cpnId);
		model.addAttribute("shopId", shopId);
		
		if (coupon == null){
			model.addAttribute("noInfo", "해당 쿠폰의 정보가 없습니다.");
		} else {
			model.addAttribute("couponInfo", coupon);
		}
		
		ShopVO shop = webStoreService.getShopInfoByShopId(shopId);
		if(shop == null){
			model.addAttribute("noShopInfo", "해당 매장의 위치정보가 없습니다.");
		} else {
			String shopName = shop.getShopName();
			model.addAttribute("shopName", shopName);
		}
		
		return "/coupon/detail/info";
	}
	
}
