package com.pichangetheworld.eminentdomainnew.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.pichangetheworld.eminentdomainnew.card.BaseCard;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 09/02/2015
 */
public class CardDrawableData implements Parcelable {
    // All cards
    public String name;
    public int drawable;

    public CardDrawableData() {
        name = "none";
        drawable = -1;
    }

    public void setData(BaseCard card) {
        name = card.getName();
        drawable = card.getDrawable();
    }

    CardDrawableData(Parcel cpy) {
        name = cpy.readString();
        drawable = cpy.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(drawable);
    }

    public static final Parcelable.Creator<CardDrawableData> CREATOR = new Parcelable.Creator<CardDrawableData>() {
        @Override
        public CardDrawableData createFromParcel(Parcel source) {
            return new CardDrawableData(source);
        }
        @Override
        public CardDrawableData[] newArray(int size) {
            return new CardDrawableData[0];
        }
    };
}