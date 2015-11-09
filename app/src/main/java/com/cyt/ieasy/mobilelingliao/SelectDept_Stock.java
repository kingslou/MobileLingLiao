package com.cyt.ieasy.mobilelingliao;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.cyt.ieasy.fragments.DeptFragment;
import com.cyt.ieasy.fragments.StockFragment;
import com.cyt.ieasy.widget.CustomViewPager;
import com.cyt.ieasy.widget.TabBarView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jin on 2015.11.02.
 */
public class SelectDept_Stock extends BaseActivity {

    private TabBarView mTabBarView;
    private int PAGE_COUNT = 2;
    private MainScreenPagerAdapter mMainScreenPagerAdapter;
    public static CustomViewPager mViewPager;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_deptstock);
        ButterKnife.bind(this);
        setToolbar();
        setCustumTab();
        setPageListener();
    }

    void setToolbar(){
        setSupportActionBar(toolbar);
        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //setting TabBarView
        View customTabView = mLayoutInflater.inflate(R.layout.custom_tab_view, null);
        mTabBarView = (TabBarView) customTabView.findViewById(R.id.customTabBar);
        mTabBarView.setStripHeight(3);
        mTabBarView.setStripColor(getResources().getColor(R.color.ms_white));

        //setting the properties of ActionBar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Setting the Customized Toolbar into toolbar object
        toolbar.addView(customTabView);
    }

    void setCustumTab(){
        mMainScreenPagerAdapter = new MainScreenPagerAdapter(getSupportFragmentManager());
        mViewPager = (CustomViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mMainScreenPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);
        //Setting the CustomizedViewPager into Toolbar for tabOption
        mTabBarView.setViewPager(mViewPager);
    }

    void setPageListener(){
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTabBarView.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mTabBarView.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mTabBarView.onPageScrollStateChanged(state);
            }
        });
    }

    public class MainScreenPagerAdapter extends FragmentStatePagerAdapter implements TabBarView.IconTabProvider {


        //Defining the array for Tab icons..which is going to call dynamically and load it into tabBar of toolbar
        private int[] tab_icons = {
                R.mipmap.icon_mm,
                R.mipmap.icon_hisy
        };

        public MainScreenPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        //this method is returning the ref of our fragments
        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return new DeptFragment();
                case 1:
                    return new StockFragment();
                default:
                    return null;
            }
        }

        //returning the number of pages
        @Override
        public int getCount() {
            return PAGE_COUNT;
        }


        //this is TabBarView.IconTabProvider's method to return the position of icon to load into tabBar of Toolbar
        @Override
        public int getPageIconResId(int position) {
            return tab_icons[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "设置部门";
                case 1:
                    return "设置仓库";
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
