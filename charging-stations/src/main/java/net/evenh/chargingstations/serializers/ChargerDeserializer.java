package net.evenh.chargingstations.serializers;

import com.google.gson.*;
import net.evenh.chargingstations.models.charger.Attribute;
import net.evenh.chargingstations.models.charger.Charger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Deserializer for a single charger
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
		HashMap<String, ArrayList<Attribute>> connectors = new HashMap<String, ArrayList<Attribute>>();

		// Original JSON response
		JsonObject obj = je.getAsJsonObject();

		// Charger object itself
		JsonElement chargerObject = obj.get("csmd");
		Charger returnCharger = new Gson().fromJson(chargerObject, Charger.class);

		// Station info
		JsonObject stationObject = obj.get("attr").getAsJsonObject().get("st").getAsJsonObject();
		// Map to a set
		Set<Map.Entry<String, JsonElement>> stationSet = stationObject.entrySet();
		// Loop through the set
		for(Map.Entry<String, JsonElement> entry : stationSet){
			JsonObject element = (JsonObject) entry.getValue();
			station.add(new Attribute(
					element.get("attrname").getAsString(),
					element.get("attrtypeid").getAsInt(),
					element.get("attrvalid").getAsInt(),
					element.get("attrval").getAsString(),
					element.get("trans").getAsString()
			));
		}

		// Connector info
		JsonObject connectorsObject = obj.get("attr").getAsJsonObject().get("conn").getAsJsonObject();
		// Loop through the available connectors
		Set<Map.Entry<String, JsonElement>> connectorSet = connectorsObject.entrySet();
		for(Map.Entry<String, JsonElement> entry : connectorSet) {
			JsonObject element = (JsonObject) entry.getValue();
			// We now have a single connector, loop through that to get it's attributes
			ArrayList<Attribute> currentIterationAttributes = new ArrayList<Attribute>();
			// Loop
			Set<Map.Entry<String, JsonElement>> singleConnectorSet = element.entrySet();
			for(Map.Entry<String, JsonElement> connector : singleConnectorSet){
				JsonObject connectorAttributes = (JsonObject) connector.getValue();
				currentIterationAttributes.add(new Attribute(
						connectorAttributes.get("attrname").getAsString(),
						connectorAttributes.get("attrtypeid").getAsInt(),
						connectorAttributes.get("attrvalid").getAsInt(),
						connectorAttributes.get("attrval").getAsString(),
						connectorAttributes.get("trans").getAsString()
				));
			}

			// Add to result
			connectors.put(entry.getKey(), currentIterationAttributes);
		}


		returnCharger.setStation(station);
		returnCharger.setConnectors(connectors);

		return returnCharger;
	}
}
