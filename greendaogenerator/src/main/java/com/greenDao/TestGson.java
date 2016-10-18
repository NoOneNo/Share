package com.greenDao;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;

/**
 * Created by yuhy on 2016/10/18.
 */

public class TestGson {

    public static void main(String[] args) {

        String json = "{\n" +
                "    \"error\": \"request uid's value must be the current user\",\n" +
                "    \"error_code\": 21335,\n" +
                "    \"request\": \"/2/statuses/user_timeline.json\"\n" +
                "}";

        Gson gson = new Gson();

        try {
            JsonReader jsonReader = gson.newJsonReader(new StringReader(json));
            testGson(jsonReader);
            testGson(jsonReader);
            jsonReader.close();

            Hello hello = gson.fromJson(json, Hello.class);
            if(hello == null) {
                System.out.println("hello is null");
            }else{
                System.out.println(hello);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testGson(JsonReader jsonReader) {


        try {
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                System.out.println("name : " + name + " , value : " + jsonReader.nextString());
            }

            jsonReader.endObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Hello{
        String hello;

        public String getHello() {
            return hello;
        }

        public void setHello(String hello) {
            this.hello = hello;
        }

        @Override
        public String toString() {
            return "Hello{" +
                    "hello='" + hello + '\'' +
                    '}';
        }
    }

    private class Error {
        String error;

        String error_code;

        String request;
    }
}
