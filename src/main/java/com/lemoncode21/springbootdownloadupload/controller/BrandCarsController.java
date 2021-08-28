package com.lemoncode21.springbootdownloadupload.controller;

import com.lemoncode21.springbootdownloadupload.model.BrandCars;
import com.lemoncode21.springbootdownloadupload.response.ResponseHandler;
import com.lemoncode21.springbootdownloadupload.service.BrandCarsService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@RestController
@RequestMapping("/brandCars")
public class BrandCarsController {


    @Autowired
    private BrandCarsService brandCarsService;

    @PostMapping(value = "/importExcel")
    public ResponseHandler importExcel(@RequestParam("file") MultipartFile files, HttpServletRequest raw)throws NullPointerException{
        try {

            XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            List<BrandCars> brandCarsList = new ArrayList<>();

            for (int index = 0; index < worksheet.getPhysicalNumberOfRows() ; index++) {
                if(index > 0){
                    BrandCars brandCars = new BrandCars();
                    XSSFRow row = worksheet.getRow(index);

                    brandCars.setName(row.getCell(0).getStringCellValue());
                    brandCars.setType(row.getCell(1).getStringCellValue());
                    brandCars.setPrice(BigDecimal.valueOf(row.getCell(2).getNumericCellValue()));

                    this.brandCarsService.save(brandCars);
                    brandCarsList.add(brandCars);
                }
            }
            return new ResponseHandler(HttpStatus.OK.toString(),"Success import data!",brandCarsList);
        }catch (Exception e){
            return new ResponseHandler(HttpStatus.MULTI_STATUS.toString(),e.getMessage());
        }
    }

    @GetMapping(value = "/downloadExcel")
    public ResponseEntity<Resource> downloadExcel(){
        try{
            String fileName = "brandCarsDownload.xlsx";
            String SHEET = "Brand Cars";
            String[] headers = { "Name", "Type", "Price" };

            List<BrandCars> brandCarsList = this.brandCarsService.getAll();
            ByteArrayInputStream in = renderExcel(brandCarsList,headers,SHEET);
            InputStreamResource file = new InputStreamResource(in);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +fileName)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(file);
        }catch (Exception e){
            return (ResponseEntity<Resource>) ResponseEntity.internalServerError();
        }
    }

    private static ByteArrayInputStream renderExcel( List<BrandCars> brandCarsList,String[] headers, String SHEET){
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            Sheet sheet = workbook.createSheet(SHEET);
            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
            }

            int rowIdx = 1;
            for (BrandCars brandCars : brandCarsList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(brandCars.getName());
                row.createCell(1).setCellValue(brandCars.getType());
                row.createCell(2).setCellValue(String.valueOf(brandCars.getPrice()));
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }catch (Exception e){
            throw new RuntimeException("fail to download Excel file: " + e.getMessage());
        }
    }

}
