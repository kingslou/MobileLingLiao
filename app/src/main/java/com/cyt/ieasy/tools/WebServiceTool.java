package com.cyt.ieasy.tools;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnectionSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * @author
 */
public class WebServiceTool {

    public static final String WebAddress = "http://www.easytimestudio.com";
    public static final String WebServiceNamespace = "http://tempuri.org/";
    public static final int CompanyID = 104;

    public static Handler handler = new Handler();

    public static final String WEB_SERVICE_BASE = "http://${SERVER}:${PORT}/webserver/WebService/BaseService.asmx";

    public static final String WEB_SERVICE_ORDER = "http://${SERVER}:${PORT}/webserver/WebService/OrderService.asmx";

    public static final String WEB_SERVICE_KITCHEN = "http://${SERVER}:${PORT}/webserver/WebService/KitchenService.asmx";

    public static final String WEB_SERVICE_YUDING = "http://${SERVER}:${PORT}/webserver/WebService/YuDingService.asmx";

    public static void init() {
    }

//	public static String getProperUrl(Service service, String server, String port) {
//		String url = "";
//		switch (service) {
//		case BASE:
//			url = WEB_SERVICE_BASE.replace("${SERVER}", server).replace("${PORT}", port);
//			break;
//		case ORDER:
//			url = WEB_SERVICE_ORDER.replace("${SERVER}", server).replace("${PORT}", port);
//			break;
//		case KITCHEN:
//			url = WEB_SERVICE_KITCHEN.replace("${SERVER}", server).replace("${PORT}", port);
//			break;
//		case YUDING:
//			url = WEB_SERVICE_YUDING.replace("${SERVER}", server).replace("${PORT}", port);
//			break;
//		}
//		return url;
//	}

//	public static void callWebserviceAsync(WebServiceCallback callback, Service service, String method,
//			String[] params, Object... values) {
//		callWebserviceAsync(callback, getProperUrl(service, Constants.SERVER, Constants.PORT), method, params, values);
//	}
//
//	public static void callWebserviceAsync(WebServiceCallback callback, Service service, boolean retResult,
//			String method, String[] params, Object... values) {
//		callWebserviceAsync(callback, getProperUrl(service, Constants.SERVER, Constants.PORT), retResult, method,
//				params, values);
//	}

    /**
     * 异步调用WS
     *
     * @param callback
     * @param WebServiceUrl
     * @param method
     * @param params
     * @param values
     */
    public static void callWebserviceAsync(WebServiceCallback callback, String WebServiceUrl,
                                           String method, String[] params, Object... values) {
        callWebserviceAsync(callback, WebServiceUrl, true, method, params, values);
    }

