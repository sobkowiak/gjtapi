package net.sourceforge.gjtapi.events;

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
import javax.telephony.callcontrol.*;

import net.sourceforge.gjtapi.FreeCall;
/**
 * A CallControl dropped event
 * Creation date: (2000-08-03 14:15:39)
 * @author: Richard Deadman
 */
public class CCTermConnRingingEv extends CCTermConnEv {
/**
 * CCtermConnDroppedEv constructor comment.
 * @param cause int
 * @param tc net.sourceforge.gjtapi.FreeTerminalConnection
 */
public CCTermConnRingingEv(int cause, net.sourceforge.gjtapi.FreeTerminalConnection tc) {
	super(cause, tc);
}
/**
 * Return the id for the CallControl ringing event.
 */
public int getID() {
	return CallControlTerminalConnection.RINGING;
}
/**
 * Define how an event dispatches itself to registered clients.
 * Creation date: (2000-04-26 10:48:23)
 * @author: Richard Deadman
 */
public void dispatch() {
	super.dispatch();	// send to Obsersers

	// now send to listeners
	((CallControlTerminalConnectionListener)((FreeCall)this.getCall()).getListener()).terminalConnectionRinging(this);
}

}
