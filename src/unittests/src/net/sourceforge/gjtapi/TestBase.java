/**
 * Copyright (c) 2010 Deadman Consulting Inc. (www.deadman.ca)

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
package net.sourceforge.gjtapi;

import java.util.Properties;

import net.sourceforge.gjtapi.raw.ProviderFactory;
import net.sourceforge.gjtapi.raw.emulator.EmProvider;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Test base class shared by others that need a provider
 * @author Richard Deadman
 *
 */
public class TestBase {
	protected static GenericProvider prov = null;
	
	/**
	 * Static initializer.
	 * Create the provider once
	 */
	@BeforeClass
	public static void classSetUp() {
		Properties emulatorProps = new Properties();
		emulatorProps.setProperty("Address1", "21");
		emulatorProps.setProperty("Address2", "22");
		emulatorProps.setProperty("Address3", "23");
		emulatorProps.setProperty("display", "false");
		emulatorProps.setProperty("throttle", "f");
		emulatorProps.setProperty("termSendPrivateData", "t");
		emulatorProps.setProperty("tcSendPrivateData", "t");
		
		TelephonyProvider tp = ProviderFactory.createProvider(new EmProvider());
		tp.initialize(emulatorProps);
		prov = new GenericProvider("Emulator", tp, emulatorProps);
	}
	
	@AfterClass
	public static void classTearDown() {
		prov.shutdown();
	}

}
