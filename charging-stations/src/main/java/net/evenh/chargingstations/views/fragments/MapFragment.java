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
import net.evenh.chargingstations.ApiSettings;
import net.evenh.chargingstations.R;
import net.evenh.chargingstations.api.NobilService;
import net.evenh.chargingstations.models.Charger;
import net.evenh.chargingstations.serializers.ChargerDeserializer;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

import java.lang.reflect.Type;

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


		nobil.getCharger("NOR_00171", new Callback<Charger>(){
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
		});


	}
}
