package de.fau.cs.mad.gamekobold.alljoyn;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.ThumbnailLoader;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class TemplateListArrayAdapter extends ArrayAdapter<Template> {
	Context context;
	// the list of objects we want to display
	private List<Template> templates;

	public TemplateListArrayAdapter(Context context, List<Template> templates) {
		super(context, R.layout.rowlayout_template_browser, templates);
		this.context = context;
		this.templates = templates;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView;

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

		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);

		Template curTemplate = templates.get(position);

		tName.setText(curTemplate.getTemplateName());
		tWorld.setText(curTemplate.getWorldName());
		tAdditionalInfo.setText(String.format(context
				.getString(R.string.template_item_adapter_author_date_label),
				curTemplate.getAuthor(), curTemplate.getDate()));

		// load image bitmap
		final Bitmap icon = ThumbnailLoader.loadThumbnail(
				curTemplate.getIconPath(), context);
		if (icon != null) {
			imageView.setImageBitmap(icon);
		}
		return rowView;
	}
}
