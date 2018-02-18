package kr.co.jhta.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.jhta.dao.UserDAO;
import kr.co.jhta.domain.MessageVO;
import kr.co.jhta.domain.UserVO;
import kr.co.jhta.exception.InvalidPasswordException;
import kr.co.jhta.exception.UserNotFoundException;

@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	private UserDAO userDao;
	
	@Override
	public UserVO findUser(String email, String password) {
		UserVO user = userDao.getUserByEmail(email);
		
		if(user == null) {
			throw new UserNotFoundException();
		}
		
		String encryptedPwd = DigestUtils.sha256Hex(password);
		
		//System.out.println(encryptedPwd + " : " + user.getPassword());
		
		if(!encryptedPwd.equals(user.getPassword())){
			throw new InvalidPasswordException();
		}
		
		return user;
	}
	
	@Override
	public void registerNewUser(UserVO user) {
		// pwd 암호화하기
		user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
		
		// 사용자 추가하기
		userDao.addUser(user);
	}
	
	@Override
	public List<MessageVO> getRecentMessage(int userNo, int count) {
		Map<String, Integer> countMap = new HashMap<>();
		countMap.put("USER_NO", userNo);
		countMap.put("START", 1);
		countMap.put("END", count);
		
		List<MessageVO> messageList = userDao.getRankedMessage(countMap);		
		
		// 사용자 설정하기?
		
		return messageList;
	}
}
