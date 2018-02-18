package kr.co.jhta.view;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import kr.co.jhta.domain.DestVO;
import kr.co.jhta.domain.PlanDetailVO;
import kr.co.jhta.domain.PlanVO;

/**
 * PlanVO 데이터를 엑셀로 변환하는 View 
 * @author 성인
 */
public class PlanToExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		System.out.println(req.getLocalAddr());
		
		PlanVO plan = (PlanVO)model.get("plan");
		String title = plan.getTitle();
		Map<Integer, List<PlanDetailVO>> details = plan.getPlanDetailMap();
		int period = plan.getPeriod();
		
		HSSFSheet sh = wb.createSheet("요약");
		
		// 0행~1행, 1행~10행 병합
		sh.addMergedRegion(new CellRangeAddress(0, 1, 1, 7));

		final int beginColNum = 1;
		int rowNum = 0;
		int colNum = beginColNum;
		
		// 셀 생성부
		Map<String, HSSFCell> cellMap = new HashMap<>();
		cellMap.put("title", sh.createRow(rowNum++).createCell(colNum++));
		colNum = 8; 
		cellMap.put("tUrl", sh.createRow(rowNum).createCell(colNum++));
		//cellMap.put("url", sh.createRow(rowNum).createCell(colNum++));
		cellMap.put("tDate", sh.createRow(--rowNum).createCell(colNum));
		cellMap.put("date", sh.createRow(++rowNum).createCell(colNum));
		cellMap.put("tCities", sh.createRow(++rowNum).createCell(colNum));
		cellMap.put("cities", sh.createRow(++rowNum).createCell(colNum));
		/*rowNum-=2;
		rowNum+=2;*/
		colNum = beginColNum;
		
		cellMap.put("tDetailTitle", sh.createRow(rowNum).createCell(colNum));
		rowNum++;
		colNum = beginColNum;
		
		cellMap.put("tDetailDay_Dest", sh.createRow(rowNum).createCell(colNum++));
		cellMap.put("tDetailDay_DestName", sh.createRow(rowNum).createCell(colNum++));
		cellMap.put("tDetailDay_DestAddress", sh.createRow(rowNum).createCell(colNum++));
		cellMap.put("tDetailDay_DestOpenTime", sh.createRow(rowNum).createCell(colNum++));
		cellMap.put("tDetailDay_DestContact", sh.createRow(rowNum).createCell(colNum++));
		cellMap.put("tDetailDay_DestBudget", sh.createRow(rowNum).createCell(colNum++));
		cellMap.put("tDetailDay_DestMemo", sh.createRow(rowNum).createCell(colNum));
		
		rowNum++;
		colNum = beginColNum;
		
		for(int day = 1; day<=period; day++) {
			List<PlanDetailVO> dayDetails = details.get(day);
			cellMap.put("detailDay"+day, sh.createRow(rowNum).createCell(colNum++));
			cellMap.put("detailCity"+day, sh.createRow(rowNum).createCell(colNum++));
			cellMap.put("detailDate"+day, sh.createRow(rowNum).createCell(colNum));
			rowNum++;
			colNum = beginColNum;
			
			for(int i = 0; i< dayDetails.size(); i++) {
				PlanDetailVO dayDetail = dayDetails.get(i);
				
				cellMap.put("detailDay"+day+"_Dest"+i, sh.createRow(rowNum).createCell(colNum++));
				cellMap.put("detailDay"+day+"_Dest"+i+"Name", sh.createRow(rowNum).createCell(colNum++));
				cellMap.put("detailDay"+day+"_Dest"+i+"Address", sh.createRow(rowNum).createCell(colNum++));
				cellMap.put("detailDay"+day+"_Dest"+i+"OpenTime", sh.createRow(rowNum).createCell(colNum++));
				cellMap.put("detailDay"+day+"_Dest"+i+"Contact", sh.createRow(rowNum).createCell(colNum++));
				cellMap.put("detailDay"+day+"_Dest"+i+"Budget", sh.createRow(rowNum).createCell(colNum++));
				cellMap.put("detailDay"+day+"_Dest"+i+"Memo", sh.createRow(rowNum).createCell(colNum));
				
				rowNum++;
				colNum = beginColNum;
			}
		}
		
		
		// 셀 스타일 지정부
		//HSSFCellStyle titleCellStyle = wb.createCellStyle().;
		
		final SimpleDateFormat ymdSdf = new SimpleDateFormat("yyyy-MM-dd");
		final SimpleDateFormat mdSdf = new SimpleDateFormat("MM-dd (E)");
		
		// 셀 데이터 지정부
		cellMap.get("title").setCellValue(title);
		cellMap.get("tUrl").setCellValue("링크");
		HSSFHyperlink link =  new HSSFHyperlink(HSSFHyperlink.LINK_URL);
		
		
		
		link.setAddress("http://192.168.0.24/plan/detail.tm?no="+plan.getNo());
		cellMap.get("tUrl").setHyperlink(link);
		cellMap.get("tDate").setCellValue("총 여행기간");
		cellMap.get("date").setCellValue(String.format("%s~%s (%d일)", ymdSdf.format(plan.getLeaveDateToDate()), ymdSdf.format(plan.getArriveDateToDate()), period));
		cellMap.get("tCities").setCellValue("여행도시");
		//cellMap.get("cities").setCellValue(plan.getCitiesOfPlanDetail())
		cellMap.get("tDetailTitle").setCellValue("일정");
		
		cellMap.get("tDetailDay_Dest").setCellValue("순서");
		cellMap.get("tDetailDay_DestName").setCellValue("여행지명");
		cellMap.get("tDetailDay_DestAddress").setCellValue("주소");
		cellMap.get("tDetailDay_DestOpenTime").setCellValue("영업시간");
		cellMap.get("tDetailDay_DestContact").setCellValue("연락처");
		cellMap.get("tDetailDay_DestBudget").setCellValue("예산");
		cellMap.get("tDetailDay_DestMemo").setCellValue("메모");
		
		Set<String> allCityNameList = new TreeSet<>();
		for(int day = 1; day<=period; day++) {
			List<PlanDetailVO> dayDetails = details.get(day);
			cellMap.get("detailDay"+day).setCellValue("Day " + day);
			//cellMap.get("detailCity"+day).setCellValue(dayDetails.);
			cellMap.get("detailDate"+day).setCellValue(mdSdf.format(plan.getDateInPeriodToDate(day)));
			
			Set<String> cityNameList = new TreeSet<>(); 
			for(int i = 0; i< dayDetails.size(); i++) {
				PlanDetailVO dayDetail = dayDetails.get(i);
				
				DestVO dest = dayDetail.getDest();
				cellMap.get("detailDay"+day+"_Dest"+i).setCellValue(dayDetail.getNumbering());
				cellMap.get("detailDay"+day+"_Dest"+i+"Name").setCellValue(dest.getName());
				cellMap.get("detailDay"+day+"_Dest"+i+"Address").setCellValue(dest.getAddress());
				cellMap.get("detailDay"+day+"_Dest"+i+"OpenTime").setCellValue(dest.getOpenTime());
				cellMap.get("detailDay"+day+"_Dest"+i+"Contact").setCellValue(dest.getContact());
				cellMap.get("detailDay"+day+"_Dest"+i+"Budget").setCellValue(dayDetail.getBudget());
				cellMap.get("detailDay"+day+"_Dest"+i+"Memo").setCellValue(dayDetail.getMemo());
				cityNameList.add(dest.getCity().getName());
			}
			allCityNameList.addAll(cityNameList);
			cellMap.get("detailCity"+day).setCellValue(cityNameList.toString());
		}
		
		cellMap.get("cities").setCellValue(allCityNameList.toString());
		
		sh.setColumnWidth(0, 256*2);
		sh.setColumnWidth(1, 256*7);
		sh.setColumnWidth(2, 256*12);
		sh.setColumnWidth(3, 256*40);
		sh.setColumnWidth(4, 256*24);
		sh.setColumnWidth(5, 256*20);
		sh.setColumnWidth(6, 256*8);
		sh.setColumnWidth(7, 256*24);
		sh.setColumnWidth(8, 256*5);
		sh.setColumnWidth(9, 256*24);
		//for(int i=0; i<15; i++){
			//sh.autoSizeColumn((short)i);
			//sh.setColumnWidth(i, (sh.getColumnWidth(i))+512 );  // 윗줄만으로는 컬럼의 width 가 부족하여 더 늘려야 함.
		//}
		
		res.setHeader("Content-Disposition", "filename="+URLEncoder.encode(plan.getTitle()+"_"+plan.getUser().getName()+".xls", "utf-8"));
	}
}
