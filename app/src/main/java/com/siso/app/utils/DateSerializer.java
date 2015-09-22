/**
 * Copyright (c) 2015
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.siso.app.utils;

import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.Date;
 
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
 
/**
 * 
 * description :
 *
 * @version 1.0
 * @author Owater
 * @createtime : 2015-4-8 上午12:17:44
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * Owater        2015-4-8 上午12:17:44
 *
 */
public class DateSerializer implements JsonSerializer<Date>,JsonDeserializer<java.util.Date> {
	
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTime());
    }
    
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
    }
}

	