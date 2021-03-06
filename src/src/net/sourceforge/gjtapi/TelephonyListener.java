package net.sourceforge.gjtapi;

/*
 Copyright (c) 2002 8x8 Inc. (www.8x8.com) 

 All rights reserved. 

 Permission is hereby granted, free of charge, to any person obtaining a 
 copy of this software and associated documentation files (the 
 "Software"), to deal in the Software without restriction, including 
 without limitation the rights to use, copy, modify, merge, publish, 
 distribute, and/or sell copies of the Software, and to permit persons 
 to whom the Software is furnished to do so, provided that the above 
 copyright notice(s) and this permission notice appear in all copies of 
 the Software and that both the above copyright notice(s) and this 
 permission notice appear in supporting documentation. 

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS 
 OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT 
 OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR 
 HOLDERS INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL 
 INDIRECT OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING 
 FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, 
 NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION 
 WITH THE USE OR PERFORMANCE OF THIS SOFTWARE. 

 Except as contained in this notice, the name of a copyright holder 
 shall not be used in advertising or otherwise to promote the sale, use 
 or other dealings in this Software without prior written authorization 
 of the copyright holder.
 */
import java.io.Serializable;
import javax.telephony.media.Symbol;

/**
 * A listener for events from the raw provider.
 **/

public interface TelephonyListener {
    /**
     * Return Terminal data to the application
     * 
     * @author Richard Deadman
     * @param address
     *            The name of the address to return private data for.
     * @param data
     *            The data to return. Serializable to allow remote proxying.
     * @param The
     *            ProvPrivateEv cause field.
     **/
    void addressPrivateData(String address, Serializable data, int cause);

    /**
     * A call now exists in a real sense. Creation date: (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The call identifier
     * @param cause
     *            Event cause -- CAUSE_NORMAL, CAUSE_NEW_CALL or CAUSE_SNAPSHOT
     *            (see javax.telephony.events.Ev or javax.telephony.Event)
     */
    void callActive(CallId id, int cause);

    /**
     * Basically a call has ended.
     * <P>
     * Note that this will signal to the Framework to release the CallId for
     * future reuse, so this should be the last event ever issued for the
     * logical call. Future uses of CallId will be interpreted as the implied
     * creation of a new call. Thus Call teardown should be careful to report
     * TerminalConnection dropped and Connection disconnected events before the
     * call invalid event. Creation date: (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The call identifier
     * @param cause
     *            The Event cause constant that identifies why the call went to
     *            Invalid state.
     */
    void callInvalid(CallId id, int cause);

    /**
     * Tell any registered Jain Provider that a call overload on a set address
     * has ended. Creation date: (2000-11-14 15:37:35)
     * 
     * @param address
     *            The name of the address affected.
     */
    void callOverloadCeased(String address);

    /**
     * Tell any registered Jain Provider that a call overload on a set address
     * has occurred. Creation date: (2000-11-14 15:37:35)
     * 
     * @param address
     *            The name of the address affected.
     */
    void callOverloadEncountered(String address);

    /**
     * Return Terminal data to the application
     * 
     * @author Richard Deadman
     * @param call
     *            The CallId of the call to return private data for.
     * @param data
     *            The data to return. Serializable to allow remote proxying.
     * @param The
     *            ProvPrivateEv cause field.
     **/
    void callPrivateData(CallId call, Serializable data, int cause);

    /**
     * Jain ADDRESS_AUTHORIZE event. Should not be generated by non-Jain stacks.
     * Represents the connection ADDRESS_ANALYZE state. Entry criteria This
     * state is entered on the availability of complete initial information
     * package/dialing string from the originating party. Functions: The
     * information collected is analyzed and/or translated according to a
     * dialing plan to determine routing address and call type (e.g. local
     * exchange call, transit exchange call, international exchange call). Exit
     * criteria: This state is exited on the availability of routing address.
     * Invalid information and Abandon indications also cause transition out of
     * this state. Exception criteria such as network busy, abandon, route busy
     * etc. will cause exit from this state. Creation date: (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The call of the connection
     * @param address
     *            The address the connection has
     * @param cause
     *            The Event cause code (CAUSE_NORMAL, CAUSE_SNAPSHOT) (see
     *            javax.telephony.events.Ev or javax.telephony.Event)
     */
    void connectionAddressAnalyse(CallId id, String address, int cause);

