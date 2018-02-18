package kr.co.jhta.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import kr.co.jhta.dao.DestDAO;
import kr.co.jhta.dao.UserDAO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.UserVO;

@Service
public class MypageServiceImpl implements MypageService {
	
	@Autowired
	UserDAO userDao;
	@Autowired
	DestDAO destDao;
	
	//유저정보 업데이트
	@Override 
	public void updateUser(UserVO user) {
		System.out.println("serviceImpl-name="+ user.getName());
		System.out.println("serviceImpl-email="+ user.getEmail());
		System.out.println("serviceImpl-password="+ user.getPassword());
		System.out.println("serviceImpl-gender="+ user.getGender());
		System.out.println("serviceImpl-point="+ user.getPoint());
		userDao.updateUser(user);
	}

	//유저탈퇴
	@Override
	public void dropUserByNo(int no) {
		userDao.dropUserByNo(no);
	}

	//목표 : 사용자 번호를 주면 >> 그 사용자가 가진 일정번호, 각 일정속에 있는 도시사진들과 장소이름들을 주는 서비스. 
	@Override
	public List<HashMap<String, Object>> getUserPlanList(int userNo) {
		
		
		//도시사진파일이름과 장소제목을 담은 해쉬맵들을 묶을 상자로 userPlanList를 만든다.
		List<HashMap<String, Object>> userPlanLists = new ArrayList<>();
		
		List<Integer> userplanNo = userDao.getUserPlanNo(userNo);
		
		for (int thePlanNo : userplanNo){

			//단 하나의 도시사진파일이름과 여러장소를하나로 붙인 장소제목을 담을 해쉬맵.
			HashMap<String, Object> theUserPlanDetail = new HashMap<>();
			
			//중복을 없앨 set 그릇을 만든다. 이름은 placeNameSet.
			HashSet<String> placeNameSet = new HashSet<>();

			//여러개의 장소리스트에서 장소 하나하나를 뽑는다.
			List<HashMap> placeDetailNames = userDao.getPlaceDetailName(thePlanNo);
			
			//장소들 중 하나의 장소의 도시이미지 파일이름을 뽑는다.
			HashMap cityFileName = placeDetailNames.get(1);
			String cityimgName = (String)cityFileName.get("CITYFILENAME");
			
			
			for (HashMap placeDetailName : placeDetailNames){
				placeNameSet.add((String)placeDetailName.get("PLACENAME"));
			}
			
			//이제 placeNameSet에는 일정 하나에 속한 여행지들의 이름이 들어있다.
			String userPlanList = StringUtils.collectionToCommaDelimitedString(placeNameSet);
			
			
			//가장 처음 만들어둔 해쉬맵 안에, 도시사진파일이름과 장소제목을 담는다.
			theUserPlanDetail.put("cityimgName", cityimgName);
			theUserPlanDetail.put("userPlanList", userPlanList);
			theUserPlanDetail.put("planNo", thePlanNo);
			
			//도시사진파일이름과 장소제목을 담은 해쉬맵을, 도시사진파일이름과 장소제목을 담은 해쉬맵들을 묶을 상자인 userPlanList에 담는다.
			userPlanLists.add(theUserPlanDetail);
		}
		
		return userPlanLists;
	}
	
	@Override
	public List<DestVO> getClipboardDataByUserNo(int userNo) {
		List<DestVO> clipboardDataList = userDao.getMyClipboard(userNo);
		
		// 이미지 정보도 추가하기
		for(DestVO dest: clipboardDataList) {
			int destNo = dest.getNo();
			
			List<String> imgNameList = destDao.getDestImgFileNameByNo(destNo);
			
			dest.setImgNameList(imgNameList);
		}
		
		return clipboardDataList;
	}
}
