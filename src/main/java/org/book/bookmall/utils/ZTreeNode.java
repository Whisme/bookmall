package org.book.bookmall.utils;

import org.book.bookmall.entity.Privilege;

import java.util.Date;

public class ZTreeNode extends Privilege {
    private int id;
    private int state;
    private int pId;
    private String icon;
    private String iconClose;
    private String iconOpen;
    private String name;
    private boolean open;
    private boolean isParent;
    private boolean checked;
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public int getpId() {
        return pId;
    }
    public void setpId(int pId) {
        this.pId = pId;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getIconClose() {
        return iconClose;
    }
    public void setIconClose(String iconClose) {
        this.iconClose = iconClose;
    }
    public String getIconOpen() {
        return iconOpen;
    }
    public void setIconOpen(String iconOpen) {
        this.iconOpen = iconOpen;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isOpen() {
        return open;
    }
    public void setOpen(boolean open) {
        this.open = open;
    }
    public boolean isParent() {
        return isParent;
    }
    public void setParent(boolean parent) {
        isParent = parent;
    }

    public int getParentId() {
        return 0;
    }

    public int getPrivId() {
        return 0;
    }

    public int getIsParent() {
        return 0;
    }

    public void setIsParent(int i) {
    }

    public void setUpdated(Date date) {
    }
}
