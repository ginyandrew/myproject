package kr.co.jhta.domain;

//여행지 사진VO
public class DestImgVO {
	
	private String fileName; // 여행지 사진 파일이름
	private DestVO dest;	 // 사진이 해당되는 여행지의 번호 (FK, NN)
	
	public DestImgVO() {}

	public DestImgVO(String fileName, DestVO dest) {
		super();
		this.fileName = fileName;
		this.dest = dest;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public DestVO getDest() {
		return dest;
	}

	public void setDest(DestVO dest) {
		this.dest = dest;
	}

	@Override
	public String toString() {
		return "DestImgVO [fileName=" + fileName + ", dest=" + dest + "]";
	}

	
	
}
