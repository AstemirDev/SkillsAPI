package org.astemir.api.io.json;


import com.google.gson.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonWrap {

    public static Map<Class,Method> DESERIALIZERS = new HashMap<>();

    private Object value;

    public JsonWrap(Object element) {
        this.value = element;
    }

    public JsonWrap(String text) {
        this.value = JsonParser.parseString(text);
    }


    @SuppressWarnings("unused")
    public <T> T get(String name,Class<T> className){
        JsonElement otherValue = asJsonObject().get(name);
        return new JsonWrap(otherValue).cast(className);
    }

    public boolean contains(String name){
        return asJsonObject().has(name);
    }

    public <T> T getOrDefault(String name,Class<T> className,T defaultValue){
        if (asJsonObject().has(name)){
            return get(name,className);
        }else{
            return defaultValue;
        }
    }

    public <T> T cast(Class<T> className){
        if (className == JsonWrap.class){
            return (T) new JsonWrap(value);
        }else
        if (className.isAnnotationPresent(Json.class)){
            Json json = className.getAnnotation(Json.class);
            try {
                Method deserializeMethod = null;
                if (DESERIALIZERS.containsKey(className)){
                    deserializeMethod = DESERIALIZERS.get(className);
                }else{
                    deserializeMethod = className.getMethod(json.deserialize()[0].value(),JsonElement.class);
                    DESERIALIZERS.put(className,deserializeMethod);
                }
                if (deserializeMethod != null){
                    return (T)deserializeMethod.invoke(null,asJsonElement());
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }else
        if (asJsonElement().isJsonPrimitive()) {
            JsonPrimitive primitive = (JsonPrimitive) asJsonElement();
            if (className == Boolean.class){
                return (T)(Boolean)primitive.getAsBoolean();
            }else
            if (className == Integer.class){
                return (T) (Integer)primitive.getAsInt();
            }else
            if (className == Double.class){
                return (T) (Double)primitive.getAsDouble();
            }else
            if (className == Float.class){
                return (T) (Float)primitive.getAsFloat();
            }else
            if (className == String.class){
                return (T) primitive.getAsString();
            }else
            if (className == Object.class){
                if (primitive.isNumber()){
                    return (T)(Double)primitive.getAsDouble();
                }else
                if (primitive.isBoolean()){
                    return (T)(Boolean)primitive.getAsBoolean();
                }else
                if (primitive.isString()){
                    return (T)primitive.getAsString();
                }
            }
        }
        return (T) value;
    }


    public Object getValue() {
        return value;
    }


    public List<JsonWrap> asList(){
        List<JsonWrap> list = new ArrayList<>();
        for (JsonElement element : asJsonArray()) {
            list.add(new JsonWrap(element));
        }
        return list;
    }

    public List<JsonWrap> asFullList(){
        List<JsonWrap> list = new ArrayList<>();
        for (JsonElement element : asJsonArray()) {
            if (element.isJsonArray()){
                list.add(new JsonWrap(new JsonWrap(element).asFullList()));
            }else {
                list.add(new JsonWrap(element));
            }
        }
        return list;
    }


    public Map<String, JsonWrap> asMap(){
        Map<String, JsonWrap> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : asJsonObject().entrySet()) {
            JsonWrap value = new JsonWrap(entry.getValue());
            map.put(entry.getKey(),value);
        }
        return map;
    }

    public Map<String, JsonWrap> asFullMap(){
        Map<String, JsonWrap> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : asJsonObject().entrySet()) {
            JsonWrap value = new JsonWrap(entry.getValue());
            if (value.isJsonObject()) {
                map.put(entry.getKey(), new JsonWrap(value.asFullMap()));
            }else{
                map.put(entry.getKey(),value);
            }
        }
        return map;
    }

    public JsonWrap next(String name){
        return new JsonWrap(asJsonObject().get(name));
    }

    public boolean isJsonElement(){
        return (value instanceof JsonElement);
    }

    public boolean isJsonObject(){
        return ((JsonElement)value).isJsonObject();
    }

    public JsonObject asJsonObject(){
        return asJsonElement().getAsJsonObject();
    }

    public JsonElement asJsonElement() {
        return (JsonElement) value;
    }

    public JsonArray asJsonArray(){
        return asJsonElement().getAsJsonArray();
    }
    @Override
    public String toString() {
        return value.toString();
    }
}
