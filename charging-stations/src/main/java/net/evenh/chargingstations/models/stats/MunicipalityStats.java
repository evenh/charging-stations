package net.evenh.chargingstations.models.stats;

import com.google.gson.annotations.Expose;

/**
 * A representation of municipality stats
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class MunicipalityStats extends CountyStats {
	@Expose
	private String municipalityid;
	@Expose
	private String municipality;

	public MunicipalityStats(Integer count, String countyid, String county, String municipalityid, String municipality) {
		super(count, countyid, county);
		this.municipalityid = municipalityid;
		this.municipality = municipality;
	}

	public String getMunicipalityId() {
		return municipalityid;
	}

	public void setMunicipalityId(String municipalityId) {
		this.municipalityid = municipalityId;
	}

	public String getMunicipality() {
		return municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	@Override
	public String toString() {
		return "MunicipalityStats{" +
				"municipalityid=" + municipalityid +
				", municipality='" + municipality + '\'' +
				'}';
	}
}
