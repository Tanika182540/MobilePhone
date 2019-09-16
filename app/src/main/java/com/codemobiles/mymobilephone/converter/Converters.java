package com.codemobiles.mymobilephone.converter;

import androidx.room.TypeConverter;

import com.codemobiles.mobilephone.models.MobileBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
@TypeConverter
public static ArrayList<MobileBean> fromString(String value) {
    Type listType = new TypeToken<ArrayList<MobileBean>>() {}.getType();
    return new Gson().fromJson(value, listType);
}

@TypeConverter
public static String fromArrayList(ArrayList<MobileBean> list) {
    Gson gson = new Gson();
    String json = gson.toJson(list);
    return json;
}
}