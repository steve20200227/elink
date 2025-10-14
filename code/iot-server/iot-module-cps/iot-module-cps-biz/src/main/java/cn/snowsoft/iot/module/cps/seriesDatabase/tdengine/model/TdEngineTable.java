package cn.snowsoft.iot.module.cps.seriesDatabase.tdengine.model;

import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseColumn;
import cn.snowsoft.iot.module.cps.seriesDatabase.common.model.BaseSeriesTable;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TdEngineTable extends BaseSeriesTable {


    public String createTableSql(){
        StringBuilder createSql = new StringBuilder();
        //创建超级表
        createSql.append("create  TABLE IF NOT EXISTS `ST_" + this.getTableName() + "`( ${column} )TAGS ( `deviceCode` nchar(128))");
        List<BaseColumn> tableColumns = this.getTableColumns();
        //TdEngine第一列必须为时间戳
        TdEngineColumn column = new TdEngineColumn();
        column.setColumnName("thetime");
        column.setColumnType("timestamp");
        tableColumns.add(0,column);
        TdEngineColumn columnType = new TdEngineColumn();
        List<String> columnSql = new ArrayList<>();
        for (BaseColumn tableColumn : tableColumns) {
            BeanUtils.copyProperties(tableColumn,columnType);
            columnSql.add(columnType.getColumnName() +" " + columnType.getColumnType());
        }

        String collect = columnSql.stream().map(s -> "`" + s + "`").collect(Collectors.joining(","));

        return createSql.toString().replace("${column}", collect);
    }

    public String createChildTableSql(){
        StringBuilder createSql = new StringBuilder();
        //创建子表
        createSql.append("CREATE TABLE IF NOT EXISTS `" + this.getTableName() + "` USING "+ "`ST_"+this.getTableName() +"` ( deviceCode )TAGS ('" + this.getTableName() + "')");

        return createSql.toString();
    }

}
