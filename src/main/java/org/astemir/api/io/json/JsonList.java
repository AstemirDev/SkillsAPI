package org.astemir.api.io.json;


import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.math.Vector4f;
import org.astemir.api.math.components.Vector2;
import org.astemir.api.math.components.Vector3;
import java.util.ArrayList;
import java.util.List;

@Json(deserialize = @Deserialize("deserialize"))
public class JsonList<T> {

    private List<T> list = new ArrayList<>();

    public void add(T child){
        list.add(child);
    }

    public JsonPrimitive get(int index){
        return (JsonPrimitive) list.get(index);
    }

    public List<T> list() {
        return list;
    }

    public <K> List<K> newList(Class<K> className){
        List<K> result = new ArrayList<>();
        for (T t : list) {
            result.add(new JsonWrap(t).cast(className));
        }
        return result;
    }

    public Vector2 vector2(){
        if (list.size() > 0) {
            return new Vector2(get(0).getAsDouble(), get(1).getAsDouble());
        }else{
            return new Vector2(0,0);
        }
    }

    public Vector3 vector3(){
        if (list.size() > 0) {
            return new Vector3(get(0).getAsDouble(), get(1).getAsDouble(), get(2).getAsDouble());
        }else{
            return new Vector3(0,0,0);
        }
    }

    public Vector4f vector4f(){
        if (list.size() > 0) {
            return new Vector4f(get(0).getAsFloat(), get(1).getAsFloat(), get(2).getAsFloat(), get(3).getAsFloat());
        }else{
            return new Vector4f(0,0,0,0);
        }
    }


    public static JsonList deserialize(JsonElement element) {
        JsonWrap root = new JsonWrap(element);
        JsonList list = new JsonList();
        for (JsonElement child : root.asJsonArray()) {
            list.add(child);
        }
        return list;
    }
}
