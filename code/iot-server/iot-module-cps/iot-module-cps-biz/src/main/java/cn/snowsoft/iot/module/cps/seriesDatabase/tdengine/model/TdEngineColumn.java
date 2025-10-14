package cn.snowsoft.iot.module.cps.seriesDatabase.tdengine.model;

import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseColumn;

public class TdEngineColumn extends BaseColumn {

    @Override
    public String getColumnType() {
        String columnType = super.getColumnType();
        switch (columnType) {
            case "bool":
                return "BOOLEAN";
            case "float":
                return "DOUBLE";
            case "string":
                return "VARCHAR(255)";
            case "date":
                return "TIMESTAMP";
        }
        return super.getColumnType();
    }
}
