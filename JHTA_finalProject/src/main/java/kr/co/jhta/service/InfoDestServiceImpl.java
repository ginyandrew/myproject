package kr.co.jhta.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.jhta.dao.DestDAO;
import kr.co.jhta.dao.PlanDAO;
import kr.co.jhta.dao.UserDAO;
import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.ClipboardVO;
import kr.co.jhta.domain.DestImgVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.ReviewVO;
import kr.co.jhta.domain.UserVO;
@Service
public class InfoDestServiceImpl implements InfoDestService {
	
	@Autowired
	DestDAO destDao;
	@Autowired
	UserDAO userDao;
	@Autowired
	PlanDAO planDao;
	
	@Override
	public void chingePlanArriveDate(int userNo) {
		planDao.updatePlanArriveDate(userNo);
		
	}
	@Override
	public boolean hasClipBoard(int userNo, int destNo) {
		ClipboardVO vo= new ClipboardVO();
		DestVO dest = new DestVO();
		dest.setNo(destNo);
		UserVO user = new UserVO();
		user.setNo(userNo);
		vo.setDest(dest);
		vo.setUser(user);
		
		ClipboardVO resultVO = planDao.getClipBoard(vo);
		
		System.out.println(resultVO!=null?resultVO.getDest().getNo():"null이래요");
		
		return resultVO!=null;
	}
	
	@Override
	public void addClipBoard(int userNo, int destNo) {

		ClipboardVO vo= new ClipboardVO();
		DestVO dest = new DestVO();
		dest.setNo(destNo);
		UserVO user = new UserVO();
		user.setNo(userNo);
		vo.setDest(dest);
		vo.setUser(user);
		
		planDao.addClipBoard(vo);
		
	}
	
	public void updatePlanArriveDate(PlanVO vo){
		planDao.updatePlanArriveDate(vo);
	}
	
	@Override
	public Map<String, BigDecimal> searchCountByThemeCode(int destNo) {
		
		return destDao.getCountByThemeCode(destNo);
	}
	
	@Override
	public Map<String, BigDecimal> searchEmotionCountByDest(int destNo) {
		return destDao.getEmotionCountByDest(destNo);
	}
	
	/**
	 *@category 리뷰
	 *@param	여행지 번호에 맞는 이미지를 찾아낸다.
	 *@author redsoul007
	 *@return  Integer
	 */
	public Integer getCountByReview(int destNo) {
		return destDao.getCountByReview(destNo);
	}
	
	/**
	 * @category 여행지 이미지
	 * @param	여행지 번호에 맞는 이미지를 찾아낸다.
	 * @author redsoul007
	 * @return DestImgVO
	 */
	
	@Override
	public DestImgVO searchDestImgByDestNo(int destNo) {
		List<DestImgVO> imgList = destDao.getDestImgsByDestNo(destNo);
		return imgList==null||imgList.size()==0?null:imgList.get(0);
	}
	
	/**
	 * @category 유저
	 * @param	유저의 이름을 찾아온다.
	 * @return	String
	 * @author redsoul007
	 */
	@Override
	public String searchUserNameByUserNo(int userNo) {
		return destDao.getUserNameByUserNo(userNo);
	}
	
	@Override
	public List<UserVO> searchUserIdByUserNo(int no) {
		
		return destDao.getUserNoByDestNo(no);
				
	}
	
	@Override
	public List<UserVO> searchUserNoByDestNo(int destNo) {
		return destDao.getUserNoByDestNo(destNo);
	}
	/**
	 * @category 유저
	 * @param 여행지번호를 받아서 여행지에 리뷰를 불러오기
	 * @return List<ReviewVO>
	 * @author redsoul007
	 */
	@Override
	public List<ReviewVO> searchReviewByDestNo(int destNo) {
		return destDao.getReviewByDestNo(destNo);
	}
	
