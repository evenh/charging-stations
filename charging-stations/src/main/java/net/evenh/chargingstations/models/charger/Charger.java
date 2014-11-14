package net.evenh.chargingstations.models.charger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;


public class Charger {

	@SerializedName("Available_charging_points")
	@Expose
	private Integer AvailableChargingPoints;

	@SerializedName("City")
	@Expose
	private String city;

	@SerializedName("Contact_info")
	@Expose
	private String ContactInfo;

	@Expose
	private String County;
	@SerializedName("County_ID")

	@Expose
	private String CountyID;

	@Expose
	private String Created;

	@SerializedName("Description_of_location")
	@Expose
	private String DescriptionOfLocation;

	@SerializedName("House_number")
	@Expose
	private String HouseNumber;

	@Expose
	private Integer id;

	@Expose
	private String Image;

	@SerializedName("International_id")
	@Expose
	private String InternationalId;

	@SerializedName("Land_code")
	@Expose
	private String LandCode;

	@Expose
	private String Municipality;

	@SerializedName("Municipality_ID")
	@Expose
	private String MunicipalityID;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("Number_charging_points")
	@Expose
	private Integer NumberChargingPoints;

	@SerializedName("Owned_by")
	@Expose
	private String OwnedBy;

	@Expose
	private String Position;

	@SerializedName("Station_status")
	@Expose
	private Integer StationStatus;

	@Expose
	private String Street;

	@Expose
	private String Updated;

	@SerializedName("User_comment")
	@Expose
	private String UserComment;

	@Expose
	private String Zipcode;

	private ArrayList<Attribute> station;

	private HashMap<String, ArrayList<Attribute>> connectors;

	/**
	 * @return The AvailableChargingPoints
	 */
	public Integer getAvailableChargingPoints() {
		return AvailableChargingPoints;
	}

	/**
	 * @param AvailableChargingPoints The Available_charging_points
	 */
	public void setAvailableChargingPoints(Integer AvailableChargingPoints) {
		this.AvailableChargingPoints = AvailableChargingPoints;
	}

	/**
	 * @return The City
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param City The City
	 */
	public void setCity(String City) {
		this.city = City;
	}

	/**
	 * @return The ContactInfo
	 */
	public String getContactInfo() {
		return ContactInfo;
	}

	/**
	 * @param ContactInfo The Contact_info
	 */
	public void setContactInfo(String ContactInfo) {
		this.ContactInfo = ContactInfo;
	}

	/**
	 * @return The County
	 */
	public String getCounty() {
		return County;
	}

	/**
	 * @param County The County
	 */
	public void setCounty(String County) {
		this.County = County;
	}

	/**
	 * @return The CountyID
	 */
	public String getCountyID() {
		return CountyID;
	}

	/**
	 * @param CountyID The County_ID
	 */
	public void setCountyID(String CountyID) {
		this.CountyID = CountyID;
	}

	/**
	 * @return The Created
	 */
	public String getCreated() {
		return Created;
	}

	/**
	 * @param Created The Created
	 */
	public void setCreated(String Created) {
		this.Created = Created;
	}

	/**
	 * @return The DescriptionOfLocation
	 */
	public String getDescriptionOfLocation() {
		return DescriptionOfLocation;
	}

	/**
	 * @param DescriptionOfLocation The Description_of_location
	 */
	public void setDescriptionOfLocation(String DescriptionOfLocation) {
		this.DescriptionOfLocation = DescriptionOfLocation;
	}

	/**
	 * @return The HouseNumber
	 */
	public String getHouseNumber() {
		return HouseNumber;
	}

	/**
	 * @param HouseNumber The House_number
	 */
	public void setHouseNumber(String HouseNumber) {
		this.HouseNumber = HouseNumber;
	}

	/**
	 * @return The id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return The Image
	 */
	public String getImage() {
		return Image;
	}

	/**
	 * @param Image The Image
	 */
	public void setImage(String Image) {
		this.Image = Image;
	}

	/**
	 * @return The InternationalId
	 */
	public String getInternationalId() {
		return InternationalId;
	}

	/**
	 * @param InternationalId The International_id
	 */
	public void setInternationalId(String InternationalId) {
		this.InternationalId = InternationalId;
	}

	/**
	 * @return The LandCode
	 */
	public String getLandCode() {
		return LandCode;
	}

