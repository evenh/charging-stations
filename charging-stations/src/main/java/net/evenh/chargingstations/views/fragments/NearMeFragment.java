package net.evenh.chargingstations.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.evenh.chargingstations.ApiSettings;
import net.evenh.chargingstations.R;
import net.evenh.chargingstations.api.NobilService;
import net.evenh.chargingstations.models.charger.Charger;
import net.evenh.chargingstations.models.stats.CountyStats;
import net.evenh.chargingstations.models.stats.DetailedMunicipality;
import net.evenh.chargingstations.serializers.ChargerDeserializer;
import net.evenh.chargingstations.serializers.ChargerListDeserializer;
import net.evenh.chargingstations.serializers.CountyStatsDeserializer;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

import java.util.ArrayList;

/**
 * Created by evenh on 06/11/14.
 */
public class NearMeFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
	public static final String TAG = "NearMeFragment";
	private View view;
	private TextView placeholder;

	LocationClient mLocationClient;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_nearme, container, false);
		placeholder = (TextView) view.findViewById(R.id.placeholderText);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocationClient = new LocationClient(getActivity(), this, this);
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
		placeholder.setText("Connected!");
	}

	@Override
	public void onDisconnected() {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}
}
