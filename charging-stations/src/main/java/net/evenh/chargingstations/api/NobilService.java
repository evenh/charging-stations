package net.evenh.chargingstations.api;

import net.evenh.chargingstations.models.charger.Charger;
import net.evenh.chargingstations.models.stats.CountyStats;
import net.evenh.chargingstations.models.stats.MunicipalityStats;
import retrofit.Callback;
import retrofit.http.*;

import java.util.ArrayList;

/**
 * Charger interface for Retrofit
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public interface NobilService {
	// Chargers
	@GET("/chargers/id/{id}")
	void getCharger(@Path("id") String chargerId, Callback<Charger> cb);

	@GET("/chargers/map/{northeast}/{southwest}")
	void getChargerByMapReferences(@Path("northeast") String northEast, @Path("southwest") String southWest, Callback<ArrayList<Charger>> cb);

	@GET("/chargers/near/{lat}/{lon}")
	void getChargersNearLocation(@Path("lat") String latitude, @Path("lon") String longtiude, @Query("distance") Integer distanceInMeters, @Query("limit") Integer limit, Callback<ArrayList<Charger>> cb);

	// Stats
	@GET("/stats/{country}")
	void getStatsForCountry(@Path("country") String countryCode, Callback<ArrayList<CountyStats>> cb);

	@GET("/stats/{country}/county/{county}")
	void getStatsForCounty(@Path("country") String countryCode, @Path("county") String countyCode, Callback<CountyStats> cb);

	@GET("/stats/{country}/county/{county}/municipalities")
	void getMunicipalitiesForCounty(@Path("country") String countryCode, @Path("county") String countyCode, Callback<ArrayList<MunicipalityStats>> cb);
}
