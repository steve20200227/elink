package cn.snowsoft.iot.module.cps.seriesDatabase.influxdb.build;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class FilterBuilder {
    //字段
    private String field;
    //值
    private String value;
    //是否条件连接符
    private String conditional;
    //是否条件连接符
    private Boolean isConditional;
    //最终sql
    StringBuilder sql;
    //下一个
    List<FilterBuilder> filterBuilderList = new ArrayList<>();
    //normal 正常键值  conditional  条件连接符 and  or  记录正常键值 以及 条件连接符的顺序  根据该顺序 构建where条件sql
    List<String> operatorList = new ArrayList<>();


    //等于
    public FilterBuilder eq(String field, String value){
        //对 field  value进行处理
        field = "r[\"" + field + "\"]";
        value = "\"" + value + "\"";
        FilterBuilder filterBuilder = new FilterBuilder();
        filterBuilder.setField(field);
        filterBuilder.setValue(value);
        filterBuilder.setConditional("==");
        filterBuilder.setIsConditional(false);
        filterBuilderList.add(filterBuilder);
        operatorList.add("normal");
        setDefaultOperator(operatorList);

        return this;
    }


    //不等于
    public FilterBuilder nq(String field, String value){
        //对 field  value进行处理
        field = "r[\"" + field + "\"]";
        value = "\"" + value + "\"";
        FilterBuilder filterBuilder = new FilterBuilder();
        filterBuilder.setField(field);
        filterBuilder.setValue(value);
        filterBuilder.setConditional("!=");
        filterBuilder.setIsConditional(false);
        filterBuilderList.add(filterBuilder);
        operatorList.add("normal");
        setDefaultOperator(operatorList);
        return this;
    }
    //大于
    public FilterBuilder gt(String field, String value){
        //对 field  value进行处理
        field = "r[\"" + field + "\"]";
        value = "\"" + value + "\"";
        FilterBuilder filterBuilder = new FilterBuilder();
        filterBuilder.setField(field);
        filterBuilder.setValue(value);
        filterBuilder.setConditional(">");
        filterBuilder.setIsConditional(false);
        filterBuilderList.add(filterBuilder);
        operatorList.add("normal");
        setDefaultOperator(operatorList);
        return this;
    }

    //小于
    public FilterBuilder lt(String field, String value){
        //对 field  value进行处理
        field = "r[\"" + field + "\"]";
        value = "\"" + value + "\"";
        FilterBuilder filterBuilder = new FilterBuilder();
        filterBuilder.setField(field);
        filterBuilder.setValue(value);
        filterBuilder.setConditional("<");
        filterBuilder.setIsConditional(false);
        filterBuilderList.add(filterBuilder);
        operatorList.add("normal");
        setDefaultOperator(operatorList);
        return this;
    }

    //大于等于
    public FilterBuilder ge(String field, String value){
        //对 field  value进行处理
        field = "r[\"" + field + "\"]";
        value = "\"" + value + "\"";
        FilterBuilder filterBuilder = new FilterBuilder();
        filterBuilder.setField(field);
        filterBuilder.setValue(value);
        filterBuilder.setConditional(">=");
        filterBuilder.setIsConditional(false);
        filterBuilderList.add(filterBuilder);
        operatorList.add("normal");
        setDefaultOperator(operatorList);
        return this;
    }

    //小于等于
    public FilterBuilder le(String field, String value){
        //对 field  value进行处理
        field = "r[\"" + field + "\"]";
        value = "\"" + value + "\"";
        FilterBuilder filterBuilder = new FilterBuilder();
        filterBuilder.setField(field);
        filterBuilder.setValue(value);
        filterBuilder.setConditional("<=");
        filterBuilder.setIsConditional(false);
        filterBuilderList.add(filterBuilder);
        operatorList.add("normal");
        setDefaultOperator(operatorList);
        return this;
    }


    //或者
    public FilterBuilder or(){
        FilterBuilder filterBuilder = new FilterBuilder();
        filterBuilder.setConditional("or");
        filterBuilder.setIsConditional(true);
        setDefaultOperator(operatorList,filterBuilder);
        return this;
    }


    //并且
    public FilterBuilder and(){
        FilterBuilder filterBuilder = new FilterBuilder();
        filterBuilder.setConditional("and");
        filterBuilder.setIsConditional(true);
        setDefaultOperator(operatorList,filterBuilder);

        return this;
    }


    //构建sql
    public String buildSql(){
        StringBuilder stringBuilder = new StringBuilder();
        List<Integer> indexList = getIndexList();
        for (Integer integer : indexList) {
            FilterBuilder filterBuilder = filterBuilderList.get(integer);
            String s = filterBuilder.toSql();
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }
    //根据 operatorList 获取正确语法的索引
    public List<Integer> getIndexList(){
        String changeOperator = "";
        String firstOperator = "";
        String lastOperator = "";
        List<Integer> list = new ArrayList<>();
        //遍历  遇到不同的就存  交替式的  中途有多个连接符的 按最后一个处理
        for (int i = 0; i < operatorList.size(); i++) {
            firstOperator = operatorList.get(0);
            lastOperator = operatorList.get(operatorList.size()-1);
            if (!changeOperator.equals(operatorList.get(i))){
                list.add(i);
                changeOperator = operatorList.get(i);
            }
        }
        //如果第一个不是normal就移除
        if (firstOperator.equals("conditional")){
            list.remove(0);
        }
        //如果最后一个不是normal就移除
        if (lastOperator.equals("conditional")){
            list.remove(operatorList.size()-1);
        }
        return list;
    }

    public String toSql(){
        //如果是 条件连接符 直接返回 操作符
        if (isConditional){
            return " " +this.conditional + " ";
        }else{
            return " " + field +" " +conditional+ " " +value+ " ";
        }

    }

    public void setDefaultOperator(List<String> list){
        //如果不为空并且上一个是 normal
        if (CollectionUtils.isNotEmpty(list)){
            if (list.get(list.size()-1).equals("normal")){
                FilterBuilder filterBuilder = new FilterBuilder();
                filterBuilder.setConditional("or");
                filterBuilder.setIsConditional(true);
                filterBuilderList.add(filterBuilder);
                list.add("conditional");
            }
        }
    }

    public void setDefaultOperator(List<String> list, FilterBuilder filterBuilder){
        //如果不为空并且上一个是 normal
        if (CollectionUtils.isNotEmpty(list)){
            if (list.get(list.size()-1).equals("conditional")){
                filterBuilderList.remove(filterBuilderList.size() - 1);
                filterBuilderList.add(filterBuilder);
            }else {
                filterBuilderList.add(filterBuilder);
                list.add("conditional");
            }
        }
    }

    private String getField() {
        return field;
    }

    private void setField(String field) {
        this.field = field;
    }

    private String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    private String getConditional() {
        return conditional;
    }

    private void setConditional(String conditional) {
        this.conditional = conditional;
    }

    private Boolean getIsConditional() {
        return isConditional;
    }

    private void setIsConditional(Boolean isConditional) {
        this.isConditional = isConditional;
    }

    private StringBuilder getSql() {
        return sql;
    }

    private void setSql(StringBuilder sql) {
        this.sql = sql;
    }
}
