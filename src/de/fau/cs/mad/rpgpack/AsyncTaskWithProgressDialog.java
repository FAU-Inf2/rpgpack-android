package de.fau.cs.mad.rpgpack;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Abstract class for faster creation of AsyncTask that should show a {@link ProgressDialog}.
 * Call {@link AsyncTaskWithProgressDialog#onPreExecute(Context, String, String)} to set up the Dialog.
 * Call {@link AsyncTaskWithProgressDialog#onPostExecute(Object)} to dismiss the dialog.
 *
 * @param <Params> See  {@link AsyncTask}.
 * @param <Progress> See  {@link AsyncTask}.
 * @param <Result> See  {@link AsyncTask}.
 */
public abstract class AsyncTaskWithProgressDialog<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	private ProgressDialog pd = null;

	/**
	 * Sets up and shows the {@link ProgressDialog}.
	 * @param context Context for creation.
	 * @param title Title for the Dialog.
	 * @param message Message for the Dialog.
	 */
	protected void onPreExecute(Context context, final String title, final String message) {
		// create new progress dialog
		pd = new ProgressDialog(context);
		pd.setTitle(title);
		pd.setMessage(message);
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		// show it
		pd.show();
	}
	
	/**
	 * Dismisses the {@link ProgressDialog}.
	 */
	@Override
	protected void onPostExecute(Result result) {
		// close progress dialog
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}
}
