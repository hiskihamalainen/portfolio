package fi.tuni.prog3.sisu;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * A class that enables gson to know which subclass of an abstract class a Json object should be deserialized into.
 */
public class AbstractClassAdapter implements JsonSerializer<Object>, JsonDeserializer<Object> {

    private static final String CLASS_FIELD_NAME = "class";

    /**
     * Deserializes into a class specified by an extra field.
     * {@inheritDoc}
     * @param jsonElement
     * @param type
     * @param jsonDeserializationContext
     * @return
     */
    @Override
    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        try {
            JsonObject obj = jsonElement.getAsJsonObject();
            Class<?> clazz = Class.forName(obj.get(CLASS_FIELD_NAME).getAsString());

            return jsonDeserializationContext.deserialize(jsonElement, clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds an extra field that specifies the object's class.
     * {@inheritDoc}
     * @param object
     * @param type
     * @param jsonSerializationContext
     * @return
     */
    @Override
    public JsonElement serialize(Object object, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonElement elem = jsonSerializationContext.serialize(object, object.getClass());
        elem.getAsJsonObject().addProperty(CLASS_FIELD_NAME, object.getClass().getCanonicalName());

        return elem;
    }

}
