package net.evenh.chargingstations.serializers;

import android.util.Log;
import com.google.gson.*;
import net.evenh.chargingstations.models.stats.CountyStats;
import net.evenh.chargingstations.models.stats.DetailedMunicipality;
import net.evenh.chargingstations.models.stats.MunicipalityStats;

import java.lang.reflect.Type;

/**
 * Deserializer for county/municipality/detailed municipality stats
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class CountyStatsDeserializer implements JsonDeserializer<CountyStats> {
	@Override
	public CountyStats deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		if(jsonObject.has("municipalityid") && !jsonObject.has("city")){
			return new MunicipalityStats(jsonObject.get("count").getAsInt(),
					jsonObject.get("countyid").getAsString(),
					jsonObject.get("county").getAsString(),
					jsonObject.get("municipalityid").getAsString(),
					jsonObject.get("municipality").getAsString());
		} else if ((jsonObject.has("municipalityid") && jsonObject.has("city"))){
			return new DetailedMunicipality(jsonObject.get("count").getAsInt(),
					jsonObject.get("countyid").getAsString(),
					jsonObject.get("county").getAsString(),
					jsonObject.get("municipalityid").getAsString(),
					jsonObject.get("municipality").getAsString(),
					jsonObject.get("zip").getAsString(),
					jsonObject.get("city").getAsString());
		}

		return new CountyStats(jsonObject.get("count").getAsInt(), jsonObject.get("countyid").getAsString(), jsonObject.get("county").getAsString());
	}
}
