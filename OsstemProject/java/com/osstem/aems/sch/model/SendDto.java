package com.osstem.aems.sch.sch.model;

/*
 * @author : Ginyoung Lee 
 * @since 2016. 10. 25
 */

public class SendDto {

	private int seq;
	private int send_id;
	private int sch_id;
	private int file_id;
	private String send_dt;
	private String reg_id;
	private String reg_dt;
	private String mod_id;
	private String mod_dt;
	
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getSend_id() {
		return send_id;
	}
	public void setSend_id(int send_id) {
		this.send_id = send_id;
	}
	public int getSch_id() {
		return sch_id;
	}
	public void setSch_id(int sch_id) {
		this.sch_id = sch_id;
	}
	public String getSend_dt() {
		return send_dt;
	}
	public void setSend_dt(String send_dt) {
		this.send_dt = send_dt;
	}
	public int getFile_id() {
		return file_id;
	}
	public void setFile_id(int file_id) {
		this.file_id = file_id;
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
		return "SendDto [seq=" + seq + ", send_id=" + send_id + ", sch_id="
				+ sch_id + ", file_id=" + file_id + ", send_dt=" + send_dt
				+ ", reg_id=" + reg_id + ", reg_dt=" + reg_dt + ", mod_id="
				+ mod_id + ", mod_dt=" + mod_dt + "]";
	}
	
}
