package de.fau.cs.mad.rpgpack.templatestore;

import de.fau.cs.mad.rpgpack.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TemplateStoreSidebarArrayAdapter extends ArrayAdapter<String>{

	private Integer[] images;
	private String[] texts;
	private Context context;
	private int active = 0;
	
	public TemplateStoreSidebarArrayAdapter (Context context, String[] texts, Integer[] images) {
			super(context, R.layout.template_store_rowlayout_sidebar, texts);
			this.context = context;
			this.texts = texts;
			this.images = images;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView= inflater.inflate(R.layout.template_store_rowlayout_sidebar, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		txtTitle.setText(texts[position]);
		imageView.setImageResource(images[position]);
		if(position == active) {
			rowView.setBackgroundResource(R.drawable.store_list_selector_pressed);
		}
		return rowView;
	}
	
	public void setActive(int pos) {
		this.active = pos;
	}
	
}
