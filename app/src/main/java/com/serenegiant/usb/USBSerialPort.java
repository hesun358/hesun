package com.serenegiant.usb;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.serenegiant.utils.OrderUtils;
import com.serenegiant.utils.Unity;

import java.util.Arrays;

public class USBSerialPort {

    private Context context;
    private UsbManager usbManager;
    private UsbInterface usbInterface;
    private UsbDeviceConnection deviceConnection;
    private UsbEndpoint usbEpOut, usbEpIn;
    private UsbDevice device;

    public USBSerialPort(Context context,UsbDevice device) {
        this.context = context;
        this.device = device;
        init();
    }

    private void init(){
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        //一个设备上面一般只有一个接口，有两个端点，分别接受和发送数据
        usbInterface = device.getInterface(0);
        getEndpoint(usbInterface);
        deviceConnection = usbManager.openDevice(device); //打开设备
        deviceConnection.claimInterface(usbInterface, false);
        Unity.SetConfig(deviceConnection,9600, (byte) 8, (byte) 1,(byte) 0, (byte) 0);
        Log.e("TAG","deviceConnection:"+deviceConnection.toString()+"====="+"device:"+device.getInterfaceCount());
    }

    /**
     * 获取设备端点
     */
    private void getEndpoint( UsbInterface usbInterface){
        for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
            UsbEndpoint ep = usbInterface.getEndpoint(i);
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                    usbEpOut = ep;
                    Log.e("TAG", "获取发送数据的端点");
                } else {
                    usbEpIn = ep;
                    Log.e("TAG", "获取接受数据的端点");
                }
            }
        }
    }

    /**
     * 获取USB手柄模式
     */
    public int checkMode(byte[] data) {
        if (deviceConnection != null && usbEpOut != null) {
            int i = deviceConnection.bulkTransfer(usbEpOut, data, data.length, 200);
            byte[] InBuffer = new byte[50];
            int length = InBuffer.length;
            int cnt = deviceConnection.bulkTransfer(usbEpOut, InBuffer, length, 500);
            String verification = new String(InBuffer);
            if (verification.contains("<[") && verification.contains("]>")) {
                return 3;
            } else {
                return 2;
            }
        }
        return -1;
    }

    /**
     * bulkTransfer 块传输 ，controlTransfer 控制传输
     */
    public void sendMsg(byte[] data){
        if (deviceConnection!=null&&usbEpOut!=null&&data!=null){
            int i =deviceConnection.bulkTransfer(usbEpOut,data,data.length,500);
            Log.e("TAG","sendUsbMsg:"+ Arrays.toString(data)+" code:"+i);
        }
    }

    /**
     * 获取生物电阻值
     */
    public String sendMsgGetWater(int mode){
        String water ="";
        byte[] data = null;
        switch (mode){
            case 2:
                data = OrderUtils.USB_WATER_VALUE_MODE2();
                break;
            case 3:
                data = OrderUtils.USB_WATER_VALUE();
                break;
        }
        if (deviceConnection != null && usbEpOut != null && data != null) {
            int i = deviceConnection.bulkTransfer(usbEpOut, data, data.length, 500);
            byte[] InBuffer = new byte[50];
            int length = InBuffer.length;
            int timeout = 5000;
            int cnt = deviceConnection.bulkTransfer(usbEpIn, InBuffer, length, timeout);
            String verification = new String(InBuffer);
//            Log.e("XXXXXXXX%%%%",""+verification);
            if (verification.contains("<[")&&verification.contains("]>")){
                water = decodingUsb(verification);
//                Log.e("XXXXXXXXXXX/////",""+water);
                if (water.contains("-")){
                    water=water.replace("-",".");
//                    Log.e("XXXXXXXXXXX/////",""+water);
                }
            }else{
                water = InBuffer[1]+"."+InBuffer[2];
            }
        }
        return water;
    }

    public String decodingUsb(String str) {
        String tempStr;
        tempStr = str.substring(str.indexOf("<[") + 2, str.lastIndexOf("]>")).trim();
        return tempStr;
    }

    public void close(){
        if (deviceConnection!=null){
            deviceConnection.releaseInterface(usbInterface);
            deviceConnection.close();
            deviceConnection = null;
        }
        if (usbInterface!=null) {
            usbInterface = null;
        }
        if (usbManager!=null){
            usbManager = null;
        }
        if (usbEpOut!=null){
            usbEpOut = null;
        }
        if (usbEpIn!=null){
            usbEpIn = null;
        }
    }

}
