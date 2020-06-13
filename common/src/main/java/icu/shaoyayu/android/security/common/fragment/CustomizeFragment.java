package icu.shaoyayu.android.security.common.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author shaoyayu
 * 自定义Fragment
 */
public abstract class CustomizeFragment extends Fragment implements MyFragment {

    protected View mRoot;
    protected String TAG;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TAG = getClass().getName();
        if (mRoot==null){
            int layId = getInterfaceResourceId();
            //初始化当前的根布局，但是不在创建的时候添加到 container 中，
            this.mRoot = inflater.inflate(layId,container,false);
            initTheControl(mRoot);
        }else {
            if (mRoot.getParent()!=null){
                //把当前的root从父布局中移除
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    /**
     * 界面初始化完成
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initControlBinding();
    }

    /**
     * 初始化的数据是不是正确
     * is the initialized data correct
     * @param bundle 携带的参数 parameters carried
     * @return 参数正确返回true parameters return true correctly
     */
    protected void initArgs(Bundle bundle){

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
    protected void initTheControl(View view){

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
     * 返回按键触发时候调用
     * @return true 代表当前的Fragment 已经处理逻辑 activity 不用finish
     *          false 代表没有处理
     */
    public boolean onBackPressed(){
        return false;
    }

    /**
     * 子类可以直接使用的Toast
     * @param text
     */
    protected void showToast(CharSequence text){
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
