package ua.com.juja.yar_tur.sqlcmd.model;

public interface MakeDBConnectLine extends CommandProcess {
	String setSocketProperties();

	String setSocketData(String[] commandLine);
}
