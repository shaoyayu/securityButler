package icu.shaoyayu.android.security.butler.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.butler.activity.AddressBookActivity;
import icu.shaoyayu.android.security.butler.adapter.ContactAdapter;
import icu.shaoyayu.android.security.common.fragment.CustomizeFragment;
import icu.shaoyayu.android.security.presenter.service.AntiTheftServiceImpl;

/**
 * @author shaoyayu
 * 绑定联系人
 *
 */
public class GuideAntiTheftThreeFragment extends CustomizeFragment {

    private ImageView ivStartContact;

    private RecyclerView rvSavedContacts;
    private ContactAdapter mContactAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private AntiTheftServiceImpl antiTheftService;

    public static GuideAntiTheftThreeFragment newInstance() {
        return new GuideAntiTheftThreeFragment();
    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.fragment_guide_anti_theft_three;
    }

    @Override
    protected void initTheControl(View view) {
        super.initTheControl(view);
        ivStartContact = mRoot.findViewById(R.id.iv_start_contact);
        rvSavedContacts = mRoot.findViewById(R.id.rv_saved_contacts);
        antiTheftService = new AntiTheftServiceImpl(getContext());
        mLinearLayoutManager = new LinearLayoutManager(getContext());
    }

    @Override
    protected void initData() {
        super.initData();
        updateView();
    }

    @Override
    protected void initControlBinding() {
        super.initControlBinding();
        ivStartContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                intent.setClass(getContext(), AddressBookActivity.class);
                startActivity(intent);
            }
        });
    }

    public void updateView(){
        mContactAdapter = new ContactAdapter(getContext(),antiTheftService.getContactPerson(),antiTheftService.getContactPerson());
        rvSavedContacts.setAdapter(mContactAdapter);
        rvSavedContacts.setLayoutManager(mLinearLayoutManager);
    }

}
