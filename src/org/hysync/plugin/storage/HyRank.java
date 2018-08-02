package org.hysync.plugin.storage;

import org.hysync.plugin.util.StringUtil;

public class HyRank {
    private String id;
    private String alias;
    private String prefix;
    private String display;

    public HyRank(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public String getPrefix() {
        return StringUtil.translate(prefix);
    }

    public String getDisplay() {
        return display;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
