package ua.com.juja.yar_tur.sqlcmd.model;

import java.util.LinkedList;
import java.util.List;

public class DataSet {


    private List<Data> columnList = new LinkedList<>();

    public List<Data> getData() {
        return columnList;
    }

    public void add(String columnName, String value) {
        columnList.add(new Data(columnName, value));
    }

    public class Data {
        private String columnName;
        private String value;

        Data(String columnName, String value) {
            this.columnName = columnName;
            this.value = value;
        }

        public String getName() {
            return columnName;
        }

        public Object getValue() {
            return value;
        }
    }
}
