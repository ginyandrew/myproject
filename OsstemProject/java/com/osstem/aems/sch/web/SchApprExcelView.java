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

@Component("SchApprExcelView")
public class SchApprExcelView extends AbstractExcelView {
	private HSSFCellStyle hSSFCellStyle;
	private HSSFCellStyle CellStyle;
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model,HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
			String filename = "연자평가_리스트_" + DateUtil.getCurTime() ;

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
			worksheet.setColumnWidth(1, 30*245);	// Faculty/ Director
			worksheet.setColumnWidth(2, 20*245);	// 상위분류
		    worksheet.setColumnWidth(3, 20*245);	// 하위분류
		    worksheet.setColumnWidth(4, 60*245);	// 제목
		    worksheet.setColumnWidth(5, 20*245);	// 강의일자
		    worksheet.setColumnWidth(6, 20*245);	// 평균점수
		    worksheet.setColumnWidth(7, 10*245);	// 연자추천
		    
		    row = worksheet.createRow(0);
		    
		    row.createCell(0).setCellValue("No");
		    row.createCell(1).setCellValue("Faculty/Director");
		    row.createCell(2).setCellValue("상위분류");
		    row.createCell(3).setCellValue("하위분류");
		    row.createCell(4).setCellValue("제목");
		    row.createCell(5).setCellValue("강의일자");
		    row.createCell(6).setCellValue("평균점수");
		    row.createCell(7).setCellValue("연자추천");
		    
		    for (int i = 0; i < 8; i++) {
		    	row.getCell(i).setCellStyle(hSSFCellStyle);
		    }
		    
		    @SuppressWarnings("unchecked")
		    List<HashMap<String, Object>> list = (List<HashMap<String, Object>> ) model.get("schApprList");
		    if (list.size()>0){
		    	
			    // 가져온 데이터를 각 컬럼에 집어넣어서 열을 늘린다.
				for (int i = 0 ; i < list.size() ; i++){
					
					HashMap<String, Object> sch = list.get(i);
					
					row = worksheet.createRow(i+1);								// sheet에 새로운 줄 생성.
					
					row.createCell(0).setCellValue(i);									// No
					row.createCell(1).setCellValue(String.valueOf(sch.get("mbr")));		// Faculty Director	
					row.createCell(2).setCellValue("강연회");								// 연자평가는 무조건 강연회.
			    		
		    		if(sch.get("gb02").equals("01")){
		    			row.createCell(3).setCellValue("연수회");  
		    		} else if(sch.get("gb02").equals("02")){
		    			row.createCell(3).setCellValue("보수교육"); 
		    		} else if(sch.get("gb02").equals("03")){
		    			row.createCell(3).setCellValue("Faculty Seminar"); 
		    		} else if(sch.get("gb02").equals("04")){
		    			row.createCell(3).setCellValue("수요화상"); 
		    		} else if(sch.get("gb02").equals("05")){
		    			row.createCell(3).setCellValue("Osstem Meeting");  
		    		}
			    	
		    		row.createCell(4).setCellValue(String.valueOf(sch.get("title")));			//제목 
		    		row.createCell(5).setCellValue(String.valueOf(sch.get("stadt")));// 강의일자
			    	row.createCell(6).setCellValue(String.valueOf(sch.get("apprCd")));// 평균점수
			    	row.createCell(7).setCellValue(String.valueOf(sch.get("recmYn")));// 연자추천
				}
		    }
			filename = URLEncoder.encode(filename, "UTF-8");// 한글로 인코딩
			response.setHeader("Content-Disposition", "attachment; filename=" + filename+ ".xls" );// 엑셀 파일명 지정
			response.setHeader("Content-Transfer-Encoding", "binary"); 
			
	}
}
