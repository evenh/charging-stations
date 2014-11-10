package net.evenh.chargingstations.api;

import com.google.gson.Gson;
import net.evenh.chargingstations.ApiSettings;
import net.evenh.chargingstations.models.Charger;
import retrofit.Callback;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.http.*;

import java.util.ArrayList;

/**
 * Charger interface for Retrofit
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public interface NobilService {
    Converter DATA_CONVERTER = new GsonConverter(new Gson());
    String SERVICE_ENDPOINT = ApiSettings.API_BASE_URL;

	@GET("/chargers/id/{id}")
	void getCharger(@Path("id") String chargerId, Callback<Charger> cb);

	@GET("/chargers/map/{northeast}/{southwest}")
	void getChargerByMapReferences(@Path("northeast") String northEast, @Path("southwest") String southWest, Callback<ArrayList<Charger>> cb);

}
