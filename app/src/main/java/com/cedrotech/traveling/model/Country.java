package com.cedrotech.traveling.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ivo on 05/06/16.
 */
public class Country implements Serializable{

    private int id;
    private String iso;
    private String shortname;
    private String longname;
    private String callingcode;
    private int status;
    private String culture;
    private Date visiteddate;

    public Country() {
    }

    public String getCallingcode() {
        return callingcode;
    }

    public void setCallingcode(String callingcode) {
        this.callingcode = callingcode;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getLongname() {
        return longname;
    }

    public void setLongname(String longname) {
        this.longname = longname;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getVisiteddate() {
        return visiteddate;
    }

    public void setVisiteddate(Date visiteddate) {
        this.visiteddate = visiteddate;
    }
}
