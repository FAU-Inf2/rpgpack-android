package de.fau.cs.mad.gamekobold;



import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusListener;
import org.alljoyn.bus.Mutable;
import org.alljoyn.bus.ProxyBusObject;
import org.alljoyn.bus.SessionListener;
import org.alljoyn.bus.SessionOpts;
import org.alljoyn.bus.Status;


import org.alljoyn.bus.annotation.BusSignalHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AlljoynTest extends Activity {
	
	/* load alljoyn library */
	
	static {
		System.loadLibrary("alljoyn_java");
	}
	
    private static final int MESSAGE_PING = 1;
    private static final int MESSAGE_PING_REPLY = 2;
    private static final int MESSAGE_POST_TOAST = 3;
    private static final int MESSAGE_START_PROGRESS_DIALOG = 4;
    private static final int MESSAGE_STOP_PROGRESS_DIALOG = 5;
    private static final int SIGNAL_RECEIVED = 6;
	
    private static final String TAG = "Alljoyn";
    private ProgressDialog mDialog;
    TextView replyText;
    TextView signalText;
    
    /* Handler used to make calls to AllJoyn methods. See onCreate(). */
    private BusHandler mBusHandler;
    
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_PING_REPLY:
                String ret = (String) msg.obj;
                //mListViewArrayAdapter.add("Reply:  " + ret);
                //mEditText.setText("");
                replyText.setText(ret);
                break;
            case MESSAGE_POST_TOAST:
            	Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
            	break;
            case MESSAGE_START_PROGRESS_DIALOG:
                mDialog = ProgressDialog.show(AlljoynTest.this, 
                                              "", 
                                              "Finding Service.\nPlease wait...", 
                                              true,
                                              true);
                break;
            case MESSAGE_STOP_PROGRESS_DIALOG:
                mDialog.dismiss();
                break;
            case SIGNAL_RECEIVED:
            	Toast.makeText(getApplicationContext(), "Signal received", Toast.LENGTH_SHORT).show();
            default:
                break;
            }
        }
    };

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alljoyn_test);
	       
	    replyText = (TextView) findViewById(R.id.answerFromService);
	       
	    /* Make all AllJoyn calls through a separate handler thread to prevent blocking the UI. */
	    HandlerThread busThread = new HandlerThread("BusHandler");
	    busThread.start();
	    mBusHandler = new BusHandler(busThread.getLooper());

	   /* Connect to an AllJoyn object. */
	   mBusHandler.sendEmptyMessage(BusHandler.CONNECT);
	   mHandler.sendEmptyMessage(MESSAGE_START_PROGRESS_DIALOG);		
		
	}
	
	public void onBtnClicked(View v) {
        Message msg = mBusHandler.obtainMessage(BusHandler.PING, "count");
        mBusHandler.sendMessage(msg);
	}
	
	class SignalHandler {


		/*Signal handler */
        @BusSignalHandler(iface="de.fau.cs.mad.gamekobold.signalinterface", signal = "testSignal")
        public void testSignal(int number) {
        	mHandler.sendEmptyMessage(SIGNAL_RECEIVED);
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
        private boolean mIsInASession;
        private boolean mIsConnected;
        private boolean mIsStoppingDiscovery;
        
        /* These are the messages sent to the BusHandler from the UI. */
        public static final int CONNECT = 1;
        public static final int JOIN_SESSION = 2;
        public static final int DISCONNECT = 3;
        public static final int PING = 4;

        public BusHandler(Looper looper) {
            super(looper);
            
            mIsInASession = false;
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
                
                /*
                 *  We register our signal handler which is implemented inside the SimpleService
                 */
                status = mBus.registerSignalHandlers(SignalHandler.class);
            	if (status != Status.OK) {
            		Log.i(TAG, "Problem while registering signal handler");
                	return;
            	} else {
            		Log.i(TAG, "SignalHandler is registered");
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
             * Call the service's Ping method through the ProxyBusObject.
             *
             * This will also print the String that was sent to the service and the String that was
             * received from the service to the user interface.
             */
            case PING: {
                try {
                	if (mSimpleInterface != null) {
                		// sendUiMessage(MESSAGE_PING, msg.obj);
                		
                		/* here the actual request is done */
                		String reply = !((String) msg.obj).equals("json") 
                				? mSimpleInterface.count()
                				: mSimpleInterface.getJSON();
                		
                		sendUiMessage(MESSAGE_PING_REPLY, reply);
                		//mHandler.sendEmptyMessage(SIGNAL_RECEIVED);
                	}
                } catch (BusException ex) {
                    logException("SimpleInterface.Ping()", ex);
                }
                break;
            }
            default:
                break;
            }
        }
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
