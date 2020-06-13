package icu.shaoyayu.android.security.butler.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import icu.shaoyayu.android.security.butler.R;


/**
 * @author shaoyayu
 * 自定义的简约菜单键
 */
public class SimpleMenu extends FrameLayout {

    private TextView tvMenuBacktrack,tvMenuTheme,tvMenuMore;
    //跟多的URL地址，打开一个WebView显示当前Activity的介绍
    private String url;

    private OnMoreClickListener onMoreClickListener;

    private OnReturnClickListener onReturnClickListener;

    public SimpleMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_simple_menu,this);
        tvMenuBacktrack = findViewById(R.id.tv_menu_back);
        tvMenuTheme = findViewById(R.id.tv_menu_theme);
        tvMenuMore = findViewById(R.id.tv_menu_more);
        //返回监听器
        tvMenuBacktrack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onReturnClickListener!=null){
                    onReturnClickListener.onReturnClickListener(v);
                }else {
                    ((Activity) getContext()).finish();
                }
            }
        });
        //更多跳转到WebView见面上去
        if (onMoreClickListener!=null){
            tvMenuMore.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMoreClickListener.onMoreClickListener(v);
                }
            });
        }
    }

    public void setComeBackText(CharSequence text){
        tvMenuBacktrack.setText(text);
    }

    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener) {
        this.onMoreClickListener = onMoreClickListener;
    }

    public void setOnReturnClickListener(OnReturnClickListener onReturnClickListener) {
        this.onReturnClickListener = onReturnClickListener;
    }

    public void setTvMenuThemeText(CharSequence tvMenuThemeText) {
        this.tvMenuTheme.setText(tvMenuThemeText);
    }

    public void seteMoreTitles(CharSequence tx){
        this.tvMenuMore.setText(tx);
    }

    //设置一个更多的点击事件回调
    public interface OnMoreClickListener{
        void  onMoreClickListener(View view);
    }

    //设置一个返回调用的监听
    public interface OnReturnClickListener{
        void onReturnClickListener(View view);
    }
}
