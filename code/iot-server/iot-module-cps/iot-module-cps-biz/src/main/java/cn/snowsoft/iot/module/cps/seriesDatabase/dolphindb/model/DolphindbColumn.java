package cn.snowsoft.iot.module.cps.seriesDatabase.dolphindb.model;

import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseColumn;

public class DolphindbColumn extends BaseColumn {
    @Override
    public String getColumnType() {
        String columnType = super.getColumnType();
        switch (columnType) {
            case "bool":
                return "BOOL";
            case "float":
                return "DOUBLE";
            case "string":
                return "STRING";
            case "datetime":
                return "TIMESTAMP";
        }
        return super.getColumnType();
    }

}
