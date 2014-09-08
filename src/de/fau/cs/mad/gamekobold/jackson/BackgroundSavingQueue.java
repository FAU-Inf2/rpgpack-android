package de.fau.cs.mad.gamekobold.jackson;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.os.AsyncTask;

/**
 * This is a queue for saving @link {@link Template} and @link {@link CharacterSheet} in the background.
 * Holds up to {@link #maxQueueSize} tasks. Tries to execute a task when @link {@link #executeTask()} is called.
 * @author Benjamin
 *
 */
public class BackgroundSavingQueue {
	private final Context appContext;
	private final int maxQueueSize;
	private final Queue<SavingQueueTask> taskQueue;
	private SavingQueueTask currentTask;
	private final boolean setLastEditFlagForTemplates;
	
	/**
	 * 
	 * @param appContext Application Context or any other valid context
	 * @param maxQueueSize The maximum number of tasks that are buffered in the queue.
	 * @param setLastEditFlagForTempaltes If set to true, the flag will be updated when saving a {@link Template}.
	 */
	public BackgroundSavingQueue(Context appContext, int maxQueueSize, boolean setLastEditFlagForTempaltes) {
		this.appContext = appContext;
		this.maxQueueSize = maxQueueSize;
		this.taskQueue = new LinkedList<BackgroundSavingQueue.SavingQueueTask>();
		this.currentTask  = null;
		this.setLastEditFlagForTemplates = setLastEditFlagForTempaltes;
	}
	
	/**
	 * Tries to insert a new task in the queue.
	 * @param template The {@link Template} to be saved.
	 * @return True if a new task has been added to the queue, false otherwise.
	 */
	public boolean enqueueSavingTask(Template template) {
		synchronized (taskQueue) {
			if(taskQueue.size() < maxQueueSize) {
				taskQueue.add(new SavingQueueTask(this, template));
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Tries to insert a new task in the queue.
	 * @param characterSheet The {@link CharacterSheet} to be saved.
	 * @return True if a new task has been added to the queue, false otherwise.
	 */
	public boolean enqueueSavingTask(CharacterSheet characterSheet) {
		synchronized (taskQueue) {
			if(taskQueue.size() < maxQueueSize) {
				taskQueue.add(new SavingQueueTask(this, characterSheet));
				return true;
			}	
		}
		return false;		
	}
	
	/**
	 * Tries to execute a task from the queue.
	 */
	public void executeTask() {
		synchronized (currentTask) {
			if(currentTask != null) {
				synchronized (taskQueue) {
					currentTask = taskQueue.poll();					
				}
				if(currentTask != null) {
					currentTask.execute();
				}
			}			
		}
	}
	
	/**
	 * AsyncTask for saving {@link Template} and {@link CharacterSheet}.
	 * @author Benjamin
	 *
	 */
	private class SavingQueueTask extends AsyncTask<Void, Void, Boolean> {
		private final Object objectToSave;
		private final BackgroundSavingQueue myQueue;
		
		public SavingQueueTask(BackgroundSavingQueue queue, Object objectToSave) {
			this.myQueue = queue;
			this.objectToSave = objectToSave;
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			if(objectToSave == null) {
				return Boolean.FALSE;
			}
			if(objectToSave instanceof Template) {
				try {
					JacksonInterface.saveTemplate((Template)objectToSave, appContext, setLastEditFlagForTemplates);
				}
				catch(Throwable e) {
					e.printStackTrace();
					return Boolean.FALSE;
				}
				return Boolean.TRUE;
			}
			else if(objectToSave instanceof CharacterSheet) {
				final File characterSheetFile = new File(((CharacterSheet)objectToSave).getFileAbsolutePath());
				try {
					JacksonInterface.saveCharacterSheet((CharacterSheet)objectToSave, characterSheetFile);
				}
				catch(Throwable e) {
					e.printStackTrace();
					return Boolean.FALSE;
				}
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		
		@Override
		public void onPostExecute(Boolean result) {
			synchronized (myQueue.currentTask) {
				myQueue.currentTask = null;	
			}
		}
	}
}
