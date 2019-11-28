package cn.detachment.example.core.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;

public class ExcelUtil {


    public static void main(String[] args) {
        String fp = "h:/desk/a.xlsx";
        DemoDataListener listener = new DemoDataListener();
        EasyExcel.read(fp, DemoData.class, listener).sheet().doRead();
        System.out.println(listener.getReadCnt());

    }

}
