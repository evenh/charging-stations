package net.evenh.chargingstations.api;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.evenh.chargingstations.ApiSettings;
import net.evenh.chargingstations.models.charger.Charger;
import net.evenh.chargingstations.models.stats.CountyStats;
import net.evenh.chargingstations.models.stats.DetailedMunicipality;
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
				.registerTypeAdapter(new TypeToken<ArrayList<Charger>>() {
				}.getType(), new ChargerListDeserializer())
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
	 * Gets a single charger
	 *
	 * @param id The id of the charger to find
	 * @return A Charger object on success, null on failure
	 * @since 1.0.0
	 */
	public Charger getCharger(final String id){
		final Charger[] chargerFound = { null };

		api.getCharger("NOR_00171", new Callback<Charger>() {
			@Override
			public void success(Charger charger, Response response) {
				chargerFound[0] = charger;
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Log.e(TAG, "Failure while getting charger with id " + id + ": " + retrofitError.toString());
			}
		});

		return chargerFound[0];
	}

	/**
	 * Gets a charger by map references
	 *
	 * @param northEast North east position
	 * @param southWest South west position
	 * @return A list of chargers found
	 * @since 1.0.0
	 */
	public ArrayList<Charger> getChargerByMapReferences(final String northEast, final String southWest){
		final ArrayList<Charger>[] chargerList = new ArrayList[1];

		api.getChargerByMapReferences(northEast, southWest, new Callback<ArrayList<Charger>>() {
			@Override
			public void success(ArrayList<Charger> chargers, Response response) {
				chargerList[0] = chargers;
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				chargerList[0] = null;
				Log.e(TAG, "Failure while getting chargers with coordinates: NE(" + northEast + "), SW(" + southWest + ")");
			}
		});

		return chargerList[0];
	}

	/**
	 * Gets chargers in proximity of a location
	 *
	 * @param latitude The position's latitude
	 * @param longitude The position's longitude
	 * @param distanceInMeters The search radius in meters
	 * @param limit Maximum number of results
	 * @return A list of chargers on success, null otherwise
	 * @since 1.00
	 */
	public ArrayList<Charger> getChargersNearLocation(final String latitude, final String longitude, final int distanceInMeters, final int limit){
		final ArrayList<Charger>[] chargerList = new ArrayList[1];

		api.getChargersNearLocation("59.91673", "10.74782", 1000, 20, new Callback<ArrayList<Charger>>() {
			@Override
			public void success(ArrayList<Charger> chargers, Response response) {
				chargerList[0] = chargers;
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				chargerList[0] = null;
				Log.e(TAG, "Failure while getting chargers with coordinates: lat("+latitude+"), lon("+longitude+"), distance("+distanceInMeters+"), limit("+limit+")");
			}
		});

		return chargerList[0];
	}

	/**
	 * Gets stats for a country
	 *
	 * @param countryCode NOR, SWE, DAN or FIN
	 * @return A list of counties with stats in a country on success, null otherwise
	 * @since 1.0.0
	 */
	public ArrayList<CountyStats> getStatsForCountry(final String countryCode){
		final ArrayList<CountyStats>[] counties = new ArrayList[1];

		api.getStatsForCountry(countryCode, new Callback<ArrayList<CountyStats>>() {
			@Override
			public void success(ArrayList<CountyStats> countyStats, Response response) {
				counties[0] = countyStats;
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				counties[0] = null;
				Log.e(TAG, "Failure while getting country stats for " + countryCode);
			}
		});

		return counties[0];
	}

	/**
	 * Gets stats for a given county
	 *
	 * @param countryCode NOR, SWE, DAN or FIN
	 * @param countyCode A code for a given county
	 * @return A CountyStats object on success, null otherwise
	 * @since 1.0.0
	 */
	public CountyStats getStatsForCounty(final String countryCode, final String countyCode){
		final CountyStats[] cs = {null};

		api.getStatsForCounty(countryCode, countyCode, new Callback<CountyStats>() {
			@Override
			public void success(CountyStats countyStats, Response response) {
				cs[0] = countyStats;
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Log.e(TAG, "Failure while getting county stats for " + countryCode + "/" + countyCode);
			}
		});

		return cs[0];
	}

	/**
	 * Gets municipality stats for a given county
	 *
	 * @param countryCode NOR, SWE, DAN or FIN
	 * @param countyCode A code for a given county
	 * @return A list of municipality stats on success, null otherwise
	 * @since 1.0.0
	 */
	public ArrayList<MunicipalityStats> getMunicipalitiesForCounty(final String countryCode, final String countyCode){
		final ArrayList<MunicipalityStats>[] ms = new ArrayList[1];

		api.getMunicipalitiesForCounty(countryCode, countyCode, new Callback<ArrayList<MunicipalityStats>>() {
			@Override
			public void success(ArrayList<MunicipalityStats> municipalityStats, Response response) {
				ms[0] = municipalityStats;
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				ms[0] = null;
				Log.e(TAG, "Failure while getting municipality stats for " + countryCode + "/" + countyCode);
			}
		});

		return ms[0];
	}

	/**
	 * Gets a given municipality
	 *
	 * @param countryCode NOR, SWE, DAN or FIN
	 * @param municipalityId A id for a given municipality
	 * @return Municipality stats on success, null otherwise
	 * @since 1.0.0.
	 */
	public MunicipalityStats getMunicipality(final String countryCode, final String municipalityId){
		final MunicipalityStats[] ms = {null};

		api.getMunicipality(countryCode, municipalityId, new Callback<MunicipalityStats>() {
			@Override
			public void success(MunicipalityStats municipalityStats, Response response) {
				ms[0] = municipalityStats;
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				ms[0] = null;
				Log.e(TAG, "Failure while getting municipality stats for " + municipalityId + "("+countryCode+")");
			}
		});

		return ms[0];
	}

	/**
	 * Gets a detailed stats about a given municipality
	 *
	 * @param countryCode NOR, SWE, DAN or FIN
	 * @param municipalityId A id for a given municipality
	 * @return Detailed municipality stats on success, null otherwise
	 * @since 1.0.0.
	 */
	public ArrayList<DetailedMunicipality> getMunicipalityDetails(final String countryCode, final String municipalityId){
		final ArrayList<DetailedMunicipality>[] dm = new ArrayList[1];

		api.getMunicipalityDetails(countryCode, municipalityId, new Callback<ArrayList<DetailedMunicipality>>() {
			@Override
			public void success(ArrayList<DetailedMunicipality> detailedMunicipalities, Response response) {
				dm[0] = detailedMunicipalities;
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				dm[0] = null;
				Log.e(TAG, "Failure while getting detailed municipality stats for " + municipalityId + "("+countryCode+")");
			}
		});

		return dm[0];
	}
}
