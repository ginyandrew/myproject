package com.osstem.aems.sch.sch.model;

import java.util.List;

/*
 * @author : Ginyoung Lee 
 * @since 2016. 10. 19
 */

public class SchMbrDto {

	private int id;			
	private int no;			// 회차 (TB_SCH:0, TB_SCH_DTL: n -회차를 의미-)
	private int psn_id;		// 원장키
	private String mbr_gb;	// 구성원 구분 (01 02 03 04 05)
	private String reg_id;	 
	private String reg_dt;	
	private String mod_id;
	private String mod_dt;
	
	private List<SchMbrDto> schMbrList;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getPsn_id() {
		return psn_id;
	}
	public void setPsn_id(int psn_id) {
		this.psn_id = psn_id;
	}
	public String getMbr_gb() {
		return mbr_gb;
	}
	public void setMbr_gb(String mbr_gb) {
		this.mbr_gb = mbr_gb;
	}
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getMod_id() {
		return mod_id;
	}
	public void setMod_id(String mod_id) {
		this.mod_id = mod_id;
	}
	public String getMod_dt() {
		return mod_dt;
	}
	public void setMod_dt(String mod_dt) {
		this.mod_dt = mod_dt;
	}
	public List<SchMbrDto> getSchMbrList() {
		return schMbrList;
	}
	public void setSchMbrList(List<SchMbrDto> schMbrList) {
		this.schMbrList = schMbrList;
	}
	@Override
	public String toString() {
		return "SchMbrDto [id=" + id + ", no=" + no + ", psn_id=" + psn_id
				+ ", mbr_gb=" + mbr_gb + ", reg_id=" + reg_id + ", reg_dt="
				+ reg_dt + ", mod_id=" + mod_id + ", mod_dt=" + mod_dt
				+ ", schMbrList=" + schMbrList + "]";
	}

}
