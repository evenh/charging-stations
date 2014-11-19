package net.evenh.chargingstations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ChargerDetail extends Activity {
	private TextView title;

	private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_detail);

		if(getIntent().getExtras().getString("id") == null) finish();

		id = getIntent().getExtras().getString("id");
		title = (TextView) findViewById(R.id.title);

		title.setText("Got id: " + id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return(true);
		}

		return super.onOptionsItemSelected(item);
	}
}
