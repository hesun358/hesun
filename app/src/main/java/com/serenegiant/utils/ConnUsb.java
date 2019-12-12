package com.serenegiant.utils;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/11/8.
 */

public class ConnUsb {

    private UsbManager usbManager;
    private UsbInterface intf;
    private UsbDeviceConnection connection;
    private UsbEndpoint epOut, epIn;
    private usbMsgThread usb_Msg_Thread;
    private Context context;
    private boolean isRun = true;

    private ConnUsbInterface anInterface;

    public void setConnUsbInterface(ConnUsbInterface anInterface) {
        this.anInterface = anInterface;
    }

    public ConnUsb(Context context){
        this.context = context;
    }

    public void get_usb_info(UsbDevice device) {
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        connection = usbManager.openDevice(device);
        if (connection == null || device.getInterfaceCount() != 1) {
            return;
        }
        intf = device.getInterface(0);
        epOut = intf.getEndpoint(0);
        connection.claimInterface(intf, false);

        Unity.SetConfig(connection,9600, (byte) 8, (byte) 1,(byte) 0, (byte) 0);

        int cnt = intf.getEndpointCount();
        if (cnt < 1) {
            return;
        }
        for (int index = 0; index < cnt; index++) {
            UsbEndpoint ep = intf.getEndpoint(index);
            if ((ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
                    && (ep.getDirection() == UsbConstants.USB_DIR_OUT)) {
                epOut = ep;
                if (epOut == null) {
                    return;
                }
            }
            if ((ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
                    && (ep.getDirection() == UsbConstants.USB_DIR_IN)) {
                epIn = ep;
            }
        }
        if (usb_Msg_Thread!=null){

        }
        //usb_Msg_Thread = new usbMsgThread(connection, epIn,epOut);
        //usb_Msg_Thread.start();
    }
    public void sendMsg(byte[] data){
        if (connection!=null&&epOut!=null&&data!=null){
//            byte [] b   = {0,1};
            int i =connection.bulkTransfer(epOut,data,data.length,500);
            Log.e("yjh :"+i,Arrays.toString(data));
        }
    }

    public void close(){
        isRun = false;
        usbManager = null;
        if (connection != null) {
            connection.releaseInterface(intf);
            connection.close();
            connection= null;
        }
        intf = null;
        epOut = null;
        epIn = null;
        usb_Msg_Thread = null;
    }
    public String decodingUsb(String str) {
        String tempStr;
        tempStr = str.substring(str.indexOf("<[") + 2, str.lastIndexOf("]>")).trim();
        return tempStr;
    }


    class usbMsgThread extends Thread {
        UsbEndpoint epIn,epOut;
        UsbDeviceConnection connection;

        public usbMsgThread(UsbDeviceConnection connection, UsbEndpoint epIn,UsbEndpoint epOut) {
            this.connection = connection;
            this.epIn = epIn;
            this.epOut=epOut;
        }

        public void run() {
            Log.e("TAG","run");
            byte[] InBuffer = new byte[25];
            int length = InBuffer.length;
            int timeout = 5000;
            while (isRun) {
                int cnt = connection.bulkTransfer(epIn, InBuffer, length, timeout);
                Log.e("TAG","bulkTransfer:"+cnt);
                if (cnt < 0) {
                    continue;
                }
                try{
                    String verification = new String(InBuffer);
                    Log.e("TAG","verification:"+verification);
                    if (verification.contains("<[")&&verification.contains("]>")){
                        String byteData=new String(InBuffer);
                        byteData = decodingUsb(byteData);
                        if (byteData.equals("System_OnLine")){
                            //*****
                        }else{
                            if (byteData.contains("-")){
                                float data = 0;
                                byteData=byteData.replace("-",".");
                                try {
                                    data= Float.parseFloat(byteData);
                                }catch (Exception e){
                                    data=0;
                                }
                               // Message msg = Message.obtain();
                                if (anInterface!=null){
                                    anInterface.getConnUsbDate(data);
                                }
                                Log.e("TAG","data:"+data);
                                /*msg.what = WaxHandler.WHAT_RESISTANCE;
                                msg.obj = context;
                                try{
                                    Bundle bundle = new Bundle();
                                    bundle.putFloat("data",data);
                                    msg.setData(bundle);
                                }catch (Exception e){
                                }
                                WaxApplication.getWaxApplication().getHandler().sendMessage(msg);*/
                            }

                        }
                    }
                    InBuffer = new byte[25];


                }catch (Exception e){

                }
//                Log.e("zxzz", Arrays.toString(InBuffer));
//                Log.e("zxzz2", new String(InBuffer));
//                Message msg = Message.obtain();
//                msg.what = WaxHandler.WHAT_RESISTANCE;
//                msg.obj = context;
//                Log.e("zxzz2",""+decodingUsb(new String(InBuffer)));
//                try{
//                    Bundle bundle = new Bundle();
//                    bundle.putString("data",(String)(InBuffer[1]+"."+InBuffer[2]));
//                    msg.setData(bundle);
//                }catch (Exception e){
//                }
//                WaxApplication.getWaxApplication().getHandler().sendMessage(msg);

            }
        }
    }

}
