package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/StringEntry.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

public final class StringEntry implements org.omg.CORBA.portable.IDLEntity
{
  public String key = null;
  public String value = null;

  public StringEntry ()
  {
  } // ctor      
  public StringEntry (String _key, String _value)
  {
	key = _key;
	value = _value;
  } // ctor      
} // class StringEntry
