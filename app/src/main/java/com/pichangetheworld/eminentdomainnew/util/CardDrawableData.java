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

    public boolean survey;
    public boolean warfare;
    public boolean colonize;
    public boolean produce;
    public boolean trade;
    public boolean research;

    public CardDrawableData() {
        name = "none";
        drawable = -1;

        survey = false;
        warfare = false;
        colonize = false;
        produce = false;
        trade = false;
        research = false;
    }

    public void setData(BaseCard card) {
        name = card.getName();
        drawable = card.getDrawable();

        survey = card.getSurvey() > 0;
        warfare = card.getWarfare() > 0;
        colonize = card.getColonize() > 0;
        produce = card.getProduce() > 0;
        trade = card.getTrade() > 0;
        research = card.getResearch() > 0;
    }

    CardDrawableData(Parcel cpy) {
        name = cpy.readString();
        drawable = cpy.readInt();

        survey = cpy.readByte() != 0;
        warfare = cpy.readByte() != 0;
        colonize = cpy.readByte() != 0;
        produce = cpy.readByte() != 0;
        trade = cpy.readByte() != 0;
        research = cpy.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(drawable);

        dest.writeByte((byte) (survey ? 1 : 0));
        dest.writeByte((byte) (warfare ? 1 : 0));
        dest.writeByte((byte) (colonize ? 1 : 0));
        dest.writeByte((byte) (produce ? 1 : 0));
        dest.writeByte((byte) (trade ? 1 : 0));
        dest.writeByte((byte) (research ? 1 : 0));
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