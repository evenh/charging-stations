package net.evenh.chargingstations.api;

import com.google.gson.Gson;
import net.evenh.chargingstations.ApiSettings;
import net.evenh.chargingstations.models.Charger;
import retrofit.Callback;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.http.*;

/**
 * Charger interface for Retrofit
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public interface NobilService {
    Converter DATA_CONVERTER = new GsonConverter(new Gson());
    String SERVICE_ENDPOINT = "http://nobil.no/api/server";

    @FormUrlEncoded
    @POST("/search.php?apikey=" + ApiSettings.API_KEY + "&apiversion=3&action=search&type=id")
    Charger getChargerById(@Field("id") String chargerId);

}
