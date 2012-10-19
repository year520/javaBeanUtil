package com.util;

import java.util.Comparator;

public class JxpathCommonBean{

    private int     level;

    private String  jxpath;

    private String  parentPath;

    private boolean isList;
    
    private String keyName;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean isList) {
        this.isList = isList;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getJxpath() {
        return jxpath;
    }

    public void setJxpath(String jxpath) {
        this.jxpath = jxpath;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public final class ComparatorBean implements Comparator<JxpathCommonBean> {
        @Override
        public int compare(JxpathCommonBean o1, JxpathCommonBean o2) {
            // 首先比较年龄，如果年龄相同，则比较名字

            int flag = o1.getLevel() - (o2.getLevel());
            if (flag == 0) {
                return o1.getJxpath().compareTo(o2.getJxpath());
            } else {
                return flag;
            }
        }

    }
    
    public String toString(){
        return "level:"+this.level+",jxpath:"+this.jxpath+",keyName:"+this.keyName+",parentPath:"+this.parentPath+",isList:"+this.isList;
        
    }
}
