package icu.shaoyayu.android.security.common.bean;

/**
 * @author shaoyayu
 */
public class AddressBookBean {

    private String name;

    private String phone;

    public AddressBookBean(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public AddressBookBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        phone = phone.replace("-","");
        phone = phone.replace(" ","");
        this.phone = phone;
    }

    @Override
    public String toString() {
        return name+":"+phone;
    }
}
