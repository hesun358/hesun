package com.serenegiant.utils;

/**
 * Created by Administrator on 2018/4/4.
 */

public class OrderUtils {
    private final static byte[] USB_MODULE_CODE = {(byte) 0xAA, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x00};
    private final static byte[] USB_APP_VER = {(byte) 0xAA, (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x00};
    private final static byte[] USB_CONTROLLER_INFO = {(byte) 0xAA, (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00};
    private final static byte[] USB_IMAGE_INFO = {(byte) 0xAA, (byte) 0x01, (byte) 0x04, (byte) 0x00, (byte) 0x00};
    private final static byte[] USB_HEAD_INFO = {(byte) 0xAA, (byte) 0x01, (byte) 0x05, (byte) 0x00, (byte) 0x00};
    private final static byte[] USB_SYS_ONLINE = {(byte) 0xAA, (byte) 0x10, (byte) 0x01, (byte) 0x00, (byte) 0x00};
    private final static byte[] USB_WATER_VALUE = {(byte) 0xAA, (byte) 0x10, (byte) 0x02, (byte) 0x00, (byte) 0x00};
    private final static byte[] USB_LIGHT_RGB = {(byte) 0xAA, (byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x00};
    private final static byte[] USB_LIGHT_PL = {(byte) 0xAA, (byte) 0x10, (byte) 0x04, (byte) 0x00, (byte) 0x00};
    private final static byte[] USB_LIGHT_UV = {(byte) 0xAA, (byte) 0x10, (byte) 0x05, (byte) 0x00, (byte) 0x00};
    private final static byte[] USB_LIGHT_OFF = {(byte) 0xAA, (byte) 0x10, (byte) 0x06, (byte) 0x00, (byte) 0x00};
    private final static byte[] USB_CLOSE = {(byte) 0xAA, (byte) 0x10, (byte) 0x07, (byte) 0x00, (byte) 0x00};

    private final static byte[] USB_WATER_VALUE_MODE2 = {0, 1};



    private static byte[] encryption(byte[] data) {
        byte[] returnData = new byte[data.length + 1];
        try {
            byte a = 0;
            for (int i = 0; i < data.length; i++) {
                returnData[i] =data[i];
                if (i!=0){
                    a ^=data[i];
                }
            }
            returnData[data.length] = a;
        } catch (Exception e) {
        }
        return returnData;
    }
    public static byte[] USB_MODULE_CODE(){
        return encryption(USB_MODULE_CODE);
    }
    public static byte[] USB_APP_VER(){
        return encryption(USB_APP_VER);
    }
    public static byte[] USB_CONTROLLER_INFO(){
        return encryption(USB_CONTROLLER_INFO);
    }
    public static byte[] USB_IMAGE_INFO(){
        return encryption(USB_IMAGE_INFO);
    }
    public static byte[] USB_HEAD_INFO(){
        return encryption(USB_HEAD_INFO);
    }
    public static byte[] USB_SYS_ONLINE(){
        return encryption(USB_SYS_ONLINE);
    }
    public static byte[] USB_WATER_VALUE(){
        return encryption(USB_WATER_VALUE);
    }
    public static byte[] USB_LIGHT_RGB(){
        return encryption(USB_LIGHT_RGB);
    }
    public static byte[] USB_LIGHT_PL(){
        return encryption(USB_LIGHT_PL);
    }
    public static byte[] USB_LIGHT_UV(){
        return encryption(USB_LIGHT_UV);
    }
    public static byte[] USB_LIGHT_OFF(){
        return encryption(USB_LIGHT_OFF);
    }
    public static byte[] USB_CLOSE(){
        return encryption(USB_CLOSE);
    }
    public static byte[] USB_WATER_VALUE_MODE2(){
        return USB_WATER_VALUE_MODE2;
    }
}