    /**
     * Jain ADDRESS_COLLECT event. Should not be generated by non-Jain stacks.
     * Represents the connection ADDRESS_COLLECT state. Entry criteria The
     * JccConnection object enters this state with the originating party having
     * been authorized for this call. Functions: In this state the initial
     * information package is collected from the originating party. Information
     * is examined according to dialing plan to determine the end of collection.
     * No further action may be required if en bloc signaling method is in use.
     * Exit criteria: This state is exited either because the complete initial
     * information package or dialing string has been collected from the
     * originating party or because of failure to collect information or even
     * due to reception of invalid information from the caller. Timeout and
     * abandon indications may also cause the exit from this state.
     * 
     * @author: Richard Deadman
     * @param id
     *            The call of the connection
     * @param address
     *            The address the connection has
     * @param cause
     *            The Event cause code (CAUSE_NORMAL, CAUSE_SNAPSHOT) (see
     *            javax.telephony.events.Ev or javax.telephony.Event)
     */
    void connectionAddressCollect(CallId id, String address, int cause);

    /**
     * A connection between a call and an address is ringing. Creation date:
     * (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The call of the connection
     * @param address
     *            The address the connection has
     * @param cause
     *            The Event cause code (CAUSE_NORMAL, CAUSE_SNAPSHOT,
     *            CAUSE_NEW_CALL) (see javax.telephony.events.Ev or
     *            javax.telephony.Event)
     */
    void connectionAlerting(CallId id, String address, int cause);

    /**
     * Jain AUTHORIZE_CALL_ATTEMPT event. Should not be generated by non-Jain
     * stacks. Represents the connection AUTHORIZE_CALL_ATTEMPT state. This
     * state implies that the originating or terminating terminal needs to be
     * authorized for the call. Entry criteria An indication that the
     * originating or terminating terminal needs to be authorized for the call.
     * Functions: The originating or terminating terminal characteristics should
     * be verified using the calling party's identity and service profile. The
     * authority/ability of the party to place the call with given properties is
     * verified. The types of authorization may vary for different types of
     * originating and terminating resources. Exit criteria: The JccConnection
     * object exits this state on receiving indication of the success or failure
     * of the authorization process. The originating JccConnection might move to
     * the ADDRESS_COLLECT state while the terminating JccConnection has to move
     * to the CALL_DELIVERY state or beyond. Thus, the terminating JccConnection
     * cannot be either in the ADDRESS_COLLECT or the ADDRESS_ANALYZE states.
     * 
     * @author: Richard Deadman
     * @param id
     *            The call of the connection
     * @param address
     *            The address the connection has
     * @param cause
     *            The Event cause code (CAUSE_NORMAL, CAUSE_SNAPSHOT) (see
     *            javax.telephony.events.Ev or javax.telephony.Event)
     */
    void connectionAuthorizeCallAttempt(CallId id, String address, int cause);

    /**
     * Jain CALL_DELIVERY event. Should not be generated by non-Jain stacks.
     * Represents the connection CALL_DELIVERY state. Entry criteria: This state
     * is entered on the originating side when the routing address and call type
     * are available. On the terminating side this state is entered when the
     * termination attempt to the address is authorized. Function: On the
     * originating side this state involves selecting of the route as well as
     * sending an indication of the desire to set up a call to the specified
     * called party. On the terminating side this state is involves checking the
     * busy/idle status of the terminating access and also informing the
     * terminating message of an incoming call. Exit criteria: This state is
     * exited on the originating side when criteria such as receipt of an
     * alerting indication or call accepted is received from the terminating
     * call portion. This state is exited on the terminating side when the
     * terminating party is being alerted or the call is accepted.
     * 
     * @author: Richard Deadman
     * @param id
     *            The call of the connection
     * @param address
     *            The address the connection has
     * @param cause
     *            The Event cause code (CAUSE_NORMAL, CAUSE_SNAPSHOT) (see
     *            javax.telephony.events.Ev or javax.telephony.Event)
     */
    void connectionCallDelivery(CallId id, String address, int cause);

