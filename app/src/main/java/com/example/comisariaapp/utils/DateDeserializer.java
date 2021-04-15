package com.example.comisariaapp.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

 public  class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            String date = je.getAsString();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            formatter.setTimeZone(TimeZone.getDefault());

            try {
                return formatter.parse(date);
            } catch (ParseException e) {
                System.err.println("Failed to parse Date due to:" + e.getMessage());
                return null;
            }
        }

    }
