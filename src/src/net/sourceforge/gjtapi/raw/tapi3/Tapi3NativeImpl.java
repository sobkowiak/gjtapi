/*
	Copyright (c) 2005 Serban Iordache 
	
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
package net.sourceforge.gjtapi.raw.tapi3;

import java.util.Map;

import net.sourceforge.gjtapi.raw.tapi3.logging.Logger;

public class Tapi3NativeImpl implements Tapi3Native {
	private static final Logger logger = Tapi3Provider.getLogger();
    static {
        try {
			System.loadLibrary("Tapi3Provider");
		} catch (Throwable t) {
			String msg = "Library Tapi3Provider could not be loaded from " + System.getProperty("java.library.path");
			logger.error(msg, t);
			throw new RuntimeException(msg, t);
		}        
    }    
    private static final Tapi3NativeImpl instance = new Tapi3NativeImpl();
    
    private Tapi3Provider provider = null;
    
    private Tapi3NativeImpl() {}
    
    public static final Tapi3Native getInstance() {
        return instance;
    }
    
    /**
     * @param props
     * @return array of addresses or null on error
     */
    public native String[] tapi3Init(Map props);
    
    /**
     * @return error code (0=success) 
     */
    public native int tapi3Shutdown();
    
    /**
     * @param callID
     * @return error code (0=success) 
     */
    public native int tapi3AnswerCall(int callID); 
    
    /**
     * @param callID
     * @return error code (0=success) 
     */
    public native int tapi3DisconnectCall(int callID); 
    
    /**
     * @param callID
     * @return error code (0=success) 
     */
    public native int tapi3ReleaseCall(int callID); 

    /**
     * @param address
     * @return the reserved callID or a negative error code
     */
    public native int tapi3ReserveCallId(String address);

    /**
     * @param callID
     * @param address
     * @param dest
     * @return the callID or a negative error code
     */
    public native int tapi3CreateCall(int callID, String address, String dest); 

    /**
     * @param callID
     * @param address
     * @return error code (0=success) 
     */
    public native int tapi3Hold(int callID);
    
    /**
     * @param callID1
     * @param callID2
     * @return callID or a negative error code
     */
    public native int tapi3Join(int callID1, int callID2);
    
    /**
     * @param callID
     * @return error code (0=success) 
     */
    public native int tapi3UnHold(int callID);
    
    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.tapi3.Tapi3Native#tapi3SendSignals(java.lang.String, java.lang.String)
     */
    public native int tapi3SendSignals(String terminal, String digits);
    
    public void callback(int methodID, int callID, String address, int jniCause, String[] callInfo) {
        if(provider != null) {
            provider.callback(methodID, callID, address, jniCause, callInfo);
        } else {
        	Tapi3Provider.getLogger().error("Callback " + methodID + " called, but no provider registered.");
        }
    }

    public void registerProvider(Tapi3Provider provider) {
        this.provider = provider;
    }

}
