package net.evenh.chargingstations.serializers;

import com.google.gson.*;
import net.evenh.chargingstations.models.stats.CountyStats;

import java.lang.reflect.Type;

/**
 * Created by evenh on 14/11/14.
 */
public class CountryStatsDeserializer implements JsonDeserializer<CountyStats> {
	@Override
	public CountyStats deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		return new CountyStats(jsonObject.get("count").getAsInt(), jsonObject.get("countyid").getAsString(), jsonObject.get("county").getAsString());
	}
}
