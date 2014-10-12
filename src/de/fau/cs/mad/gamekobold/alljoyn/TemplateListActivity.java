package de.fau.cs.mad.gamekobold.alljoyn;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusListener;
import org.alljoyn.bus.Mutable;
import org.alljoyn.bus.ProxyBusObject;
import org.alljoyn.bus.SessionListener;
import org.alljoyn.bus.SessionOpts;
import org.alljoyn.bus.Status;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.gamekobold.AlljoynInterface;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateBrowserActivity;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/* load alljoyn library */


public class TemplateListActivity extends ListActivity {
	
	/** Alljoyn **/
	static {
		System.loadLibrary("alljoyn_java");
	}

    private static final int MESSAGE_POST_TOAST = 1;
    private static final int MESSAGE_START_PROGRESS_DIALOG = 2;
    private static final int MESSAGE_STOP_PROGRESS_DIALOG = 3;
    private static final int MESSAGE_TEMPLATE_REPLY = 4;
	
    private static final String TAG = "Alljoyn - Client";
    private ProgressDialog mDialog;
    TextView replyText;
    TextView signalText;
    
    /* Handler used to make calls to AllJoyn methods. See onCreate(). */
    private BusHandler mBusHandler;
    
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_POST_TOAST:
            	Toast.makeText(TemplateListActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
            	break;
            case MESSAGE_START_PROGRESS_DIALOG:
                mDialog = ProgressDialog.show(TemplateListActivity.this, 
                                              "", 
                                              "Finding Service.\nPlease wait...\n"
                                              + "Dismiss this dialog and click on 'Start service' if you want to receive Temlates", 
                                              true,
                                              true);
                break;
            case MESSAGE_STOP_PROGRESS_DIALOG:
                mDialog.dismiss();
                break;
            case MESSAGE_TEMPLATE_REPLY:
            	Toast.makeText(TemplateListActivity.this, "Template was sent to remote device " , Toast.LENGTH_SHORT).show();
            	break;
            default:
                break;
            }
        }
    };
	/** end Alljoyn **/
	
	
	private List<Template> templateList = null;
	private Boolean mode_pickTemplateForCharacterCreation = true;
	private TemplateBrowserActivity a = new TemplateBrowserActivity(
			mode_pickTemplateForCharacterCreation);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_template_browser_alljoyn);

		/** alljoyn  **/	       
	    /* Make all AllJoyn calls through a separate handler thread to prevent blocking the UI. */
	    HandlerThread busThread = new HandlerThread("BusHandler");
	    busThread.start();
	    mBusHandler = new BusHandler(busThread.getLooper());

	   /* Connect to an AllJoyn object. */
	   mBusHandler.sendEmptyMessage(BusHandler.CONNECT);
	   mHandler.sendEmptyMessage(MESSAGE_START_PROGRESS_DIALOG);
		
	   /** end alljoyn  **/
	   
		if (templateList == null) {
			templateList = new ArrayList<Template>();
		}

		final TemplateListArrayAdapter adapter = new TemplateListArrayAdapter(
				this, templateList);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				
				File f = templateList.get(position).getTemplateFile();
					
				String template = "";
				try {
				BufferedReader in = new BufferedReader(new FileReader(f));
				String line;
				while( (line = in.readLine()) != null) {
					template += line;
				}
				
				in.close();
				} catch (Exception e) {
						Toast.makeText(TemplateListActivity.this, "error",Toast.LENGTH_LONG).show();
				}
				
		        Message msg = mBusHandler.obtainMessage(BusHandler.TEMPLATE, template);
		        mBusHandler.sendMessage(msg);
			}
		});
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		TemplateListArrayAdapter adapter = (TemplateListArrayAdapter) getListAdapter();
		a.loadTemplateList(this, adapter);
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        /* Disconnect to prevent resource leaks. */
        mBusHandler.sendEmptyMessage(BusHandler.DISCONNECT);
    }
    

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_template_browser, container, false);
			return rootView;
		}
	}
	
    class BusHandler extends Handler {    	
        /*
         * Name used as the well-known name and the advertised name of the service this client is
         * interested in.  This name must be a unique name both to the bus and to the network as a
         * whole.
         *
         * The name uses reverse URL style of naming, and matches the name used by the service.
         */
    	// com.example.alljoyncomm
        private static final String SERVICE_NAME = "org.alljoyn.bus.samples.simple";
        private static final short CONTACT_PORT=42;

        private BusAttachment mBus;
        private ProxyBusObject mProxyObj;
        private AlljoynInterface mSimpleInterface;
        
        private int 	mSessionId;
        private boolean mIsConnected;
        private boolean mIsStoppingDiscovery;
        
        /* These are the messages sent to the BusHandler from the UI. */
        public static final int CONNECT = 1;
        public static final int JOIN_SESSION = 2;
        public static final int DISCONNECT = 3;
        public static final int PING = 4;
        public static final int TEMPLATE = 5;

        public BusHandler(Looper looper) {
            super(looper);
            mIsConnected = false;
            mIsStoppingDiscovery = false;
        }
        /* Helper function to send a message to the UI thread. */
        private void sendUiMessage(int what, Object obj) {
            mHandler.sendMessage(mHandler.obtainMessage(what, obj));
        }
        
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
            /* Connect to a remote instance of an object implementing the SimpleInterface. */
            case CONNECT: {
            	org.alljoyn.bus.alljoyn.DaemonInit.PrepareDaemon(getApplicationContext());
                /*
                 * All communication through AllJoyn begins with a BusAttachment.
                 *
                 * A BusAttachment needs a name. The actual name is unimportant except for internal
                 * security. As a default we use the class name as the name.
                 *
                 * By default AllJoyn does not allow communication between devices (i.e. bus to bus
                 * communication). The second argument must be set to Receive to allow communication
                 * between devices.
                 */
                mBus = new BusAttachment(getPackageName(), BusAttachment.RemoteMessage.Receive);
                
                /*
                 * Create a bus listener class
                 */
                mBus.registerBusListener(new BusListener() {
                    @Override
                    public void foundAdvertisedName(String name, short transport, String namePrefix) {
                    	logInfo(String.format("MyBusListener.foundAdvertisedName(%s, 0x%04x, %s)", name, transport, namePrefix));
                    	/*
                    	 * This client will only join the first service that it sees advertising
                    	 * the indicated well-known name.  If the program is already a member of 
                    	 * a session (i.e. connected to a service) we will not attempt to join 
                    	 * another session.
                    	 * It is possible to join multiple session however joining multiple 
                    	 * sessions is not shown in this sample. 
                    	 */
                    	if(!mIsConnected) {
                    	    Message msg = obtainMessage(JOIN_SESSION);
                    	    msg.arg1 = transport;
                    	    msg.obj = name;
                    	    sendMessage(msg);
                    	}
                    }
                });

                /* To communicate with AllJoyn objects, we must connect the BusAttachment to the bus. */
                Status status = mBus.connect();
                logStatus("BusAttachment.connect()", status);
                if (Status.OK != status) {
                    finish();
                    return;
                }

                /*
                 * Now find an instance of the AllJoyn object we want to call.  We start by looking for
                 * a name, then connecting to the device that is advertising that name.
                 *
                 * In this case, we are looking for the well-known SERVICE_NAME.
                 */
                status = mBus.findAdvertisedName(SERVICE_NAME);
                logStatus(String.format("BusAttachement.findAdvertisedName(%s)", SERVICE_NAME), status);
                if (Status.OK != status) {
                	finish();
                	return;
                }
            

                break;
            }
            case (JOIN_SESSION): {
            	/*
                 * If discovery is currently being stopped don't join to any other sessions.
                 */
                if (mIsStoppingDiscovery) {
                    break;
                }
                
                /*
                 * In order to join the session, we need to provide the well-known
                 * contact port.  This is pre-arranged between both sides as part
                 * of the definition of the chat service.  As a result of joining
                 * the session, we get a session identifier which we must use to 
                 * identify the created session communication channel whenever we
                 * talk to the remote side.
                 */
                short contactPort = CONTACT_PORT;
                SessionOpts sessionOpts = new SessionOpts();
                sessionOpts.transports = (short)msg.arg1;
                Mutable.IntegerValue sessionId = new Mutable.IntegerValue();
                
                Status status = mBus.joinSession((String) msg.obj, contactPort, sessionId, sessionOpts, new SessionListener() {
                    @Override
                    public void sessionLost(int sessionId, int reason) {
                        mIsConnected = false;
                        logInfo(String.format("MyBusListener.sessionLost(sessionId = %d, reason = %d)", sessionId,reason));
                        mHandler.sendEmptyMessage(MESSAGE_START_PROGRESS_DIALOG);
                    }
                });
                logStatus("BusAttachment.joinSession() - sessionId: " + sessionId.value, status);
                    
                if (status == Status.OK) {
                	/*
                     * To communicate with an AllJoyn object, we create a ProxyBusObject.  
                     * A ProxyBusObject is composed of a name, path, sessionID and interfaces.
                     * 
                     * This ProxyBusObject is located at the well-known SERVICE_NAME, under path
                     * "/SimpleService", uses sessionID of CONTACT_PORT, and implements the SimpleInterface.
                     */
                	mProxyObj =  mBus.getProxyBusObject(SERVICE_NAME, 
                										"/SimpleService",
                										sessionId.value,
                										new Class<?>[] { AlljoynInterface.class });

                	/* We make calls to the methods of the AllJoyn object through one of its interfaces. */
                	mSimpleInterface =  mProxyObj.getInterface(AlljoynInterface.class);
                	
                	mSessionId = sessionId.value;
                	mIsConnected = true;
                	mHandler.sendEmptyMessage(MESSAGE_STOP_PROGRESS_DIALOG);
                }
                break;
            }
            
            /* Release all resources acquired in the connect. */
            case DISCONNECT: {
            	mIsStoppingDiscovery = true;
            	if (mIsConnected) {
                	Status status = mBus.leaveSession(mSessionId);
                    logStatus("BusAttachment.leaveSession()", status);
            	}
                mBus.disconnect();
                getLooper().quit();
                break;
            }
            
            /*
             * Call the service's receiveTEmplate method through the ProxyBusObject.
             *
             */
            case TEMPLATE: {
            	try {
            	if (mSimpleInterface != null) {
            		String tpl = (String) msg.obj;
            		Toast.makeText(TemplateListActivity.this, "Trying to send template",Toast.LENGTH_SHORT).show();
            		String response = mSimpleInterface.receiveTemplate(tpl);
            		sendUiMessage(MESSAGE_TEMPLATE_REPLY, response);
            	}
                } catch (BusException ex) {
                    logInfo("SimpleInterface.receiveTemplate()");
                    sendUiMessage(MESSAGE_POST_TOAST, "You are not connected to service");
                }
            }
            default:
                break;
            }
        }
    }

    
	
	public void startService(View v) {
		Intent intent = new Intent(TemplateListActivity.this, Service.class);
		startActivity(intent);
	}
	
	private void logStatus(String msg, Status status) {
	    String log = String.format("%s: %s", msg, status);
	    if (status == Status.OK) {
	        Log.i(TAG, log);
	    } else {
	    	Message toastMsg = mHandler.obtainMessage(MESSAGE_POST_TOAST, log);
	        mHandler.sendMessage(toastMsg);
	        Log.e(TAG, log);
	    }
	}
	
	private void logException(String msg, BusException ex) {
	    String log = String.format("%s: %s", msg, ex);
	    Message toastMsg = mHandler.obtainMessage(MESSAGE_POST_TOAST, log);
	    mHandler.sendMessage(toastMsg);
	    Log.e(TAG, log, ex);
	}
	
	/*
	 * print the status or result to the Android log. If the result is the expected
	 * result only print it to the log.  Otherwise print it to the error log and
	 * Sent a Toast to the users screen. 
	 */
	private void logInfo(String msg) {
	        Log.i(TAG, msg);
	}
}
