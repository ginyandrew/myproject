package com.osstem.aems.sch.sch.model;

public class SchRegDto {

	private int sch_id;
	private int psn_id;
	private String hope_spk_yn;
	private String pres_yn;
	private String sal_emp_no;	// 담당영업사원
	private String self_yn;		// 본인 응답여부 (y:수강안내 n:강의신청)
	private String rest_yn;		// 발송제한
	private String reg_id;
	private String reg_dt;
	private String mod_id;
	private String mod_dt;
	
	
	public String getPres_yn() {
		return pres_yn;
	}
	public void setPres_yn(String pres_yn) {
		this.pres_yn = pres_yn;
	}
	public int getSch_id() {
		return sch_id;
	}
	public void setSch_id(int sch_id) {
		this.sch_id = sch_id;
	}
	public int getPsn_id() {
		return psn_id;
	}
	public void setPsn_id(int psn_id) {
		this.psn_id = psn_id;
	}
	public String getHope_spk_yn() {
		return hope_spk_yn;
	}
	public void setHope_spk_yn(String hope_spk_yn) {
		this.hope_spk_yn = hope_spk_yn;
	}
	public String getSal_emp_no() {
		return sal_emp_no;
	}
	public void setSal_emp_no(String sal_emp_no) {
		this.sal_emp_no = sal_emp_no;
	}
	public String getSelf_yn() {
		return self_yn;
	}
	public void setSelf_yn(String self_yn) {
		this.self_yn = self_yn;
	}
	public String getRest_yn() {
		return rest_yn;
	}
	public void setRest_yn(String rest_yn) {
		this.rest_yn = rest_yn;
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
		return "SchRegDto [sch_id=" + sch_id + ", psn_id=" + psn_id
				+ ", hope_spk_yn=" + hope_spk_yn + ", sal_emp_no=" + sal_emp_no
				+ ", self_yn=" + self_yn + ", rest_yn=" + rest_yn + ", reg_id="
				+ reg_id + ", reg_dt=" + reg_dt + ", mod_id=" + mod_id
				+ ", mod_dt=" + mod_dt + "]";
	}
	
}
