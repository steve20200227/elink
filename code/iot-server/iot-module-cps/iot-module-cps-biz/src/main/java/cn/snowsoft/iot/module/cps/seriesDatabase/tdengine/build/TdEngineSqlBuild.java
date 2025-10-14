package cn.snowsoft.iot.module.cps.seriesDatabase.tdengine.build;

import java.util.ArrayList;
import java.util.List;

public class TdEngineSqlBuild {


    /*
    * select [关键字 | distinct] column from table where  groupby  having orderby limit
    * */
    private boolean distinct;
    //列 列表
    private List<String> columnList = new ArrayList();

    //表名
    private List<String> tableList = new ArrayList();


    private List<String> whereList = new ArrayList();


    private List<String> orderByList = new ArrayList();

    private int limit = 0;

    private int offset = 0;

    public TdEngineSqlBuild() {

    }


    public TdEngineSqlBuild column(String name) {
        columnList.add(name);
        return this;
    }




    public TdEngineSqlBuild limit(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
        return this;
    }

    public TdEngineSqlBuild limit(int limit) {
        return limit(limit, 0);
    }


    public TdEngineSqlBuild distinct() {
        this.distinct = true;
        return this;
    }


    public TdEngineSqlBuild from(String table) {
        tableList.add(table);
        return this;
    }


    public TdEngineSqlBuild orderBy(String name) {
        orderByList.add(name);
        return this;
    }



    @Override
    public String toString() {

        StringBuilder sql = new StringBuilder("select ");

        if (distinct) {
            sql.append("distinct ");
        }

        if (columnList.size() == 0) {
            sql.append("*");
        } else {
            appendList(sql, columnList, "", ", ");
        }

        appendList(sql, tableList, " from ", ", ");
        appendList(sql, whereList, " where ", " ");
        appendList(sql, orderByList, " order by ", ", ");

        if(limit > 0)
            sql.append(" limit " + limit);
        if(offset > 0)
            sql.append(", " + offset);

        return sql.toString();
    }


    public TdEngineSqlBuild where(String expr) {
        whereList.add(expr);
        return this;
    }


    // 根据分隔符 拼接list
    private void appendList(StringBuilder sql, List list, String first, String separator) {
        //是否时第一个
        boolean isFirst = true;
        //第一个拼接关键字
        for (Object s : list) {
            if (isFirst) {
                sql.append(first);
            } else {
                sql.append(separator);
            }
            sql.append(s);
            isFirst = false;
        }
    }
}
