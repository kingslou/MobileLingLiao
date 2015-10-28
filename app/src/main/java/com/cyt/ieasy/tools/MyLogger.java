package com.cyt.ieasy.tools;

import android.util.Log;

/**
 * 自定义log日志打印类，方便管理。
 * @author jin
 * 2014年5月13日 下午3:49:09
 * 
 */
public class MyLogger {
//	private static final String TAG = "MyLogger";
	public static boolean DEBUG = false;

	/**
	 * 显示Log信息（带行号）
	 * @param logLevel 1 v ; 2 d ; 3 i ; 4 w ; 5 e .
	 * @param info 显示的log信息
	 */
	public static void showLogWithLineNum(int logLevel, String info) {
		String[] infos = getAutoJumpLogInfos();
		switch (logLevel) {
		case 1:
			if (DEBUG)
				Log.v(infos[0], info + " : " + infos[1] + infos[2]);
			break;
		case 2:
			if (DEBUG)
				Log.d(infos[0], info + " : " + infos[1] + infos[2]);
			break;
		case 3:
			if (DEBUG)
				Log.i(infos[0], info + " : " + infos[1] + infos[2]);
			break;
		case 4:
			if (DEBUG)
				Log.w(infos[0], info + " : " + infos[1] + infos[2]);
			break;
		case 5:
			if (DEBUG)
				Log.e(infos[0], info + " : " + infos[1] + infos[2]);
			break;
		}
	}
	/**
	 * 获取打印信息所在方法名，行号等信息
	 * @return
	 */
	private static String[] getAutoJumpLogInfos() {
		String[] infos = new String[] { "", "", "" };
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if (elements.length < 5) {
			Log.e("MyLogger", "Stack is too shallow!!!");
			return infos;
		} else {
			infos[0] = elements[4].getClassName().substring(
					elements[4].getClassName().lastIndexOf(".") + 1);
			infos[1] = elements[4].getMethodName() + "()";
			infos[2] = " at (" + elements[4].getClassName() + ".java:"
					+ elements[4].getLineNumber() + ")";
			return infos;
		}
	}
}
