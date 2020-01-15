package com.hoppen.uvcctest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hoppen.bean.ProjectBean
import com.hoppen.utls.ExcelUtil
import com.hoppen.utls.FileUtil
import kotlinx.android.synthetic.main.activity_jxl_layout.*


class JxlTestActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jxl_layout)

        val path = FileUtil.getExternalStoragePath(this)+"/demo.xls"
        val title = arrayOf("detType","area","race","resistance","elastic","sex","season","lightType")
        val list = ArrayList<ProjectBean>()
        for (i in 0..9){
            val bean = ProjectBean(""+i,"亚洲","黄种人","0.45f","50","男","3","0","2")
            list.add(bean)
        }

        bt_dc_table.setOnClickListener {
            ExcelUtil.initExcel(path,title)
            ExcelUtil.writeObjListToExcel(list, path)
        }
        bt_get_table.setOnClickListener {
            val arrayList = ExcelUtil.readExcel(path)
            //tv_excel.text = arrayList.toString()
        }

    }

}

