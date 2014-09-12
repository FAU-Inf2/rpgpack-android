package de.fau.cs.mad.gamekobold.filebrowser;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.R;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class FileBrowser extends Fragment {
	public static final String ARGUMENT_CURRENT_DIR_ABS_PATH = "ARGUMENT_CURRENT_DIR_ABS_PATH";
	/**
	 *
	 */
	private class DirectoryFilterClass implements FileFilter {
		@Override
		public boolean accept(File arg0) {
			return arg0.isDirectory();
		}		
	};
	/**
	 *
	 */
	private class FileFilterClass implements FileFilter {
		@Override
		public boolean accept(File pathname) {
			return pathname.isFile();
		}
	};
	//
	//
	//
	private File currentDirectory = null;
	private FileBrowserArrayAdapter adapter = null;
	private DirectoryFilterClass dirFilter;
	private IFileBrowserReceiver receiver = null;
	
	public void setReceiver(IFileBrowserReceiver receiver) {
		this.receiver = receiver;
		if(adapter != null) {
			adapter.setReceiver(receiver);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		dirFilter = new DirectoryFilterClass();
		final Bundle args = getArguments();
		if(args != null) {
			final String curDirAbsPath = getArguments().getString(ARGUMENT_CURRENT_DIR_ABS_PATH);
			if(curDirAbsPath != null) {
				currentDirectory = new File(curDirAbsPath);
			}	
		}
		if(currentDirectory == null) {
			if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				// get external storage file
				currentDirectory = Environment.getExternalStorageDirectory();
			}
			else {
				currentDirectory = new File("/");			
			}
		}
		final View rootView = inflater.inflate(R.layout.fragment_file_browser, container, false);
		final ListView listView = (ListView)rootView.findViewById(android.R.id.list);
		adapter = new FileBrowserArrayAdapter(getActivity(), new ArrayList<File>());
		adapter.setReceiver(receiver);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				currentDirectory = adapter.getItem(position);
				loadCurrentDirectory();
			}
		});
		
		loadCurrentDirectory();
		return rootView;
	}
	
	private void loadCurrentDirectory() {
		if(currentDirectory == null) {
			return;
		}
		adapter.clear();
		final File[] subDirs = currentDirectory.listFiles(dirFilter);
		final File parentFile = currentDirectory.getParentFile();
		if(parentFile != null) {
			adapter.add(parentFile);
			adapter.setHasParent(true);
		}
		else {
			adapter.setHasParent(false);
		}
		adapter.addAll(subDirs);
		adapter.notifyDataSetChanged();
	}
}
