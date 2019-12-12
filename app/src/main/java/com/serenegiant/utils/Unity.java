package com.serenegiant.utils;


import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.serenegiant.utils.CH340AndroidDriver.UsbType;
import android.hardware.usb.UsbDeviceConnection;



public class Unity {
	
	public static int getLight(String lights) {
		int light;
		try {

			light=Integer.valueOf(lights);
		} catch (Exception e) {
			// TODO: handle exception
			light=1;
		}
		
		return light;
	}
	public static boolean SetConfig(UsbDeviceConnection connection,int baudRate, byte dataBit, byte stopBit, byte parity, byte flowControl) {
		int value = 0;
		int index = 0;
		char valueHigh = 0, valueLow = 0, indexHigh = 0, indexLow = 0;
		switch (parity) {
		case 0: /* NONE */
			valueHigh = 0x00;
			break;
		case 1: /* ODD */
			valueHigh |= 0x08;
			break;
		case 2: /* Even */
			valueHigh |= 0x18;
			break;
		case 3: /* Mark */
			valueHigh |= 0x28;
			break;
		case 4: /* Space */
			valueHigh |= 0x38;
			break;
		default: /* None */
			valueHigh = 0x00;
			break;
		}

		if (stopBit == 2) {
			valueHigh |= 0x04;
		}

		switch (dataBit) {
		case 5:
			valueHigh |= 0x00;
			break;
		case 6:
			valueHigh |= 0x01;
			break;
		case 7:
			valueHigh |= 0x02;
			break;
		case 8:
			valueHigh |= 0x03;
			break;
		default:
			valueHigh |= 0x03;
			break;
		}

		valueHigh |= 0xc0;
		valueLow = 0x9c;

		value |= valueLow;
		value |= (int) (valueHigh << 8);

		switch (baudRate) {
		case 50:
			indexLow = 0;
			indexHigh = 0x16;
			break;
		case 75:
			indexLow = 0;
			indexHigh = 0x64;
			break;
		case 110:
			indexLow = 0;
			indexHigh = 0x96;
			break;
		case 135:
			indexLow = 0;
			indexHigh = 0xa9;
			break;
		case 150:
			indexLow = 0;
			indexHigh = 0xb2;
			break;
		case 300:
			indexLow = 0;
			indexHigh = 0xd9;
			break;
		case 600:
			indexLow = 1;
			indexHigh = 0x64;
			break;
		case 1200:
			indexLow = 1;
			indexHigh = 0xb2;
			break;
		case 1800:
			indexLow = 1;
			indexHigh = 0xcc;
			break;
		case 2400:
			indexLow = 1;
			indexHigh = 0xd9;
			break;
		case 4800:
			indexLow = 2;
			indexHigh = 0x64;
			break;
		case 9600:
			indexLow = 2;
			indexHigh = 0xb2;
			break;
		case 19200:
			indexLow = 2;
			indexHigh = 0xd9;
			break;
		case 38400:
			indexLow = 3;
			indexHigh = 0x64;
			break;
		case 57600:
			indexLow = 3;
			indexHigh = 0x98;
			break;
		case 115200:
			indexLow = 3;
			indexHigh = 0xcc;
			break;
		case 230400:
			indexLow = 3;
			indexHigh = 0xe6;
			break;
		case 460800:
			indexLow = 3;
			indexHigh = 0xf3;
			break;
		case 500000:
			indexLow = 3;
			indexHigh = 0xf4;
			break;
		case 921600:
			indexLow = 7;
			indexHigh = 0xf3;
			break;
		case 1000000:
			indexLow = 3;
			indexHigh = 0xfa;
			break;
		case 2000000:
			indexLow = 3;
			indexHigh = 0xfd;
			break;
		case 3000000:
			indexLow = 3;
			indexHigh = 0xfe;
			break;
		default: // default baudRate "9600"
			indexLow = 2;
			indexHigh = 0xb2;
			break;
		}

		index |= 0x88 | indexLow;
		index |= (int) (indexHigh << 8);

		Uart_Control_Out(connection, CH340AndroidDriver.UartCmd.VENDOR_SERIAL_INIT, value, index);
		return true;
	}
	
	public static int Uart_Control_Out(UsbDeviceConnection connection,int request, int value, int index) {
		int retval = 0;
		retval = connection.controlTransfer(
				UsbType.USB_TYPE_VENDOR | UsbType.USB_RECIP_DEVICE | UsbType.USB_DIR_OUT, request, value, index, null,
				0, 2000);

		return retval;
	}
	public static boolean doStartApplicationWithPackageName(Activity activity, String packagename) {

		// 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
		PackageInfo packageinfo = null;
		try {
			packageinfo = activity.getPackageManager().getPackageInfo(packagename, 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageinfo == null) {
			return false;
		}

		// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packageinfo.packageName);

		// 通过getPackageManager()的queryIntentActivities方法遍历
		List<ResolveInfo> resolveinfoList = activity.getPackageManager()
				.queryIntentActivities(resolveIntent, 0);

		ResolveInfo resolveinfo = resolveinfoList.iterator().next();
		if (resolveinfo != null) {
			// packagename = 参数packname
			String packageName = resolveinfo.activityInfo.packageName;
			// 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
			String className = resolveinfo.activityInfo.name;
			// LAUNCHER Intent
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			// 设置ComponentName参数1:packagename参数2:MainActivity路径
			ComponentName cn = new ComponentName(packageName, className);

			intent.setComponent(cn);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.startActivity(intent);
			return true;
		}
		return false;
	}
}
