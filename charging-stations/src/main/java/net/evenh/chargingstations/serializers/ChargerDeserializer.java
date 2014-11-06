package net.evenh.chargingstations.serializers;

import android.util.Log;
import com.google.gson.*;
import net.evenh.chargingstations.models.Attribute;
import net.evenh.chargingstations.models.Charger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Deserializer for the "csmd" section of the JSON
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class ChargerDeserializer implements JsonDeserializer<Charger> {
	public static final String TAG = "ChargerDeserializer";

	@Override
	public Charger deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
		// Attribute lists
		ArrayList<Attribute> station = new ArrayList<Attribute>();
		ArrayList<Attribute> connections = new ArrayList<Attribute>();

		// Original JSON response
		JsonObject obj = je.getAsJsonObject();

		// Charger object itself
		JsonElement chargerObject = obj.get("csmd");
		Charger returnCharger = new Gson().fromJson(chargerObject, Charger.class);

		// Station info
		JsonObject stationObject = obj.get("attr").getAsJsonObject().get("st").getAsJsonObject();
		// Map to a set
		Set<Map.Entry<String, JsonElement>> entrySet = stationObject.entrySet();
		// Loop through the set
		for(Map.Entry<String, JsonElement> entry : entrySet){
			JsonObject element = (JsonObject) entry.getValue();
			station.add(new Attribute(
					element.get("attrname").getAsString(),
					element.get("attrtypeid").getAsInt(),
					element.get("attrvalid").getAsInt(),
					element.get("attrval").getAsString(),
					element.get("trans").getAsString()
			));
		}
		
		returnCharger.setStation(station);

		return returnCharger;
	}
}
