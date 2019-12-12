package com.hoppen.utls;

import android.util.Log;

import com.hoppen.bean.ProjectBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * excel表格工具类
 */
public class ExcelUtil {

    private static WritableFont arial14font = null;

    private static WritableCellFormat arial14format = null;
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";

    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    private static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 10);
            arial12font.setColour(jxl.format.Colour.ORANGE);
            arial12format = new WritableCellFormat(arial12font);
            //对齐格式
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            //设置边框
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Excel
     *
     * @param fileName 导出excel存放的地址（目录）
     * @param colName excel中包含的列名（可以有多个）
     */
    public static void initExcel(String fileName, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            workbook = Workbook.createWorkbook(file);
            //设置表格的名字
            WritableSheet sheet = workbook.createSheet("皮肤检测报告表", 0);
            //创建标题栏
            sheet.addCell((WritableCell) new Label(0, 0, fileName, arial14format));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
            }
            //设置行高
            sheet.setRowView(0, 340);
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T> void writeObjListToExcel(List<T> objList, String fileName) {
        if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                in = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName), workbook);
                WritableSheet sheet = writebook.getSheet(0);

                for (int j = 0; j < objList.size(); j++) {
                    ProjectBean projectBean = (ProjectBean) objList.get(j);
                    List<String> list = new ArrayList<>();
                    list.add(projectBean.getDetType());
                    list.add(projectBean.getArea());
                    list.add(projectBean.getRace());
                    list.add(projectBean.getResistance());
                    list.add(projectBean.getElastic());
                    list.add(projectBean.getSex());
                    list.add(projectBean.getSeason());
                    list.add(projectBean.getDeviceType());
                    list.add(projectBean.getLightType());

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                        if (list.get(i).length() <= 4) {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 8);
                        } else {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 5);
                        }
                    }
                    //设置行高
                    sheet.setRowView(j + 1, 350);
                }
                writebook.write();
                Log.i("TAG","导出Excel成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public static ArrayList<ProjectBean> readExcel(String path){
        Workbook wb = null;
        try {
            //创建工作簿并载入excel文件流
            InputStream is = new FileInputStream(path);
            wb = Workbook.getWorkbook(is);
            //int sheetSize = wb.getNumberOfSheets();
            //通过索引或者名称获取Sheet
            ArrayList<ProjectBean> list = new ArrayList<>();
            Sheet[] sheet = wb.getSheets();
            for (int i = 0; i < sheet.length; i++) {
                Sheet rs = wb.getSheet(i);
                for (int j = 1; j < rs.getRows(); j++) {
                    Cell[] cells = rs.getRow(j);
                    ProjectBean projectBean = new ProjectBean();
                    for (int k = 0; k < cells.length; k++) {
                        projectBean.setDetType(cells[0].getContents());
                        projectBean.setArea(cells[1].getContents());
                        projectBean.setRace(cells[2].getContents());
                        projectBean.setResistance(cells[3].getContents());
                        projectBean.setElastic(cells[4].getContents());
                        projectBean.setSex(cells[5].getContents());
                        projectBean.setSeason(cells[6].getContents());
                        projectBean.setDeviceType(cells[7].getContents());
                        projectBean.setLightType(cells[8].getContents());
                    }
                    list.add(projectBean);
                }
            }
            is.close();
            return list;
        } catch (IOException | BiffException e) {
            e.printStackTrace();
            return null;
        }
    }

}
