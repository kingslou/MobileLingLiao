package com.cyt.ieasy.tools;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.cyt.ieasy.mobilelingliao.R;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jin on 2015.10.26.
 */
public class CommonTool {

    public static boolean isWifiOK(Context context) {

        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String join(Iterator<String> strings, String sep) {
        if (!strings.hasNext()) return "";

        String start = strings.next();
        if (!strings.hasNext()) // only one, avoid builder
            return start;

        StringBuilder sb = new StringBuilder(64).append(start);
        while (strings.hasNext()) {
            sb.append(sep);
            sb.append(strings.next());
        }
        return sb.toString();
    }

    public static String getFileName(String path) {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        return fileName;
    }

    public static String getLocalFilePath(String fileName, Context context) {
        return context.getFilesDir() + "/" + fileName;
    }

    public static Bitmap getImageFromFile(String fileName, Context context) {
        Bitmap image = null;
        try {
            InputStream is = context.openFileInput(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static List extractFromEntityList(List list, String propName) {
        List result = new ArrayList();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Object ent = it.next();
            try {
                Field f = ent.getClass().getDeclaredField(propName);
                result.add(f.get(ent));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String MD5(String signature) throws RuntimeException {
        return MD5(signature, true);
    }

    public static String MD5(String signature, boolean isUpperCase) throws RuntimeException {
        if (signature == null) {
            return null;
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密算法错误");
        }
        String ret = null;
        byte[] plainText = signature.getBytes();
        md5.update(plainText);
        ret = bytes2HexString(md5.digest(), isUpperCase);
        return ret;
    }

    public static String bytes2HexString(byte[] bytes, boolean isUpperCase) {
        String hs = "";
        if (bytes != null) {
            for (byte b : bytes) {
                String tmp = (Integer.toHexString(b & 0XFF));
                if (tmp.length() == 1) {
                    hs += "0" + tmp;
                } else {
                    hs += tmp;
                }
            }
        }
        if (isUpperCase) {
            return hs.toUpperCase();
        } else {
            return hs;
        }
    }

    public static String getStatusName(Class clazz, int value) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            try {
                if (f.getInt(null) == value) {
                    return f.getName();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return "N/A";
    }

    public static int bytes2int(byte b[]) {
        return ((((b[0] & 0xff) << 8 | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8 | (b[3] & 0xff);
    }

    /**
     * 获取手机mac地址<br/>
     * 错误返回12个0
     */
    public static String getMacAddress(Context context) {
        // 获取mac地址：
        String macAddress = "000000000000";
        try {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress()))
                    macAddress = info.getMacAddress().replace(":", "");
                else
                    return macAddress;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return macAddress;
        }
        return macAddress;
    }

    /**
     * 根据传入的手机号码加*处理
     *
     * @param str
     * @return
     */
    public static String addstar(String str) {
        String result = str;
        // 电话号码中间四个加*表示
        if (!result.equals("") && result.length() > 7) {
            int midlength = result.length() % 2;
            // 奇数
            if (midlength == 1) {
                int length = result.length() / 2;
                result =
                        result.substring(0, length - 2) + "****"
                                + result.substring(length + 2, result.length());
            } else {
                int length = (result.length() + 1) / 2;
                result =
                        result.substring(0, length - 2) + "****"
                                + result.substring(length + 2, result.length());
            }
        }
        return result;
    }

    /**
     * 保留两个小数显示
     *
     * @param num
     * @return
     */
    public static String getFormat(double num) {

        String numformat = new java.text.DecimalFormat("0.00").format(num);
        return numformat;
    }

    /** 四舍五入保留两位小数
     * @param num
     * @return
     */
    public static String getFormatTwo(double num){
        String result = String.format("%.2f", num);
        return result;
    }

    public static String getFormatint(double num) {

        String numformat = new java.text.DecimalFormat("0").format(num);
        return numformat;
    }

    // UUID 生成主键
    public static String NewGuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static byte[] convertStringToByteArray(String paramString) {
        int i = paramString.length();
        byte[] arrayOfByte = new byte[i / 2];
        for (int j = 0; j < i; j += 2) {
            int k = Integer.parseInt(paramString.substring(j, j + 1), 16);
            int m = Integer.parseInt(paramString.substring(j + 1, j + 2), 16);
            arrayOfByte[(j / 2)] = (byte) (0xFF & m + (k << 4));
        }
        return arrayOfByte;
    }

    public static boolean isIpv4(String ipAddress) {

        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();

    }

    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        return imei;
    }

    public static int getProcessId(Context ctx, String pkgName) {
        ActivityManager actManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> processes = actManager.getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> it = processes.iterator();
        while (it.hasNext()) {
            ActivityManager.RunningAppProcessInfo psinfo = it.next();
            // Log.d("andex", psinfo.pid + "");
            // Log.d("andex", psinfo.processName);
            for (int i = 0; i < psinfo.pkgList.length; i++) {
                // Log.d("andex", "  " + psinfo.pkgList[i]);
                if (psinfo.pkgList[i].equals(pkgName)) {
                    return psinfo.pid;
                }
            }
        }
        return 0;
    }


    public static void killProcess(Context ctx, String pkgName) {
        ActivityManager actManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        // version 1.5 - 2.1
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            actManager.restartPackage(pkgName);
        }
        // version 2.2+
        else {
            actManager.killBackgroundProcesses(pkgName);
        }
    }

    /**
     * Compare target version with APP version.
     *
     * @param ctx
     * @param packageName
     * @param targetVersion
     * @return Return 1 if target version higher than app version, return -1 if lower, return 0 if
     *         equals.
     */
    public static int compareAppVersion(Context ctx, String packageName, String targetVersion) {
        int[] appVersion = getAppVersion(ctx, packageName);
        StringTokenizer token = new StringTokenizer(targetVersion, ".");
        for (int i = 0; token.hasMoreTokens(); i++) {
            int v = Integer.parseInt(token.nextToken().toString());
            if (v > appVersion[i]) {
                return 1;
            } else if (v < appVersion[i]) {
                return -1;
            } else {
                continue;
            }
        }
        return 0;
    }

    public static Drawable getAppIcon(Context ctx, String pkgName) {
        // PackageInfo pi = ctx.getPackageManager().getPackageInfo(pkgName,
        // PackageManager.GET_ACTIVITIES);
        try {
            return ctx.getPackageManager().getApplicationIcon(pkgName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return ctx.getResources().getDrawable(R.mipmap.bg);
        }
    }

    /**
     * Retrieve APP displaying name by it's package name.
     *
     * @param ctx
     * @param pkgName
     * @return
     */
    public static String getAppName(Context ctx, String pkgName) {
        try {
            ApplicationInfo appinfo =
                    ctx.getPackageManager().getApplicationInfo(pkgName, PackageManager.GET_ACTIVITIES);
            if (appinfo == null) {
                return pkgName;
            }
            return ctx.getPackageManager().getApplicationLabel(appinfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return pkgName;
        }
    }

    /**
     * Retrieve all installed APPs.
     *
     * @param ctx
     * @return
     */
    public static List getInstalledApps(Context ctx) {
        List<PackageInfo> pkgs =
                ctx.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
        List ret = new ArrayList();
        for (Iterator it = pkgs.iterator(); it.hasNext();) {
            PackageInfo pkg = (PackageInfo) it.next();
            String packageName = pkg.packageName;
            String appName;
            Drawable icon;
            try {
                if ((pkg.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    continue;
                }
                appName = ctx.getPackageManager().getApplicationLabel(pkg.applicationInfo).toString();
                icon = ctx.getPackageManager().getApplicationIcon(packageName);
                ret.add(new Object[] {packageName, appName, icon});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获取应用程序的版本号
     *
     * @param ctx
     * @param packageName
     * @return 版本数组（长度取决于版本信息�?
     */
    public static int[] getAppVersion(Context ctx, String packageName) {
        List<PackageInfo> pkgs = ctx.getPackageManager().getInstalledPackages(0);
        for (Iterator it = pkgs.iterator(); it.hasNext();) {
            PackageInfo pi = (PackageInfo) it.next();
            if (pi.packageName.equals(packageName)) {
                StringTokenizer token = new StringTokenizer(pi.versionName, ".");
                int[] result = new int[token.countTokens()];
                for (int i = 0; token.hasMoreTokens(); i++) {
                    result[i] = Integer.parseInt(token.nextToken().toString());
                }
                return result;
            }
        }
        return null;
    }

    /**
     *
     * @param ctx
     * @param packageName
     * @return
     */
    public static String getAppVersionString(Context ctx, String packageName) {
        List<PackageInfo> pkgs = ctx.getPackageManager().getInstalledPackages(0);
        for (Iterator it = pkgs.iterator(); it.hasNext();) {
            PackageInfo pi = (PackageInfo) it.next();
            if (pi.packageName.equals(packageName)) {
                return pi.versionName;
            }
        }
        return null;
    }

    /**
     * Version of current APP.
     *
     * @param ctx
     * @return
     */
    public static String getAppVersion(Context ctx) {
        try {
            PackageInfo pkgInfo;
            pkgInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            return pkgInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0.0";
        }
    }

    /**
     * Divide screen height to grid.
     *
     * @param ctx
     * @param gridHeight
     * @param skipHeight
     * @return
     */
    public static int divideScreenHeight(Context ctx, int gridHeight, int skipHeight) {
        int screenH = getScreenHeight(ctx);
        int contentHeight = screenH - skipHeight;
        double spacing = ((contentHeight / 160.0) * (contentHeight / 160.0));
        return (int) Math.round(contentHeight / (gridHeight + spacing));
    }

    /**
     * Divide screen width to grid.
     *
     * @param ctx
     * @param gridWidth
     * @param skipWidth
     * @param gridHeight
     * @param skipHeight
     * @param spacing
     * @return
     */
    public static int divideScreenWidth(Context ctx, int gridWidth, int skipWidth, int gridHeight,
                                        int skipHeight, int spacing) {
        MyLogger.showLogWithLineNum(2, "Screen: " + getScreenWidth(ctx) + "X"
                + getScreenHeight(ctx));
        int result =
                (int) Math.round(getScreenWidth(ctx) / gridWidth)
                        * (int) Math.round((getScreenHeight(ctx) - skipHeight) / gridHeight);
        MyLogger.showLogWithLineNum(2,
                "Cols: " + Math.round(getScreenWidth(ctx) / gridWidth) + ", Rows: "
                        + (int) Math.round((getScreenHeight(ctx) - skipHeight) / gridHeight));
        return result;
    }

    public static int getScreenWidth(Context ctx) {
        DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context ctx) {
        DisplayMetrics dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 保存全局设置
     *
     * @param ctx
     * @param name
     * @param value
     */
    public static void saveGlobalSetting(Context ctx, String name, Object value) {
        SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
        setting.edit().putString(name, value.toString()).commit();
    }

    /**
     * Get global setting from system, return specified default value if not exists.
     *
     * @param ctx
     * @param name
     * @param defaultValue
     * @return
     */
    public static String getGlobalSetting(Context ctx, String name, Object defaultValue) {
        SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
        if (setting == null) {
            return defaultValue.toString();
        }
        if (setting.getString(name, defaultValue.toString()) == null) {
            return null;
        } else {
            return setting.getString(name, defaultValue.toString()).trim();
        }
    }

    /**
     * Get global setting.
     *
     * @param ctx
     * @param name
     * @return
     */
    public static String getGlobalSetting(Context ctx, String name) {
        SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
        if (setting.getString(name, null) == null) {
            return null;
        } else {
            return setting.getString(name, null).trim();
        }
    }

    /**
     *
     * @param ctx
     * @param prefix
     * @return
     */
    public static Map<String, Object> getGlobalSettingsWithPrefix(Context ctx, String prefix) {
        SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
        Map result = new HashMap<String, Object>();
        Map m = setting.getAll();
        for (Iterator it = m.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            if (key.startsWith(prefix) && m.get(key) != null) {
                result.put(key, m.get(key));
            }
        }
        return result;
    }

    /**
     *
     * @param ctx
     * @param key
     * @return
     */
    public static boolean removeGlobalSetting(Context ctx, String key) {
        SharedPreferences setting = ctx.getSharedPreferences(ctx.getPackageName(), 0);
        return setting.edit().remove(key).commit();
    }

    public static boolean checkObj(Object object){
        boolean result = false;
        if(null!=object&&!object.toString().equals("anyType{}")){
            result = true;
        }
        return result;
    }
}
