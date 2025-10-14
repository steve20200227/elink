package cn.snowsoft.iot.module.cps.seriesDatabase.common.model;

import cn.snowsoft.iot.module.cps.seriesDatabase.tdengine.model.TdEngineColumn;
import lombok.Data;

import java.util.List;
@Data
public class BaseSeriesTable {

    //表名
    private String tableName;
    //

    private List<BaseColumn> tableColumns;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<BaseColumn> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(List<BaseColumn> tableColumns) {
        this.tableColumns = tableColumns;
    }



}
