package net.evenh.chargingstations;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import net.evenh.chargingstations.api.NobilClient;
import net.evenh.chargingstations.api.NobilService;
import net.evenh.chargingstations.models.charger.Attribute;
import net.evenh.chargingstations.models.charger.Charger;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Shows detailed information about a charging point
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class ChargerDetail extends Activity {
	private static final String TAG = "ChargerDetail";
	private static Context context;

	// UI
	private ImageView image;
	private TableLayout table;
	private TextView information;

	// ID
	private String id;

	// Activity specific
	private ActionBar ab;
	private ProgressDialog indicator;
	private NobilService api;
	private String address;
	private Charger chargerStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_detail);
		context = this;
		ab = getActionBar();

		// Get ID of charging point
		if(getIntent().getExtras().getString("id") == null) finish();
		id = getIntent().getExtras().getString("id");

		// Find UI elements
		image = (ImageView) findViewById(R.id.charger_image);
		table = (TableLayout) findViewById(R.id.charger_points_table);
		information = (TextView) findViewById(R.id.charger_information);

		// Init loading indicator
		indicator = new ProgressDialog(this);
		indicator.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		indicator.setIndeterminate(true);
		indicator.setMessage(getResources().getString(R.string.loading_charger_point));

		// Get API instance
		api = NobilClient.getInstance().getApi();

		loadChargerStation();
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.charger_detail, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;

			// Open Google Maps with the specified address
			case R.id.action_directions:
				String uri = String.format("http://maps.google.com/maps?daddr=%s", address);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
				startActivity(intent);

				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Fetches the information and places it into the view
	 *
	 * @since 1.0.0
	 */
	private void loadChargerStation() {
		indicator.show();

		api.getCharger(id, new Callback<Charger>() {
			@Override
			public void success(Charger charger, Response response) {
				chargerStation = charger;

				// Set action bar title and subtitle
				address = charger.getStreet() + " " + charger.getHouseNumber() + ", " + charger.getZipcode() + " " + charger.getCity();
				ab.setTitle(charger.getName());
				ab.setSubtitle(address);

				// Set charger image
				String imageFile = charger.getImage();
				String url = !imageFile.equals("Kommer") ? "http://www.nobil.no/img/ladestasjonbilder/" + imageFile : "http://www.nobil.no/img/cp_img_missing.jpg";
				Picasso.with(context).load(url).into(image);

				// Build up information string
				StringBuilder sb = new StringBuilder();
				sb.append(String.format(getResources().getString(R.string.owned_by), charger.getOwnedBy()) + "\n\n");
				// Strip out bad HTML from the Nobil API
				sb.append(android.text.Html.fromHtml(charger.getContactInfo()));


				// Iterate through connectors and add it to the table view
				int chargerNo = 1;
				Iterator i = charger.getConnectors().entrySet().iterator();
				while(i.hasNext()) table.addView(generateTableRow((ArrayList<Attribute>) ((Map.Entry) i.next()).getValue(), chargerNo++));

				// Set information
				information.setText(sb.toString());

				indicator.dismiss();
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Log.d(TAG, "Fail!");
				indicator.dismiss();
			}
		});
	}

	/**
	 * Convenience method for creating a single table row
	 *
	 * @param attributes A set of charger attributes
	 * @param n Which charger number is this?
	 * @return A populated TableRow
	 * @since 1.0.0
	 */
	private TableRow generateTableRow(ArrayList<Attribute> attributes, int n) {
		boolean vacantIsSet = false;

		// Find layout
		TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.item_charger_ports, null);
		// Find UI elements
		TextView chargerNo = (TextView) tr.findViewById(R.id.chargerNo);
		TextView connector = (TextView) tr.findViewById(R.id.connector);
		TextView capacity = (TextView) tr.findViewById(R.id.capacity);
		TextView vehicleType = (TextView) tr.findViewById(R.id.vehicle_type);
		TextView chargeMode = (TextView) tr.findViewById(R.id.charge_mode);
		TextView manufacturer = (TextView) tr.findViewById(R.id.manufacturer);
		TextView vacant = (TextView) tr.findViewById(R.id.vacant);

		// Set the charger number
		chargerNo.setText("#" + n);

		// Charger vacancy is not provided for every charger
		vacant.setText(String.format(getResources().getString(R.string.detail_vacant), getResources().getString(R.string.not_specified)));

		// Insert attributes to the varying text views
		for(Attribute a : attributes){
			if(a.getKey().equals("Connector")) connector.setText(String.format(getResources().getString(R.string.detail_connector), a.getDescription()));
			if(a.getKey().equals("Charging capacity")) capacity.setText(String.format(getResources().getString(R.string.detail_capacity), a.getDescription()));
			if(a.getKey().equals("Vehicle type")) vehicleType.setText(String.format(getResources().getString(R.string.detail_vehicle_type), a.getDescription()));
			if(a.getKey().equals("Charge mode")) chargeMode.setText(String.format(getResources().getString(R.string.detail_charge_mode), a.getDescription()));
			if(a.getKey().equals("Manufacturer")) manufacturer.setText(String.format(getResources().getString(R.string.detail_manufacturer), a.getValue()));

			// Check for vacant charger
			if(a.getKey().equals("Connector sensor status") || a.getKey().equals("Connector status")){
				String status = a.getDescription();

				if(status.equals("Busy (charging)") && !vacantIsSet){
					vacant.setText(Html.fromHtml(String.format(
							getResources().getString(R.string.detail_vacant),
							"<font color='#FF0000'>" + getResources().getString(R.string.busy) + "</font>"
					)));

					vacantIsSet = true;
				} else if (status.equals("Reserved") && !vacantIsSet){
					vacant.setText(Html.fromHtml(String.format(
							getResources().getString(R.string.detail_vacant),
							"<font color='#FF9933'>" + getResources().getString(R.string.reserved) + "</font>"
					)));

					vacantIsSet = true;
				} else if (status.equals("Vacant") && !vacantIsSet){
					vacant.setText(Html.fromHtml(String.format(
							getResources().getString(R.string.detail_vacant),
							"<font color='#339933'>" + getResources().getString(R.string.available) + "</font>"
					)));
					vacantIsSet = true;
				}
			}

			// Check for empty values
			if(connector.getText().equals(getResources().getString(R.string.detail_connector))){
				manufacturer.setText(String.format(getResources().getString(R.string.detail_connector), getResources().getString(R.string.not_specified)));
			}
			if(capacity.getText().equals(getResources().getString(R.string.detail_capacity))){
				manufacturer.setText(String.format(getResources().getString(R.string.detail_capacity), getResources().getString(R.string.not_specified)));
			}
			if(vehicleType.getText().equals(getResources().getString(R.string.detail_vehicle_type))){
				manufacturer.setText(String.format(getResources().getString(R.string.detail_vehicle_type), getResources().getString(R.string.not_specified)));
			}
			if(manufacturer.getText().equals(getResources().getString(R.string.detail_manufacturer))){
				manufacturer.setText(String.format(getResources().getString(R.string.detail_manufacturer), getResources().getString(R.string.not_specified)));
			}
			if(chargeMode.getText().equals(getResources().getString(R.string.detail_charge_mode))){
				chargeMode.setText(String.format(getResources().getString(R.string.detail_charge_mode), getResources().getString(R.string.not_specified)));
			}
		}

		return tr;
	}
}
