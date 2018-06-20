package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.types_enums_except.ExitException;

public interface MakeDBConnectLine extends CommandProcess {
	String setSocketProperties() throws ExitException;
}
