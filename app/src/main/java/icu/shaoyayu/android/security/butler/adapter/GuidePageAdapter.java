package icu.shaoyayu.android.security.butler.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author shaoyayu
 */

public class GuidePageAdapter extends FragmentStatePagerAdapter {

    // 界面列表
    private List<Fragment> fragments;

    public GuidePageAdapter(FragmentManager manager, List<Fragment> fragments) {
        super(manager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }

    @Override
    public int getCount() {

        return fragments.size();
    }
}
