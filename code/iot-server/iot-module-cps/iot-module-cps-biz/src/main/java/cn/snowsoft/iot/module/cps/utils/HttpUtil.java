package cn.snowsoft.iot.module.cps.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
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
            log.warn("请求地址 {}   内容 {} [发生异常 {}]", url, jsonInput, e.getMessage());
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

    public static boolean sendPut(String url, JSONObject jsonInput) {
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

            con.disconnect();

            return responseCode == 200 || responseCode == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}