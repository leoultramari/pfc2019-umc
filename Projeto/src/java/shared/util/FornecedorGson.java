/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author superalunocmc
 */
public class FornecedorGson {

    private static Gson gson;

    public static Gson getGson() {

        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();

            //Manually deserialize LocalDate
            builder.registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    //recebendo diferentes formatos de data
                    if (json.isJsonObject()) {
                        JsonObject date = json.getAsJsonObject();
                        return LocalDate.of(date.get("year").getAsInt(), date.get("month").getAsInt(), date.get("day").getAsInt());
                    }
                    else //if(json.isJsonPrimitive())
                    {
                        return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
                    }
                }
            });

            //Manually serialize LocalDate
            builder.registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"
                }
            });

            //builder.setDateFormat("\"yyyy-MM-dd\"");
            builder.setDateFormat("yyyy-MM-dd");
            gson = builder.create();
        }

        return gson;
    }

}
