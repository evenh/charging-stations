package net.evenh.chargingstations.models.stats;

import com.google.gson.annotations.Expose;

/**
 * A representation of detailed municipality stats
 *
 * @author Even Holthe
 */
public class DetailedMunicipality extends MunicipalityStats {
	@Expose
	private String zip;
	@Expose
	private String city;

	public DetailedMunicipality(Integer count, String countyid, String county, String municipalityid, String municipality, String zip, String city) {
		super(count, countyid, county, municipalityid, municipality);
		this.zip = zip;
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "DetailedMunicipality{" +
				"zip='" + zip + '\'' +
				", city='" + city + '\'' +
				'}';
	}
}
