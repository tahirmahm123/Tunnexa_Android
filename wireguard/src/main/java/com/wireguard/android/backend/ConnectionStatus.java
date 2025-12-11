package com.wireguard.android.backend;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arne on 08.11.16.
 */
public enum ConnectionStatus implements Parcelable {
    LEVEL_CONNECTED,
    LEVEL_DISCONNECTED,
    UNKNOWN_LEVEL;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ConnectionStatus> CREATOR = new Creator<ConnectionStatus>() {
        @Override
        public ConnectionStatus createFromParcel(Parcel in) {
            return ConnectionStatus.values()[in.readInt()];
        }

        @Override
        public ConnectionStatus[] newArray(int size) {
            return new ConnectionStatus[size];
        }
    };
}