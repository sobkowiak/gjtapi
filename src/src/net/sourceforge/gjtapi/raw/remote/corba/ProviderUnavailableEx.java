package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/ProviderUnavailableEx.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

public final class ProviderUnavailableEx extends org.omg.CORBA.UserException implements org.omg.CORBA.portable.IDLEntity
{
  public int cause = (int)0;
  public String reason = null;

  public ProviderUnavailableEx ()
  {
  } // ctor      
  public ProviderUnavailableEx (int _cause, String _reason)
  {
	cause = _cause;
	reason = _reason;
  } // ctor      
} // class ProviderUnavailableEx