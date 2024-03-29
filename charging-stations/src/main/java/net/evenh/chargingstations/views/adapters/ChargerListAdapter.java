package net.evenh.chargingstations.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import net.evenh.chargingstations.ChargerDetail;
import net.evenh.chargingstations.R;
import net.evenh.chargingstations.util.Utils;
import net.evenh.chargingstations.models.charger.Charger;
import net.evenh.chargingstations.views.fragments.NearMeFragment;

import java.util.ArrayList;

/**
 * A custom adapter for Charger objects
 *
 * @author Even Holthe
 * @since 1.0.0
 */
public class ChargerListAdapter extends ArrayAdapter<Charger> {
	private static final String TAG = "ChargerListAdapter";
	private static Context context;
    private ArrayList<Charger> chargers;

    public ChargerListAdapter(Context context, ArrayList<Charger> chargers) {
        super(context, 0, chargers);
        this.context = context;
        this.chargers = chargers;
    }

    private static class ViewHolder {
        ImageView image;
        TextView name;
        TextView distance;
		String id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Charger charger = getItem(position);

        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_charger, parent, false);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.tv_distance);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(charger.getName());
		viewHolder.id = charger.getInternationalId();

		// Find distance
		Location from = new Location(NearMeFragment.mCurrentLocation);

		double[] coordinates = Utils.getCoordinatesFromString(charger.getPosition());
		Location to = new Location("");
		to.setLatitude(coordinates[0]);
		to.setLongitude(coordinates[1]);

		int distance = (int) from.distanceTo(to);

		if(distance > 1000){
			viewHolder.distance.setText(distance/1000 + " km");
		} else {
			viewHolder.distance.setText(distance + " m");
		}

		String imageFile = getItem(position).getImage();

		String url = !imageFile.equals("Kommer") ? "http://www.nobil.no/img/ladestasjonbilder/tn_" + imageFile : "http://www.nobil.no/img/cp_img_missing.jpg";
		Picasso.with(context).load(url).into(viewHolder.image);

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Utils.hasInternet(context)) {
					ViewHolder vh = (ViewHolder) v.getTag();
					Intent details = new Intent(context, ChargerDetail.class);
					details.putExtra("id", vh.id);
					context.startActivity(details);
				} else {
					Toast.makeText(context, R.string.internet_dialog_title, Toast.LENGTH_SHORT).show();
				}
			}
		});

        return convertView;
    }

    @Override
    public int getCount() {
        return chargers.size();
    }

    @Override
    public Charger getItem(int pos) {
        return chargers.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
