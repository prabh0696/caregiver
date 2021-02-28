package com.caregiver.core.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public int id, User_Type, reviewsCount, availability;
    public String First_Name = "",
            Last_Name = "",
            Password = "",
            Email = "",
            Phone = "",
            Photo = "",
            Charges = "",
            Experience = "",
            Qualification = "",
            about = "";

    public Address addr = new Address();

    public User() {
    }

    protected User(Parcel in) {
        id = in.readInt();
        User_Type = in.readInt();
        reviewsCount = in.readInt();
        availability = in.readInt();
        First_Name = in.readString();
        Last_Name = in.readString();
        Password = in.readString();
        Email = in.readString();
        Phone = in.readString();
        Photo = in.readString();
        Charges = in.readString();
        Experience = in.readString();
        Qualification = in.readString();
        about = in.readString();
        addr = in.readParcelable(Address.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(User_Type);
        parcel.writeInt(reviewsCount);
        parcel.writeInt(availability);
        parcel.writeString(First_Name);
        parcel.writeString(Last_Name);
        parcel.writeString(Password);
        parcel.writeString(Email);
        parcel.writeString(Phone);
        parcel.writeString(Photo);
        parcel.writeString(Charges);
        parcel.writeString(Experience);
        parcel.writeString(Qualification);
        parcel.writeString(about);
        parcel.writeParcelable(addr, i);
    }
}
