package de.fau.cs.mad.gamekobold.filebrowser;

import java.io.File;
import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.filebrowser.FileBrowser.Mode;
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
	private final FileBrowser.Mode mode;
	
	public void setReceiver(IFileBrowserReceiver receiver) {
		this.receiver = receiver;
	}
	
	public FileBrowserArrayAdapter(Context context, List<File> objects, final FileBrowser.Mode mode) {
		super(context,R.layout.rowlayout_file_browser, objects);
		this.mode = mode;
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
			if(file.isDirectory() && mode == Mode.PICK_DIRECTORY ||
				!file.isDirectory() && mode == Mode.PICK_FILE) {
				imageButton.setVisibility(View.VISIBLE);
				imageButton.setEnabled(true);
				imageButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(receiver != null) {
							receiver.onDirectoryPicked(file);
						}
					}
				});	
			}
			else {
				imageButton.setVisibility(View.INVISIBLE);
				imageButton.setEnabled(false);
			}
		}
		return rowView;
	}
}
