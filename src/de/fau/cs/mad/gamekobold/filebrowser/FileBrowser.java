package de.fau.cs.mad.gamekobold.filebrowser;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class FileBrowser extends DialogFragment {
	public static final String ARGUMENT_CURRENT_DIR_ABS_PATH = "ARGUMENT_CURRENT_DIR_ABS_PATH";
	
	public enum Mode {
		PICK_DIRECTORY,
		PICK_FILE
	};
	/**
	 *
	 */
	private class DirectoryFilterClass implements FileFilter {
		private final String appRootDirectoryAbsPath;
		public DirectoryFilterClass() {
			final File rootDir =  JacksonInterface.getAppRootDirectory(getActivity());
			if(rootDir != null) {
				appRootDirectoryAbsPath = rootDir.getAbsolutePath();	
			}
			else {
				appRootDirectoryAbsPath = "";
			}
		}
		
		@Override
		public boolean accept(File arg0) {
			if(appRootDirectoryAbsPath.equals(arg0.getAbsolutePath())) {
				return false;
			}
			return arg0.isDirectory();
		}		
	};
	/**
	 *
	 */
	private class FileFilterClass implements FileFilter {
		private final String appRootDirectoryAbsPath;
		public FileFilterClass() {
			final File rootDir =  JacksonInterface.getAppRootDirectory(getActivity());
			if(rootDir != null) {
				appRootDirectoryAbsPath = rootDir.getAbsolutePath();	
			}
			else {
				appRootDirectoryAbsPath = "";
			}
		}
		@Override
		public boolean accept(File arg0) {
			if(appRootDirectoryAbsPath.equals(arg0.getAbsolutePath())) {
				return false;
			}
			return true;
		}
	};
	//
	//
	//
	private File currentDirectory = null;
	private FileBrowserArrayAdapter adapter = null;
	private IFileBrowserReceiver receiver = null;
	private FileFilter fileFilter = null;
	private Mode mode;
	
	public FileBrowser() {
	}

	public void setReceiver(IFileBrowserReceiver receiver) {
		this.receiver = receiver;
		if(adapter != null) {
			adapter.setReceiver(receiver);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
		adapter = new FileBrowserArrayAdapter(getActivity(), new ArrayList<File>(), mode);
		adapter.setReceiver(receiver);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				File clickedItem = adapter.getItem(position);
				// only descend if it is a directory
				if(clickedItem.isDirectory()) {
					currentDirectory = clickedItem; 
					loadCurrentDirectory();
				}
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
		final File[] subDirs = currentDirectory.listFiles(fileFilter);
		final File parentFile = currentDirectory.getParentFile();
		if(parentFile != null) {
			adapter.add(parentFile);
			adapter.setHasParent(true);
			getDialog().setTitle(currentDirectory.getName());
		}
		else {
			adapter.setHasParent(false);
			getDialog().setTitle("File Browser");
		}
		if(subDirs != null) {
			adapter.addAll(subDirs);
		}
		adapter.notifyDataSetChanged();
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public static FileBrowser newInstance(IFileBrowserReceiver receiver, final Mode mode) {
		FileBrowser browser = new FileBrowser();
		browser.setMode(mode);
		browser.setReceiver(receiver);
		return browser;
	}
	
	public static void showAsPopup(FragmentManager fm, FileBrowser dialog) {
		FragmentTransaction ft = fm.beginTransaction();
		Fragment prev = fm.findFragmentByTag("file_browser_dialog");
		if(prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		dialog.setRetainInstance(true);
		dialog.show(ft, "file_browser_dialog");
	}
	
	public static void removeAsPopup(FragmentManager fm) {
		FragmentTransaction ft = fm.beginTransaction();
		Fragment prev = fm.findFragmentByTag("file_browser_dialog");
		if(prev != null) {
			ft.remove(prev);
		}
		ft.commit();
	}
	
	@Override
	public void onAttach(Activity activity) {
		if(fileFilter == null) {
			if(mode == Mode.PICK_DIRECTORY) {
				fileFilter = new DirectoryFilterClass();
			}
			else {
				fileFilter = new FileFilterClass();
			}
		}
		super.onAttach(activity);
	}
}
