package kr.co.jhta.domain;

import java.util.List;

public class HistoryVO<D extends TravelData> implements Comparable<HistoryVO>{
	private long no;
	private String type;
	private D data;
	
	public HistoryVO() {
	}
	
	public HistoryVO(String type, D data) {
		super();
		no = System.currentTimeMillis();
		this.type = type;
		this.data = data;
	}

	public long getNo() {
		return no;
	}

	public void setNo(long no) {
		this.no = no;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public D getData() {
		return data;
	}

	public void setData(D data) {
		this.data = data;
	}
	
	public String getUrl() {
		switch(type){
		case "plan":
			return "/plan/detail.tm?no="+data.getNo();
		case "city":
			return "/info/city.tm?cityNo="+data.getNo();
		case "dest":
			return "/info/dest.tm?destNo="+data.getNo();
		default:
			return "/main.tm";
		} 
	}
	
	@Override
	public int compareTo(HistoryVO o) {
		if(type.equals(o.getType()) && data.getNo() == o.getData().getNo()){
			return 0;				
		}
		return new Long(no).compareTo(o.getNo());		
	}
	
	public String getName() {
		switch(type){
		case "plan":
			return ((PlanVO)data).getTitle();
		case "city":
			return ((CityVO)data).getName();
		case "dest":
			return ((DestVO)data).getName();
		default:
			return "-";
		} 
	}
	
	public String getMainImgName() {
		String result = "/resources/img/";
		switch(type){
		case "plan":
			List<PlanDetailVO> details = ((PlanVO)data).getDetails();
			result += details==null||details.size()==0?
					"noimage.jpg":"city/"+details.get(0).getDest().getCity().getMainImgName();
			break;
		case "city":
			result += "city/"+((CityVO)data).getMainImgName();
			break;
		case "dest":
			result += "dest/"+((DestVO)data).getMainImgName();
			break;
		default:
			result += "noimage.jpg";
		}
		return result;
	}
}
