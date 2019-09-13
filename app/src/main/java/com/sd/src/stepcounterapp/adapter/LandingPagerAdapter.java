package com.sd.src.stepcounterapp.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class LandingPagerAdapter extends FragmentStatePagerAdapter {
	
	private final List<Fragment> mFragmentList = new ArrayList<>();
	private final List<String> mFragmentTitle = new ArrayList<>();
	
	public LandingPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	
	@Override
	public Fragment getItem(int position) {
		return mFragmentList.get(position);
	}
	
	public void addFragment(Fragment fragment) {
		mFragmentList.add(fragment);
	}
	
	public void addFragment(Fragment fragment, String title) {
		mFragmentList.add(fragment);
		mFragmentTitle.add(title);
	}
	

	@Override
	public int getCount() {
		return mFragmentList.size();
	}
	
	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return mFragmentTitle.get(position);
	}
	
	@Override
	public int getItemPosition(Object object){
		return PagerAdapter.POSITION_NONE;
	}
}
