package ua.com.juja.sqlcmd.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DataSet implements Iterator {


    class Data{
        private String columnName;
        private Object value;

        public Data(String columnName, String value) {
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

    private List<Data> columnList = new LinkedList<>();

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        return null;
    }

    public void add(String columnName, String value){
        columnList.add( new Data( columnName, value ) );
    }


}
