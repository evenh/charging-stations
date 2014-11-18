package net.evenh.chargingstations.views.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import net.evenh.chargingstations.R;
import net.evenh.chargingstations.api.NobilClient;
import net.evenh.chargingstations.api.NobilService;
import net.evenh.chargingstations.models.charger.Charger;
import net.evenh.chargingstations.views.adapters.ChargerListAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;

/**
 * Created by evenh on 06/11/14.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {
	private static final String TAG = "SearchFragment";

	private View view;
	private SearchView searchView;
	private TextView hintText;
	private ListView listView;

	private ProgressDialog indicator;

	private NobilService api;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_search, container, false);

		searchView = (SearchView) view.findViewById(R.id.searchBar);
		hintText = (TextView) view.findViewById(R.id.hintText);
		listView = (ListView) view.findViewById(R.id.listView);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = NobilClient.getInstance().getApi();
	}

	@Override
	public void onResume() {
		super.onResume();
		searchView.setOnQueryTextListener(this);
		// TODO: On clear input -> clear list
	}


	@Override
	public boolean onQueryTextSubmit(String query) {
		indicator = new ProgressDialog(getActivity());
		indicator.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		indicator.setIndeterminate(true);
		indicator.setMessage(getResources().getString(R.string.searching_for_chargers));
		indicator.show();

		api.getChargersForAddress(query, new Callback<ArrayList<Charger>>() {
			@Override
			public void success(ArrayList<Charger> chargers, Response response) {
				if(chargers.size() > 0){
					listView.setVisibility(View.VISIBLE);
					hintText.setVisibility(View.GONE);

					ChargerListAdapter adapter = new ChargerListAdapter(getActivity(), chargers);
					listView.setAdapter(adapter);
				} else {
					listView.setVisibility(View.GONE);
					hintText.setVisibility(View.VISIBLE);
					hintText.setText(R.string.no_results);
				}

				indicator.dismiss();
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				listView.setVisibility(View.GONE);
				hintText.setVisibility(View.VISIBLE);
				hintText.setText(R.string.no_results);

				indicator.dismiss();
			}
		});

		// Close keyboard
		searchView.clearFocus();

		// We are handling this
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}
}
