package cn.snowsoft.iot.module.cps.seriesDatabase.common.model;

public class BaseColumn {
    private String columnName;

    //列类型
    private String columnType;


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }
}
