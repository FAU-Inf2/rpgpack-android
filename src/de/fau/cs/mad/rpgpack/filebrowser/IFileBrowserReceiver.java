package de.fau.cs.mad.rpgpack.filebrowser;

import java.io.File;

public interface IFileBrowserReceiver {
	public void onFilePicked(File file);
}
