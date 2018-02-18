package com.osstem.aems.sch.sch.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SchSchDto implements Serializable{

	// 년월일
	private String schd_ymd;
	
	// 세부일정 그룹1
	private String sch_gb_01;
	
	// 세부일정 그룹2
	private String sch_gb_02;
	
	// 총 회차수
	private int sch_no_cnt;
	
	// 고유키 primary key
	private int id;
	
	// 템플릿여부
	private String temp_yn;
	
	// 현재년도
	private String cur_year;
	
	// 일정템플릿 관리 페이지/ 세부일정 등록 페이지 구분 
	private String templet_gb;
	
	// 제목
	private String title;
	
	// 주관
	private String org_nm;
	private String org_cd;
	
	// 담당
	private String name;
	private String sabun;
	
	// 장소
	private String place;
	
	// 일시 (일정 시작, 일정 종료)
	private String sch_sta_dt;
	private String sch_fin_dt;
	
	// 작성자
	private String reg_id;
	
	// 작성일
	private String reg_dt;
	
	// 수정자
	private String mod_id;
	
	// 수정일
	private String mod_dt;

	// 리턴받는 pk키
	private int seq;

	
	
	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getSchd_ymd() {
		return schd_ymd;
	}

	public void setSchd_ymd(String schd_ymd) {
		this.schd_ymd = schd_ymd;
	}

	public String getSch_gb_01() {
		return sch_gb_01;
	}

	public void setSch_gb_01(String sch_gb_01) {
		this.sch_gb_01 = sch_gb_01;
	}

	public String getSch_gb_02() {
		return sch_gb_02;
	}

	public void setSch_gb_02(String sch_gb_02) {
		this.sch_gb_02 = sch_gb_02;
	}

	public int getSch_no_cnt() {
		return sch_no_cnt;
	}

	public void setSch_no_cnt(int sch_no_cnt) {
		this.sch_no_cnt = sch_no_cnt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTemp_yn() {
		return temp_yn;
	}

	public void setTemp_yn(String temp_yn) {
		this.temp_yn = temp_yn;
	}

	public String getCur_year() {
		return cur_year;
	}

	public void setCur_year(String cur_year) {
		this.cur_year = cur_year;
	}

	public String getTemplet_gb() {
		return templet_gb;
	}

	public void setTemplet_gb(String templet_gb) {
		this.templet_gb = templet_gb;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrg_nm() {
		return org_nm;
	}

	public void setOrg_nm(String org_nm) {
		this.org_nm = org_nm;
	}
	
	public String getOrg_cd() {
		return org_cd;
	}

	public void setOrg_cd(String org_cd) {
		this.org_cd = org_cd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSabun() {
		return sabun;
	}

	public void setSabun(String sabun) {
		this.sabun = sabun;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
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

	@Override
	public String toString() {
		return "SchSchDto [schd_ymd=" + schd_ymd + ", sch_gb_01=" + sch_gb_01
				+ ", sch_gb_02=" + sch_gb_02 + ", sch_no_cnt=" + sch_no_cnt
				+ ", id=" + id + ", temp_yn=" + temp_yn + ", cur_year="
				+ cur_year + ", templet_gb=" + templet_gb + ", title=" + title
				+ ", org_nm=" + org_nm + ", org_cd=" + org_cd + ", name="
				+ name + ", sabun=" + sabun + ", place=" + place
				+ ", sch_sta_dt=" + sch_sta_dt + ", sch_fin_dt=" + sch_fin_dt
				+ ", reg_id=" + reg_id + ", reg_dt=" + reg_dt + ", mod_id="
				+ mod_id + ", mod_dt=" + mod_dt + "]";
	}

}
