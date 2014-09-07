package de.fau.cs.mad.gamekobold.templatebrowser;

import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TemplateBrowserArrayAdapter extends ArrayAdapter<Template> {
	Context context;

	// the list of objects we want to display
	private List<Template> objects;

	public TemplateBrowserArrayAdapter(Context context, List<Template> objects) {
		super(context, R.layout.rowlayout_template_browser, objects);
		this.context = context;
		this.objects = objects;
	}

	// needed for viewConvertion so that the system knows that there are different layouts in the adapter
	// 0 for template. 1 for "new template" and "edit last template" items
	@Override
	public int getItemViewType(int position) {
		// if "edit" or "create"
		if(position >= getCount() - 2) {
			// return 1
			return 1;
		}
		// return 0 for every other item
		return 0;
	}

	// we got 2 types: normal items and the last two ones
	@Override
	public int getViewTypeCount() {
	    return 2; // Count of different layouts
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView;
		if (convertView == null) {
			// if it is the last row -> create new template
			if (position == getCount() - 1) {
				rowView = inflater.inflate(
						R.layout.rowlayout_newtemplate_template_browser,
						parent, false);

				Log.e("er", "Position, getCount: " + getCount());

				TextView tName = (TextView) rowView
						.findViewById(R.id.textView1);
				Template curTemplate = objects.get(position);
				tName.setText(curTemplate.getTemplateName());

			}
			// JACKSON for editing last created template
			else if (position == getCount() - 2) {
				rowView = inflater.inflate(
						R.layout.rowlayout_newtemplate_template_browser,
						parent, false);

				Log.e("er", "Position, getCount: " + getCount());

				TextView tName = (TextView) rowView
						.findViewById(R.id.textView1);
				Template curTemplate = objects.get(position);
				tName.setText(curTemplate.getTemplateName());
			}
			// JACKSON end
			else {
				Log.e("er", "position: " + position);

				rowView = inflater.inflate(R.layout.rowlayout_template_browser,
						parent, false);
				TextView tName = (TextView) rowView
						.findViewById(R.id.textView1);
				TextView tWorld = (TextView) rowView
						.findViewById(R.id.textView2);
				TextView tAdditionalInfo = (TextView) rowView
						.findViewById(R.id.textView3);

				ImageView imageView = (ImageView) rowView
						.findViewById(R.id.imageView1);

				Template curTemplate = objects.get(position);

				tName.setText(curTemplate.getTemplateName());
				tWorld.setText(curTemplate.getWorldName());
				tAdditionalInfo.setText("Von: " + curTemplate.getAuthor()
						+ ", " + curTemplate.getDate());

				// load image bitmap
				if(!curTemplate.getIconPath().isEmpty()) {
					Bitmap icon = BitmapFactory.decodeFile(curTemplate.getIconPath());
					if(icon != null) {
						// create smaller thumbnail version
						final int[] thumbnail_dimens = context.getResources().getIntArray(R.array.thumbnail_dimensions);
						icon = Bitmap.createScaledBitmap(icon, thumbnail_dimens[0], thumbnail_dimens[1], false);
						imageView.setImageBitmap(icon);
					}
				}
			}
		} else {
			// check for last 2 lines -> edit last template, create new template.
			// they use an other layout
			if(position >= getCount() - 2) {
				rowView = inflater.inflate( R.layout.rowlayout_newtemplate_template_browser,
						parent, false);
				TextView tName = (TextView) rowView
						.findViewById(R.id.textView1);
				Template curTemplate = objects.get(position);
				tName.setText(curTemplate.getTemplateName());
			}
			else {
				rowView = inflater.inflate(R.layout.rowlayout_template_browser,
						parent, false);				

				TextView tName = (TextView) rowView.findViewById(R.id.textView1);
				TextView tWorld = (TextView) rowView.findViewById(R.id.textView2);
				TextView tAdditionalInfo = (TextView) rowView
						.findViewById(R.id.textView3);

				ImageView imageView = (ImageView) rowView
						.findViewById(R.id.imageView1);

				Template curTemplate = objects.get(position);

				tName.setText(curTemplate.getTemplateName());
				tWorld.setText(curTemplate.getWorldName());
				tAdditionalInfo.setText("Von: " + curTemplate.getAuthor() + ", "
						+ curTemplate.getDate());

				// load image bitmap
				if(!curTemplate.getIconPath().isEmpty()) {
					Bitmap icon = BitmapFactory.decodeFile(curTemplate.getIconPath());
					if(icon != null) {
						// create smaller thumbnail version
						final int[] thumbnail_dimens = context.getResources().getIntArray(R.array.thumbnail_dimensions);
						icon = Bitmap.createScaledBitmap(icon, thumbnail_dimens[0], thumbnail_dimens[1], false);
						imageView.setImageBitmap(icon);
					}
				}
				else {
					TemplateIcons templateIcons = TemplateIcons.getInstance();
					imageView.setImageResource(Integer.valueOf(templateIcons
							.getTempalteIcon(0)));
				}
			}
		}
		return rowView;
	}
}
