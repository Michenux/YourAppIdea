package org.michenux.drodrolib.network.json;

import android.location.Location;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class LocationDeserializer implements JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Location location = new Location("mongodb");
        JsonArray coord = jsonObject.getAsJsonArray("coordinates");
        location.setLongitude(coord.get(0).getAsDouble());
        location.setLatitude(coord.get(1).getAsDouble());
        return location ;
    }
}
