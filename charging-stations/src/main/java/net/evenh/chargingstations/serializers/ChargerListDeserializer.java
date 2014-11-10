package net.evenh.chargingstations.serializers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.evenh.chargingstations.models.Charger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by evenh on 10/11/14.
 */
public class ChargerListDeserializer implements JsonDeserializer<ArrayList<Charger>> {
	@Override
	public ArrayList<Charger> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		return new Gson().fromJson(jsonElement.getAsJsonArray(), new TypeToken<ArrayList<Charger>>(){}.getType());
	}
}
