package icu.shaoyayu.android.security.butler.fragment;

import androidx.fragment.app.Fragment;

import icu.shaoyayu.android.security.butler.R;
import icu.shaoyayu.android.security.common.fragment.CustomizeFragment;

/**
 * @author shaoyayu
 * GuideAntiTheftOneFragment
 * GuideAntiTheftTwoFragment
 * GuideAntiTheftThreeFragment
 */
public class GuideAntiTheftOneFragment extends CustomizeFragment {


    public static Fragment newInstance() {
        return new GuideAntiTheftOneFragment();
    }

    @Override
    protected int getInterfaceResourceId() {
        return R.layout.fragment_guide_anti_theft_one;
    }
}
