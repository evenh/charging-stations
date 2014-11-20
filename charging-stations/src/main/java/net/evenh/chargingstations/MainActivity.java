package net.evenh.chargingstations;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import net.evenh.chargingstations.views.adapters.TabPagerAdapter;

/**
 * The main activity, displaying tabs
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class MainActivity extends FragmentActivity {
	private ViewPager Tab;
	private TabPagerAdapter TabAdapter;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Check for Google Play Services
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		if(status == ConnectionResult.SUCCESS) {
			TabAdapter = new TabPagerAdapter(getFragmentManager());
			Tab = (ViewPager) findViewById(R.id.pager);
			Tab.setOnPageChangeListener(
					new ViewPager.SimpleOnPageChangeListener() {
						@Override
						public void onPageSelected(int position) {
							actionBar = getActionBar();
							actionBar.setSelectedNavigationItem(position);
						}
					});

			Tab.setAdapter(TabAdapter);
			actionBar = getActionBar();

			//Enable Tabs on Action Bar
			if (actionBar != null) actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			ActionBar.TabListener tabListener = new ActionBar.TabListener() {
				@Override
				public void onTabReselected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {
				}

				@Override
				public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
					Tab.setCurrentItem(tab.getPosition());
				}

				@Override
				public void onTabUnselected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {
				}
			};

			//Add New Tab
			actionBar.addTab(actionBar.newTab().setText(R.string.near_me).setTabListener(tabListener));
			actionBar.addTab(actionBar.newTab().setText(R.string.search).setTabListener(tabListener));
		} else {
			// Google Play Services error handling
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, -1);
			dialog.show();
		}
	}
}
