package de.fau.cs.mad.gamekobold.templatestore;

import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
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
		RatingBar bar;
		ImageView img;
		Bitmap bm;
	}
	
	Context context;
	List<StoreTemplate> templates;
	
	public TemplateStoreArrayAdapter(Context context, List<StoreTemplate> templates) {
		super(context, R.layout.template_store_rowlayout, templates);
		this.templates = templates;
		this.context = context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)  {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewHolder holder;
		
		View rowView = null;
		StoreTemplate curr = templates.get(position);
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.template_store_rowlayout, parent, false);
			holder = new ViewHolder();
			holder.worldname = (TextView) convertView.findViewById(R.id.tv_store_worldname);
			holder.date_author = (TextView) convertView.findViewById(R.id.tv_store_date_author);
			holder.name = (TextView) convertView.findViewById(R.id.tv_store_name);
			holder.bar = (RatingBar) convertView.findViewById(R.id.ratingBarStore);
			holder.img = (ImageView) convertView.findViewById(R.id.templateStoreImg);
			if(curr.hasImage()) {
				curr.setBm(curr.getImage_data());
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			
			
			holder.worldname.setText(curr.getWorldname());
			holder.date_author.setText(curr.getDate()+" - " + curr.getAuthor());
			holder.name.setText(curr.getName());
			if(holder.bar != null) {
				holder.bar.setRating(curr.getRating());
			}
			
			if((position % 2) == 0) {
				convertView.setBackgroundColor( context.getResources().getColor(R.color.background_green) );
			}

			if(curr.hasImage()) {
				Log.e("store", "curr has image");
				//byte[] decodedString = Base64.decode(curr.getImage_data(), Base64.DEFAULT);
				//Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
				if(curr.getBm() == null) {
					Log.e("store", "getBm == null : ja ");
					curr.setBm(curr.getImage_data());
				}
				else {
					Log.e("store", "getBm == null : nein");
				}
				holder.img.setImageBitmap(curr.getBm());
			}
		
			return convertView;	
	}

	public void add(StoreTemplate tpl) {
		this.templates.add(tpl);
		notifyDataSetChanged();
	}
}
