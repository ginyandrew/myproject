package kr.co.jhta.service;

import java.util.List;

import kr.co.jhta.domain.CategoryCodeVO;
import kr.co.jhta.domain.CityVO;
import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.PlanVO;
import kr.co.jhta.domain.StateCodeVO;
import kr.co.jhta.domain.UserVO;

public interface HostService {

	List<UserVO> getUser();
	List<CityVO> getCity();
	List<StateCodeVO> getStateCode();
	List<DestVO> getDest(); 
	List<PlanVO> getPlan();
	
	void sendMsg(String[] noArr, String msg);
	
	void dropUser(String[] noArr);
	void updateUser(UserVO user);
	//void insertNewCity(CityVO city);
	void updateCity(CityVO city);
	void addNewCityImage(int cityNo, String filename);
	int addDest(DestVO dest);
	void updateDest(DestVO dest);
	void addNewDestImage(int destNo, String filename);
	void removeDest(String[] noArr);
	void removePlan(String[] noArr);
	
	List<CategoryCodeVO> getCategoryList();
}
