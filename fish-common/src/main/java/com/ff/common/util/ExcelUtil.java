package com.ff.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

/**
 *
 * @author wangbaojun1992@163.com
 * @version poi version ： 4.1.0
 */
public class ExcelUtil {

    /**
     *
     * @param titleMape 标题行，为第一行标题内容与样式,参数Map<String,String>结构，字段如下：
     *         {
     *             value:内容值
     *             backgroundColor:背景色,为RGB颜色，3个色值以","隔开，默认"189,215,238"
     *         }
     * @param titleList 表头行，为第二行表头内容与样式，参数List<Map<String,String>>结构，字段如下：
     *         [
     *             {
     *                 value:内容值
     *                 backgroundColor:背景色,为RGB颜色，3个色值以","隔开，默认"189,215,238"
     *             }
     *         ]
     * @param contentList 内容,所有取值均被转换位String类型，参数List<List<String>>结构。
     * @param contentStyle 内容样式，参数为Map<String,String>结构，字段如下：
     *         {
     *             isZebra：内容区是否使用斑马线，枚举值：0是，1否，默认0
     *             zebraColor：斑马线颜色，为RGB颜色，3个色值以","隔开，默认"230,230,230"
     * @param dirName 缓存文件的文件夹的绝对路径
     * @param fileName 文件名，不要带后缀
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public static String exportXlsExcel(Map<String, String> titleMape,List<Map<String, String>> titleList,List<List<String>> contentList,Map<String, String> contentStyle,String dirName,String fileName) throws Exception {
        //1.验证文件和文件夹名并创建Excel文件
        if(fileName == null || fileName.trim().equals("")) {
            throw new Exception("生成Excel文件异常：传入的文件名fileName不可为null、空字符串");
        }
        File parentDir = null;
        if(dirName == null || dirName.trim().equals("")) {
            throw new Exception("生成Excel文件异常：传入的文件夹名dirName不可为null、空字符串");
        }
        try {
            parentDir = new File(dirName);
            if(!parentDir.exists()) {
                parentDir.mkdirs();
            }
        } catch (Exception e) {
            throw new Exception("生成Excel文件异常：传入的文件夹名dirName有误，dirName="+dirName);
        }finally {
            if(parentDir == null) {
                throw new Exception("生成Excel文件异常：创建文件夹出错，dirName="+dirName);
            }
        }
        File excelFile = null;
        try {
            excelFile = new File(parentDir, fileName+".xls");
            if(excelFile.exists()) {
                excelFile.delete();
            }
            excelFile.createNewFile();
        } catch (Exception e) {
            throw new Exception("生成Excel文件异常：生成File文件出错，fileName="+fileName);
        }finally {
            if(excelFile == null) {
                throw new Exception("生成Excel文件异常：生成File文件出错，fileName="+fileName);
            }
        }

        //2创建工作簿
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet=wb.createSheet();
        //3编辑标题
        //3.1标题样式
        HSSFCellStyle titleStyle=wb.createCellStyle();
        //3.1.1标题背景色
        HSSFPalette palette0_0 = wb.getCustomPalette();
        String backgroundColorStr0_0 = titleMape.get("backgroundColor");
        if(backgroundColorStr0_0 == null || backgroundColorStr0_0.trim().equals("")) {
            backgroundColorStr0_0 = "189,215,238";
        }
        String[] backGroundColorStr0_0Strs = backgroundColorStr0_0.split(",");
        if(backGroundColorStr0_0Strs.length != 3) {
            backGroundColorStr0_0Strs = "189,215,238".split(",");
        }
        palette0_0.setColorAtIndex((short)9 ,(byte)new Integer(backGroundColorStr0_0Strs[0]).intValue(),(byte)(new Integer(backGroundColorStr0_0Strs[1]).intValue()),(byte)(new Integer(backGroundColorStr0_0Strs[2]).intValue()));
        titleStyle.setFillForegroundColor((short)9 );
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //3.1.2标题合并单元格
        // Region region1 = new Region(0, (short) 0, 0, (short) 6);//参数1：行号 参数2：起始列号 参数3：行号 参数4：终止列号
        CellRangeAddress rg0_0 = new CellRangeAddress(0,0,(short)0,(short)titleList.size()-1);
        sheet.addMergedRegion(rg0_0);

        //3.1.3标题边框
        //TODO 此处合并单元格的边框样式没有生效，网络上一大堆复制黏贴的东西，试了很多未成功，头痛，暂时遗留该问题

        //使用RegionUtil类为合并后的单元格添加边框
        RegionUtil.setBorderBottom(BorderStyle.THIN, rg0_0, sheet); // 下边框
        RegionUtil.setBorderLeft(BorderStyle.THIN, rg0_0, sheet); // 左边框
        RegionUtil.setBorderRight(BorderStyle.THIN, rg0_0, sheet); // 有边框
        RegionUtil.setBorderTop(BorderStyle.THIN, rg0_0, sheet); // 上边框

        //3.1.4对齐
        titleStyle.setAlignment(HorizontalAlignment.CENTER); //居中

        //3.2写入标题
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell cell0_0 = row0.createCell(0);
        cell0_0.setCellValue(titleMape.get("value"));
        cell0_0.setCellStyle(titleStyle);

        //4编辑表头
        short fi = 11;
        HSSFRow row1 = sheet.createRow(1);
        for(int i = 0;i < titleList.size();i ++,fi++) {
            Map<String, String> oneTitle = titleList.get(i);
            //4.1当前列表头样式
            HSSFCellStyle titleStyleI = wb.createCellStyle();
            //4.1.1当前列表头背景色
            HSSFPalette palette1_I = wb.getCustomPalette();
            String backGroundColor1_I = oneTitle.get("backgroundColor");
            if(backGroundColor1_I == null || backGroundColor1_I.trim().equals("")) {
                backGroundColor1_I = "189,215,238";
            }
            String[] backGroundColor1_IStrs = backGroundColor1_I.split(",");
            if(backGroundColor1_IStrs.length != 3) {
                backGroundColor1_IStrs = "189,215,238".split(",");
            }
            palette1_I.setColorAtIndex(fi,(byte)(new Integer(backGroundColor1_IStrs[0]).intValue()),(byte)(new Integer(backGroundColor1_IStrs[1]).intValue()),(byte)(new Integer(backGroundColor1_IStrs[2]).intValue()));
            titleStyleI.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            titleStyleI.setFillForegroundColor(fi);

            //4.1.2当前列表头边框
            titleStyleI.setBorderBottom(BorderStyle.THIN); //下边框
            titleStyleI.setBorderLeft(BorderStyle.THIN);//左边框
            titleStyleI.setBorderTop(BorderStyle.THIN);//上边框
            titleStyleI.setBorderRight(BorderStyle.THIN);//右边框

            //4.1.3对齐
            titleStyleI.setAlignment(HorizontalAlignment.CENTER); //居中

            //4.2写入当前列表头
            HSSFCell cell1_I = row1.createCell(i);
            cell1_I.setCellValue(oneTitle.get("value"));
            cell1_I.setCellStyle(titleStyleI);
        }

        //5编辑内容区
        //5.1准备样式
        String isZebraStr = contentStyle.get("isZebra");
        boolean isZebra = true;
        if(isZebraStr != null && isZebraStr.equals("1")) {
            isZebra = false;
        }



        //5.1.1斑马线行样式
        HSSFCellStyle style1 = wb.createCellStyle();
        //背景色
        HSSFPalette paletteC = wb.getCustomPalette();
        String backGroundColorC = contentStyle.get("zebraColor");
        if(backGroundColorC == null || backGroundColorC.trim().equals("")) {
            backGroundColorC = "230,230,230";
        }
        String[] backGroundColorCStrs = backGroundColorC.split(",");
        if(backGroundColorCStrs.length != 3) {
            backGroundColorCStrs = "230,230,230".split(",");
        }
        paletteC.setColorAtIndex((short)10,
                (byte)(new Integer(backGroundColorCStrs[0]).intValue()),
                (byte)(new Integer(backGroundColorCStrs[1]).intValue()),
                (byte)(new Integer(backGroundColorCStrs[2]).intValue()));
        style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style1.setFillForegroundColor((short)10);
        //边框
        style1.setBorderBottom(BorderStyle.THIN); //下边框
        style1.setBorderLeft(BorderStyle.THIN);//左边框
        style1.setBorderTop(BorderStyle.THIN);//上边框
        style1.setBorderRight(BorderStyle.THIN);//右边框



        //5.1.2非斑马线行样式
        HSSFCellStyle style0 = wb.createCellStyle();
        //背景色
        style0.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style0.setFillForegroundColor((short)30);
        //边框
        style0.setBorderBottom(BorderStyle.THIN); //下边框
        style0.setBorderLeft(BorderStyle.THIN);//左边框
        style0.setBorderTop(BorderStyle.THIN);//上边框
        style0.setBorderRight(BorderStyle.THIN);//右边框

        //5.2写入内容
        for(int i = 0;i < contentList.size();i ++) {
            List<String> contents = contentList.get(i);
            HSSFRow rowI = sheet.createRow(i+2);
            for(int j = 0;j < contents.size();j ++) {
                HSSFCell cellJ = rowI.createCell(j);
                cellJ.setCellValue(contents.get(j));
                if(i % 2 == 1) {
                    if(isZebra) {
                        cellJ.setCellStyle(style1);
                    }else {
                        cellJ.setCellStyle(style0);
                    }
                }else {
                    cellJ.setCellStyle(style0);
                }
            }
        }

        //6将文件输出
        OutputStream ouputStream = null;
        try {
            ouputStream = new FileOutputStream(excelFile);
            wb.write(ouputStream);
            ouputStream.flush();
            wb.close();
        } catch (Exception e) {
            throw new Exception("生成Excel文件异常：写出Excel文件异常");
        }finally {
            try {
                if(ouputStream != null) {
                    ouputStream.close();
                }
            } catch (Exception e2) {
            }
        }

        return excelFile.getAbsolutePath();
    }
}