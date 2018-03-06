package com.engine.node.mongo.entities;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Document(collection = "traffic_statistic")
public class SessionTraffic {
    @Id
    private ObjectId id;

    private Date startDate;
    private Date endDate;

    private Integer year;
    private Integer month;
    private Integer dayOfWeek;
    private Integer hour;
    private Integer minute;
    private Integer second;
    private Double channelLoad;
    private Long dropBytes;
    private Long retransmittedBytes;
    private Long totalTrafficBytes;

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Long getDropBytes() {
        return dropBytes;
    }

    public void setDropBytes(Long dropBytes) {
        this.dropBytes = dropBytes;
    }

    public Double getChannelLoad() {
        return channelLoad;
    }

    public void setChannelLoad(Double channelLoad) {
        this.channelLoad = channelLoad;
    }

    public Long getRetransmittedBytes() {
        return retransmittedBytes;
    }

    public void setRetransmittedBytes(Long retransmittedBytes) {
        this.retransmittedBytes = retransmittedBytes;
    }


    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }


    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public Long getTotalTrafficBytes() {
        return totalTrafficBytes;
    }

    public void setTotalTrafficBytes(Long totalTrafficBytes) {
        this.totalTrafficBytes = totalTrafficBytes;
    }

    @Override
    public String toString() {
        return "SessionTraffic{" +
                "id='" + id + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalTrafficBytes=" + totalTrafficBytes +
                '}';
    }
}
