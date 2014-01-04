package org.michenux.drodrolib.network.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public class TimestampDeserializer implements JsonDeserializer<Timestamp> {

	public Timestamp deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		Timestamp result = null ;
        long time = Long.parseLong(json.getAsString());
        if ( time > 0 ) {
            result = new Timestamp(time * 1000);
        }
		return result;
	}
}