	public void addNewDest(DestVO vo) {
		/**@category 여행지 
		 * @param 새로운 일정을 추가하는 기능
		 * @return void
		 * @author redsoul007
		 */
		
		destDao.addDest(vo);
	}
	/**
	 * @category 여행지
	 * @param 일정번호에 대한 일정 지우기
	 * @return void
	 * @author redsoul007
	 */
	public void deleteDestByNo(int no) {
		destDao.deleteDestByNo(no);
		
	}
	/**
	 * @category 	여행지 이미지
	 * @param 		새로운 여행지의 이미지 추가하기
	 * @return 		void
	 * @author 		redsoul007
	 */
	public void addNewDestImg(DestImgVO city) {
		destDao.addDestImg(city);
		
	}
	/**
	 * @category 	리뷰
	 * @param 	 	새로운 리뷰 등록하기
	 * @return		void
	 * @author 		redsoul007
	 */
	@Override
	public void addNewReview(ReviewVO review) {
		destDao.addReview(review);
		
	}
	/**
	 * @category	카테고리
	 * @param		코드에 맞는 카테고리 지우기
	 * @return		void
	 * @author redsoul007
	 */
	@Override
	public void deleteCategoryCodeByCode(int code) {
		destDao.deleteCategoryCodeByCode(code);
		
	}
	/**
	 * @category	여행지 이미지
	 * @param		이미지 번호에 맞는 여행지의 이미지를 지운다.
	 * @return		void
	 * @author redsoul007
	 */
	@Override
	public void deleteDestImgByNo(int no) {
		destDao.deleteDestByNo(no);
		
	}
	/**
	 * @category	리뷰
	 * @param		번호에 맞는 리뷰를 삭제
	 * @return		void
	 * @author redsoul007
	 */
	@Override
	public void deleteReviewbyNo(int no) {
		destDao.deleteReviewbyNo(no);
	}
	/**
	 * @category	리뷰
	 * @param		유저가 쓴 번호에 맞게 삭제
	 * @return		void
	 * @author redsoul007
	 */
	@Override
	public void removeReviewByUserNo(int userNo) {
		destDao.deleteReviewbyNo(userNo);
	}
	/**
	 * @category	카테고리
	 * @param		모든 카테고리 코드를 불러오기
	 * @return		List<CategoryCodeVO>
	 * @author redsoul007
	 */
	@Override
	public List<CategoryCodeVO> getAllCategoryCode() {
		List<CategoryCodeVO> destList = destDao.getCategoryCode();
		return destList;
	}
	/**
	 * @category	여행지
	 * @param		모든 여행지 불러오기
	 * @return		List<DestVO>
	 * @author redsoul007
	 */
	@Override
	public List<DestVO> getAllDest() {
		List<DestVO> allDest = destDao.getDest();
		return allDest;
	}
	/**
	 * @category	여행지 이미지
	 * @param		모든 여행지 이미지 불러오기
	 * @return		List<DestImgVO>
	 * @author redsoul007
	 */
	@Override
	public List<DestImgVO> getAllDestImg() {
		List<DestImgVO> destImg = destDao.getDestImg();
		return destImg;
	}
	/**
	 * @category	여행지 이미지
	 * @param		번호에 맞는 이미지 가져오기
	 * @return		DestImgVO
	 * @author redsoul007
	 */
	public List<DestImgVO> serchDestImgByNo(int no) {
		List<DestImgVO> destImgByNo = destDao.getDestImgByNo(no);
		return destImgByNo;
	}
	/**
	 * @category	카테고리
	 * @param		코드번호에 맞는 카테고리 가져오기
	 * @return		CategoryCodeVO
	 * @author redsoul007
	 */
	public CategoryCodeVO serchCategoryCodeByCode(int code) {
		
		return destDao.getCategoryCodeByCode(code);
	}
	/**
	 * @category	여행지
	 * @param		번호에 맞는 여행지 불러오기
	 * @return		DestVO
	 * @author redsoul007
	 */
	@Override
	public DestVO searchDestByNo(int no) {
		
		return destDao.getDestByNo(no);
	}
	/**
	 * @category	리뷰
	 * @param		전체 리뷰 불러오기
	 * @return		List<ReviewVO>
	 * @author redsoul007
	 */
	public List<ReviewVO> serchReview() {
		
		return destDao.getReview();
	}
	/**
	 * @category	리뷰
	 * @param		번호에 맞는 리뷰 검색
	 * @return		ReviewVO
	 * @author redsoul007
	 */
	
	@Override
	public ReviewVO serchReviewbyNo(int no) {
				
		return destDao.getReviewbyNo(no);
	}
	/**
	 * @category	카테고리
	 * @param 카테고리 테이블 안의 코드와 맞는 카테고리.
	 * @return CategoryCodeVO
	 * @author redsoul007
	 */
	@Override
	public CategoryCodeVO searchCategoryCodeByCode(int code) {
		
		return destDao.getCategoryCodeByCode(code);
	}
	/**
	 * @category 이미지
	 * @param 번호에 맞는 여행지 이미지.
	 * @return DestImgVO
	 * @author redsoul007
	 */
	@Override
	public List<DestImgVO> searchDestImgByNo(int no) {
		
		return destDao.getDestImgByNo(no);
	}
	/**
	 * @category 리뷰
	 * @param 리뷰 테이블안에 있는 모든 리뷰.
	 * @return List<ReviewVO>
	 * @author redsoul007
	 */
	@Override
	public List<ReviewVO> searchReview() {
		
		return destDao.getReview();
	}
	/**
	 * @category 카테고리
	 * @param 여행지 번호에 맞는 카테고리.
	 * @return DestCategoryVO
	 * @author redsoul007
	 */
	public List<CategoryCodeVO> getDestCategoryByDestNo(int destNo) {
		
		return destDao.getCategoryByDestNo(destNo);
	};
	
	public String searchCategoryNameByDestNo(int destNo){
		return destDao.getCateNameByDestNo(destNo);
		
	}
	@Override
	public List<CategoryCodeVO> getCateByDestNo(int destNo) {
		return destDao.getCategoryByDestNo(destNo);
	}
}
