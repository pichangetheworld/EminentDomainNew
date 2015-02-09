package com.pichangetheworld.eminentdomainnew.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.pichangetheworld.eminentdomainnew.planet.BasePlanet;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 09/02/2015
 */
public class PlanetDrawableData implements Parcelable {
    // All planets
    public String name;
    public int drawable;

    // Unconquered Planets
    public int colonizeCost;
    public int warfareCost;
    public int colonizeCount;
    public boolean conquered;

    // Conquered Planets
    public int produceCapacity;
    public int curProduceCount;

    public PlanetDrawableData() {
        name = "none";
        drawable = -1;
        colonizeCost = 0;
        warfareCost = 0;
        colonizeCount = 0;
        conquered = false;
        produceCapacity = 0;
        curProduceCount = 0;
    }

    public void setData(BasePlanet planet) {
        name = planet.getName();
        drawable = planet.getDrawable();
        colonizeCost = planet.getColonizeCost();
        warfareCost = planet.getWarfareCost();
        colonizeCount = planet.getColonizeCount();
        conquered = planet.isConquered();
        produceCapacity = planet.getProduceCapacity();
        curProduceCount = planet.getCurProduceCount();
    }

    PlanetDrawableData(Parcel cpy) {
        name = cpy.readString();
        drawable = cpy.readInt();
        colonizeCost = cpy.readInt();
        warfareCost = cpy.readInt();
        colonizeCount = cpy.readInt();
        conquered = cpy.readByte() != 0;
        produceCapacity = cpy.readInt();
        curProduceCount = cpy.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(drawable);
        dest.writeInt(colonizeCost);
        dest.writeInt(warfareCost);
        dest.writeInt(colonizeCount);
        dest.writeByte((byte) (conquered ? 1 : 0));
        dest.writeInt(produceCapacity);
        dest.writeInt(curProduceCount);
    }

    public static final Creator<PlanetDrawableData> CREATOR = new Creator<PlanetDrawableData>() {
        @Override
        public PlanetDrawableData createFromParcel(Parcel source) {
            return new PlanetDrawableData(source);
        }
        @Override
        public PlanetDrawableData[] newArray(int size) {
            return new PlanetDrawableData[0];
        }
    };
}