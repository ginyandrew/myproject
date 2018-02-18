package com.osstem.aems.sch.sch.model;

import java.util.List;

public class ApprSpkDto {

	private int appr_id;
	private int sch_id;
	private int sch_no;
	private int psn_id;
	private String recm_yn;
	private String reg_id;
	private String reg_dt;
	private String mod_id;
	private String mod_dt;
	
	private List<ApprSpkDto> apprSpkList;
	
	
	public List<ApprSpkDto> getApprSpkList() {
		return apprSpkList;
	}
	public void setApprSpkList(List<ApprSpkDto> apprSpkList) {
		this.apprSpkList = apprSpkList;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public int getAppr_id() {
		return appr_id;
	}
	public void setAppr_id(int appr_id) {
		this.appr_id = appr_id;
	}
	public int getSch_id() {
		return sch_id;
	}
	public void setSch_id(int sch_id) {
		this.sch_id = sch_id;
	}
	public int getSch_no() {
		return sch_no;
	}
	public void setSch_no(int sch_no) {
		this.sch_no = sch_no;
	}
	public int getPsn_id() {
		return psn_id;
	}
	public void setPsn_id(int psn_id) {
		this.psn_id = psn_id;
	}
	public String getRecm_yn() {
		return recm_yn;
	}
	public void setRecm_yn(String recm_yn) {
		this.recm_yn = recm_yn;
	}
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
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
		return "ApprSpkDto [appr_id=" + appr_id + ", sch_id=" + sch_id
				+ ", sch_no=" + sch_no + ", psn_id=" + psn_id + ", recm_yn="
				+ recm_yn + ", reg_id=" + reg_id + ", reg_dt=" + reg_dt
				+ ", mod_id=" + mod_id + ", mod_dt=" + mod_dt
				+ ", apprSpkList=" + apprSpkList + "]";
	}
	
}
