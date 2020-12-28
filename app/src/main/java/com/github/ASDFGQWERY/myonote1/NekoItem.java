package com.github.ASDFGQWERY.myonote1;

import android.view.View;

public class NekoItem {

    private String uuid;
    private String body;
    private String dbtime;
    private String favStatus;

    /*
    public NekoItem() {

    }

     */

    public NekoItem(String uuid, String body, String dbtime, String favStatus) {
        this.uuid = uuid;
        this.body = body;
        this.dbtime = dbtime;
        this.favStatus = favStatus;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String title) {
        this.body = body;
    }

    public String getDbtime() {
        return dbtime;
    }

    public void setDbtime(String dbtime) {
        this.dbtime = dbtime;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }
}
