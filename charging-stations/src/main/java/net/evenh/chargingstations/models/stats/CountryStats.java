package net.evenh.chargingstations.models.stats;

import com.google.gson.annotations.Expose;

/**
 * Created by evenh on 14/11/14.
 */
public class CountryStats {
	@Expose
	private Integer count;
	@Expose
	private String countyid;
	@Expose
	private String county;

	public CountryStats(Integer count, String countyid, String county) {
		this.count = count;
		this.countyid = countyid;
		this.county = county;
	}

	/**
	 *
	 * @return
	 * The count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 *
	 * @param count
	 * The count
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 *
	 * @return
	 * The countyid
	 */
	public String getCountyid() {
		return countyid;
	}

	/**
	 *
	 * @param countyid
	 * The countyid
	 */
	public void setCountyid(String countyid) {
		this.countyid = countyid;
	}

	/**
	 *
	 * @return
	 * The county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 *
	 * @param county
	 * The county
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	@Override
	public String toString() {
		return "CountryStats{" +
				"count=" + count +
				", countyid='" + countyid + '\'' +
				", county='" + county + '\'' +
				'}';
	}
}
