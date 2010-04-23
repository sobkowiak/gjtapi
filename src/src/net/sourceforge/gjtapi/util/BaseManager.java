package net.sourceforge.gjtapi.util;

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
import java.util.*;
/**
 * This is a queue that accepts objects and processes these objects.
 * A user will create and instance of the EventHandler and pass it into this event collector.
 * Subclasses include an Ordered and Parallel Event version for when the objects are visitors to a common
 * handler, and Ordered Block version for when the common held object visits each queue item.
 * Creation date: (2000-02-24 17:01:26)
 * @author: Richard Deadman
 */
public abstract class BaseManager {
	private LinkedList<EventHandler> events = new LinkedList<EventHandler>(); // events waiting to be processed
	protected BaseManager self = this;
/**
 * Returns the current number of objects in the queue.
 * Creation date: (2000-02-24 17:11:30)
 * @author: Richard Deadman
 * @return The number of queued objects
 */
public int available() {
	return events.size();
}
/**
 * Gets the next object on the queue.  If none is in the queue we wait indefinitely until one appears.
 * Creation date: (2000-02-24 17:08:37)
 * @author: Richard
 * @return The returned object.
 */
protected EventHandler get() throws InterruptedException {
	synchronized(events) {
		if (events.size() == 0)
			events.wait();
		return events.removeFirst();
	}
}
/**
 * Add a new object to the queue.  If anyone is waiting for objects they are activated.
 * Creation date: (2000-02-24 17:05:21)
 * @author: Richard Deadman
 * @param o The object to add
 */
protected void put(EventHandler o) {
	synchronized(events) {
		events.add(o);
		events.notify();
	}
}
}
