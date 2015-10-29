package com.cyt.ieasy.mobilelingliao;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cyt.ieasy.widget.MyGridLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.mygrildlist) MyGridLayout grildlayout;
    @Bind(R.id.toolbar) Toolbar toolbar;
    private TextDrawable.IBuilder mDrawableBuilder;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar(toolbar);
        initMenu();
        setTitle("手机领料");
    }

    void initMenu() {
        grildlayout = (MyGridLayout) findViewById(R.id.mygrildlist);
        Resources res = getResources();
        final String[] menuItems = res.getStringArray(R.array.MenuItem);
        TypedArray ar = res.obtainTypedArray(R.array.Itemicon);
        int len = ar.length();
        final int[] resIds = new int[len];
        for (int i = 0; i < resIds.length; i++) {
            resIds[i] = ar.getResourceId(i, 0);
        }
        mDrawableBuilder = TextDrawable.builder().roundRect(10);

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
                switch (index) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            //// TODO: 2015.10.28 要求输入密码才能进去程序设置
            return true;
        } else {
            new AlertDialogWrapper.Builder(this)
                    .setTitle("退出")
                    .setMessage("确定退出吗")
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
