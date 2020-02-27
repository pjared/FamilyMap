package Handlers;

import com.google.gson.Gson;

public class Deserialize {
    public static <T> T deserialize(String value, Class<T> returnType) {
        return (new Gson()).fromJson(value, returnType);
    }
    //Use the call MyDataTypevalue = JsonSerializer.deserialize(jsonString, MyDataType.class);
    //to convert


    public static String serialize(Object inputType) {
        return (new Gson().toJson(inputType));
    }

}
