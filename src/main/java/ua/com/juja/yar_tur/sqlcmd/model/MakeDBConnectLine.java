package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.utils.ExitException;

public interface MakeDBConnectLine extends CommandProcess {
	String setSocketProperties() throws ExitException;
}
