package net.evenh.chargingstations;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import net.evenh.chargingstations.util.RefreshState;
import net.evenh.chargingstations.util.Utils;
import net.evenh.chargingstations.views.adapters.TabPagerAdapter;

/**
 * The main activity, displaying tabs
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";
	private ViewPager Tab;
	private TabPagerAdapter TabAdapter;
	private ActionBar actionBar;

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set default values
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		// Check for internet connectivity
		dialog = new AlertDialog.Builder(this)
				.setTitle(R.string.internet_dialog_title)
				.setMessage(R.string.internet_dialog_message)
				.setCancelable(true)
				.setNegativeButton(R.string.internet_dialog_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setPositiveButton(R.string.internet_dialog_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
					}
				})
				.create();


		// Check for Google Play Services
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		if (status == ConnectionResult.SUCCESS) {
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
			actionBar.addTab(actionBar.newTab().setText(R.string.near_me).setIcon(R.drawable.ic_action_place).setTabListener(tabListener));
			actionBar.addTab(actionBar.newTab().setText(R.string.search).setIcon(R.drawable.ic_action_search).setTabListener(tabListener));
		} else {
			// Google Play Services error handling
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, -1);
			dialog.show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!Utils.hasInternet(this)) {
			dialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent settingsIntent = new Intent(this, SettingsActivity.class);
				startActivity(settingsIntent);
				return true;

			case R.id.action_about:
				displayAboutBox();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Displays an about box containing Nobil's license
	 *
	 * @author Even Holthe
	 * @since 1.0.0
	 */
	private void displayAboutBox() {
		String version = "0.0.1";
		PackageManager manager = getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			version = info.versionName;
		} catch (PackageManager.NameNotFoundException nnfe){}


		StringBuilder sb = new StringBuilder("");
		sb.append(getResources().getString(R.string.app_name) + " ");
		sb.append(String.format(getResources().getString(R.string.version), version) + "\n\n");

		sb.append(getResources().getString(R.string.about_message1) + "\n\n");
		sb.append(getResources().getString(R.string.about_message2) + "\n");

		Dialog about = new AlertDialog.Builder(this)
				.setTitle(R.string.about)
				.setMessage(sb.toString())
				.setPositiveButton(R.string.internet_dialog_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.create();




		about.show();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		RefreshState.getInstance().setRefreshed(false);
	}
}
