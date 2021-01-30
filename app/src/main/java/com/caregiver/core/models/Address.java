package com.caregiver.core.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Address implements Parcelable {
    public String Address = "",
            landmark = "",
            Street = "",
            City = "",
            State = "",
            Country = "",
            Pincode = "";

    public Address(){}
    protected Address(Parcel in) {
        Address = in.readString();
        landmark = in.readString();
        Street = in.readString();
        City = in.readString();
        State = in.readString();
        Country = in.readString();
        Pincode = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Address);
        parcel.writeString(landmark);
        parcel.writeString(Street);
        parcel.writeString(City);
        parcel.writeString(State);
        parcel.writeString(Country);
        parcel.writeString(Pincode);
    }

    public String getAddressAsString(){
        String str = "";
        if(!TextUtils.isEmpty(Address))
            str = Address;
        if(!TextUtils.isEmpty(Street)){
            if(TextUtils.isEmpty(str))
                str = Street;
            else
                str = str+", "+Street;
        }

        if(!TextUtils.isEmpty(landmark)){
            if(TextUtils.isEmpty(str))
                str = landmark;
            else
                str = str+", "+landmark;
        }

        if(!TextUtils.isEmpty(City)){
            if(TextUtils.isEmpty(str))
                str = City;
            else
                str = str+", "+City;
        }

        if(!TextUtils.isEmpty(State)){
            if(TextUtils.isEmpty(str))
                str = State;
            else
                str = str+", "+State;
        }

        if(!TextUtils.isEmpty(Country)){
            if(TextUtils.isEmpty(str))
                str = State;
            else
                str = str+", "+State;
        }

        if(!TextUtils.isEmpty(Pincode)){
            if(TextUtils.isEmpty(str))
                str = Pincode;
            else
                str = str+",\n"+Pincode;
        }

        return str;
    }
}
