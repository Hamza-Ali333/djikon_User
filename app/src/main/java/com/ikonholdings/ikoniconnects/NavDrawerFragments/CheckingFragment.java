package com.ikonholdings.ikoniconnects.NavDrawerFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ikonholdings.ikoniconnects.BlankFragment;
import com.ikonholdings.ikoniconnects.ChatAdapter.SectionPagerAdapter;
import com.ikonholdings.ikoniconnects.R;


public class CheckingFragment extends Fragment {

    View mFragment;
    ViewPager mViewPager;
    TabLayout mTabLayout;

    public CheckingFragment() {
        //required
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mFragment =  inflater.inflate(R.layout.fragment_checking,container,false);

        mViewPager = mFragment.findViewById(R.id.viewPager);
        mTabLayout = mFragment.findViewById(R.id.tabMode);

       return mFragment;
    }

    //onActivity Create Method

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new ChatListFragment(),"ChatArea");
        adapter.addFragment(new BlankFragment(),"Groups");
        viewPager.setAdapter(adapter);
    }
}
