package net.evenh.chargingstations.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.evenh.chargingstations.ApiSettings;
import net.evenh.chargingstations.R;
import net.evenh.chargingstations.api.NobilService;
import net.evenh.chargingstations.models.charger.Charger;
import net.evenh.chargingstations.models.stats.CountyStats;
import net.evenh.chargingstations.models.stats.MunicipalityStats;
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
import java.util.Arrays;

/**
 * Created by evenh on 06/11/14.
 */
public class MapFragment extends Fragment {
	public static final String TAG = "MapFragment";
	private Charger hafrsfjord;
	private View view;
	private TextView placeholder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_map, container, false);
		placeholder = (TextView) view.findViewById(R.id.placeholderText);
		return view;
	}

	public MapFragment() {
		super();

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Charger.class, new ChargerDeserializer())
				.registerTypeAdapter(new TypeToken<ArrayList<Charger>>() {
				}.getType(), new ChargerListDeserializer())
				.registerTypeAdapter(CountyStats.class, new CountyStatsDeserializer())
				.create();

		RestAdapter adapter = new RestAdapter.Builder()
				.setConverter(new GsonConverter(gson))
				.setEndpoint(ApiSettings.API_BASE_URL)
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestInterceptor.RequestFacade request) {
						request.addHeader("X-Authorization", ApiSettings.API_KEY);
					}
				})
				.build();

		NobilService nobil = adapter.create(NobilService.class);


		/*nobil.getCharger("NOR_00171", new Callback<Charger>(){
			@Override
			public void success(Charger charger, Response response) {
				hafrsfjord = charger;
				Log.d(TAG, "Success");
				placeholder.setText(hafrsfjord.getName());
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Log.d(TAG, "Failure: " + retrofitError.toString());
			}
		});*/

		/*nobil.getChargerByMapReferences("(59.943921193288915,10.826683044433594)", "(59.883683240905256,10.650901794433594)", new Callback<ArrayList<Charger>>() {
			@Override
			public void success(ArrayList<Charger> chargers, Response response) {
				placeholder.setText("Ladere hentet: " + chargers.size());
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Log.d(TAG, "Feilolini");
			}
		});*/

		/*nobil.getChargersNearLocation("59.91673", "10.74782", 1000, 20, new Callback<ArrayList<Charger>>() {
			@Override
			public void success(ArrayList<Charger> chargers, Response response) {
				placeholder.setText("Ladere i nærheten: " + chargers.size());
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Log.d(TAG, "Failure: " + new String(((TypedByteArray)retrofitError.getResponse().getBody()).getBytes()));
			}
		});*/

		nobil.getMunicipalitiesForCounty("NOR", "06", new Callback<ArrayList<MunicipalityStats>>() {
			@Override
			public void success(ArrayList<MunicipalityStats> municipalityStatses, Response response) {
				for (MunicipalityStats m : municipalityStatses){
					Log.d(TAG, m.toString());
				}
			}

			@Override
			public void failure(RetrofitError retrofitError) {

			}
		});
	}
}
