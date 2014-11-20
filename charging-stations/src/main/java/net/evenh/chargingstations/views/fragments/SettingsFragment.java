package net.evenh.chargingstations.views.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import net.evenh.chargingstations.R;

/**
 * Settings fragment
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class SettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
	}
}
