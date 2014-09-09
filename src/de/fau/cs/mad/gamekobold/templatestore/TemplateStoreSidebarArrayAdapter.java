package de.fau.cs.mad.gamekobold.templatestore;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
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
		return rowView;
	}
	
}
