package de.fau.cs.mad.gamekobold.templatestore;

import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TemplateStoreArrayAdapter extends ArrayAdapter<StoreTemplate> {
	Context context;
	List<StoreTemplate> templates;
	
	public TemplateStoreArrayAdapter(Context context, List<StoreTemplate> templates) {
		super(context, R.layout.template_store_rowlayout, templates);
		this.templates = templates;
		this.context = context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)  {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = null;
		
		if(convertView == null) {
			rowView = inflater.inflate(R.layout.template_store_rowlayout, parent, false);
			
			StoreTemplate curr = templates.get(position);
			
			TextView worldname = (TextView) rowView.findViewById(R.id.tv_store_worldname);
			worldname.setText(curr.getWorldname());
			
			TextView date_author = (TextView) rowView.findViewById(R.id.tv_store_date_author);
			date_author.setText(curr.getDate()+" - " + curr.getAuthor());
			
			TextView name = (TextView) rowView.findViewById(R.id.tv_store_name);
			name.setText(curr.getName());
			
		} else {
			return convertView;		
		}
		
		return rowView;	
	}

	public void add(StoreTemplate tpl) {
		this.templates.add(tpl);
		notifyDataSetChanged();
	}
}
