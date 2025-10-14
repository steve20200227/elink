package cn.snowsoft.iot.module.cps.seriesDatabase.dolphindb.model;

import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseColumn;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseSeriesTable;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxdb.data.Entity;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class DolphindbTable extends BaseSeriesTable {
    public JSONArray createTableBuild(){
        List<BaseColumn> tableColumns = this.getTableColumns();
        JSONArray jsonArray = new JSONArray();
        DolphindbColumn column = new DolphindbColumn();
        for (BaseColumn tableColumn : tableColumns) {
            BeanUtils.copyProperties(tableColumn, column);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", column.getColumnName());
            jsonObject.put("type", column.getColumnType());
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

}
