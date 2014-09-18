package de.fau.cs.mad.gamekobold.filebrowser;

import java.io.File;

public interface IFileBrowserReceiver {
	public void onFilePicked(File directory);
}
