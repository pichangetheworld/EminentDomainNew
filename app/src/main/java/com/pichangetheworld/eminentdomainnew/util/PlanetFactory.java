package com.pichangetheworld.eminentdomainnew.util;

import android.util.Log;

import com.pichangetheworld.eminentdomainnew.planet.BasePlanet;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 15/02/2015
 */
public class PlanetFactory {

    public static BasePlanet getPlanet(String line) {
        String[] data = line.split(",");
        String name = data[0];
        String type = data[1];
        int colonizeCost = Integer.parseInt(data[2]);
        int warfareCost = Integer.parseInt(data[3]);
        int canProduce = Integer.parseInt(data[4]);
        int VPs = Integer.parseInt(data[5]);
        PlanetType planetType;
        switch (type) {
            case "Metallic":
                planetType = PlanetType.METALLIC;
                break;
            case "Fertile":
                planetType = PlanetType.FERTILE;
                break;
            case "Advanced":
                planetType = PlanetType.ADVANCED;
                break;
            case "Utopian":
                planetType = PlanetType.UTOPIAN;
                break;
            case "Prestige":
                planetType = PlanetType.PRESTIGE;
                break;
            default:
                Log.e("PlanetFactory", "Unknown planet type " + type);
                planetType = PlanetType.UTOPIAN;
        }
        return new BasePlanet(name, planetType, colonizeCost, warfareCost, canProduce, VPs);
    }
}
