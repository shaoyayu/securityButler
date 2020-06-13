package icu.shaoyayu.android.security.butler.activity;

import android.Manifest;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.adapter.GuidePageAdapter;
import icu.shaoyayu.android.security.butler.fragment.GuideAntiTheftOneFragment;
import icu.shaoyayu.android.security.butler.fragment.GuideAntiTheftThreeFragment;
import icu.shaoyayu.android.security.butler.fragment.GuideAntiTheftTwoFragment;
import icu.shaoyayu.android.security.butler.view.SimpleMenu;
import icu.shaoyayu.android.security.common.activity.CustomizeActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author shaoyayu
 * 引导防盗功能
 * 权限注册，获取SIM卡权限等
 */
public class GuideAntiTheftActivity extends CustomizeActivity implements ViewPager.OnPageChangeListener, EasyPermissions.PermissionCallbacks {

    private SimpleMenu smBootAntiTheft;

    private ViewPager mViewPager;
    private GuidePageAdapter mPageAdapter;
    private List<Fragment> mFragments;
    private LinearLayout mDotLayout;
    private ImageView[] dots;
    private int currentIndex;
    private GuideAntiTheftThreeFragment contactPersonFragment;
    public static final int PERMISSION_STORAGE_CODE = 10002;
    public static final String PERMISSION_STORAGE_MSG = "手机手机防盗涉及的用户权限";
    public static final String[] PERMISSION_STORAGE = new String[]{Manifest.permission.READ_PHONE_STATE    //读取SIM卡读取权限
            ,Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS,Manifest.permission.GET_ACCOUNTS,  //读取联系人
            Manifest.permission.RECEIVE_BOOT_COMPLETED,//监听手机开机事件 receiver
            Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void initWindows() {
        Log.d(TAG,"启动");
    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_guide_anti_theft;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();
        smBootAntiTheft = findViewById(R.id.sm_boot_anti_theft);
        mViewPager =  findViewById(R.id.guide_view_pager);
        mDotLayout =  findViewById(R.id.dots_layout);
        contactPersonFragment = GuideAntiTheftThreeFragment.newInstance();
    }

    @Override
    protected void initData() {
        super.initData();
        smBootAntiTheft.setTvMenuThemeText("开启防盗功能");
        mFragments = new ArrayList<Fragment>();
        mFragments.add(GuideAntiTheftOneFragment.newInstance());
        mFragments.add(GuideAntiTheftTwoFragment.newInstance());
        mFragments.add(contactPersonFragment);
        mPageAdapter = new GuidePageAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mPageAdapter);
        initDots();
    }

    @Override
    protected void initControlBinding() {
        super.initControlBinding();
        mViewPager.addOnPageChangeListener(this);
        initSimple();
    }

    // 当前页面被滑动时调用
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int position) {

        // 设置底部小点选中状态
        setCurrentDot(position);
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initDots() {

        dots = new ImageView[mFragments.size()];
        for (int i = 0; i < mFragments.size(); i++) {
            dots[i] = (ImageView) mDotLayout.getChildAt(i);
            if (i != 0) {
                dots[i].setImageResource(R.drawable.ic_circle_dot);
            }
        }
        currentIndex = 0;
        dots[currentIndex].setImageResource(R.drawable.ic_rect_dot);
    }

    private void setCurrentDot(int position) {

        if (position < 0 || position > mFragments.size() - 1
                || currentIndex == position) {
            return;
        }
        dots[position].setImageDrawable(null);
        dots[position].setImageResource(R.drawable.ic_rect_dot);
        dots[currentIndex].setImageDrawable(null);
        dots[currentIndex].setImageResource(R.drawable.ic_circle_dot);
        currentIndex = position;
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        return EasyPermissions.hasPermissions(context, permissions);
    }

    /**
     * @param context
     * @return
     */
    public static boolean hasStoragePermission(Context context) {
        return hasPermissions(context, PERMISSION_STORAGE);
    }

    @AfterPermissionGranted(PERMISSION_STORAGE_CODE)
    public void initSimple() {
        if (hasStoragePermission(getApplicationContext())) {
            //有权限
        } else {
            //申请权限
            EasyPermissions.requestPermissions(this, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMISSION_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        Log.d(TAG,"权限申请成功");
    }

    //失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        showToast("请到设置上设置权限获取");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        contactPersonFragment.updateView();
    }
}
