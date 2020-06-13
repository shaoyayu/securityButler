package icu.shaoyayu.android.security.butler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.common.bean.SecurityFunctionBean;

/**
 * @author shaoyayu
 *
 */
public class SecurityFunctionAdapter extends RecyclerView.Adapter<SecurityFunctionAdapter.SecurityFunctionItem> {

    private Context context;

    private List<SecurityFunctionBean> securityFunctions;

    private ItemOnClickListener itemOnClickListener;

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public SecurityFunctionAdapter(Context context, List<SecurityFunctionBean> securityFunctions) {
        this.context = context;
        this.securityFunctions = securityFunctions;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public SecurityFunctionItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_function_item,parent,false);
        return new SecurityFunctionItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SecurityFunctionItem holder, final int position) {
        holder.ivFunctionIcon.setImageResource(securityFunctions.get(position).getIcoAddress());
        holder.tvFunctionName.setText(securityFunctions.get(position).getFunctionName());
        holder.tvFunctionInfo.setText(securityFunctions.get(position).getFunctionInfo());
        if (securityFunctions.get(position).getIcoWarning()!=0){
            holder.ivWarning.setImageResource(securityFunctions.get(position).getIcoWarning());
        }
        if (itemOnClickListener!=null){
            holder.clFunctionalModule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClickListener.itemOnClick(v,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return securityFunctions.size();
    }

    class SecurityFunctionItem extends RecyclerView.ViewHolder {
        ConstraintLayout clFunctionalModule;
        ImageView ivFunctionIcon,ivWarning;
        TextView tvFunctionName,tvFunctionInfo;
        public SecurityFunctionItem(@NonNull View itemView) {
            super(itemView);
            clFunctionalModule = itemView.findViewById(R.id.cl_functional_module);
            ivFunctionIcon = itemView.findViewById(R.id.iv_function_icon);
            ivWarning = itemView.findViewById(R.id.iv_warning);
            tvFunctionName = itemView.findViewById(R.id.tv_function_name);
            tvFunctionInfo = itemView.findViewById(R.id.tv_function_info);
        }
    }

    public interface ItemOnClickListener{
        void itemOnClick(View view,int position);
    }

}
