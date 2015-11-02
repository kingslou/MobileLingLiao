package com.cyt.ieasy.tools;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cyt.ieasy.mobilelingliao.R;

public class MyToast {
  
  
  /** 带图片的自定义Toast
   * @param context
   * @param message
   */
  
  public static void tostpic(Activity context,String message){
    LayoutInflater inflater = context.getLayoutInflater();
    View layout=inflater.inflate(R.layout.layout_tost, null);
    TextView txtmessage = (TextView)layout.findViewById(R.id.tostcontent);
    txtmessage.setText(message);
    Toast toast = new Toast(context);
    toast.setDuration(Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER, 0, 0); 
    toast.setView(layout);
    toast.show();
  }
  
  /** 普通长时间Toast
   * @param context
   * @param message
   */
  public static void toastlong(Activity context,String message){
    Toast toast = new Toast(context);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER, 0, 0); 
    toast.show();
    toast.setText(message);
  }
  /** 普通短时间Toast
   * @param context
   * @param message
   */
  public static void toast(Activity context,String message){
    Toast toast = new Toast(context);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER, 0, 0); 
    toast.show();
    toast.setText(message);
  }
}