    /**
     * A connection between a call and an address is connected. Creation date:
     * (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The call of the connection
     * @param address
     *            The address the connection has
     * @param cause
     *            The Event cause code (CAUSE_NORMAL, CAUSE_SNAPSHOT) (see
     *            javax.telephony.events.Ev or javax.telephony.Event)
     */
    void connectionConnected(CallId id, String address, int cause);

    /**
     * A connection between a call and an address is disconnected. Creation
     * date: (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The call of the connection
     * @param address
     *            The address the connection has
     * @param cause
     *            The Event cause code (CAUSE_NORMAL, CAUSE_SNAPSHOT, ...) (see
     *            javax.telephony.events.Ev or javax.telephony.Event)
     */
    void connectionDisconnected(CallId id, String address, int cause);

    /**
     * A connection between a call and an address has failed. Creation date:
     * (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The call of the connection
     * @param address
     *            The address the connection has
     * @param cause
     *            The Event cause code detailing why the connection failed.
     */
    void connectionFailed(CallId id, String address, int cause);

    /**
     * A connection between a call and an address is being processed. Creation
     * date: (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The call of the connection
     * @param address
     *            The address the connection has
     * @param cause
     *            The Event cause code (CAUSE_NORMAL, CAUSE_SNAPSHOT) (see
     *            javax.telephony.events.Ev or javax.telephony.Event)
     */
    void connectionInProgress(CallId id, String address, int cause);

    /**
     * Jain SUSPEND event. Should not be generated by non-Jain stacks.
     * Represents the SUSPEND state. This state implies that this JccConnection
     * object is suspended from the call, although it's references to a JccCall
     * and JccAddress objects will stil remain valid. Entry criteria: A suspend
     * indication is received that the terminating party has disconnected, but
     * disconnect timing has not completed. This state might also be entered on
     * cases like the flash hook. Function: The connections for the originating
     * and terminating party are maintained and depending on the incoming
     * network connection, appropriate backward signaling takes place. Exit
     * criteria: Exception criteria such as disconnect cause exit from this
     * state.
     * 
     * @author: Richard Deadman
     * @param id
     *            The call of the connection
     * @param address
     *            The address the connection has
     * @param cause
     *            The Event cause code (CAUSE_NORMAL, CAUSE_SNAPSHOT) (see
     *            javax.telephony.events.Ev or javax.telephony.Event)
     */
    void connectionSuspended(CallId id, String address, int cause);

    /**
     * A MediaService's player has paused due to an RTC Creation date:
     * (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param terminal
     *            The terminal that the media service is connected to.
     * @param index
     *            The media stream that was paused
     * @param offset
     *            How far into the media stream we were
     * @param trigger
     *            The symbol representing the RTC that triggered the pause
     */
    void mediaPlayPause(String terminal, int index, int offset, Symbol trigger);

    /**
     * A MediaService's player has resumed due to an RTC Creation date:
     * (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param terminal
     *            The terminal that the media service is connected to.
     * @param trigger
     *            The symbol representing the RTC that triggered the pause
     */
    void mediaPlayResume(String terminal, Symbol trigger);

    /**
     * A MediaService's player has paused due to an RTC Creation date:
     * (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param terminal
     *            The terminal that the media service is connected to.
     * @param duration
     *            The length of the recording, in milliseconds
     * @param trigger
     *            The symbol representing the RTC that triggered the pause
     */
    void mediaRecorderPause(String terminal, int duration, Symbol trigger);

    /**
     * A MediaService's recorder has resumed due to an RTC Creation date:
     * (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param terminal
     *            The terminal that the media service is connected to.
     * @param trigger
     *            The symbol representing the RTC that triggered the resume
     */
    void mediaRecorderResume(String terminal, Symbol trigger);

    /**
     * A MediaService's signal detector has detected some signals and
     * p_enabledEvents includes ev_SignalDetected Creation date: (2000-04-15
     * 0:38:57)
     * 
     * @author: Richard Deadman
     * @param terminal
     *            The terminal that the media service is connected to.
     * @param sig
     *            The detected signal
     */
    void mediaSignalDetectorDetected(String terminal, Symbol[] sigs);

