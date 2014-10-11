package de.fau.cs.mad.gamekobold;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import de.fau.cs.mad.gamekobold.filebrowser.FileBrowser;
import de.fau.cs.mad.gamekobold.filebrowser.FileCopyUtility;
import de.fau.cs.mad.gamekobold.filebrowser.FileWouldOverwriteException;
import de.fau.cs.mad.gamekobold.filebrowser.IFileBrowserReceiver;
import de.fau.cs.mad.gamekobold.jackson.JacksonFileValidator;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.jackson.Template;
import de.fau.cs.mad.gamekobold.templatestore.ApiResponse;
import de.fau.cs.mad.gamekobold.templatestore.TemplateStoreClient;
import de.fau.cs.mad.gamekobold.templatestore.TemplateStoreMainActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class DownloadTemplateMenu  extends AbstractThreeButtonMenu implements IFileBrowserReceiver{
	  private ProgressDialog progress;
	  
	  private class ApiTask extends AsyncTask<String, Integer, ApiResponse> {
			 
		  
		  protected void onPreExecute() {
			  super.onPreExecute();
	
			  progress = new ProgressDialog(DownloadTemplateMenu.this);
		      progress.setTitle(getResources().getString(R.string.loading));
		      progress.setMessage(getResources().getString(R.string.loading_wait));
		      progress.setCanceledOnTouchOutside(false);
		      progress.show();
		  }

		@Override
		protected ApiResponse doInBackground(String... params) {
			TemplateStoreClient client = new TemplateStoreClient();
			return client.getTemplateQR(params[0]);
		}
		
		protected void onPostExecute(ApiResponse response) {
	    	 if(progress != null && progress.isShowing()) {
	    		 progress.dismiss();
	    	 }
	    	 if(response.resultCode == 200) {
	    		 ObjectMapper mapper = new ObjectMapper();
	    		 Template template = null;
	    			try {
	    				template = mapper.readValue(response.responseBody, Template.class);
	    				JacksonInterface.saveTemplate(template, DownloadTemplateMenu.this, false);
	    			} catch (IOException e) {
	    				Log.e("store", "invalid template");
	    				alertMessage(getResources().getString(R.string.invalid_template));
	    				return;
	    			}
	    			alertMessage(getResources().getString(R.string.download_successful));
	    		 
	    	 } else {
	    		 // for debug
	    		 // alertMessage(response.toString());
	    	 }
		}
	  }
	
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
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// QR-Code scan
		IntentResult qrscanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if(qrscanResult != null) {
			// to get the decoded data use qrscanResult.getContents()
			// potentialURL could be a url to download a template.
			// you should check if it is valid or some other qr/barcode.
			final String potentialURL = qrscanResult.getContents();
			
			if(potentialURL == null) {
				alertMessage(getResources().getString(R.string.scan_error));
				return;
			}
			
			if(potentialURL.startsWith("http://www.rpg-pack.de")) {
				// scan successful
				Log.d("QR-CodeScan", potentialURL);
				ApiTask task = new ApiTask();
				task.execute(potentialURL);
				
			} else {
				alertMessage(getResources().getString(R.string.invalid_qr_code));
			}
		} else {
			alertMessage(getResources().getString(R.string.scan_error));
		}
	}
	
	public void alertMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Add the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.setMessage(message);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
