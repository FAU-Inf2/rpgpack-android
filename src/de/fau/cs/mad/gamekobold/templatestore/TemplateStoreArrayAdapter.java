package de.fau.cs.mad.gamekobold.templatestore;

import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class TemplateStoreArrayAdapter extends ArrayAdapter<StoreTemplate> {

	static class ViewHolder {
		TextView worldname;
		TextView date_author;
		TextView name;
		TextView rating;
		RatingBar bar;
		ImageView img;
		Bitmap bm;
	}

	Context context;
	List<StoreTemplate> templates;

	public TemplateStoreArrayAdapter(Context context,
			List<StoreTemplate> templates) {
		super(context, R.layout.template_store_rowlayout, templates);
		this.templates = templates;
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewHolder holder;

		StoreTemplate curr = templates.get(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.template_store_rowlayout,
					parent, false);
			holder = new ViewHolder();
			holder.worldname = (TextView) convertView
					.findViewById(R.id.tv_store_worldname);
			holder.date_author = (TextView) convertView
					.findViewById(R.id.tv_store_date_author);
			holder.name = (TextView) convertView
					.findViewById(R.id.tv_store_name);
			holder.bar = (RatingBar) convertView
					.findViewById(R.id.ratingBarStore);
			holder.img = (ImageView) convertView
					.findViewById(R.id.templateStoreImg);
			holder.rating = (TextView) convertView
					.findViewById(R.id.tv_store_rating_text);
			if (curr.hasImage()) {
				curr.setBm(curr.getImage_data());
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.worldname.setText(curr.getWorldname());
		holder.date_author.setText(curr.getDate() + " - " + curr.getAuthor());
		holder.name.setText(curr.getName());
		if (holder.bar != null) {
			holder.bar.setRating(curr.getRating());
		}
		if (holder.rating != null) {
			holder.rating.setText("\u00d8" + " " + Float.valueOf(curr.getRating()).toString());
		}
		if ((position % 2) == 0) {
			convertView.setBackgroundColor(Color.parseColor("#0f0f0f"));
		} else {
			convertView.setBackgroundColor(Color.parseColor("#353535"));
		}

		if (curr.hasImage()) {
			// byte[] decodedString = Base64.decode(curr.getImage_data(),
			// Base64.DEFAULT);
			// Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,
			// 0, decodedString.length);
			if (curr.getBm() == null) {
				curr.setBm(curr.getImage_data());
			}
			holder.img.setImageBitmap(curr.getBm());
		} else {
			Drawable defaultImage = context.getResources().getDrawable(R.drawable.game_default_white);
			holder.img.setImageDrawable(defaultImage);
		}

		return convertView;
	}

	public void add(StoreTemplate tpl) {
		this.templates.add(tpl);
		notifyDataSetChanged();
	}
}
