package net.evenh.chargingstations.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.evenh.chargingstations.ApiSettings;
import net.evenh.chargingstations.models.charger.Charger;
import net.evenh.chargingstations.models.stats.CountyStats;
import net.evenh.chargingstations.serializers.ChargerDeserializer;
import net.evenh.chargingstations.serializers.CountyStatsDeserializer;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * A class to interact with the Nobil API
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class NobilClient {
	private NobilService api;
	public static final String TAG = "NobilClient";

	/**
	 * Private constructor, used in the singleton pattern
	 */
	private NobilClient() {
		// Initialize the GSON object in order to deserialize data
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Charger.class, new ChargerDeserializer())
				.registerTypeAdapter(CountyStats.class, new CountyStatsDeserializer())
				.create();

		// Create a rest adapter
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

		api = adapter.create(NobilService.class);
	}

	private static class NobilClientHolder {
		private static final NobilClient INSTANCE = new NobilClient();
	}

	/**
	 * Gets an instance of the NobilClient
	 *
	 * @return An instance of NobilClient
	 */
	public static NobilClient getInstance() {
		return NobilClientHolder.INSTANCE;
	}

	/**
	 * Gets an instance of NobilService to interact with
	 *
	 * @return An instance of the API
	 * @since 1.0.0
	 */
	public NobilService getApi() {
		return api;
	}
}
