package net.sourceforge.gjtapi.raw.mjsip;

import net.sourceforge.gjtapi.raw.MediaTpi;
import net.sourceforge.gjtapi.raw.mjsip.ua.UA;

import net.sourceforge.gjtapi.CallId;
import net.sourceforge.gjtapi.RawSigDetectEvent;
import net.sourceforge.gjtapi.RawStateException;
import net.sourceforge.gjtapi.TelephonyListener;
import net.sourceforge.gjtapi.TermData;

import javax.telephony.media.MediaResourceException;
import javax.telephony.media.RTC;
import javax.telephony.media.Symbol;
import javax.telephony.media.PlayerConstants;
import javax.telephony.media.RecorderConstants;
import javax.telephony.ProviderUnavailableException;
import javax.telephony.ResourceUnavailableException;
import javax.telephony.InvalidArgumentException;
import javax.telephony.PrivilegeViolationException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.InvalidPartyException;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;

import java.util.Properties;
import java.util.Dictionary;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.concurrent.Semaphore;


/**
 * This is a provider that hooks into the MjSip to provide
 * Session Initiation Protocol support for GJTAPI
 *
 * If possible disable ReINVITEs in you registrar. There has been some problems
 * between MjSIP UserAgent and X-Lite during call hangup, causing the UserAgent
 * to keep his online status. (On Asterisk use canreinvite=no on sip.conf)
 *
 */
public class MjSipProvider implements MediaTpi {

    private HashMap<String, UA> loadedUAs = new HashMap<String, UA>();
    private TelephonyListener listener;

    private Semaphore playSemaphore;
    private Semaphore recSemaphore;

    public MjSipProvider() {
        playSemaphore = new Semaphore(1, true);
        recSemaphore = new Semaphore(1, true);
    }

    // ******************* Core GJTAPI Functions ******************

