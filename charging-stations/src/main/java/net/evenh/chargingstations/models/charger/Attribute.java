package net.evenh.chargingstations.models.charger;

/**
 * A class for holding a single attribute in Nobil's API
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class Attribute {
	private String key;
	private int typeId;
	private int valueId;
	private String value;
	private String description;

	public Attribute(String key, int typeId, int valueId, String value, String description) {
		this.key = key;
		this.typeId = typeId;
		this.valueId = valueId;
		this.value = value;
		this.description = description;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getValueId() {
		return valueId;
	}

	public void setValueId(int valueId) {
		this.valueId = valueId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Attribute{" +
				"key='" + key + '\'' +
				", typeId=" + typeId +
				", valueId=" + valueId +
				", value='" + value + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
