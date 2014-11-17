package net.evenh.chargingstations.views.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;

/**
 * Created by evenh on 06/11/14.
 */
public class NearMeFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	public static final String TAG = "NearMeFragment";
	private View view;

	LocationClient mLocationClient;
	Location mCurrentLocation;
	LocationRequest mLocationRequest;

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
	}

	@Override
	public void onStart() {
		super.onStart();
		mLocationClient.connect();
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

		showChargersNearLocation();
	}

	private void showChargersNearLocation() {
		String latitude = String.valueOf(mCurrentLocation.getLatitude());
		String longitude = String.valueOf(mCurrentLocation.getLongitude());

		NobilService api = NobilClient.getInstance().getApi();

		api.getChargersNearLocation(latitude, longitude, 2000, 30, new Callback<ArrayList<Charger>>() {
			@Override
			public void success(ArrayList<Charger> chargers, Response response) {
				ArrayList<String> items = new ArrayList<String>();

				for(Charger c : chargers){
					items.add(c.getName());
				}

				ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);

				ListView listView = (ListView) view.findViewById(R.id.listView);
				listView.setAdapter(itemsAdapter);
			}

			@Override
			public void failure(RetrofitError retrofitError) {

			}
		});
	}
}
