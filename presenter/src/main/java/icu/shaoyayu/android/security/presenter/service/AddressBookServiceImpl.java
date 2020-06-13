package icu.shaoyayu.android.security.presenter.service;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import icu.shaoyayu.android.security.common.bean.AddressBookBean;
import icu.shaoyayu.android.security.presenter.util.PrefUtils;

/**
 * @author shaoyayu
 */
public class AddressBookServiceImpl {

    private Context context;

    public AddressBookServiceImpl(Context context) {
        this.context = context;
    }

    public List<AddressBookBean> strToLisBran(String s){
        List<AddressBookBean> bookBeans = new ArrayList<>();
        try {
            String contactPersons[] = s.split("-");
            for (String contactPerson : contactPersons) {
                String[] info = contactPerson.split(":");
                bookBeans.add(new AddressBookBean(info[0],info[1]));
            }
            return bookBeans;
        }catch (Exception e){
            return null;
        }

    }

    public String listBranToStr(List<AddressBookBean> bookBeans){
        String ps = "";
        for (int i = 0; i < bookBeans.size(); i++) {
            if (i==0){
                ps = bookBeans.get(i).toString();
            }else{
                ps = ps +"-"+bookBeans.get(i).toString();
            }
        }
        return ps;
    }


    /**
     * 删除联系人
     * @param bookBeans
     */
    public void setUpContactPerson(List<AddressBookBean> bookBeans){
        String s = listBranToStr(bookBeans);
        PrefUtils.putString(TAGS.CONTACT_PERSON,s,context);
    }

    /**
     * 获取联系人
     * @return
     */
    public List<AddressBookBean> getContactPerson(){
        String s = getContactPersonStr();
        if (s==null){
            return null;
        }
        return strToLisBran(s);
    }

    /**
     * 获取联系人字符串
     * @return
     */
    public String getContactPersonStr(){
        return  PrefUtils.getString(TAGS.CONTACT_PERSON,null,context);
    }

    /**
     * 删除联系人
     */
    public void removeContactPersonStr(){
        PrefUtils.remove(TAGS.CONTACT_PERSON,context);
    }

    /**
     * 修改联系人
     * @param bookBeans
     */
    public void modifyContactPerson(List<AddressBookBean> bookBeans){
        removeContactPersonStr();
        setUpContactPerson(bookBeans);
    }

    interface TAGS{
        String CONTACT_PERSON = "contact_person";
    }
}
