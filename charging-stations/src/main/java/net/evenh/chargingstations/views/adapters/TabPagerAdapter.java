package net.evenh.chargingstations.views.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import net.evenh.chargingstations.views.fragments.MapFragment;
import net.evenh.chargingstations.views.fragments.StatsFragment;

/**
 * Created by evenh on 06/11/14.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
			case 0:
				return new MapFragment();
			case 1:
				return new StatsFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}
}
