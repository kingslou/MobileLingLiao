package com.cyt.ieasy.tools;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
}
