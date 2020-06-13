package icu.shaoyayu.android.security.butler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.common.bean.AddressBookBean;
import icu.shaoyayu.android.security.presenter.service.AddressBookServiceImpl;

/**
 * @author shaoyayu
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewItem>{

    private Context context;
    private List<AddressBookBean> addressBooks,selectedContact;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<Integer> selectedPositions;
    private AddressBookServiceImpl addressBookService;
    public ContactAdapter(Context context, List<AddressBookBean> addressBooks, List<AddressBookBean> selectedContact) {
        if (this.addressBookService==null){
            this.addressBookService = new AddressBookServiceImpl(context);
        }
        initData(context,addressBooks,selectedContact);
    }

    void initData(Context context, List<AddressBookBean> addressBooks, List<AddressBookBean> selectedContact){
        this.selectedPositions = new ArrayList<>();
        this.context = context;
        this.addressBooks = addressBooks;
        if (addressBooks==null){
            return;
        }
        if (selectedContact==null||selectedContact.size()==0){
            this.selectedContact = new ArrayList<>();
            for (int i = 0; i < addressBooks.size(); i++) {
                selectedPositions.add(i,0);
            }
        }else {
            this.selectedContact = selectedContact;
            //标记已经被选中的号码
            for (int i = 0; i < addressBooks.size(); i++) {
                int is = 0;
                for (int j = 0; j < selectedContact.size(); j++) {
                    boolean equals = addressBooks.get(i).getPhone().equals(selectedContact.get(j).getPhone());
                    if (equals){
                        //被选中
                        is = 1;
                    }
                }
                selectedPositions.add(i,is);
            }
        }
    }
    /**
     *
     * @param context
     * @param addressBooks
     * @param selectedContactStr shaoyayu:121212-post:12121514
     */
    public ContactAdapter(Context context, List<AddressBookBean> addressBooks, String selectedContactStr) {
        this.addressBookService = new AddressBookServiceImpl(context);
        List<AddressBookBean> bookBeans = addressBookService.strToLisBran(selectedContactStr);
        initData(context,addressBooks,bookBeans);
    }


    @NonNull
    @Override
    public ContactViewItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_address_boot_item,parent,false);
        return new ContactViewItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactViewItem holder, final int position) {
        //判断是不是被选中
        if (selectedPositions.get(position)==1){
            holder.cbAdpChooseContact.setChecked(true);
        }
        holder.tvAdpContactName.setText(addressBooks.get(position).getName());
        holder.tvAdpContactNumber.setText(addressBooks.get(position).getPhone());
        //点击回调
        holder.clContactEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变选择的状态
                boolean select = holder.cbAdpChooseContact.isChecked();
                holder.cbAdpChooseContact.setChecked(!select);
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClickListener(v,position);
                }
            }
        });
        if (onItemLongClickListener!=null){
            holder.clContactEntry.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //回调过后尽量的返回一个 true
                    return onItemLongClickListener.onItemLongClickListener(v,position,getSelectedContact());
                }
            });
        }
        holder.cbAdpChooseContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    //选择联系人
                    addChooseContact(holder,position);
                }else {
                    //取消选择联系人
                    deleteChooseContact(holder,position);
                }
            }
        });
    }

    public List<AddressBookBean> getSelectedContact() {
        return selectedContact;
    }

    public String getSelectedContactString() {
        return addressBookService.listBranToStr(selectedContact);
    }

    private void addChooseContact(ContactViewItem item,int pos){
        //判断是否含有相同的
        for (int i = 0; i < selectedContact.size(); i++) {
            boolean equals = selectedContact.get(i).getPhone().equals(item.tvAdpContactNumber.getText().toString());
            if (equals){
                return;
            }
        }
        selectedContact.add(new AddressBookBean(item.tvAdpContactName.getText().toString(),item.tvAdpContactNumber.getText().toString()));
        selectedPositions.set(pos,1);
    }

    private void deleteChooseContact(ContactViewItem item,int pos){
        int position = selectedContact.size();
        for (int i = 0; i < selectedContact.size(); i++) {
            boolean equals = selectedContact.get(i).getPhone().equals(item.tvAdpContactNumber.getText().toString());
            if (equals){
                position = i;
                break;
            }
        }
        if (position!=selectedContact.size()){
            selectedContact.remove(position);
            selectedPositions.set(pos,0);
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (addressBooks==null){
            return 0;
        }
        return addressBooks.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    class ContactViewItem extends RecyclerView.ViewHolder{
        ConstraintLayout clContactEntry;
        TextView tvAdpContactName, tvAdpContactNumber;
        CheckBox cbAdpChooseContact;
        public ContactViewItem(@NonNull View view) {
            super(view);
            tvAdpContactName = view.findViewById(R.id.tv_adp_contact_name);
            tvAdpContactNumber = view.findViewById(R.id.tv_adp_contact_number);
            cbAdpChooseContact = view.findViewById(R.id.cb_adp_choose_contact);
            clContactEntry = view.findViewById(R.id.cl_contact_entry);
        }
    }

    public interface OnItemClickListener{
        void onItemClickListener(View view,int position);
    }
    public interface OnItemLongClickListener{
        boolean onItemLongClickListener(View view,int position,List<AddressBookBean> selectedContact);
    }

    public interface OnAddError{
        void onAddError(String phone,String msg);
    }
}


