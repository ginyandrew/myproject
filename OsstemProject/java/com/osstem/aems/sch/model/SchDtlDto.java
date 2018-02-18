package com.osstem.aems.sch.sch.model;

import java.util.List;

/*
 * @author : Ginyoung Lee 
 * @since 2016. 10. 19
 */

public class SchDtlDto {

	
	private int id;			
	private int no;			// 각 회차
	private String sch_sta_dt;	// 시작시작
	private String sch_fin_dt;	// 종료시간
	private String subject;	// 제목
	private String content;	// 내용
	private String reg_id;
	private String reg_dt;
	private String mod_id;
	private String mod_dt;
	private String sub_gb;	// 연제 주제
	
	private List<SchDtlDto> schDtlList;

	
	public String getSub_gb() {
		return sub_gb;
	}

	public void setSub_gb(String sub_gb) {
		this.sub_gb = sub_gb;
	}

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

	public String getSch_sta_dt() {
		return sch_sta_dt;
	}

	public void setSch_sta_dt(String sch_sta_dt) {
		this.sch_sta_dt = sch_sta_dt;
	}

	public String getSch_fin_dt() {
		return sch_fin_dt;
	}

	public void setSch_fin_dt(String sch_fin_dt) {
		this.sch_fin_dt = sch_fin_dt;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<SchDtlDto> getSchDtlList() {
		return schDtlList;
	}

	public void setSchDtlList(List<SchDtlDto> schDtlList) {
		this.schDtlList = schDtlList;
	}

	@Override
	public String toString() {
		return "SchDtlDto [id=" + id + ", no=" + no + ", sch_sta_dt="
				+ sch_sta_dt + ", sch_fin_dt=" + sch_fin_dt + ", subject="
				+ subject + ", content=" + content + ", reg_id=" + reg_id
				+ ", reg_dt=" + reg_dt + ", mod_id=" + mod_id + ", mod_dt="
				+ mod_dt + ", sub_gb=" + sub_gb + ", schDtlList=" + schDtlList
				+ "]";
	}

}
