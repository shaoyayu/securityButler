package icu.shaoyayu.android.security.butler.activity;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.adapter.ContactAdapter;
import icu.shaoyayu.android.security.butler.view.SimpleMenu;
import icu.shaoyayu.android.security.common.activity.CustomizeActivity;
import icu.shaoyayu.android.security.common.bean.AddressBookBean;
import icu.shaoyayu.android.security.presenter.service.AntiTheftServiceImpl;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author shaoyayu
 * 申请访问通讯录的权限
 * 通讯录，获取联系人权限，读取联系人
 */
public class AddressBookActivity extends CustomizeActivity implements EasyPermissions.PermissionCallbacks {

    private SimpleMenu smAddressBook;

    private List<AddressBookBean> bookBeans;

    //联系人 adapter
    private RecyclerView rvContactDisplay;

    private LinearLayoutManager mLinearLayoutManager;

    private ContactAdapter mContactAdapter;

    private AntiTheftServiceImpl antiTheftService;

    public static final int PERMISSION_STORAGE_CODE = 10003;
    public static final String PERMISSION_STORAGE_MSG = "需要访问你的通讯录获取紧急联系人";
    public static final String[] PERMISSION_STORAGE = new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS,Manifest.permission.GET_ACCOUNTS};

    @Override
    protected void initWindows() {

    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.activity_address_book;
    }

    @Override
    protected void initTheControl() {
        super.initTheControl();
        mLinearLayoutManager = new LinearLayoutManager(this);
        smAddressBook = findViewById(R.id.sm_address_book);
        smAddressBook.setTvMenuThemeText("选择联系人");
        smAddressBook.setComeBackText("确定");
        rvContactDisplay = findViewById(R.id.rv_contact_display);
        antiTheftService = new AntiTheftServiceImpl(getApplicationContext());
    }

    @Override
    protected void initData() {
        super.initData();
        //需要访问权限
        initSimple();
    }

    private void  initContactPerson(){
        bookBeans = getContacts();
        mContactAdapter = new ContactAdapter(getApplicationContext(),bookBeans,antiTheftService.getContactPerson());
//        mContactAdapter = new ContactAdapter(getApplicationContext(),bookBeans,new ArrayList<AddressBookBean>());
        mContactAdapter.setOnItemLongClickListener(new ContactAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClickListener(View view, int position, List<AddressBookBean> selectedContact) {
                return true;
            }
        });
        rvContactDisplay.setAdapter(mContactAdapter);
        rvContactDisplay.setLayoutManager(mLinearLayoutManager);
    }

    @Override
    protected void initControlBinding() {
        super.initControlBinding();
        smAddressBook.setOnReturnClickListener(new SimpleMenu.OnReturnClickListener() {
            @Override
            public void onReturnClickListener(View view) {
                //返回事件回调,讲数据加载到存储中。
                antiTheftService.setUpContactPerson(mContactAdapter.getSelectedContact());
                //设置已经设置联系人了
                antiTheftService.setUpIsContactPerson();
                finish();
            }
        });
    }

    /**
     * 读取联系人
     * @return
     */
    private List<AddressBookBean> getContacts() {
        //联系人的Uri，也就是content://com.android.contacts/contacts
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //指定获取_id和display_name两列数据，display_name即为姓名
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        //根据Uri查询相应的ContentProvider，cursor为获取到的数据集
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);

        List<AddressBookBean> addressBookBeans = new ArrayList<>();
        AddressBookBean addressBookBean;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                addressBookBean = new AddressBookBean();
                Long id = cursor.getLong(0);
                //获取姓名
                String name = cursor.getString(1);
                //指定获取NUMBER这一列数据
                String[] phoneProjection = new String[] {
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                addressBookBean.setName(name);
                //根据联系人的ID获取此人的电话号码
                Cursor phonesCusor = this.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        phoneProjection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null,
                        null);
                //因为每个联系人可能有多个电话号码，所以需要遍历
                if (phonesCusor != null && phonesCusor.moveToFirst()) {
                    do {
                        String num = phonesCusor.getString(0);
                        addressBookBean.setPhone(num);
                    }while (phonesCusor.moveToNext());
                }
                addressBookBeans.add(addressBookBean);
            } while (cursor.moveToNext());
        }
        return addressBookBeans;
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
            initContactPerson();
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
}
