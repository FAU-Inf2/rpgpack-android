package de.fau.cs.mad.gamekobold.templatebrowser;

import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.ThumbnailLoader;
import android.content.Context;
import android.graphics.Bitmap;
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
	// flag to distinguish if we are only displaying templates or if we got the
	// extra "edit last template item"
	private boolean mode_onlyTemplates = false;

	public TemplateBrowserArrayAdapter(Context context, List<Template> objects) {
		super(context, R.layout.rowlayout_template_browser, objects);
		this.context = context;
		this.objects = objects;
	}

	public void setModeOnlyTemplates(boolean value) {
		mode_onlyTemplates = value;
	}

	// needed for viewConvertion so that the system knows that there are
	// different layouts in the adapter
	// if we are in onlyTemplate mode -> 0
	// otherwise 1 for the edit last template item and 0 for templates
	@Override
	public int getItemViewType(int position) {
		if (mode_onlyTemplates) {
			return 0;
		}
		// if "edit last template"
		if (position == getCount() - 1) {
			// return 1
			return 1;
		}
		// return 0 for every other item
		return 0;
	}

	// if we are in only template mode -> 1;
	// 2 otherwise
	@Override
	public int getViewTypeCount() {
		if (mode_onlyTemplates) {
			return 1;
		}
		return 2; // Count of different layouts
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView;
		// handle template only mode first
		if (mode_onlyTemplates) {
			if (convertView == null) {
				rowView = inflater.inflate(R.layout.rowlayout_template_browser,
						parent, false);
			} else {
				rowView = convertView;
			}
			TextView tName = (TextView) rowView.findViewById(R.id.textView1);
			TextView tWorld = (TextView) rowView.findViewById(R.id.textView2);
			TextView tAdditionalInfo = (TextView) rowView
					.findViewById(R.id.textView3);

			ImageView imageView = (ImageView) rowView
					.findViewById(R.id.imageView1);

			Template curTemplate = objects.get(position);

			tName.setText(curTemplate.getTemplateName());
			tWorld.setText(curTemplate.getWorldName());
			tAdditionalInfo.setText(curTemplate.getDate());

			// load image bitmap
			final Bitmap icon = curTemplate.getIcon(context);
			if (icon != null) {
				imageView.setImageBitmap(icon);
			}
			return rowView;
		}
		// we are in normal mode with the additional edit last template item
		if (convertView == null) {
			// if it is the last row -> edit last template
			if (position == getCount() - 1) {
				rowView = inflater.inflate(
						R.layout.rowlayout_edittemplate_template_browser,
						parent, false);
			} else {
				rowView = inflater.inflate(R.layout.rowlayout_template_browser,
						parent, false);
			}
		} else {
			rowView = convertView;
		}
		// check position
		if (position == getCount() - 1) {
			// edit last template
			TextView tName = (TextView) rowView.findViewById(R.id.textView1);
			Template curTemplate = objects.get(position);
			tName.setText(curTemplate.getTemplateName());
		} else {
			// normal template
			TextView tName = (TextView) rowView.findViewById(R.id.textView1);
			TextView tWorld = (TextView) rowView.findViewById(R.id.textView2);
			TextView tAdditionalInfo = (TextView) rowView
					.findViewById(R.id.textView3);

			ImageView imageView = (ImageView) rowView
					.findViewById(R.id.imageView1);

			Template curTemplate = objects.get(position);

			tName.setText(curTemplate.getTemplateName());
			tWorld.setText(curTemplate.getWorldName());
			tAdditionalInfo.setText(curTemplate.getDate());

			// load image bitmap
			final Bitmap icon = curTemplate.getIcon(context);
			if (icon != null) {
				imageView.setImageBitmap(icon);
			}
		}
		return rowView;
	}
}
