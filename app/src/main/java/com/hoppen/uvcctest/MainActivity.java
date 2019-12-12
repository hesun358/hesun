package com.hoppen.uvcctest;

import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.serenegiant.usb.IButtonCallback;
import com.serenegiant.usb.IStatusCallback;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.USBMonitor.OnDeviceConnectListener;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.widget.SimpleUVCCameraTextureView;

import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity{

    private final Object mSync = new Object();

    private USBMonitor mUSBMonitor;
    private UVCCamera mUVCCamera;
    private SimpleUVCCameraTextureView mUVCCameraView;
    private Surface mPreviewSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUVCCameraView = findViewById(R.id.my_uvc_camera);
        mUVCCameraView.setAspectRatio(UVCCamera.DEFAULT_PREVIEW_WIDTH / (float)UVCCamera.DEFAULT_PREVIEW_HEIGHT);

        mUSBMonitor = new USBMonitor(this, mOnDeviceConnectListener);
    }

    private final OnDeviceConnectListener mOnDeviceConnectListener = new OnDeviceConnectListener() {
        @Override
        public void onAttach(UsbDevice device) {
            Log.e("TAG", "USB设备onAttach："+device.getDeviceName());
            mUSBMonitor.requestPermission(device);
        }

        @Override
        public void onDettach(UsbDevice device) {

        }

        @Override
        public void onConnect(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock, boolean createNew) {
            Log.e("TAG", "USB设备Connect："+device.getDeviceName());
            releaseCamera();
            final UVCCamera camera = new UVCCamera();
            camera.open(ctrlBlock);
            camera.setStatusCallback(new IStatusCallback() {
                @Override
                public void onStatus(int statusClass, int event, int selector, int statusAttribute, ByteBuffer data) {

                }
            });
            camera.setButtonCallback(new IButtonCallback() {
                @Override
                public void onButton(int button, int state) {
                    Log.e("TAG","onButton(button=" + button + "; " + "state=" + state + ")");
                }
            });
            if (mPreviewSurface != null) {
                mPreviewSurface.release();
                mPreviewSurface = null;
            }
            try {
                camera.setPreviewSize(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera.DEFAULT_PREVIEW_HEIGHT, UVCCamera.FRAME_FORMAT_MJPEG);
            } catch (final IllegalArgumentException e) {
                try {
                    // fallback to YUV mode
                    camera.setPreviewSize(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera.DEFAULT_PREVIEW_HEIGHT, UVCCamera.DEFAULT_PREVIEW_MODE);
                } catch (final IllegalArgumentException e1) {
                    camera.destroy();
                    return;
                }
            }
            final SurfaceTexture st = mUVCCameraView.getSurfaceTexture();
            if (st != null) {
                mPreviewSurface = new Surface(st);
                camera.setPreviewDisplay(mPreviewSurface);
//						camera.setFrameCallback(mIFrameCallback, UVCCamera.PIXEL_FORMAT_RGB565/*UVCCamera.PIXEL_FORMAT_NV21*/);
                camera.startPreview();
            }
            synchronized (mSync) {
                mUVCCamera = camera;
            }
        }

        @Override
        public void onDisconnect(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock) {
            synchronized (mSync) {
                if (mUVCCamera != null) {
                    mUVCCamera.close();
                    if (mPreviewSurface != null) {
                        mPreviewSurface.release();
                        mPreviewSurface = null;
                    }
                }
            }
        }

        @Override
        public void onCancel(UsbDevice device) {

        }

    };

    @Override
    protected void onStart() {
        super.onStart();
        mUSBMonitor.register();
        synchronized (mSync) {
            if (mUVCCamera != null) {
                mUVCCamera.startPreview();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        synchronized (mSync) {
            if (mUVCCamera != null) {
                mUVCCamera.stopPreview();
            }
            if (mUSBMonitor != null) {
                mUSBMonitor.unregister();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (mSync) {
            releaseCamera();
            if (mUVCCamera != null) {
                mUVCCamera.destroy();
                mUVCCamera = null;
            }
            if (mUSBMonitor != null) {
                mUSBMonitor.destroy();
                mUSBMonitor = null;
            }
        }
        mUVCCameraView = null;
    }

    private synchronized void releaseCamera() {
        synchronized (mSync) {
            if (mUVCCamera != null) {
                try {
                    mUVCCamera.setStatusCallback(null);
                    mUVCCamera.setButtonCallback(null);
                    mUVCCamera.close();
                    mUVCCamera.destroy();
                } catch (final Exception e) {
                    //
                }
                mUVCCamera = null;
            }
            if (mPreviewSurface != null) {
                mPreviewSurface.release();
                mPreviewSurface = null;
            }
        }
    }
}
