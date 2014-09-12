package de.fau.cs.mad.gamekobold.filebrowser;

import java.io.File;
import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class FileBrowserArrayAdapter extends ArrayAdapter<File>{
	private boolean hasParent;
	private IFileBrowserReceiver receiver;
	
	public void setReceiver(IFileBrowserReceiver receiver) {
		this.receiver = receiver;
	}
	
	public FileBrowserArrayAdapter(Context context, List<File> objects) {
		super(context,R.layout.rowlayout_file_browser, objects);
	}
	
	public boolean isHasParent() {
		return hasParent;
	}

	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		// check for layout type
		if(convertView == null) {
			rowView = inflater.inflate(
					R.layout.rowlayout_file_browser,
					parent, false);
		}
		else {
			rowView = convertView;
		}
		final TextView textView1 = (TextView) rowView.findViewById(R.id.textView1);
		final ImageButton imageButton = (ImageButton) rowView.findViewById(R.id.counter);
		
		if(position == 0 && hasParent) {
			textView1.setText("..");
			imageButton.setEnabled(false);
			imageButton.setVisibility(View.INVISIBLE);
		}
		else {
			final File file = getItem(position);	
			textView1.setText(file.getName());
			imageButton.setEnabled(true);
			imageButton.setVisibility(View.VISIBLE);
			imageButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(receiver != null) {
						receiver.onDirectoryPicked(file);
					}
				}
			});
		}
		return rowView;
	}
}
