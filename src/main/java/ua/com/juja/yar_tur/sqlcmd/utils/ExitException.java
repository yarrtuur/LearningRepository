package ua.com.juja.yar_tur.sqlcmd.utils;

public class ExitException extends Exception {

	public ExitException() {
	}

	public ExitException(String inException) {
		super(inException);
	}

	public ExitException(String inException, Throwable cause) {
		super(inException, cause);
	}
}
