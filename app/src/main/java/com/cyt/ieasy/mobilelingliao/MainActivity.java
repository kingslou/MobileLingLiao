package com.cyt.ieasy.mobilelingliao;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bigkoo.pickerview.OptionsPopupWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.db.DeptTableUtil;
import com.cyt.ieasy.db.StockTableUtil;
import com.cyt.ieasy.tools.CommonTool;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.MyToast;
import com.cyt.ieasy.tools.StringUtils;
import com.cyt.ieasy.tools.TimeUtils;
import com.cyt.ieasy.widget.MyGridLayout;
import com.ieasy.dao.Dept_Table;
import com.ieasy.dao.WUZI_STOCK;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.mygrildlist) MyGridLayout grildlayout;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.loginName)
    TextView loginname;
    @Bind(R.id.loginDept)
    TextView logindept;
    private TextDrawable.IBuilder mDrawableBuilder;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private static boolean isExit = false;
    private final static int MESSAGE_EXIT = 0x00001;
    private TimePopupWindow pwTime;
    private OptionsPopupWindow optionsPopupWindow;
    private List<Dept_Table> dept_tableList;
    private List<WUZI_STOCK> stockList;
    private List<String> deptlist;
    private View view;
    private String CK_NAME;//选择的仓库名称
    private String DEPT_NAME;//选择的部门名称
    private String SELECT_TIME;//选择的领料时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar(toolbar);
        initMenu();
        initTime();
        setTitle("手机领料");
    }

    void initMenu() {
        loginname.setText(CommonTool.getGlobalSetting(context,Const.cachuser));
        logindept.setText(CommonTool.getGlobalSetting(context,Const.cachuserdept));
        grildlayout = (MyGridLayout) findViewById(R.id.mygrildlist);
        Resources res = getResources();
        final String[] menuItems = res.getStringArray(R.array.MenuItem);
        TypedArray ar = res.obtainTypedArray(R.array.Itemicon);
        int len = ar.length();
        final int[] resIds = new int[len];
        for (int i = 0; i < resIds.length; i++) {
            resIds[i] = ar.getResourceId(i, 0);
        }
        //方形
        //mDrawableBuilder = TextDrawable.builder().roundRect(10);
        //圆形
        mDrawableBuilder = TextDrawable.builder().round();
        grildlayout.setGridAdapter(new MyGridLayout.GridAdatper() {
            @Override
            public View getView(int index) {
                View view = getLayoutInflater().inflate(R.layout.menu_item, null);
                TextDrawable drawable = mDrawableBuilder.build(String.valueOf(menuItems[index].charAt(0)), mColorGenerator.getColor(menuItems[index]));
                TextView title = (TextView) view.findViewById(R.id.tv);
                ImageView image = (ImageView) view.findViewById(R.id.iv);
                title.setText(menuItems[index]);
//                image.setImageResource(resIds[index]);
                image.setImageDrawable(drawable);
                return view;
            }

            @Override
            public int getCount() {
                return menuItems.length;
            }
        });
        grildlayout.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int index) {
                Intent intent = new Intent();
                view = v;
                switch (index) {
                    case 0:
                        pwTime.showAtLocation(v, Gravity.BOTTOM, 0, 0, new Date());
                        break;
                    case 1:
                        startActivity(SelectDept_Stock.class, false);
                        break;
                    case 2:
                        startActivity(HistoryActivity.class,false);
                        break;
                    case 3:
                        new MaterialDialog.Builder(context)
                                .title("注销登录")
                                .content("确定注销登录吗?")
                                .positiveText(R.string.agree)
                                .negativeText(R.string.disagree)
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                        materialDialog.dismiss();
                                    }
                                })
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                        Intent intent = new Intent();
                                        intent.setClass(MainActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        materialDialog.dismiss();
                                        finish();
                                    }
                                })
                                .show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    void initTime(){
        pwTime = new TimePopupWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setTime(new Date());
        //时间选择后回调
        pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                MyLogger.showLogWithLineNum(5, "data" + date + "格式化后" + TimeUtils.getDateStr(date));
                SELECT_TIME = TimeUtils.getDateStr(date);
                String deptConfig = CommonTool.getGlobalSetting(MainActivity.this, Const.editBm);
                if (!StringUtils.isBlank(deptConfig)&&deptConfig.equals("true")) {
                    initDept();
                } else {
                    initStock();
                }
                optionsPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        });
    }

    void initDept(){
        dept_tableList = DeptTableUtil.getDeptTableUtil().getAlldata();
        if(dept_tableList.isEmpty()){
            
            return;
        }
        optionsPopupWindow = new OptionsPopupWindow(MainActivity.this);
        final ArrayList<String> nameList = new ArrayList<>();
        String deptConfig = CommonTool.getGlobalSetting(MainActivity.this,Const.editBm);
        if(!StringUtils.isBlank(deptConfig)){
            String deptstr = CommonTool.getGlobalSetting(MainActivity.this, Const.dept_filter);
            if(!StringUtils.isBlank(deptstr)){
                List<Dept_Table> filteDeptList = new ArrayList<>();
                String [] depts = deptstr.split(",");
                for(String s:depts){
                    for(Dept_Table deptTable : dept_tableList){
                        if(s.equals(deptTable.getINNERID())){
                            filteDeptList.add(deptTable);
                            nameList.add(deptTable.getDEPTNAME());
                        }
                    }
                }
            }else{
                for(Dept_Table deptTable : dept_tableList){
                    nameList.add(deptTable.getDEPTNAME());
                }
            }
        }else{
            for(Dept_Table deptTable : dept_tableList){
                nameList.add(deptTable.getDEPTNAME());
            }
        }
        optionsPopupWindow.setPicker(nameList);
        optionsPopupWindow.setSelectOptions(0);
        optionsPopupWindow.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int i, int i1, int i2) {
                MyLogger.showLogWithLineNum(5, nameList.get(i) + "打印");
                DEPT_NAME = nameList.get(i);
                optionsPopupWindow=null;
                initStock();
                optionsPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        });
    }

    void initStock(){
        stockList = StockTableUtil.getStockTableUtil().getAlldata();
        optionsPopupWindow = new OptionsPopupWindow(MainActivity.this);
        final ArrayList<String> nameList = new ArrayList<>();
        String stockstr = CommonTool.getGlobalSetting(MainActivity.this, Const.stock_filter);
        if(!StringUtils.isBlank(stockstr)){
            List<WUZI_STOCK> filteDeptList = new ArrayList<>();
            String [] depts = stockstr.split(",");
            for(String s:depts){
                for(WUZI_STOCK wuzi_stock : stockList){
                    if(s.equals(wuzi_stock.getCK_ID())){
                        filteDeptList.add(wuzi_stock);
                        nameList.add(wuzi_stock.getCK_NAME());
                    }
                }
            }
        }else{
            for(WUZI_STOCK wuzi_stock : stockList){
                nameList.add(wuzi_stock.getCK_NAME());
            }
        }
        optionsPopupWindow.setPicker(nameList);
        optionsPopupWindow.setSelectOptions(0);
        optionsPopupWindow.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int i, int i1, int i2) {
                String selectName = nameList.get(i);
                CK_NAME = selectName;
                Bundle bundle = new Bundle();
                bundle.putString(Const.intent_ckname,CK_NAME);
                bundle.putString(Const.intent_deptname,DEPT_NAME);
                bundle.putString(Const.intent_time,SELECT_TIME);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this,MuBanActivity.class);
                startActivity(intent);
            }
        });
    }

    // 创建Handler对象，用来处理消息
    static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// 处理消息
            super.handleMessage(msg);
            if (msg.what == MESSAGE_EXIT)
                isExit = false;
        }
    };

    private void ToQuitTheApp() {
        if (isExit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);// 使虚拟机停止运行并退出程序
        } else {
            isExit = true;
            MyToast.tostpic(MainActivity.this, "再按一次返回键退出程序");
            mHandler.sendEmptyMessageDelayed(MESSAGE_EXIT, 3000);// 3秒后发送消息
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean result = false;
        if(keyCode==KeyEvent.KEYCODE_BACK){
            ToQuitTheApp();
            result = true;
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else {
            showback();
        }
        return super.onOptionsItemSelected(item);
    }
}
