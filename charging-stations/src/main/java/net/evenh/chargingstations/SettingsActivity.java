package net.evenh.chargingstations;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import net.evenh.chargingstations.views.fragments.SettingsFragment;

/**
 * Displays the Settings fragment
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class SettingsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
