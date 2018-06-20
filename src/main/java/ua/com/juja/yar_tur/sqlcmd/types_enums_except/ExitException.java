package ua.com.juja.yar_tur.sqlcmd.types_enums_except;

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
