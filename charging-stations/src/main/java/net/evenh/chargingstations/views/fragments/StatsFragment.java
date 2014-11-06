package net.evenh.chargingstations.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.evenh.chargingstations.R;

/**
 * Created by evenh on 06/11/14.
 */
public class StatsFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View map = inflater.inflate(R.layout.fragment_stats, container, false);

		return map;
	}
}
