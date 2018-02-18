package com.osstem.aems.sch.sch.model;

import org.springframework.web.multipart.MultipartFile;

public class FileDto {

	private MultipartFile uploadFile;
	
	private int file_id;				// 파일키 (seq)	
	private String file_no;				// 파일구분
	private String file_path;			// 파일경로
	private String file_nm;				// 파일명
	private String file_physical_nm;	// 실제파일명
	private int file_size;				// 파일크기
	private String file_type;			// 파일타입
	private String reg_id;				// 작성자
	private String reg_dt;				// 작성일 (오늘날짜)
	private String mod_id;
	private String mod_dt;
	
	
	public MultipartFile getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}
	public int getFile_id() {
		return file_id;
	}
	public void setFile_id(int file_id) {
		this.file_id = file_id;
	}
	public String getFile_no() {
		return file_no;
	}
	public void setFile_no(String file_no) {
		this.file_no = file_no;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public String getFile_nm() {
		return file_nm;
	}
	public void setFile_nm(String file_nm) {
		this.file_nm = file_nm;
	}
	public String getFile_physical_nm() {
		return file_physical_nm;
	}
	public void setFile_physical_nm(String file_physical_nm) {
		this.file_physical_nm = file_physical_nm;
	}
	public int getFile_size() {
		return file_size;
	}
	public void setFile_size(int file_size) {
		this.file_size = file_size;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
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
		return "FileDto [file_id=" + file_id + ", file_no=" + file_no
				+ ", file_path=" + file_path + ", file_nm=" + file_nm
				+ ", file_physical_nm=" + file_physical_nm + ", file_size="
				+ file_size + ", file_type=" + file_type + ", reg_id=" + reg_id
				+ ", reg_dt=" + reg_dt + ", mod_id=" + mod_id + ", mod_dt="
				+ mod_dt + "]";
	}
	
}
