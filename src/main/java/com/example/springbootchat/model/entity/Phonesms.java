package com.example.springbootchat.model.entity;

import lombok.Builder;

import java.io.Serializable;
import java.util.Date;

@Builder
/**
 *
 * @TableName phonesms
 */
public class Phonesms implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String phone;
    /**
     *
     */
    private Integer everyDayCount;
    /**
     *
     */
    private Integer everyWeekCount;
    /**
     *
     */
    private Integer everyMonthCount;
    /**
     *
     */
    private Integer uid;
    /**
     *
     */
    private String mostRecentCode;
    /**
     *
     */
    private Date mostRecentTime;

    /**
     *
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     */
    public Integer getEveryDayCount() {
        return everyDayCount;
    }

    /**
     *
     */
    public void setEveryDayCount(Integer everyDayCount) {
        this.everyDayCount = everyDayCount;
    }

    /**
     *
     */
    public Integer getEveryWeekCount() {
        return everyWeekCount;
    }

    /**
     *
     */
    public void setEveryWeekCount(Integer everyWeekCount) {
        this.everyWeekCount = everyWeekCount;
    }

    /**
     *
     */
    public Integer getEveryMonthCount() {
        return everyMonthCount;
    }

    /**
     *
     */
    public void setEveryMonthCount(Integer everyMonthCount) {
        this.everyMonthCount = everyMonthCount;
    }

    /**
     *
     */
    public Integer getUid() {
        return uid;
    }

    /**
     *
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     *
     */
    public String getMostRecentCode() {
        return mostRecentCode;
    }

    /**
     *
     */
    public void setMostRecentCode(String mostRecentCode) {
        this.mostRecentCode = mostRecentCode;
    }

    /**
     *
     */
    public Date getMostRecentTime() {
        return mostRecentTime;
    }

    /**
     *
     */
    public void setMostRecentTime(Date mostRecentTime) {
        this.mostRecentTime = mostRecentTime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Phonesms other = (Phonesms) that;
        return (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getEveryDayCount() == null ? other.getEveryDayCount() == null : this.getEveryDayCount().equals(other.getEveryDayCount()))
                && (this.getEveryWeekCount() == null ? other.getEveryWeekCount() == null : this.getEveryWeekCount().equals(other.getEveryWeekCount()))
                && (this.getEveryMonthCount() == null ? other.getEveryMonthCount() == null : this.getEveryMonthCount().equals(other.getEveryMonthCount()))
                && (this.getUid() == null ? other.getUid() == null : this.getUid().equals(other.getUid()))
                && (this.getMostRecentCode() == null ? other.getMostRecentCode() == null : this.getMostRecentCode().equals(other.getMostRecentCode()))
                && (this.getMostRecentTime() == null ? other.getMostRecentTime() == null : this.getMostRecentTime().equals(other.getMostRecentTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getEveryDayCount() == null) ? 0 : getEveryDayCount().hashCode());
        result = prime * result + ((getEveryWeekCount() == null) ? 0 : getEveryWeekCount().hashCode());
        result = prime * result + ((getEveryMonthCount() == null) ? 0 : getEveryMonthCount().hashCode());
        result = prime * result + ((getUid() == null) ? 0 : getUid().hashCode());
        result = prime * result + ((getMostRecentCode() == null) ? 0 : getMostRecentCode().hashCode());
        result = prime * result + ((getMostRecentTime() == null) ? 0 : getMostRecentTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", phone=" + phone +
                ", everyDayCount=" + everyDayCount +
                ", everyWeekCount=" + everyWeekCount +
                ", everyMonthCount=" + everyMonthCount +
                ", uid=" + uid +
                ", mostRecentCode=" + mostRecentCode +
                ", mostRecentTime=" + mostRecentTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
        return sb;
    }
}