    /**
     * A MediaService's signal detector has overflowed and p_enableEvents
     * contains ev_Overflow Creation date: (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param terminal
     *            The terminal that the media service is connected to.
     * @param sigs
     *            The signals detected up to the overflow
     */
    void mediaSignalDetectorOverflow(String terminal, Symbol[] sigs);

    /**
     * A MediaService's signal detector has matched a pattern and p_enableEvents
     * contains ev_Pattern[i] Creation date: (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param terminal
     *            The terminal that the media service is connected to.
     * @param sigs
     *            The signals detected up to the overflow
     * @param index
     *            The index into the ev_pattern array
     */
    void mediaSignalDetectorPatternMatched(String terminal, Symbol[] sigs,
            int index);

    /**
     * Return Provider data to the application
     * 
     * @author Richard Deadman
     * @param data
     *            The data to return. Serializable to allow remote proxying.
     * @param The
     *            ProvPrivateEv cause field.
     **/
    void providerPrivateData(Serializable data, int cause);

    /**
     * A terminal connection between a call and an address's terminal has been
     * created. Creation date: (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The Call of the Terminal Connection
     * @param address
     *            The address of the Terminal Connection
     * @param terminal
     *            The physical endpoint of the Terminal Connection
     * @param cause
     *            The Event cause code (CAUSE_NORMAL, CAUSE_SNAPSHOT,
     *            CAUSE_NEW_CALL) (see javax.telephony.events.Ev or
     *            javax.telephony.Event)
     */
    void terminalConnectionCreated(CallId id, String address, String terminal,
            int cause);

    /**
     * A terminal connection between a call and an address's terminal has been
     * dropped. Creation date: (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The Call of the Terminal Connection
     * @param address
     *            The address of the Terminal Connection
     * @param terminal
     *            The physical endpoint of the Terminal Connection
     * @param cause
     *            The Event cause code describing why the TerminalConnection was
     *            dropped.
     */
    void terminalConnectionDropped(CallId id, String address, String terminal,
            int cause);

    /**
     * A terminal connection between a call and an address's terminal has become
     * active and is held. Creation date: (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The Call of the Terminal Connection
     * @param address
     *            The address of the Terminal Connection
     * @param terminal
     *            The physical endpoint of the Terminal Connection
     * @param cause
     *            The Event cause code describing the event trigger
     *            (CAUSE_NORMAL, CAUSE_SNAPSHOT) (see javax.telephony.events.Ev
     *            or javax.telephony.Event)
     */
    void terminalConnectionHeld(CallId id, String address, String terminal,
            int cause);

    /**
     * A terminal connection between a call and an address's terminal is
     * ringing. Creation date: (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The Call of the Terminal Connection
     * @param address
     *            The address of the Terminal Connection
     * @param terminal
     *            The physical endpoint of the Terminal Connection
     * @param cause
     *            The Event cause code descibing why the event was sent
     *            (CAUSE_NORMAL, CAUSE_SNAPSHOT) (see javax.telephony.events.Ev
     *            or javax.telephony.Event)
     */
    void terminalConnectionRinging(CallId id, String address, String terminal,
            int cause);

    /**
     * A terminal connection between a call and an address's terminal has become
     * active and is talking. Creation date: (2000-04-15 0:38:57)
     * 
     * @author: Richard Deadman
     * @param id
     *            The Call of the Terminal Connection
     * @param address
     *            The address of the Terminal Connection
     * @param terminal
     *            The physical endpoint of the Terminal Connection
     * @param cause
     *            The Event cause code descibing why the event was sent
     *            (CAUSE_NORMAL, CAUSE_SNAPSHOT) (see javax.telephony.events.Ev
     *            or javax.telephony.Event)
     */
    void terminalConnectionTalking(CallId id, String address, String terminal,
            int cause);

    /**
     * Return Terminal data to the application
     * 
     * @author Richard Deadman
     * @param terminal
     *            The name of the terminal to return private data for.
     * @param data
     *            The data to return. Serializable to allow remote proxying.
     * @param The
     *            ProvPrivateEv cause field.
     **/
    void terminalPrivateData(String terminal, Serializable data, int cause);
}
