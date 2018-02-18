package com.osstem.aems.sch.sch.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.osstem.aems.sch.sch.model.SchSchDto;
import com.osstem.aems.sch.sch.service.DateUtil;

@Component("SchSendExcelView")
public class SchSendExcelView extends AbstractExcelView {
	private HSSFCellStyle hSSFCellStyle;
	private HSSFCellStyle CellStyle;
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model,HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
			String filename = "강의수강안내_리스트_" + DateUtil.getCurTime() ;

			// sheet 생성.
			HSSFSheet worksheet = null;
			HSSFRow row = null;
			hSSFCellStyle = null;
		    hSSFCellStyle = workbook.createCellStyle();
		    CellStyle = workbook.createCellStyle();
		    CellStyle.setWrapText(true);
		    
		    hSSFCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		    hSSFCellStyle.setFillPattern(org.apache.poi.ss.usermodel.CellStyle.SOLID_FOREGROUND);
		    hSSFCellStyle.setBorderBottom((short) 1);
		    hSSFCellStyle.setBorderRight(HSSFCellStyle.BORDER_DASHED);
			hSSFCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			worksheet = workbook.createSheet();		
			
			// sheet에 상단 컬럼 생성.
			worksheet.setColumnWidth(0, 10*245);	// No
		    worksheet.setColumnWidth(1, 20*245);	// 상위분류
		    worksheet.setColumnWidth(2, 20*245);	// 하위분류
		    worksheet.setColumnWidth(3, 60*245);	// 제목
		    worksheet.setColumnWidth(4, 20*245);	// 강의일자
		    worksheet.setColumnWidth(5, 30*245);	// 수신자정보
		    worksheet.setColumnWidth(6, 25*245);	// 발송일시
		    
		    row = worksheet.createRow(0);
		    
		    row.createCell(0).setCellValue("No");
		    row.createCell(1).setCellValue("상위분류");
		    row.createCell(2).setCellValue("하위분류");
		    row.createCell(3).setCellValue("제목");
		    row.createCell(4).setCellValue("강의일자");
		    row.createCell(5).setCellValue("수신자정보");
		    row.createCell(6).setCellValue("발송일시");
		    
		    for (int i = 0; i < 7; i++) {
		    	row.getCell(i).setCellStyle(hSSFCellStyle);
		    }
		    
		    @SuppressWarnings("unchecked")
		    List<HashMap<String, Object>> list = (List<HashMap<String, Object>> ) model.get("schSendList");
		    
		    // 가져온 데이터를 각 컬럼에 집어넣어서 열을 늘린다.
			for (int i = 0 ; i < list.size() ; i++){
				
				HashMap<String, Object> sch = list.get(i);
				
				row = worksheet.createRow(i+1);								// sheet에 새로운 줄 생성.
				
				row.createCell(0).setCellValue(i);						// No
		    	
		    	if (sch.get("SCH_GB_01").equals("01")){
		    		
		    		row.createCell(1).setCellValue("강연회");				// 강연회이면 
		    		row.createCell(4).setCellValue(String.valueOf(sch.get("STADT")));// 강의일자
		    		
		    		if(sch.get("SCH_GB_01").equals("01")){
		    			row.createCell(2).setCellValue("연수회");  
		    		} else if(sch.get("SCH_GB_02").equals("02")){
		    			row.createCell(2).setCellValue("보수교육"); 
		    		} else if(sch.get("SCH_GB_02").equals("03")){
		    			row.createCell(2).setCellValue("Faculty Seminar"); 
		    		} else if(sch.get("SCH_GB_02").equals("04")){
		    			row.createCell(2).setCellValue("수요화상"); 
		    		} else if(sch.get("SCH_GB_02").equals("05")){
		    			row.createCell(2).setCellValue("Osstem Meeting");  
		    		}
		    	} else {
		    		row.createCell(1).setCellValue("연수회");				// 연수회이면
		    		row.createCell(4).setCellValue(String.valueOf(sch.get("STADT")) + "~"
		    				+ String.valueOf(sch.get("FINDT")));// 강의일자
		    		
		    		if(sch.get("SCH_GB_01").equals("01")){
		    			row.createCell(2).setCellValue("Basic");  
		    		} else if(sch.get("SCH_GB_02").equals("02")){
		    			row.createCell(2).setCellValue("Advanced"); 
		    		} else if(sch.get("SCH_GB_02").equals("03")){
		    			row.createCell(2).setCellValue("Master"); 
		    		} else if(sch.get("SCH_GB_02").equals("04")){
		    			row.createCell(2).setCellValue("One-day Course"); 
		    		} 
		    	}
		    	
		    	row.createCell(3).setCellValue(String.valueOf(sch.get("TITLE")));			//제목 
		    	
		    	row.createCell(5).setCellValue(String.valueOf(sch.get("NAME")));// 수신자정보
		    	row.createCell(6).setCellValue(String.valueOf(sch.get("SENDDT")) 
		    			+ " " + String.valueOf(sch.get("SENDTM")));	// 발송일시
			}
			
			filename = URLEncoder.encode(filename, "UTF-8");// 한글로 인코딩
			response.setHeader("Content-Disposition", "attachment; filename=" + filename+ ".xls" );// 엑셀 파일명 지정
			response.setHeader("Content-Transfer-Encoding", "binary"); 
			
	}
}
