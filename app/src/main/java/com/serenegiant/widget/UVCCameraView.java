package com.serenegiant.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hoppen.uvcctest.R;
import com.serenegiant.usb.IButtonCallback;
import com.serenegiant.usb.IStatusCallback;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.USBMonitor.UsbControlBlock;
import com.serenegiant.usb.USBSerialPort;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.utils.ConnUsbInterface;
import com.serenegiant.utils.OrderUtils;

import java.nio.ByteBuffer;

public class UVCCameraView extends FrameLayout implements USBMonitor.OnDeviceConnectListener, IStatusCallback, IButtonCallback, ConnUsbInterface {

    private Context context;
    private USBMonitor usbMonitor;
    private UVCCamera uvcCamera;
    private USBSerialPort usbSerialPort;

    private Surface surface;
    private SimpleUVCCameraTextureView uvc_camera_texture_view;

    public UVCCameraView(@NonNull Context context) {
        this(context,null);
    }

    public UVCCameraView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UVCCameraView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(context).inflate(R.layout.view_uvccamera_layout,this);
        initView();
    }

    private void initView() {
        uvc_camera_texture_view = findViewById(R.id.uvc_camera_texture_view);
        uvc_camera_texture_view.setAspectRatio(UVCCamera.DEFAULT_PREVIEW_WIDTH / (float)UVCCamera.DEFAULT_PREVIEW_HEIGHT);
        usbMonitor = new USBMonitor(context,this);
    }

    private void initCamera(UsbControlBlock ctrlBlock){
        uvcCamera = new UVCCamera();
        uvcCamera.open(ctrlBlock);
        uvcCamera.setStatusCallback(this);
        uvcCamera.setButtonCallback(this);
        if (surface != null) {
            surface.release();
            surface = null;
        }
        try {
            uvcCamera.setPreviewSize(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera.DEFAULT_PREVIEW_HEIGHT, UVCCamera.FRAME_FORMAT_MJPEG);
        } catch (final IllegalArgumentException e) {
            try {
                // fallback to YUV mode
                uvcCamera.setPreviewSize(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera.DEFAULT_PREVIEW_HEIGHT, UVCCamera.DEFAULT_PREVIEW_MODE);
            } catch (final IllegalArgumentException e1) {
                uvcCamera.destroy();
                return;
            }
        }
        final SurfaceTexture st = uvc_camera_texture_view.getSurfaceTexture();
        if (st != null) {
            surface = new Surface(st);
            uvcCamera.setPreviewDisplay(surface);
            //camera.setFrameCallback(mIFrameCallback, UVCCamera.PIXEL_FORMAT_RGB565/*UVCCamera.PIXEL_FORMAT_NV21*/);
            uvcCamera.startPreview();
        }

    }

    public void uvcCameraStar(){
        usbMonitor.register();
        if (uvcCamera != null) {
            uvcCamera.startPreview();
        }
    }

    public void uvcCameraStop(){
        if (uvcCamera != null) {
            uvcCamera.stopPreview();
        }
        if (usbMonitor != null) {
            usbMonitor.unregister();
        }
    }

    public void uvcCameraDestroy(){
        releaseCamera();
        if (uvcCamera!=null){
            uvcCamera.destroy();
            uvcCamera = null;
        }
        if (usbMonitor!=null){
            usbMonitor.destroy();
            usbMonitor = null;
        }
        uvc_camera_texture_view = null;
    }

    private void releaseCamera(){
        if (uvcCamera != null) {
            try {
                uvcCamera.setStatusCallback(null);
                uvcCamera.setButtonCallback(null);
                uvcCamera.close();
                uvcCamera.destroy();
            } catch (final Exception e) {
                //
            }
            uvcCamera = null;
        }
        if (surface != null) {
            surface.release();
            surface = null;
        }
    }

    public Bitmap getBitmap(){
        return uvc_camera_texture_view.getBitmap(400, 300);
    }

    @Override
    public void onAttach(UsbDevice device) {
        usbMonitor.requestPermission(device);
        Log.e("TAG","onAttach 插入USB设备："+device.getVendorId()+" device.getProductId():"+device.getProductId());
    }

    @Override
    public void onDettach(UsbDevice device) {
        Log.e("TAG","onDettach...");
    }

    @Override
    public void onConnect(UsbDevice device, UsbControlBlock ctrlBlock, boolean createNew) {
        Log.e("TAG","onConnect："+device.getVendorId()+" device.getProductId():"+device.getProductId());
        if (device.getVendorId() == 1423 && device.getProductId() == 14433) {//1423--14433
            Log.e("TAG","onConnect连接USB摄像头:"+device.getVendorId());
            releaseCamera();
            initCamera(ctrlBlock);
        }
        if (device.getVendorId() == 6790 && device.getProductId() == 29987) {
            usbSerialPort = new USBSerialPort(context,device);
            int mode = usbSerialPort.checkMode(OrderUtils.USB_MODULE_CODE());
            Log.e("TAG","onConnect:连接USB电阻mode:"+mode);
        }

        //onConnect：3034 device.getProductId():46880
        //onConnect：8746 device.getProductId():1
        //onConnect：11388 device.getProductId():293

        //1423 device.getProductId():14433
        //6790 device.getProductId():29987

    }

    public void switchLight(int index){
        if (usbSerialPort==null)return;
        switch (index){
            case 0:
                usbSerialPort.sendMsg(OrderUtils.USB_LIGHT_RGB());
                break;
            case 1:
                usbSerialPort.sendMsg(OrderUtils.USB_LIGHT_RGB());
                break;
            case 2:
                usbSerialPort.sendMsg(OrderUtils.USB_LIGHT_PL());
                break;
            case 3:
                usbSerialPort.sendMsg(OrderUtils.USB_LIGHT_PL());
                break;
            case 4:
                usbSerialPort.sendMsg(OrderUtils.USB_LIGHT_UV());
                break;
        }
    }

    @Override
    public void onDisconnect(UsbDevice device, UsbControlBlock ctrlBlock) {
        Log.e("TAG","onDisconnect...");
        if (uvcCamera != null) {
            uvcCamera.close();
            if (surface != null) {
                surface.release();
                surface = null;
            }
        }
    }

    @Override
    public void onCancel(UsbDevice device) {
        Log.e("TAG","onCancel...");
    }

    @Override
    public void onButton(int button, int state) {
        Log.e("TAG","onButton:"+"button = "+button+"== state = "+state);
    }

    @Override
    public void onStatus(int statusClass, int event, int selector, int statusAttribute, ByteBuffer data) {

    }

    @Override
    public void getConnUsbDate(float data) {
        Log.e("TAG","getConnUsbDate:"+data);
    }
}
