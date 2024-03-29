package net.evenh.chargingstations.views.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

import net.evenh.chargingstations.R;
import net.evenh.chargingstations.util.RefreshState;
import net.evenh.chargingstations.util.Utils;
import net.evenh.chargingstations.api.NobilClient;
import net.evenh.chargingstations.api.NobilService;
import net.evenh.chargingstations.models.charger.Charger;
import net.evenh.chargingstations.views.adapters.ChargerListAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Shows a user chargers near his/her current location
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class NearMeFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, SwipeRefreshLayout.OnRefreshListener {
	public static final String TAG = "NearMeFragment";
	private View view;
	private View header;
	private SwipeRefreshLayout swipeLayout;
	private ListView listView;
	private TextView noResults;

	private LocationClient mLocationClient;
	public static Location mCurrentLocation;
	private LocationRequest mLocationRequest;

	private Geocoder geocoder;
	private String address;

	private ProgressDialog indicator;

	private int distance;
	private int limit;

	// Milliseconds per second
	private static final int MILLISECONDS_PER_SECOND = 1000;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 15;
	// Update frequency in milliseconds
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 5;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

	private RefreshState rs;
	private Activity mActivity;

	private SharedPreferences.OnSharedPreferenceChangeListener settingsChangedListener =
			new SharedPreferences.OnSharedPreferenceChangeListener() {
				@Override
				public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
					RefreshState.getInstance().setRefreshed(false);
					indicator.setMessage(getResources().getString(R.string.indicator_settings));
					if (Utils.hasInternet(mActivity)) indicator.show();
				}
			};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_nearme, container, false);
		// Inflate listview and error message
		listView = (ListView) view.findViewById(R.id.listView);
		noResults = (TextView) view.findViewById(R.id.no_results_found);
		// Inflate header
		header = inflater.inflate(R.layout.item_charger_header, listView, false);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rs = RefreshState.getInstance();

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
		prefs.registerOnSharedPreferenceChangeListener(settingsChangedListener);

		mLocationClient = new LocationClient(mActivity, this, this);

		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL_IN_SECONDS);

		indicator = new ProgressDialog(mActivity);
		indicator.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		indicator.setIndeterminate(true);
		indicator.setCancelable(false);
		indicator.setMessage(getResources().getString(R.string.indicator_message));
		if (Utils.hasInternet(mActivity)) indicator.show();
	}

	@Override
	public void onRefresh() {
		indicator.setMessage(getResources().getString(R.string.indicator_message));
		if (Utils.hasInternet(mActivity)) {
			indicator.show();
			rs.setRefreshed(false);
			onConnected(null);
			swipeLayout.setRefreshing(false);
		} else {
			swipeLayout.setRefreshing(false);
			indicator.dismiss();
			Toast.makeText(mActivity, R.string.internet_dialog_title, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setSize(SwipeRefreshLayout.LARGE);

		// Enables scroll to refresh when having multiple childs of a SwipeRefreshLayout
		listView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				if (listView.getChildAt(0) != null) {
					if (listView.getChildAt(0).getTop() == 0) swipeLayout.setEnabled(true);
					else swipeLayout.setEnabled(false);
				}
			}
		});
	}

	@Override
	public void onStop() {
		mLocationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnected(Bundle bundle) {
		// Get the users settings
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mActivity);
		try {
			distance = Integer.parseInt(settings.getString(getResources().getString(R.string.key_distance), "3000"));
			limit = Integer.parseInt(settings.getString(getResources().getString(R.string.key_limit), "25"));
		} catch (NumberFormatException nfe) {
			Log.d(TAG, "Could not convert Settings' strings to integers, setting default values for 'Near me'");
			distance = 3000;
			limit = 25;
		}

		Log.d(TAG, "Connected to Google Play Services");
		mCurrentLocation = mLocationClient.getLastLocation();
		mLocationClient.requestLocationUpdates(mLocationRequest, this);

		// Provide a fixed location for the emulator
		if (Utils.isAndroidEmulator()) {
			Log.i(TAG, "Detected emulator, simulating location...");

			Location mockLocation = new Location("Pilestredet 35 Mock Location Provider");
			mockLocation.setLatitude(59.919570);
			mockLocation.setLongitude(10.735562);

			mCurrentLocation = mockLocation;
			mLocationClient.setMockLocation(mockLocation);
			onLocationChanged(mockLocation);
		}

		// Remove "No results found" text if exists
		noResults.setText("");
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(mActivity, -1);
			} catch (IntentSender.SendIntentException e) {
				Log.d(TAG, "GPS connection failed", e);
			}
		} else {
			Dialog d = GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), mActivity, -2);
			d.show();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "Got high accurancy location!");

		if (mCurrentLocation != null && mLocationClient != null) {
			// Enable automatic refresh if location has changed by >50 meters
			if (mCurrentLocation.distanceTo(location) > 50) rs.setRefreshed(false);

			mCurrentLocation = location;

			if (mLocationClient.isConnected() && !mLocationClient.isConnecting())
				mLocationClient.removeLocationUpdates(this);

			geocoder = new Geocoder(mActivity, Locale.getDefault());
			try {
				List<Address> addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
				address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1);
			} catch (Exception e) {
				address = getResources().getString(R.string.no_address_found);
			}

			// Refresh one time, then disable until the user moves >50 meters or uses pull to refresh
			if (!rs.isRefreshed()) {
				showChargersNearLocation();
				rs.setRefreshed(true);
			}
		}
	}

	/**
	 * Fetches new data and updates the UI
	 *
	 * @since 1.0.0
	 */
	private void showChargersNearLocation() {
		if (!indicator.isShowing()) indicator.show();

		String latitude = String.valueOf(mCurrentLocation.getLatitude());
		String longitude = String.valueOf(mCurrentLocation.getLongitude());

		final TextView locationText = (TextView) header.findViewById(R.id.tv_location);
		final TextView updatedText = (TextView) header.findViewById(R.id.tv_updated);

		final DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mActivity.getApplicationContext());
		final DateFormat timeFormat = DateFormat.getTimeInstance();

		NobilService api = NobilClient.getInstance().getApi();

		api.getChargersNearLocation(latitude, longitude, distance, limit, new Callback<ArrayList<Charger>>() {
			@Override
			public void success(ArrayList<Charger> chargers, Response response) {
				ChargerListAdapter adapter = new ChargerListAdapter(mActivity, chargers);

				listView.setAdapter(adapter);

				if (isAdded()) {
					// Fixes scrolling bug (header view)
					if (listView.getHeaderViewsCount() == 1) listView.removeHeaderView(header);

					long time = System.currentTimeMillis();

					locationText.setText(address);

					updatedText.setText(String.format(
							getResources().getString(R.string.last_updated),
							chargers.size(),
							(dateFormat.format(time) + " " + timeFormat.format(time)
							)));


					listView.addHeaderView(header);

					listView.setVisibility(View.VISIBLE);
					noResults.setVisibility(View.GONE);
				}

				indicator.dismiss();
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				listView.setVisibility(View.GONE);
				noResults.setVisibility(View.VISIBLE);
				noResults.setText(R.string.no_chargers_nearby);

				indicator.dismiss();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
		prefs.unregisterOnSharedPreferenceChangeListener(settingsChangedListener);
	}

	@Override
	public void onResume() {
		super.onResume();
		mLocationClient.connect();
	}
}
