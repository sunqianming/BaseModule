package com.base.module.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Gson处理类
 * Created by SQM on 2017/5/26.
 */

public class GsonUtils {

    private static String GSONTIME = "yyyy-MM-dd hh:mm:ss";

    public static Object genObj(byte[] jsonBytes, Class<?> classType) {
        String jsonString = new String(jsonBytes);
        return genObj(jsonString, classType);
    }

    /**
     * 创建一个gson
     *
     * @return
     */
    public static Gson getTheGson() {
        return new GsonBuilder()
                .setDateFormat(GSONTIME)
                .create();
    }

    /**
     * 解析实体
     *
     * @param jsonString json
     * @param classType  类型
     * @return
     */
    public static Object genObj(String jsonString, Class<?> classType) {//解析实体
        if (jsonString != null) {
            Object object = null;
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat(GSONTIME);
            gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
            Gson gson = gsonBuilder.create();
            try {
                object = gson.fromJson(jsonString, classType);
            } catch (JsonSyntaxException e) {

            }
            return object;
        } else {
            return null;
        }
    }

    public static Object genObj(String jsonString, TypeToken<?> typeToken) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        Object object = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(GSONTIME);
        gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
        Gson gson = gsonBuilder.create();
        try {
            object = gson.fromJson(jsonString, typeToken.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static String toJson(Object obj, Class<?> objType) {
        if (obj != null && objType != null) {
            // Body Content
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat(GSONTIME);
            gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
            Gson gson = gsonBuilder.create();
            return gson.toJson(obj, objType);
        }
        return null;
    }

    public static class TimestampTypeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

        private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public JsonElement serialize(Timestamp ts, Type t, JsonSerializationContext jsc) {
            String dfString = format.format(new Date(ts.getTime()));
            return new JsonPrimitive(dfString);
        }

        @Override
        public Timestamp deserialize(JsonElement json, Type t, JsonDeserializationContext jsc) throws JsonParseException {
            if (!(json instanceof JsonPrimitive)) {
                throw new JsonParseException("The date should be a string value");
            }
            try {
                Date date = format.parse(json.getAsString());
                return new Timestamp(date.getTime());
            } catch (ParseException e) {
                return new Timestamp(new Date().getTime());
            }
        }

    }
}

