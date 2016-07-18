package com.notrace.zip;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import cn.trinea.android.common.util.ShellUtils;
import cn.trinea.android.common.util.ShellUtils.CommandResult;

//simulate android system app on 5.0+
public class MainActivity extends Activity {

	private static String path = "/data/local/tmp/zr/";
	private static String UZIPPath="/sdcard/zr/";
	private static String targetPath = "/data/local/tmp/zr/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 解压apk，得到文件列表
		findViewById(R.id.btn_unzip).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					Ziputils.unzip(new File(UZIPPath + "zr.apk"), new File(UZIPPath));
					// Ziputils.unzip(new File("/sdcard/zr/zr.apk"), new
					// File("/sdcard/zr/"));
					Log.d("==", "==");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		findViewById(R.id.btn_change).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					File file = new File(UZIPPath);
					File[] list = file.listFiles();
					for (int i = 0; i < list.length; i++) {
						if (list[i].toString().contains("lib") || list[i].toString().contains(".apk"))
							continue;
						String path = list[i].getAbsolutePath();
						if (list[i].isDirectory()) {
							FileUtils.deleteForders(path);
							FileUtils.deleteBlankPath(path);
						} else {
							list[i].delete();
						}
					}

					File[] libs = file.listFiles();
					for (int i = 0; i < libs.length; i++) {
						if (libs[i].toString().contains("lib")) {
							File f = new File(libs[i].getAbsolutePath() + "/armeabi");
							if (f != null) {
								File tf = new File(libs[i].getAbsolutePath() + "/arm");
								f.renameTo(tf);
							}
							return;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		findViewById(R.id.btn_move).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				try {
//					File file = new File(path);
//					File target = new File("/sdcard/tmp/zr/");
//					if (!target.exists()) {
//						target.mkdirs();
//					}
//					file.renameTo(target);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}

			}
		});

		findViewById(R.id.btn_install).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {

					// 解压，生成
					String[] cmds = new String[] { 
							"mount -o rw,remount /system",
							"cp -r /sdcard/zr /data/local/tmp/",
							// 改名
							"mv /data/local/tmp/zr/zr.apk /data/local/tmp/zr/zr", 
							"mount -o rw,remount /system",
							"cp -r /data/local/tmp/zr system/app/zr",
							"mount -o rw,remount /system",
							
							"chmod -R 755 /system/app/zr",
							//"chmod 755 /system/app/zr/lib/",
							//"chmod 755 /system/app/zr/lib/arm",
							//"mount -o rw,remount /system",
							//"chmod -R 644 /system/app/zr/lib/arm",//最后一个/
							//"mount -o rw,remount /system",
							"mv /system/app/zr/zr /system/app/zr/zr.apk",
							//"chmod 644 /system/app/zr/zr.apk"
							
							};
					CommandResult res=ShellUtils.execCommand(cmds, true);
					Log.d("===========", res.errorMsg);
					Log.d("===========", res.successMsg);
					Log.d("===========", res.result+"");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
