package net.evenh.chargingstations.views.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

import net.evenh.chargingstations.R;
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
 * Created by evenh on 06/11/14.
 */
public class NearMeFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, SwipeRefreshLayout.OnRefreshListener {
	public static final String TAG = "NearMeFragment";
	private View view;
	private View header;
	private SwipeRefreshLayout swipeLayout;
	private ListView listView;

	private LocationClient mLocationClient;
	public static Location mCurrentLocation;
	private LocationRequest mLocationRequest;

	private Geocoder geocoder;
	private String address;

    private ProgressDialog indicator;

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


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_nearme, container, false);
		// Inflate listview
		listView = (ListView) view.findViewById(R.id.listView);
		// Inflate header
		header = inflater.inflate(R.layout.item_charger_header, listView, false);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocationClient = new LocationClient(getActivity(), this, this);

		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL_IN_SECONDS);

        indicator = new ProgressDialog(getActivity());
        indicator.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        indicator.setIndeterminate(true);
        indicator.setMessage(getResources().getString(R.string.indicator_message));
        indicator.show();
	}

	@Override public void onRefresh() {
		indicator.show();
		onConnected(null);
		swipeLayout.setRefreshing(false);
	}

	@Override
	public void onStart() {
		super.onStart();
		mLocationClient.connect();

		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
	}

	@Override
	public void onStop() {
		mLocationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnected(Bundle bundle) {
		Log.d(TAG, "Connected to Google Play Services");
		mCurrentLocation = mLocationClient.getLastLocation();
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(getActivity(), -1);
			} catch (IntentSender.SendIntentException e) {
				Log.d(TAG, "GPS connection failed", e);
			}
		} else {
			Dialog d = GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), getActivity(), -2);
			d.show();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "Got high accurancy location!");
		mCurrentLocation = location;
		mLocationClient.removeLocationUpdates(this);

		geocoder = new Geocoder(getActivity(), Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
			address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1);
		} catch (IOException e) {
			address = getResources().getString(R.string.no_address_found);
		}

		showChargersNearLocation();
	}

	private void showChargersNearLocation() {
		String latitude = String.valueOf(mCurrentLocation.getLatitude());
		String longitude = String.valueOf(mCurrentLocation.getLongitude());

		final TextView locationText = (TextView) header.findViewById(R.id.tv_location);
		final TextView updatedText = (TextView) header.findViewById(R.id.tv_updated);

		final DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
		final DateFormat timeFormat = DateFormat.getTimeInstance();

		NobilService api = NobilClient.getInstance().getApi();

		api.getChargersNearLocation(latitude, longitude, 5000, 15, new Callback<ArrayList<Charger>>() {
			@Override
			public void success(ArrayList<Charger> chargers, Response response) {
                ChargerListAdapter adapter = new ChargerListAdapter(getActivity(), chargers);
                listView.setAdapter(adapter);

				long time = System.currentTimeMillis();

				locationText.setText(address);

				updatedText.setText(String.format(
						getResources().getString(R.string.last_updated),
						dateFormat.format(time) + " " + timeFormat.format(time)
				));

				listView.addHeaderView(header);
                indicator.dismiss();
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				// TODO: Implement failhandler
			}
		});
	}
}
