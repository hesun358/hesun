package com.hoppen.uvcctest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cs_layout.*

class CsActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cs_layout)

        initView()
    }

    private fun initView(){

        bt_lift_test.setOnClickListener {

        }

        bt_cargo_lane_test.setOnClickListener {
           // CargoWaySerialPort.getInstance().s
        }

        bt_dc_dj_test.setOnClickListener {

        }
    }

}