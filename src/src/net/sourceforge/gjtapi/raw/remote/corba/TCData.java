package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/TCData.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

public final class TCData implements org.omg.CORBA.portable.IDLEntity
{
  public int state = (int)0;
  public net.sourceforge.gjtapi.raw.remote.corba.TermData terminal = null;

  public TCData ()
  {
  } // ctor      
  public TCData (int _state, net.sourceforge.gjtapi.raw.remote.corba.TermData _terminal)
  {
	state = _state;
	terminal = _terminal;
  } // ctor        
} // class TCData
