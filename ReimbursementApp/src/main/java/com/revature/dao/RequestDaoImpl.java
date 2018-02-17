package com.revature.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.util.ConnectionUtil;
import com.revature.vo.ImgVo;
import com.revature.vo.MemberVo;
import com.revature.vo.RequestVo;

public class RequestDaoImpl implements RequestDao {

	public static String filename = "connection.properties";

	@Override
	public void insertImg(String filename, int request_no) {
		String sql = "INSERT INTO IMG (NO, REQUEST_NO, FILENAME) VALUES (IMG_SEQ.NEXTVAL, ?, ?) ";
		PreparedStatement pstmt = null;
		try (Connection con = ConnectionUtil.getConnectionFromFile(filename)) {

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, request_no);
			pstmt.setString(2, filename);

			pstmt.executeUpdate();
			pstmt.close();
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public RequestVo getRequestByRNo(int request_no) {
		RequestVo rVo = new RequestVo();
		//String sql = "SELECT NO, STATUS, TO_CHAR(DAY, 'YYYY-MM-DD') AS DAY, AMOUNT, PURPOSE, "
		//		+ " EMPLOYEE_NO, NVL(MANAGER_NO, 0) AS MANAGER_NO FROM REQUEST WHERE NO = ? ";
		String sql = "SELECT R.NO AS NO, R.STATUS AS STATUS, TO_CHAR(R.DAY, 'YYYY-MM-DD') AS DAY, " + 
				"R.PURPOSE AS PURPOSE, R.AMOUNT AS AMOUNT, " + 
				"R.EMPLOYEE_NO AS EMPLOYEE_NO,  " + 
				"NVL(R.MANAGER_NO,0) AS MANAGER_NO, " + 
				"(SELECT ME.ID FROM MEMBER ME WHERE R.EMPLOYEE_NO = ME.NO) AS EMPLOYEE_ID, " + 
				"NVL((SELECT MG.ID FROM MEMBER MG WHERE R.MANAGER_NO = MG.NO),'0') AS MANAGER_ID " + 
				"FROM REQUEST R " + 
				"WHERE R.NO = ? ";
		PreparedStatement pstmt = null;

		try (Connection con = ConnectionUtil.getConnectionFromFile(filename)) {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, request_no);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				rVo.setNo(rs.getInt("NO"));
				rVo.setDay(rs.getString("DAY"));
				rVo.setStatus(rs.getInt("STATUS"));
				rVo.setAmount(rs.getInt("AMOUNT"));
				rVo.setPurpose(rs.getString("PURPOSE"));
				rVo.setEmployee_no(rs.getInt("EMPLOYEE_NO"));
				rVo.setManager_no(rs.getInt("MANAGER_NO"));
				rVo.setEmployee_id(rs.getString("EMPLOYEE_ID "));		// why not working?
				rVo.setManager_id(rs.getString("MANAGER_ID"));
			}
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rVo;
	}

	@Override
	public List<RequestVo> getRequests(int pageNo) {

		List<RequestVo> list = new ArrayList<RequestVo>();
		/*
		 * String sql =
		 * "SELECT NO, STATUS, TO_CHAR(DAY, 'YYYY-MM-DD') AS DAY, AMOUNT, PURPOSE, " +
		 * " EMPLOYEE_NO, NVL(MANAGER_NO, 0) AS MANAGER_NO FROM REQUEST ORDER BY NO DESC "
		 * ;
		 */
		String sql = "SELECT  C.THEROW AS THEROW, C.EMPLOYEE_ID AS EMPLOYEE_ID, C.NO AS NO,  " + 
				"        						C.STATUS AS STATUS, C.DAY AS DAY, C.AMOUNT AS AMOUNT, C.PURPOSE AS PURPOSE, " + 
				"       						C.EMPLOYEE_NO AS EMPLOYEE_NO,  C.MANAGER_NO AS MANAGER_NO"
				+ "    		FROM 	(SELECT ROWNUM AS THEROW, A.EMPLOYEE_ID AS EMPLOYEE_ID,"
				+ "        									A.NO AS NO, A.STATUS AS STATUS,"
				+ "        									A.DAY AS DAY, A.PURPOSE AS PURPOSE, A.AMOUNT AS AMOUNT,"
				+ "        									A.EMPLOYEE_NO AS EMPLOYEE_NO, A.MANAGER_NO AS MANAGER_NO "
				+ "        						FROM (SELECT M.ID AS EMPLOYEE_ID, R.NO AS NO, R.STATUS AS STATUS, "
				+ "                								TO_CHAR(R.DAY, 'YYYY-MM-DD') AS DAY, R.AMOUNT AS AMOUNT,"
				+ "                								R.PURPOSE AS PURPOSE, R.EMPLOYEE_NO AS EMPLOYEE_NO, NVL(R.MANAGER_NO, 0) AS MANAGER_NO"
				+ "                								FROM REQUEST R, MEMBER M  WHERE M.NO = R.EMPLOYEE_NO"
				+ "                								ORDER BY R.NO DESC) A) C  "
				+ " 			WHERE C.THEROW BETWEEN (?*10-9) AND (?*10)";
		PreparedStatement pstmt = null;

		try (Connection con = ConnectionUtil.getConnectionFromFile(filename)) {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, pageNo);
			pstmt.setInt(2, pageNo);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				RequestVo rVo = new RequestVo();
				rVo.setRowNum(rs.getInt("THEROW"));
				rVo.setEmployee_id(rs.getString("EMPLOYEE_ID"));
				rVo.setNo(rs.getInt("NO"));
				rVo.setDay(rs.getString("DAY"));
				rVo.setStatus(rs.getInt("STATUS"));
				rVo.setAmount(rs.getInt("AMOUNT"));
				rVo.setPurpose(rs.getString("PURPOSE"));
				rVo.setEmployee_no(rs.getInt("EMPLOYEE_NO"));
				rVo.setManager_no(rs.getInt("MANAGER_NO"));

				list.add(rVo);
			}
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<ImgVo> getImgByRNo(int request_no) {
		List<ImgVo> iVos = new ArrayList<ImgVo>();
		String sql = "SELECT NO, REQUEST_NO, FILENAME FROM IMG WHERE REQUEST_NO = ? ";
		PreparedStatement pstmt = null;

		try (Connection con = ConnectionUtil.getConnectionFromFile(filename)) {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, request_no);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				ImgVo rVo = new ImgVo(rs.getInt("NO"), rs.getInt("REQUEST_NO"), "img/" + rs.getString("FILENAME"));
				iVos.add(rVo);
			}
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return iVos;
	}
}
