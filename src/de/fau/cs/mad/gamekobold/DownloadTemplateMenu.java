package de.fau.cs.mad.gamekobold;

import java.io.File;
import java.io.IOException;

import de.fau.cs.mad.gamekobold.filebrowser.FileBrowser;
import de.fau.cs.mad.gamekobold.filebrowser.FileCopyUtility;
import de.fau.cs.mad.gamekobold.filebrowser.FileWouldOverwriteException;
import de.fau.cs.mad.gamekobold.filebrowser.IFileBrowserReceiver;
import de.fau.cs.mad.gamekobold.jackson.JacksonFileValidator;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.templatestore.TemplateStoreMainActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class DownloadTemplateMenu  extends AbstractThreeButtonMenu implements IFileBrowserReceiver{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final int startColor = getResources().getColor(R.color.menu_templates_start_color);
		final int endColor = getResources().getColor(R.color.menu_templates_end_color);
		final int[] midColors = getMidGradientColors(startColor, endColor);
		setTitle(R.string.menu_download_templates);
		// gradient colors
		// button 1 gradient color
		int[] gradient1 = {
			// start color
			startColor,
			// end color
			midColors[0]
		};
		// button 2 gradient color
		int[] gradient2 = {
			// start color
			midColors[0],
			// end color
			midColors[1]
		};
		// button 3 gradient color
		int[] gradient3 = {
			// start color
			midColors[1],
			// end color
			endColor
		};

		// Set the texts
		// button 1
		setButton1MainText(getString(R.string.menu_template_store));
		setButton1DescriptionText(getString(R.string.menu_template_store_description));
		
		// button 2
		setButton2MainText(getString(R.string.menu_template_import));
		setButton2DescriptionText(getString(R.string.menu_template_import_description));

		// button 3
		setButton3MainText(getString(R.string.menu_scan_qrcode));
		setButton3DescriptionText(getString(R.string.menu_scan_qrcode_description));

		// set the colors for the gradients
		setButton1Color(gradient1);
		setButton2Color(gradient2);
		setButton3Color(gradient3);
	}

	@Override
	protected void button1Action() {
		// start template store
		Intent intent = new Intent(DownloadTemplateMenu.this,
				TemplateStoreMainActivity.class);
		startActivity(intent);
	}

	@Override
	protected void button2Action() {
		// show file browser for template import from file
		showFileBrowserPopup();
	}

	@Override
	protected void button3Action() {
		Toast.makeText(DownloadTemplateMenu.this, "QR-Code scan", Toast.LENGTH_LONG).show();
	}

	/**
	 * Shows the FileBrowser as a popup.
	 */
	private void showFileBrowserPopup() {
		FileBrowser.showAsPopup(getFragmentManager(), FileBrowser.newInstance(this, FileBrowser.Mode.PICK_FILE));
		Toast.makeText(this, getString(R.string.toast_fileexplorer_msg_pick_template), Toast.LENGTH_LONG).show();
	}

	/**
	 * Callback for FileBrowser. This is called when the user picked a file to import.
	 */
	@Override
	public void onFilePicked(File file) {
		// close file browser popup
		FileBrowser.removeAsPopup(getFragmentManager());
		// check if selected file is a template
		if(JacksonFileValidator.isValidTemplate(file)) {
			importTemplateFromFile(file, false);
		}
		else {
			// file is not a template, inform user
			Toast.makeText(this, getString(R.string.toast_file_is_not_template), Toast.LENGTH_LONG).show();			
		}
	}

	private void importTemplateFromFile(final File templateFile, boolean overwriteIfExists) {
		// get template directory
		File templateRootDir = JacksonInterface.getTemplateRootDirectory(this);
		try {
			// try to copy file to template directory
			FileCopyUtility.copyFile(templateFile, new File(templateRootDir, templateFile.getName()), overwriteIfExists);
			// inform user
			Toast.makeText(this, getString(R.string.toast_imported_template), Toast.LENGTH_LONG).show();
		}
		catch(IOException e) {
			e.printStackTrace();
			// failed to import template, inform user
			Toast.makeText(this, getString(R.string.toast_imported_template_failed), Toast.LENGTH_LONG).show();
		} catch (FileWouldOverwriteException e) {
			// we would overwrite an existing file
			e.printStackTrace();
			// failed to import template, inform user
			// ask if he wants to overwrite the file
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.question_overwrite_file).setTitle(R.string.msg_file_with_same_name_already_exists);
			builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing
					dialog.dismiss();
				}
			});
			builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// overwrite file
					importTemplateFromFile(templateFile, true);
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
	}
}
