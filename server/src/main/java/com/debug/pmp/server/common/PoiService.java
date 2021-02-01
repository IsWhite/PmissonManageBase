package com.debug.pmp.server.common;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通用的导出excel服务
 */
@Service
public class PoiService {

    private static final Logger log= LoggerFactory.getLogger(PoiService.class);

    private static final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private Environment env;



    //TODO：dataList.subList(0,100)
    //TODO：截取 0-100 总数100 调用一次fillExcelSheetData
    //TODO：截取 100-200 总数 100  调用一次fillExcelSheetData

    /**
     * 分sheet导出实战
     * @param dataList
     * @param headers
     * @param sheetName
     * @return
     */
    public Workbook manageSheet(List<Map<Integer, Object>> dataList, String[] headers, String sheetName){
        final Integer sheetSize=1000;
        //final Integer sheetSize=env.getProperty("poi.product.excel.sheet.size",Integer.class);
        int dataTotal=dataList.size();

        int sheetTotal = (dataTotal%sheetSize==0)? dataTotal/sheetSize : (dataTotal/sheetSize + 1);
        int start=0;
        int end=sheetSize;

        List<Map<Integer, Object>> subList;
        Workbook wb=new HSSFWorkbook();

        for (int i=0;i<sheetTotal;i++){
            subList=dataList.subList(start,end);
            wb=this.fillExcelSheetDataV2(subList,headers,sheetName+"_"+(i+1),wb);

            start += sheetSize;
            end += sheetSize;
            if (end>=dataTotal){
                end=dataTotal;
            }
        }
        return wb;
    }


    /**
     * 填充数据到excel的sheet中 - 分sheet实战
     * @param dataList
     * @param headers
     * @param sheetName
     */
    public Workbook fillExcelSheetDataV2(List<Map<Integer, Object>> dataList, String[] headers, String sheetName,Workbook wb){
        Sheet sheet=wb.createSheet(sheetName);

        //TODO：创建sheet的第一行数据-即excel的表头
        Row headerRow=sheet.createRow(0);
        for(int i=0;i<headers.length;i++){
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        //TODO：从第二行开始塞入真正的数据列表
        int rowIndex=1;
        Row row;
        Object obj;

        if (dataList!=null && dataList.size()>0){
            for(Map<Integer, Object> rowMap:dataList){
                try {
                    row=sheet.createRow(rowIndex++);

                    //TODO：遍历表头行-每个key -> 取到实际的value
                    for(int i=0;i<headers.length;i++){
                        obj=rowMap.get(i);

                        if (obj==null) {
                            log.debug("--");

                            row.createCell(i).setCellValue("");
                        }else if (obj instanceof Date) {
                            String tempDate=simpleDateFormat.format((Date)obj);
                            row.createCell(i).setCellValue((tempDate==null)?"":tempDate);
                        }else {
                            row.createCell(i).setCellValue(String.valueOf(obj));
                        }
                    }
                } catch (Exception e) {
                    log.debug("充数据到excel的sheet中 - 分sheet实战 发生异常： ",e.fillInStackTrace());
                }
            }
        }


        return wb;
    }



    /**
     * 填充数据到excel的sheet中 - v1
     * @param dataList
     * @param headers
     * @param sheetName
     */
    public Workbook fillExcelSheetData(List<Map<Integer, Object>> dataList, String[] headers, String sheetName){
        Workbook wb=new HSSFWorkbook();
        Sheet sheet=wb.createSheet(sheetName);

        //TODO：创建sheet的第一行数据-即excel的表头
        Row headerRow=sheet.createRow(0);
        for(int i=0;i<headers.length;i++){
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        //TODO：从第二行开始塞入真正的数据列表
        int rowIndex=1;
        Row row;
        Object obj;

        if (dataList!=null && dataList.size()>0){
            for(Map<Integer, Object> rowMap:dataList){
                try {
                    row=sheet.createRow(rowIndex++);

                    //TODO：遍历表头行-每个key -> 取到实际的value
                    for(int i=0;i<headers.length;i++){
                        obj=rowMap.get(i);

                        if (obj==null) {
                            row.createCell(i).setCellValue("");
                        }else if (obj instanceof Date) {
                            String tempDate=simpleDateFormat.format((Date)obj);
                            row.createCell(i).setCellValue((tempDate==null)?"":tempDate);
                        }else {
                            row.createCell(i).setCellValue(String.valueOf(obj));
                        }
                    }
                } catch (Exception e) {
                    log.debug("excel sheet填充数据 发生异常： ",e.fillInStackTrace());
                }
            }
        }

        return wb;
    }

}



















































