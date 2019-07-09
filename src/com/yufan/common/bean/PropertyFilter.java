package com.yufan.common.bean;

/**
 * @author wt
 */
public class PropertyFilter {
    private static final String prefix = "filter_";

    public enum MatchType {
        EQ, NOTEQ, LIKE, LT, GT, LE, GE;
    }

    private String fieldName;
    private String filterName;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    /*
     * eg. filter_EQ_name
     */
    public PropertyFilter(final String property) {
        if (property == null || property.indexOf(prefix) < 0) {
            this.fieldName = property;
            this.filterName = "=";
        } else {
            String unprefixed = property.substring(prefix.length());
            String matchTypeCode = unprefixed.substring(0, unprefixed.indexOf("_"));
            try {
                MatchType matchType = Enum.valueOf(MatchType.class, matchTypeCode);
                String name = unprefixed.substring(unprefixed.indexOf("_") + 1);
                switch (matchType) {
                    case EQ:
                        this.fieldName = name;
                        this.filterName = "=";
                        break;
                    case NOTEQ:
                        this.fieldName = name;
                        this.filterName = "<>";
                        break;
                    case LIKE:
                        this.fieldName = name;
                        this.filterName = "like";
                        break;
                    case LT:
                        this.fieldName = name;
                        this.filterName = ">=";
                        break;
                    case GT:
                        this.fieldName = name;
                        this.filterName = "<=";
                        break;
                    case LE:
                        this.fieldName = name;
                        this.filterName = ">";
                        break;
                    case GE:
                        this.fieldName = name;
                        this.filterName = "<";
                        break;
                }
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("filter名称" + unprefixed + "没有按规则编写,无法得到属性比较类型.", e);
            }
        }
    }

    public static void main(String[] args) {
        PropertyFilter filter = new PropertyFilter("filter_EQ_username");
        System.out.println(filter.fieldName + ":" + filter.filterName);
    }
}