    /**
     * {@inheritDoc}
     */
    public void initialize(java.util.Map props) throws
            ProviderUnavailableException {

        String strPhone = (String) props.get("gjtapi.mjsip.ua");
        if (strPhone == null) {
            try {
                InputStream pIS = MjSipProvider.class.getResourceAsStream(
                        "/MjSip.props");
                Properties properties = System.getProperties();
                properties.load(pIS);
                pIS.close();
                strPhone = properties.getProperty("gjtapi.mjsip.ua");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("strPhone: " + strPhone);
        StringTokenizer st = new StringTokenizer(strPhone, ",");

        while (st.hasMoreTokens()) {
            try {
                String phone = st.nextToken();
                UA ua = new UA(phone, this);
                loadedUAs.put(ua.getAddress(), ua);
                //System.out.println("MjSipProvider, initialize(): UA: " + ua.getAddress());
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Properties getCapabilities() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getAddresses() throws ResourceUnavailableException {
        String[] ret = new String[loadedUAs.size()];
        int i = 0;
        for (String addresses : loadedUAs.keySet()) {
            ret[i] = addresses;
            i++;
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getAddresses(String terminal) throws
            InvalidArgumentException {
        UA ua = loadedUAs.get(terminal);
        if (ua == null) {
            return new String[] {};
        } else {
            return new String[] {terminal};
        }
    }

    /**
     * {@inheritDoc}
     */
    public TermData[] getTerminals() throws ResourceUnavailableException {
        TermData[] ret = new TermData[loadedUAs.size()];
        int i = 0;
        for (String address : loadedUAs.keySet()) {
            ret[i] = new TermData(address, true);
            i++;
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    public TermData[] getTerminals(String address) throws
            InvalidArgumentException {
        UA ua = loadedUAs.get(address);
        if (ua == null) {
            return new TermData[] {};
        } else {
            return new TermData[] {
                    new TermData(address, true)
            };
        }
    }

    /**
     * {@inheritDoc}
     */
    public void releaseCallId(CallId id) {
    }


    /** Add a listener for RawEvnts
     * Creation date: (2007-10-02 16:30:54)
     * @author:
     * @return
     * @param to Listener to add
     * @throws
     *
     */
    public void addListener(TelephonyListener ro) {
        if (listener == null) {
            listener = ro;
        } else {
            System.err.println("Request to add a TelephonyListener to "
                               + this.getClass().getName() +
                               ", but one is already registered");
        }

    }

    /**
     * {@inheritDoc}
     */
    public void removeListener(TelephonyListener ro) {
        if (ro == listener) {
            listener = null;
        } else {
            System.err.println("Request to remove a TelephonyListener from "
                               + this.getClass().getName() +
                               ", but it wasn't registered");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        for (UA ua : loadedUAs.values()) {
            ua.hangup();
            ua.closeMediaApplication();
        }
        loadedUAs.clear();
    }


    //	 ******************* Basic Call Control ******************

    /**
     * {@inheritDoc}
     */
    public void answerCall(CallId call, String address, String terminal) throws
            PrivilegeViolationException, ResourceUnavailableException,
            MethodNotSupportedException, RawStateException {

        //Get UA
        UA ua = loadedUAs.get(address);
        if (ua == null) {
            throw new ResourceUnavailableException(ResourceUnavailableException.
                    ORIGINATOR_UNAVAILABLE, "Address not found: " + address);
        }
        if (ua.getMjSipCallId().equals((Object) call)) {
            ua.accept();
        } else {
            throw new ResourceUnavailableException(ResourceUnavailableException.
                    UNSPECIFIED_LIMIT_EXCEEDED);
        }
    }

    /**
     * {@inheritDoc}
     */
    public CallId reserveCallId(String address) throws InvalidArgumentException {
        MjSipCallId id = new MjSipCallId();
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public CallId createCall(CallId id, String address, String term,
                             String dest) throws ResourceUnavailableException,
            PrivilegeViolationException, InvalidPartyException,
            InvalidArgumentException, RawStateException,
            MethodNotSupportedException {

        //Get UA
        UA ua = loadedUAs.get(address);
        if (ua == null) {
            throw new ResourceUnavailableException(ResourceUnavailableException.
                    ORIGINATOR_UNAVAILABLE, "Address not found: " + address);
        }
        ua.setMjSipCallId((MjSipCallId) id);
        ua.call(dest);
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public void release(String address, CallId call) throws
            PrivilegeViolationException, ResourceUnavailableException,
            MethodNotSupportedException, RawStateException {

        UA ua = loadedUAs.get(address);
        if (ua == null) {
            throw new ResourceUnavailableException(ResourceUnavailableException.
                    ORIGINATOR_UNAVAILABLE, "Address not found: " + address);
        }
        ua.hangup();
    }


    //	 ******************* TelephonyListener ******************

    /**
     * {@inheritDoc}
     */
    public void callActive(CallId id, int cause) {
        if (listener != null)
            listener.callActive(id, cause);
    }

    /**
     * {@inheritDoc}
     */
    public void connectionInProgress(CallId id, String address, int cause) {
        if (listener != null)
            listener.connectionInProgress(id, address, cause);
    }

    /**
     * {@inheritDoc}
     */
    public void connectionAlerting(CallId id, String address, int cause) {
        if (listener != null)
            listener.connectionAlerting(id, address, cause);
    }

    /**
     * {@inheritDoc}
     */
    public void connectionConnected(CallId id, String address, int cause) {
        if (listener != null)
            listener.connectionConnected(id, address, cause);
    }

    /**
     * {@inheritDoc}
     */
    public void connectionDisconnected(CallId id, String address, int cause) {

        //Get UA
        UA ua = loadedUAs.get(address);
        if (ua != null) {
            ua.stopPlay();
            ua.stopRecord();
        }

        if (listener != null) {
            listener.connectionDisconnected(id, address, cause);
        }

    }

    /**
     * {@inheritDoc}
     */
    public void terminalConnectionCreated(CallId id, String address,
                                          String terminal, int cause) {
        if (listener != null)
            listener.terminalConnectionCreated(id, address, terminal, cause);
    }

    /**
     * {@inheritDoc}
     */
    public void terminalConnectionRinging(CallId id, String address,
                                          String terminal, int cause) {
        if (listener != null)
            listener.terminalConnectionRinging(id, address, terminal, cause);
    }


    //	 *************************** Media *************************

    /**
     * {@inheritDoc}
     */
    public boolean allocateMedia(String terminal, int type,
                                 Dictionary resourceArgs) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean freeMedia(String terminal, int type) {
        //Get UA
        UA ua = loadedUAs.get(terminal);
        if (ua == null) {
            return false;
        } else {
            ua.closeMediaApplication();
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void play(String terminal, String[] streamIds, int offset,
                     RTC[] rtcs, Dictionary optArgs) throws
            MediaResourceException {

        try {
            //System.out.println("GJTAPI Play, starting");
            do {
                try {
                    playSemaphore.acquire(1);
                    break;
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } while (true);
            //System.out.println("GJTAPI Play, acquired semaphore");

            //Get UA
            UA ua = loadedUAs.get(terminal);
            if (ua == null) {
                playSemaphore.release(1);
                throw new MediaResourceException("Terminal not found: " +
                                                 terminal);
            }
            for (String streamID : streamIds) {
                URI uri = new URI(streamID);

                if (uri.getScheme().equals("file")) {
                    FileInputStream fis = new FileInputStream(uri.getPath());
                    ua.play(fis);
                } else {
                    URL url = new URL(streamID);
                    URLConnection c = url.openConnection();
                    c.connect();
                    InputStream is = c.getInputStream();
                    ua.play(is);
                    is.close();
                }
            }
        } catch (IllegalMonitorStateException e) {
            e.printStackTrace();
            throw new MediaResourceException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MediaResourceException(e.getMessage());
        } finally {
            //System.out.println("GJTAPI Play, finished");
            playSemaphore.release(1);
        }
    }

    public void record(String terminal, String streamId, RTC[] rtcs,
                       Dictionary optArgs) throws MediaResourceException {

        //System.out.println("GJTAPI Record, starting");
        do {
            try {
                recSemaphore.acquire(1);
                break;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } while (true);
        //System.out.println("GJTAPI Record, acquired semaphore");

        //Get UA
        UA ua = loadedUAs.get(terminal);
        if (ua == null) {
            recSemaphore.release(1);
            throw new MediaResourceException("Terminal not found: " + terminal);
        }
        try {
            URI uri = new URI(streamId);
            if (uri.getScheme().equals("file")) {
                FileOutputStream fos = new FileOutputStream(uri.getPath());
                ua.record(fos);
            } else {
                URL url = new URL(streamId);
                URLConnection c = url.openConnection();
                c.connect();
                OutputStream os = c.getOutputStream();
                ua.record(os);
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MediaResourceException();
        } finally {
            //System.out.println("GJTAPI Record, finished");
            recSemaphore.release(1);
        }
    }

    public RawSigDetectEvent retrieveSignals(String terminal, int num,
                                             Symbol[] patterns, RTC[] rtcs,
                                             Dictionary optArgs) throws
            MediaResourceException {
        return null;
    }

    public void sendSignals(String terminal, Symbol[] syms, RTC[] rtcs,
                            Dictionary optArgs) throws MediaResourceException {
    }

    public void stop(String terminal) {
        //Get UA
        UA ua = loadedUAs.get(terminal);
        if (ua == null) {
            return;
        }
        ua.stop();
    }

    public void triggerRTC(String terminal, Symbol action) {
        //Get UA
        UA ua = loadedUAs.get(terminal);
        if (ua == null) {
            return;
        }
        if (action.equals(PlayerConstants.rtca_Stop)) {
            //System.out.println("JTAPI stop Play, triggerRTC");
            ua.stopPlay();
        }
        else if (action.equals(RecorderConstants.rtca_Stop)) {
            //System.out.println("GJTAPI stop Record, triggerRTC");
            ua.stopRecord();
        }
    }

    public boolean isMediaTerminal(String terminal) {
        return true;
    }
}