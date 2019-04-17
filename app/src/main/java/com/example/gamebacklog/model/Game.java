package com.example.gamebacklog.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
// table of database

// entity
@Entity(tableName = "game_table")

public class Game implements Parcelable {
    // attributes of entity game table
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "platform")
    private String platform;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "date")
    private String date;

    public Game(String title, String platform, String status) {
        this.title = title;
        this.platform = platform;
        this.status = status;
    }

    // methods returning the information
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    protected Game(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.platform = in.readString();
        this.status = in.readString();
        this.date = in.readString();
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(platform);
        parcel.writeString(status);
        parcel.writeString(date);
    }
}