    /**
     * 异步调用WS
     *
     * @param callback
     * @param WebServiceUrl
     * @param retResult     是否有返回�?
     * @param method
     * @param params
     * @param values
     */
    public static void callWebserviceAsync(final WebServiceCallback callback, final String WebServiceUrl, final boolean retResult,
                                           final String method, final String[] params, final Object... values) {
        new Thread(new Runnable() {

            public void run() {
                Object ret = null;
                try {
                    ret = callWebservice(WebServiceUrl, method, params, values);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.fail();
                    callback.fail(e);
                }
                if ((retResult && ret == null) || (ret != null && ret.toString().startsWith("[ERR]"))) {
                    callback.fail();
                    callback.fail(ret);
                    return;
                }
                Log.i("WebServices", "返回值：" + ret);
                try {
                    if (retResult) {
                        callback.success(ret);
                    } else {
                        callback.success();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//					Log.e("WebServices", "" + e.getLocalizedMessage());
                }
            }
        }).start();
    }

    public static Object callWebservice(String WebServiceUrl, String method, String[] params, Object... values) {
        try {
            return callWebservice(false, WebServiceUrl, method, params, values);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    // 调用WebService
    public static Object callWebservice(boolean isThrowEx, String WebServiceUrl, String method, String[] params, Object... values) throws Exception {
        Object result = null;

        Log.i("Call WebServices", WebServiceUrl + "/" + method);

        SoapObject rpc = new SoapObject(WebServiceTool.WebServiceNamespace, method);
        if (params != null) {
            for (int i = 0; i < params.length; i++)
                rpc.addProperty(params[i], values[i]);
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = rpc;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(rpc);


        MyAndroidHttpTransport ht = new MyAndroidHttpTransport(WebServiceUrl);
        ht.debug = true;
        System.setProperty("http.keepAlive", "true");

        String SOAP_ACTION = WebServiceTool.WebServiceNamespace + method;

        try {
            ht.call(SOAP_ACTION, envelope);
            result = envelope.getResponse();
        } catch (IOException e) {
            if (isThrowEx)
                throw e;
            else
                e.printStackTrace();
        } catch (XmlPullParserException e) {
            if (isThrowEx)
                throw e;
            else
                e.printStackTrace();
        } catch (Exception e) {
            if (isThrowEx)
                throw e;
            else
                e.printStackTrace();
        }

        return result;
    }

    // 将WebService调用获得的对象转换成对象
    public static Object toObject(Object obj, Class<?> cls) {
        if (obj == null)
            return null;
        if (obj instanceof String || cls.equals(String.class))
            return obj;

        if (cls.getName().equals("java.util.Date")) {
            try {
                return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(obj.toString().trim().replace('T', ' '));
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return null;
            }
        }

        Object result = null;
        if (!(obj instanceof SoapObject))
            return null;
        try {
            result = cls.newInstance();
            SoapObject so = (SoapObject) obj;
            // System.out.println(so.getNamespace());
            for (int i = 0; i < so.getPropertyCount(); i++) {
                PropertyInfo pinfo = new PropertyInfo();
                so.getPropertyInfo(i, pinfo);

//				System.out.print(pinfo.name);

                Object value = so.getProperty(i);

//				System.out.print("=" + value + "\r\n");

                if (value == null)
                    continue;
                Object returnValue = value;
                Field field = null;
                try {
                    field = cls.getField(pinfo.name);
                } catch (NoSuchFieldException e) {
                    continue;
                }

                String tpName = field.getType().getName();
                //System.out.println("------pinfo.name: " + pinfo.name);
                try {
                    if (tpName.equals("int"))
                        returnValue = Integer.valueOf(returnValue.toString().trim());
                    else if (tpName.equals("short"))
                        returnValue = Short.valueOf(value.toString().trim());
                    else if (tpName.equals("long"))
                        returnValue = Long.valueOf(value.toString().trim());
                    else if (tpName.equals("byte"))
                        returnValue = Byte.valueOf(value.toString().trim());
                    else if (tpName.equals("float"))
                        returnValue = Float.valueOf(value.toString().trim());
                    else if (tpName.equals("double"))
                        returnValue = Double.valueOf(value.toString().trim());
                    else if (tpName.equals("BigInteger"))
                        returnValue = new BigInteger(value.toString().trim());
                    else if (tpName.equals("boolean"))
                        returnValue = Boolean.valueOf(value.toString().trim());
                    else if (tpName.equals("char"))
                        returnValue = value.toString().charAt(0);
                    else if (tpName.equals("java.util.Date")) {
                        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        returnValue = bartDateFormat.parse(value.toString().trim().replace('T', ' '));
                        //System.out.println(pinfo.name +  ":  returnValue:"  + returnValue);

                    } else if (tpName.equals("java.lang.String"))
                        returnValue = value.toString().replace("anyType{}", "");

                    cls.getField(pinfo.name).set(result, returnValue);
                    //System.out.println(cls.getField(pinfo.name).get(result));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将WebService调用获得的对象转换成对象数组
     *
     * @param soapObj
     * @param cls
     * @return
     */
    public static Object[] toObjects(Object soapObj, Class<?> cls) {
        if (soapObj == null)
            return null;
        if (!(soapObj instanceof SoapObject))
            return null;

        SoapObject so = (SoapObject) soapObj;
        int count = so.getPropertyCount();
        Object[] results = new Object[count];
        if (count == 0) {
            System.out.println("对象为空");
            return results;
        }
        for (int i = 0; i < count; i++) {
            //System.out.println(so.getProperty(i));
            results[i] = toObject(so.getProperty(i), cls);
            //System.out.println(results[i]);
        }
        //System.out.println(results.getClass());
        return results;
    }

//	public static List toList(Object obj, Class<?> cls) {
//		if (obj == null)
//			return null;
//		if (!(obj instanceof SoapObject))
//			return null;
//
//		SoapObject so = (SoapObject) obj;
//		int count = so.getPropertyCount();
//		List results = new ArrayList(count);
//		if (count == 0) {
//			System.out.println("对象为空");
//			return results;
//		}
//		for (int i = 0; i < count; i++) {
//			// System.out.println(so.getProperty(i));
//			results.add(toObject(so.getProperty(i), cls));
//			System.out.println(results.get(i).getClass());
//		}
//		return results;
//	}

    /**
     * 将WebService调用获得的对象转换成对象数组
     *
     * @param soapObj
     * @param cls
     * @return
     */
    public static List toObjectList(Object soapObj, Class<?> cls) {
        if (soapObj == null)
            return null;
        if (!(soapObj instanceof SoapObject))
            return null;

        SoapObject so = (SoapObject) soapObj;
        int count = so.getPropertyCount();
        ArrayList objs = new ArrayList();
        for (int i = 0; i < count; i++) {
            objs.add(toObject(so.getProperty(i), cls));
        }
        return objs;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList toObjectList(Object obj) {
        if (obj == null)
            return null;
        if (!(obj instanceof SoapObject))
            return null;

        SoapObject so = (SoapObject) obj;
        int count = so.getPropertyCount();
        ArrayList objs = new ArrayList();
        for (int i = 0; i < count; i++) {
            objs.add(so.getProperty(i));
        }

        return objs;
    }

    //把得到的SoapObject转化为byte[]
    public static byte[] serilizeSoapObject(SoapObject aSoapObject) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(aSoapObject);
        XmlSerializer aSerializer = Xml.newSerializer();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            aSerializer.setOutput(os, "utf-8");
            envelope.write(aSerializer);
            aSerializer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] bytes = os.toByteArray();
        return bytes;
    }


    //将得到的byte[]转化为SoapObject
    public static SoapObject deSerilizeSoapObject(byte[] bytes) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        SoapObject soap = null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            XmlPullParser p = Xml.newPullParser();
            p.setInput(inputStream, "utf-8");
            envelope.parse(p);
            soap = (SoapObject) envelope.bodyIn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soap;
    }

    public static void pingServer(final String address, final int timeoutInSecond, final WebServiceCallback callback) {
        new Thread(new Runnable() {

            public void run() {
                Log.d("", " Ping: " + address);
                try {
                    if (InetAddress.getByName(address).isReachable(timeoutInSecond * 1000)) {
                        callback.success();
                    } else {
                        callback.fail();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.fail();
                }
            }
        }).start();
    }

    /**
     * WS回调
     *
     * @author Administrator
     */
    public static class WebServiceCallback {
        /**
         * WS执行成功
         *
         * @param
         */
        public void success() {
        }

        ;

        /**
         * WS执行成功
         *
         * @param data
         */
        public void success(Object data) {
        }

        ;

        /**
         * WS执行失败
         */
        public void fail() {
        }

        ;

        /**
         * WS执行失败
         */
        public void fail(Object ret) {
        }

        ;

        /**
         * 完成后调用，传递完成结�?
         */
        protected void complete(final boolean isSuccess) {

            handler.post(new Runnable() {
                public void run() {
                    onUIUpdate(isSuccess);
                }
            });
        }

        /**
         * 完成后更新UI
         *
         * @param isSuccess
         */
        public void onUIUpdate(boolean isSuccess) {
        }

        ;
    }


}

class MyAndroidHttpTransport extends HttpTransportSE {
    private int timeout = Const.TIME_OUT * 1000; //默认超时时间为20s

    public MyAndroidHttpTransport(String url) {
        super(url);
    }

    public MyAndroidHttpTransport(String url, int timeout) {
        super(url);
        this.timeout = timeout;
    }

    @Override
    protected org.ksoap2.transport.ServiceConnection getServiceConnection()
            throws IOException {
        ServiceConnectionSE serviceConnection = new ServiceConnectionSE(this.url, timeout);
        return serviceConnection;
    }
}
