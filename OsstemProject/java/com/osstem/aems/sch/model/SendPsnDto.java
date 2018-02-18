package com.osstem.aems.sch.sch.model;

import java.util.List;

/*
 * @author : Ginyoung Lee 
 * @since 2016. 10. 25
 */

public class SendPsnDto {

	private int send_id;
	private int psn_id;
	private String mt_refkey;
	private String reg_id;
	private String reg_dt;
	private String mod_id;
	private String mod_dt;
	
	private List<SendPsnDto> sendPsnList;
	
	public List<SendPsnDto> getSendPsnList() {
		return sendPsnList;
	}
	public void setSendPsnList(List<SendPsnDto> sendPsnList) {
		this.sendPsnList = sendPsnList;
	}
	public int getSend_id() {
		return send_id;
	}
	public void setSend_id(int send_id) {
		this.send_id = send_id;
	}
	public int getPsn_id() {
		return psn_id;
	}
	public void setPsn_id(int psn_id) {
		this.psn_id = psn_id;
	}
	public String getMt_refkey() {
		return mt_refkey;
	}
	public void setMt_refkey(String mt_refkey) {
		this.mt_refkey = mt_refkey;
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
		return "SendPsnDto [send_id=" + send_id + ", psn_id=" + psn_id
				+ ", mt_refkey=" + mt_refkey + ", reg_id=" + reg_id
				+ ", reg_dt=" + reg_dt + ", mod_id=" + mod_id + ", mod_dt="
				+ mod_dt + ", sendPsnList=" + sendPsnList + "]";
	}
	
}
