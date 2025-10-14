package cn.snowsoft.iot.module.warning.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public static String sendPost(String url, JSONObject jsonInput) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            con.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = con.getOutputStream()) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                if (jsonInput != null) {
                    writer.write(jsonInput.toString());
                } else {
                    writer.write("{}");
                }
                writer.flush();
                writer.close();
            }

            int responseCode = con.getResponseCode();

            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            con.disconnect();
            if (responseCode == 200 || responseCode == 201) {
                return response.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sendGet(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();

            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            con.disconnect();
            if (responseCode == 200 || responseCode == 201) {
                return response.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean sendDelete(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("DELETE");

            int responseCode = con.getResponseCode();

            con.disconnect();

            return responseCode == 200 || responseCode == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String sendPut(String url, JSONObject jsonInput) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("PUT");
            con.setDoOutput(true);

            con.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = con.getOutputStream()) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonInput.toString());
                writer.flush();
                writer.close();
            }

            int responseCode = con.getResponseCode();
            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            con.disconnect();

            if (responseCode == 200 || responseCode == 201) {
                return response.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}