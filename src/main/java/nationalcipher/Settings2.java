package nationalcipher;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class Settings2 {

    public static Gson gson;
    
    public boolean checkShift;
    public boolean checkReverse;
    public boolean checkRoutes;
    public boolean useParallel;
    
    public static void init() {
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC).setPrettyPrinting();
        builder.registerTypeAdapter(Settings2.class, new SettingsSerializer());
        gson = builder.create();
    }
    
    public static class SettingsSerializer implements JsonSerializer<Settings2>, JsonDeserializer<Settings2> {

        @Override
        public Settings2 deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Settings2 settings = new Settings2();
            final JsonObject jsonObject = json.getAsJsonObject();

 
            settings.checkShift = jsonObject.has("checkShift") ? jsonObject.get("checkShift").getAsBoolean() : true;
            settings.checkReverse = jsonObject.has("checkReverse") ? jsonObject.get("checkReverse").getAsBoolean() : true;
            settings.checkRoutes = jsonObject.has("checkRoutes") ? jsonObject.get("checkRoutes").getAsBoolean() : true;
            settings.useParallel = jsonObject.has("useParallel") ? jsonObject.get("useParallel").getAsBoolean() : false;
            
            
            return settings;
        }

        @Override
        public JsonElement serialize(Settings2 src, Type typeOfSrc, JsonSerializationContext context) {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("checkShift", src.checkShift);
            jsonObject.addProperty("checkReverse", src.checkReverse);
            jsonObject.addProperty("checkRoutes", src.useParallel);
            jsonObject.addProperty("useParallel", src.checkRoutes);

            //final JsonArray jsonAuthorsArray = new JsonArray();
            //for (final String author : book.getAuthors()) {
            //    final JsonPrimitive jsonAuthor = new JsonPrimitive(author);
            //    jsonAuthorsArray.add(jsonAuthor);
           // }
            //jsonObject.add("authors", jsonAuthorsArray);

            return jsonObject;
        }
        
    }
}
