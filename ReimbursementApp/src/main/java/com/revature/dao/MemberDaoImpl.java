package com.revature.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static java.lang.Math.toIntExact;
import com.revature.exception.NotExistUserException;
import com.revature.exception.WrongPassworException;
import com.revature.util.ConnectionUtil;
import com.revature.util.LoggingClass;
import com.revature.vo.MemberVo;
import com.revature.vo.RequestVo;

public class MemberDaoImpl implements MemberDao {

	/*
	 * public static final int MEMBER_RIGHT_PW = 1; public static final int
	 * MEMBER_MANAGER_LV = 0; public static final int MEMBER_EMPLOYEE_LV = 1;
	 */

	public static String filename = "connection.properties";
	public static LoggingClass lc = new LoggingClass();

	@Override
	public MemberVo getMemberById(String id) {

		MemberVo mVo = new MemberVo();
		String sql = "SELECT * FROM MEMBER WHERE ID = ?";
		PreparedStatement pstmt = null;

		try (Connection con = ConnectionUtil.getConnectionFromFile(filename)) {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				mVo.setNo(rs.getInt("NO"));
				mVo.setId(rs.getString("ID"));
				mVo.setPwd(rs.getString("PWD"));
				mVo.setEmail(rs.getString("EMAIL"));
				mVo.setLv(rs.getString("LV"));
			}
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mVo;
	}

	@Override
	public int insertMember(MemberVo memberVo) {

		int result = 0;

		String sql = "INSERT INTO MEMBER (NO, ID, PWD, EMAIL, LV)  "
				+ "VALUES (MEMBER_SEQ.NEXTVAL, ?, ?, ?, ?)";
		PreparedStatement pstmt = null;

		try (Connection con = ConnectionUtil.getConnectionFromFile(filename)) {
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, memberVo.getId());
			pstmt.setString(2, memberVo.getPwd());
			pstmt.setString(3, memberVo.getEmail());
			pstmt.setString(4, memberVo.getLv());

			pstmt.executeUpdate();
			result = 1;
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return result;
	}

	@Override
	public int insertRequest(RequestVo rVo) {
		String sql = "INSERT INTO REQUEST (NO, AMOUNT, PURPOSE, EMPLOYEE_NO) VALUES (REQUEST_SEQ.NEXTVAL, ?, ?, ?)";
		PreparedStatement pstmt = null;

		int request_id = 0;
		try (Connection con = ConnectionUtil.getConnectionFromFile(filename)) {
			String key[] = {"NO"};
			pstmt = con.prepareStatement(sql, key);
			
			pstmt.setInt(1, rVo.getAmount());
			pstmt.setString(2, rVo.getPurpose());
			pstmt.setInt(3, rVo.getEmployee_no());

			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				request_id = toIntExact(rs.getLong(1));
			}
			
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 		
		return request_id; 
	}

	@Override
	public int ifMemberExist(String id) {
		String sql = "SELECT COUNT(*) AS CNT FROM MEMBER WHERE ID = ?";
		PreparedStatement pstmt = null;

		int cnt = 0;
		try (Connection con = ConnectionUtil.getConnectionFromFile(filename)) {

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				cnt = rs.getInt("CNT"); // if cnt is 0, not-exist
			}
			/*
			 * if (cnt < 1) { throw new NotExistUserException(); }
			 */
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} /*
			 * catch (NotExistUserException e) {
			 * 
			 * }
			 */
		return cnt;
	}

	@Override
	public int ifRightPwd(String id, String pwd) {
		String sql = "SELECT CASE WHEN PWD = ? THEN 1 ELSE 0 END AS IFRIGHTPW" + " FROM MEMBER WHERE ID = ?";
		PreparedStatement pstmt = null;

		int ifRightPW = 0;
		try (Connection con = ConnectionUtil.getConnectionFromFile(filename)) {

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, pwd);
			pstmt.setString(2, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ifRightPW = rs.getInt("IFRIGHTPW"); // if cnt is 0, not-exist
			}
			if (ifRightPW < 1) {
				throw new WrongPassworException();
			}
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongPassworException e) {

		}
		return ifRightPW;
	}

	@Override
	public int ifRequestExistByEmployeeNo(int empNo) {
		String sql = "SELECT COUNT(*) AS CNT "
				+ "FROM MEMBER M  JOIN REQUEST R  ON M.NO = R.EMPLOYEE_NO  WHERE R.EMPLOYEE_NO = ? ";
		PreparedStatement pstmt = null;
		int cnt = 0;
		try (Connection con = ConnectionUtil.getConnectionFromFile(filename)) {

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, empNo);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				cnt = rs.getInt("CNT");
			}
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cnt;
	}

	@Override
	public List<RequestVo> getRequestsByEmployeeNo(int empNo) {
		String sql = "SELECT R.NO AS R_NO, R.STATUS AS STATUS, TO_CHAR(R.DAY, 'YYYY-MM-DD') AS DAY, R.AMOUNT AS AMOUNT, R.PURPOSE AS PURPOSE "
				+ "FROM MEMBER M  JOIN REQUEST R  ON M.NO = R.EMPLOYEE_NO  WHERE R.EMPLOYEE_NO = ? ";
		PreparedStatement pstmt = null;

		List<RequestVo> vos = new ArrayList<RequestVo>();
		try (Connection con = ConnectionUtil.getConnectionFromFile(filename)) {

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, empNo);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				RequestVo vo = new RequestVo(rs.getInt("R_NO"), rs.getInt("STATUS"), rs.getString("DAY"),
						rs.getInt("AMOUNT"), rs.getString("PURPOSE"), empNo);
				vos.add(vo);
			}
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vos;
	}

	 
}
