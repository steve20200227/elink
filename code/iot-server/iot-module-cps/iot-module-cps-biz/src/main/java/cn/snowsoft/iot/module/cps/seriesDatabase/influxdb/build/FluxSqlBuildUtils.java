package cn.snowsoft.iot.module.cps.seriesDatabase.influxdb.build;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FluxSqlBuildUtils {
    private StringBuilder fluxSql = new StringBuilder();


    //设置桶
    public FluxSqlBuildUtils from(String bucket){
        String bucketSql = "from(bucket: \"${bucket}\")".replace("${bucket}",bucket);
        fluxSql.append(bucketSql);
        return this;
    }

    public FluxSqlBuildUtils measurement(String measurement){
        String rangeSql = " |> filter(fn: (r) => r[\"_measurement\"] == \"${measurement}\")"
                .replace("${measurement}",measurement);
        fluxSql.append(rangeSql);
        return this;
    }

    //查询固定开始结束时间范围
    public FluxSqlBuildUtils range(String startTime,String endTime){
        String rangeSql = " |> range(start: ${startTime}, stop: ${endTime})"
                .replace("${startTime}",startTime)
                .replace("${endTime}",endTime);
        fluxSql.append(rangeSql);
        return this;
    }

    //根据持续时间数据
    public FluxSqlBuildUtils range(String time){
        String rangeSql = " |> range(start: ${time})"
                .replace("${time}",time);
        fluxSql.append(rangeSql);
        return this;
    }
//Arrays.asList("Larry", "Curly", "Moe")
    public FluxSqlBuildUtils pivot(List<String> rowKeys, List<String> columnKey, List<String> valueColumn){
        String row = rowKeys.stream().map(e->"\""+e+"\"").collect(Collectors.joining(","));
        String column = columnKey.stream().map(e->"\""+e+"\"").collect(Collectors.joining(","));
        String value = valueColumn.stream().map(e->"\""+e+"\"").collect(Collectors.joining(","));
        String rangeSql = " |> pivot(rowKey: [${rowKeys}], columnKey: [${columnKey}], valueColumn: ${valueColumn})"
                .replace("${rowKeys}",row)
                .replace("${columnKey}",column)
                .replace("${valueColumn}",value);
        fluxSql.append(rangeSql);
        return this;
    }

    public static void main(String[] args) {
        Arrays.asList("Larry", "Curly", "Moe");
    }
    //根据持续时间数据
    public FluxSqlBuildUtils filter(FilterBuilder filterBuilder){
        String s = filterBuilder.buildSql();

        String rangeSql = " |> filter(fn: (r) => ${expr})"
                .replace("${expr}",s);
        fluxSql.append(rangeSql);
        return this;
    }

    /*public static void main(String[] args) {
        FilterBuilder filterBuilder = new FilterBuilder();
        filterBuilder.eq("r[\"_field\"]","bbb").eq("r[\"_field\"]", "ccc");
        System.out.println(filterBuilder.buildSql());

    }*/
    public FluxSqlBuildUtils script(String script){
        fluxSql.append(script);
        return this;
    }


    public FluxSqlBuildUtils keep(List<String> columns){
        String collect = columns.stream().map(e -> "\"" + e + "\"").collect(Collectors.joining(","));
        String keepSql = " |> keep(columns: [${columns}])"
                .replace("${columns}",collect);
        fluxSql.append(keepSql);
        return this;
    }

    public FluxSqlBuildUtils yield(String yield){
        String yieldSql = " |>yield(name :\"${yield}\")"
                .replace("${yield}",yield);
        fluxSql.append(yieldSql);
        return this;
    }

    //设置桶
    public FluxSqlBuildUtils join(FluxSqlBuildUtils leftTable,FluxSqlBuildUtils rightTable,String method,String on,String as){
        StringBuilder leftTableSql = leftTable.getFluxSql();
        StringBuilder rightTableSql = rightTable.getFluxSql();
        method = "\""+method+"\"";
        String bucketSql = "join.tables( method:${method}, left:${left} , right:${right} , on:${on} ,as:${as}"
                .replace("${method}",method)
                .replace("${left}",leftTableSql.toString())
                .replace("${right}",rightTableSql.toString())
                .replace("${on}",on)
                .replace("${as}",as);
        fluxSql.append(bucketSql);
        return this;
    }


    public StringBuilder getFluxSql() {
        return fluxSql;
    }

    public void setFluxSql(StringBuilder fluxSql) {
        this.fluxSql = fluxSql;
    }

}
