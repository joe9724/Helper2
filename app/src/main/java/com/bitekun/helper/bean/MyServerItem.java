package com.bitekun.helper.bean;

import java.io.Serializable;

public class MyServerItem implements Serializable {
    int Id ;
    String DispeopleId;
    String IdcardNo;
    String DiscardNo;
    String ServiceId;
    String AgencyId;
    String Name;
    String Gender;

    public String getPickerId() {
        return pickerId;
    }

    public void setPickerId(String pickerId) {
        this.pickerId = pickerId;
    }

    String pickerId;

    public String getDisableModeId() {
        return DisableModeId;
    }

    public void setDisableModeId(String disableModeId) {
        DisableModeId = disableModeId;
    }

    String DisableModeId;

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    String Avatar;

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    String Level;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    String Phone;
    String Age;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDispeopleId() {
        return DispeopleId;
    }

    public void setDispeopleId(String dispeopleId) {
        DispeopleId = dispeopleId;
    }

    public String getIdcardNo() {
        return IdcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        IdcardNo = idcardNo;
    }

    public String getDiscardNo() {
        return DiscardNo;
    }

    public void setDiscardNo(String discardNo) {
        DiscardNo = discardNo;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public String getAgencyId() {
        return AgencyId;
    }

    public void setAgencyId(String agencyId) {
        AgencyId = agencyId;
    }

    public String getAccessRecord() {
        return AccessRecord;
    }

    public void setAccessRecord(String accessRecord) {
        AccessRecord = accessRecord;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getAccessTime() {
        return AccessTime;
    }

    public void setAccessTime(String accessTime) {
        AccessTime = accessTime;
    }

    public String getLivePhoto() {
        return LivePhoto;
    }

    public void setLivePhoto(String livePhoto) {
        LivePhoto = livePhoto;
    }

    public String getRecordPhoto() {
        return RecordPhoto;
    }

    public void setRecordPhoto(String recordPhoto) {
        RecordPhoto = recordPhoto;
    }

    String AccessRecord;
    float Latitude ;
    float Longitude ;
    String Location;
    String AccessTime;
    String LivePhoto;
    String RecordPhoto;



}
