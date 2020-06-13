package icu.shaoyayu.android.security.butler.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.presenter.entity.AppInfoEntity;

/**
 * @author shaoyayu
 * 应用信息的见面
 */
public class AppInfoBottomDialog extends BottomDialog {

    private View mView;
    private ImageView ivCloseBottomDialog;
    private TextView tvDlgAppName,tvDlgAppRun,tvDlgAppShare,tvDlgAppUninstall;

    private AppInfoEntity mAppInfo;

    private OnBottomDialogListener onBottomDialogListener;

    public AppInfoBottomDialog(AppInfoEntity appInfoEntity){
        this.mAppInfo = appInfoEntity;
    }

    public void setOnBottomDialogListener(OnBottomDialogListener onBottomDialogListener) {
        this.onBottomDialogListener = onBottomDialogListener;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.dialog_app_info,container,false);
        controlBinding();
        initData();
        setUpListener();
        return mView;
    }

    private void controlBinding(){
        ivCloseBottomDialog = mView.findViewById(R.id.iv_close_bottom_dialog);
        tvDlgAppName = mView.findViewById(R.id.tv_dlg_app_name);
        tvDlgAppRun = mView.findViewById(R.id.tv_dialog_app_run);
        tvDlgAppShare = mView.findViewById(R.id.tv_dialog_app_share);
        tvDlgAppUninstall = mView.findViewById(R.id.tv_dialog_app_uninstall);
    }

    private void initData(){
        tvDlgAppName.setText(mAppInfo.getAppName());
    }

    private void setUpListener(){
        ivCloseBottomDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //注册监听事件
        if (onBottomDialogListener!=null){
            tvDlgAppRun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBottomDialogListener.onAppRun(mAppInfo);
                    dismiss();
                }
            });
            tvDlgAppShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBottomDialogListener.onAppShare(mAppInfo);
                    dismiss();
                }
            });
            tvDlgAppUninstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBottomDialogListener.onAppUninstall(mAppInfo);
                    dismiss();
                }
            });
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface OnBottomDialogListener{
        void onAppRun(AppInfoEntity appInfoEntity);
        void onAppShare(AppInfoEntity appInfoEntity);
        void onAppUninstall(AppInfoEntity appInfoEntity);
    }
}