	/**
	 * @param LandCode The Land_code
	 */
	public void setLandCode(String LandCode) {
		this.LandCode = LandCode;
	}

	/**
	 * @return The Municipality
	 */
	public String getMunicipality() {
		return Municipality;
	}

	/**
	 * @param Municipality The Municipality
	 */
	public void setMunicipality(String Municipality) {
		this.Municipality = Municipality;
	}

	/**
	 * @return The MunicipalityID
	 */
	public String getMunicipalityID() {
		return MunicipalityID;
	}

	/**
	 * @param MunicipalityID The Municipality_ID
	 */
	public void setMunicipalityID(String MunicipalityID) {
		this.MunicipalityID = MunicipalityID;
	}

	/**
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The NumberChargingPoints
	 */
	public Integer getNumberChargingPoints() {
		return NumberChargingPoints;
	}

	/**
	 * @param NumberChargingPoints The Number of charging points
	 */
	public void setNumberChargingPoints(Integer NumberChargingPoints) {
		this.NumberChargingPoints = NumberChargingPoints;
	}

	/**
	 * @return The OwnedBy
	 */
	public String getOwnedBy() {
		return OwnedBy;
	}

	/**
	 * @param OwnedBy The Owned_by
	 */
	public void setOwnedBy(String OwnedBy) {
		this.OwnedBy = OwnedBy;
	}

	/**
	 * @return The Position
	 */
	public String getPosition() {
		return Position;
	}

	/**
	 * @param Position The Position
	 */
	public void setPosition(String Position) {
		this.Position = Position;
	}

	/**
	 * @return The StationStatus
	 */
	public Integer getStationStatus() {
		return StationStatus;
	}

	/**
	 * @param StationStatus The Station_status
	 */
	public void setStationStatus(Integer StationStatus) {
		this.StationStatus = StationStatus;
	}

	/**
	 * @return The Street
	 */
	public String getStreet() {
		return Street;
	}

	/**
	 * @param Street The Street
	 */
	public void setStreet(String Street) {
		this.Street = Street;
	}

	/**
	 * @return The Updated
	 */
	public String getUpdated() {
		return Updated;
	}

	/**
	 * @param Updated The Updated
	 */
	public void setUpdated(String Updated) {
		this.Updated = Updated;
	}

	/**
	 * @return The UserComment
	 */
	public String getUserComment() {
		return UserComment;
	}

	/**
	 * @param UserComment The User_comment
	 */
	public void setUserComment(String UserComment) {
		this.UserComment = UserComment;
	}

	/**
	 * @return The Zipcode
	 */
	public String getZipcode() {
		return Zipcode;
	}

	/**
	 * @param Zipcode The Zipcode
	 */
	public void setZipcode(String Zipcode) {
		this.Zipcode = Zipcode;
	}

	public ArrayList<Attribute> getStation() {
		return station;
	}

	public void setStation(ArrayList<Attribute> station) {
		this.station = station;
	}

	public HashMap<String, ArrayList<Attribute>> getConnectors() {
		return connectors;
	}

	public void setConnectors(HashMap<String, ArrayList<Attribute>> connectors) {
		this.connectors = connectors;
	}

	@Override
	public String toString() {
		return "Charger{" +
				"AvailableChargingPoints=" + AvailableChargingPoints +
				", city='" + city + '\'' +
				", ContactInfo='" + ContactInfo + '\'' +
				", County='" + County + '\'' +
				", CountyID='" + CountyID + '\'' +
				", Created='" + Created + '\'' +
				", DescriptionOfLocation='" + DescriptionOfLocation + '\'' +
				", HouseNumber='" + HouseNumber + '\'' +
				", id=" + id +
				", Image='" + Image + '\'' +
				", InternationalId='" + InternationalId + '\'' +
				", LandCode='" + LandCode + '\'' +
				", Municipality='" + Municipality + '\'' +
				", MunicipalityID='" + MunicipalityID + '\'' +
				", name='" + name + '\'' +
				", NumberChargingPoints=" + NumberChargingPoints +
				", OwnedBy='" + OwnedBy + '\'' +
				", Position='" + Position + '\'' +
				", StationStatus=" + StationStatus +
				", Street='" + Street + '\'' +
				", Updated='" + Updated + '\'' +
				", UserComment='" + UserComment + '\'' +
				", Zipcode='" + Zipcode + '\'' +
				", station=" + station +
				", connectors=" + connectors +
				'}';
	}
}
