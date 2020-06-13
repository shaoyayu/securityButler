package icu.shaoyayu.android.security.common.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

import icu.shaoyayu.android.security.common.fragment.CustomizeFragment;


/**
 * @author shaoyayu
 * Anima
 */
public abstract class CustomizeActivity extends AppCompatActivity implements MyActivityBase {

    protected String  TAG ;

    /**
     * 防止重写
     * @param savedInstanceState
     */
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getName();
        //初始化窗口
        initWindows();
        if(initArgs(getIntent().getExtras())){
            //初始化数据成功 initialized data successfully
            int layId = getInterfaceResourceId();
            setContentView(layId);
            initTheControl();
            initData();
            initControlBinding();
        }else {
            //
            finish();
        }

    }

    /**
     * 初始化窗口
     * initialization window
     */
    protected abstract void initWindows();

    /**
     * 初始化的数据是不是正确
     * is the initialized data correct
     * @param bundle 携带的参数 parameters carried
     * @return 参数正确返回true parameters return true correctly
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 获取见面的资源id
     * get meeting resource id
     * @return
     */
    protected abstract int getInterfaceResourceId();

    /**
     * 初始化控件
     * Initialize the control
     */
    protected void initTheControl(){

    }


    /**
     * 初始化数据
     * initialization data
     */
    protected void initData(){

    }

    /**
     * 初始化控件的事件绑定
     * initialize the event binding of the control
     */
    protected void initControlBinding(){

    }

    /**
     * 导航上的返回事件
     * return event on navigation
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 主要管理activity上的fragment的生命周期
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //得到当前activity下面的所有fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments!=null&&fragments.size()!=0){
            for (Fragment fragment : fragments) {
                //是不是自定义的fragment
                if (fragment instanceof CustomizeFragment){
                    //是否拦截了返回按钮的事假
                    if (((CustomizeFragment) fragment).onBackPressed()){
                        return;
                    }
                }
            }
        }
        finish();
    }

    /**
     * 子类可以直接使用的Toast
     * @param text
     */
    protected void showToast(CharSequence text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
