package icu.shaoyayu.android.security.presenter.service;

import android.content.Context;

import icu.shaoyayu.android.security.common.service.AntiTheftService;
import icu.shaoyayu.android.security.presenter.util.MD5Utils;
import icu.shaoyayu.android.security.presenter.util.PrefUtils;

/**
 * @author shaoyayu
 */
public class AntiTheftServiceImpl extends AddressBookServiceImpl implements AntiTheftService {

    private Context context;

    public AntiTheftServiceImpl(Context context) {
        super(context);
        this.context = context;
    }


    /**
     * 开启防盗
     */
    public void setUpAntiTheft(){
        PrefUtils.putBoolean(AntiTheftSign.IS_ANTI_THEFT,true,context);
    }

    /**
     * 判断手机是否开启防盗功能
     * @return
     */
    public boolean isAntiTheft(){
        boolean antiTheft = PrefUtils.getBoolean(AntiTheftSign.IS_ANTI_THEFT,false,context);
        return antiTheft;
    }

    public void removeAntiTheft(){
        PrefUtils.remove(AntiTheftSign.IS_ANTI_THEFT,context);
        //调用删除密码
        removePassword();
    }

    /**
     * 设置密码
     * @param psw
     */
    public void setUpPassword(String psw){
        PrefUtils.putString(AntiTheftSign.MD5_EN_TH_PSW, MD5Utils.encode(psw),context);
    }

    /**
     * 获取密码
     * @return
     */
    public String getPassword(){
        return PrefUtils.getString(AntiTheftSign.MD5_EN_TH_PSW,null,context);
    }

    /**
     * 删除密码
     */
    public void removePassword(){
        PrefUtils.remove(AntiTheftSign.MD5_EN_TH_PSW,context);
        //删除绑定SIM卡状态
        removeIsBindingSim();
    }

    /**
     * 设置绑定SIM卡
     * @return
     */
    public void setUpIsBindingSim(){
        PrefUtils.putBoolean(AntiTheftSign.IS_BINDING_SIM,true,context);
    }

    /**
     * 获取绑定SIM卡的状态
     * @return
     */
    public boolean gutIsBindingSim(){
        return PrefUtils.getBoolean(AntiTheftSign.IS_BINDING_SIM,false,context);
    }

    /**
     * 删除Sim卡的绑定状态
     */
    public void removeIsBindingSim(){
        PrefUtils.remove(AntiTheftSign.IS_BINDING_SIM,context);
        removeSIMNumber();
    }

    /**
     * 设置SIM卡号
     * @param sim
     */
    public void setUpSIMNumber(String sim){
        PrefUtils.putString(AntiTheftSign.SIM_CARD_NUMBER,sim,context);
    }

    /**
     * 读取SIM卡号
     * @return
     */
    public String getSIMNumber(){
        return PrefUtils.getString(AntiTheftSign.SIM_CARD_NUMBER,null,context);
    }

    /**
     * 删除SIM卡号
     */
    public void removeSIMNumber(){
        PrefUtils.remove(AntiTheftSign.SIM_CARD_NUMBER,context);
        //删除联系人状态
        removeIsContactPerson();
    }

    /**
     * 设置联系人状态
     */
    public void setUpIsContactPerson(){
        PrefUtils.putBoolean(AntiTheftSign.IS_SET_CONTACTS,true,context);
    }

    /**
     * 联系人是否存在
     * @return
     */
    public boolean getIsContactPerson(){
        return PrefUtils.getBoolean(AntiTheftSign.IS_SET_CONTACTS,false,context);
    }

    /**
     * 删除是否存储来写人
     */
    public void  removeIsContactPerson(){
        PrefUtils.remove(AntiTheftSign.IS_SET_CONTACTS,context);
        //删除联系人
        removeContactPersonStr();
    }

    /**
     * 比较两个密码
     * @param pas
     * @return
     */
    public boolean comparePasswords(String pas){
        return MD5Utils.encode(pas).equals(getPassword());
    }

    /**
     * 是否设置超级管理员
     * @return
     */
    public boolean isSetUpAdmin(){
        return PrefUtils.getBoolean(AntiTheftSign.IS_SET_ADMIN,false,context);
    }

    /**
     * 设置成功管理员
     */
    public void setUpAdmin(){
        PrefUtils.putBoolean(AntiTheftSign.IS_SET_ADMIN,true,context);
    }

    /**
     * 移除超级管理员
     */
    public void removeAdmin(){
        PrefUtils.remove(AntiTheftSign.IS_SET_ADMIN,context);
    }

    public interface AntiTheftSign{
        //是否开启防盗功能
        String IS_ANTI_THEFT = "anti_theft";
        //设置密码
        String MD5_EN_TH_PSW = "MD5_anti_theft_password";
        //是否绑定SIM卡
        String IS_BINDING_SIM = "is_binding_sim";
        //SIM卡号
        String SIM_CARD_NUMBER = "sim_card_number";
        //是否设置联系人
        String IS_SET_CONTACTS =  "set_up_contacts";
        //是否开启管理员身份
        String IS_SET_ADMIN = "is_set_admin";

    }
}
