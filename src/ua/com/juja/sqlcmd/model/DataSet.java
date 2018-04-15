package ua.com.juja.sqlcmd.model;

import java.util.LinkedList;
import java.util.List;

public class DataSet  {


    public class Data{
        private String columnName;
        private String value;

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

    public List<Data> getData(){
        return columnList;
    }

    public String getColumns(){
        //TODO
        return null;
    }

    public String getValues(){
        //TODO
        return null;
    }

    public void add(String columnName, String value){
       //TODO check unique columnName
        columnList.add( new Data( columnName, value ) );
    }


}
