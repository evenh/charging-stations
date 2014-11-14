package net.evenh.chargingstations.serializers;

import android.util.Log;
import com.google.gson.*;
import net.evenh.chargingstations.models.stats.CountryStats;

import java.lang.reflect.Type;

/**
 * Created by evenh on 14/11/14.
 */
public class CountryStatsDeserializer implements JsonDeserializer<CountryStats> {
	@Override
	public CountryStats deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		return new CountryStats(jsonObject.get("count").getAsInt(), jsonObject.get("countyid").getAsString(), jsonObject.get("county").getAsString());
	}
}
