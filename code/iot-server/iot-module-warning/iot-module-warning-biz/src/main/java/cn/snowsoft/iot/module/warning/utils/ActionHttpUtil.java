package cn.snowsoft.iot.module.warning.utils;

import cn.snowsoft.iot.module.warning.dal.dataobject.warning.north.HTTPDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.record.WarningActionRecord;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class ActionHttpUtil {
    public static WarningActionRecord sendPost(HTTPDO httpdo, JSONObject jsonInput) {
        WarningActionRecord warningActionRecord = new WarningActionRecord();
        try {
            URL obj = new URL(httpdo.getRequestPath());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            con.setRequestProperty("Content-Type", "application/json");
            //设置请求头
            List<HashMap<String, Object>> requestHeaders = httpdo.getRequestHeaders();
            for (HashMap<String, Object> header : requestHeaders) {
                con.setRequestProperty(header.get("key").toString(), header.get("value").toString());
            }

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
                //请求成功
                warningActionRecord.setOutputStatus(1);
                return warningActionRecord;
            } else {
                //请求失败
                warningActionRecord.setFailReason("HTTP请求失败,状态码为 "+responseCode);
                warningActionRecord.setOutputStatus(0);
                return warningActionRecord;
            }
        } catch (Exception e) {
            warningActionRecord.setFailReason("HTTP请求失败");
            warningActionRecord.setOutputStatus(0);
            //e.printStackTrace();
            log.error("HTTP请求失败:{}",e.getMessage());
            return warningActionRecord;
        }
    }

    public static WarningActionRecord sendGet(HTTPDO httpdo) {
        WarningActionRecord warningActionRecord = new WarningActionRecord();
        try {
            URL obj = new URL(httpdo.getRequestPath());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            //设置请求头
            List<HashMap<String, Object>> requestHeaders = httpdo.getRequestHeaders();
            for (HashMap<String, Object> header : requestHeaders) {
                con.setRequestProperty(header.get("key").toString(), header.get("value").toString());
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
                //请求成功
                warningActionRecord.setOutputStatus(1);
                return warningActionRecord;
            } else {
                //请求失败
                warningActionRecord.setFailReason("HTTP请求失败,状态码为 "+responseCode);
                warningActionRecord.setOutputStatus(0);
                return warningActionRecord;
            }
        } catch (Exception e) {
            warningActionRecord.setFailReason("HTTP请求失败");
            warningActionRecord.setOutputStatus(0);
            //e.printStackTrace();
            log.error("HTTP请求失败:{}",e.getMessage());
            return warningActionRecord;
        }
    }

    public static WarningActionRecord sendDelete(HTTPDO httpdo) {
        WarningActionRecord warningActionRecord = new WarningActionRecord();
        try {
            URL obj = new URL(httpdo.getRequestPath());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("DELETE");
            //设置请求头
            List<HashMap<String, Object>> requestHeaders = httpdo.getRequestHeaders();
            for (HashMap<String, Object> header : requestHeaders) {
                con.setRequestProperty(header.get("key").toString(), header.get("value").toString());
            }


            int responseCode = con.getResponseCode();

            con.disconnect();

            if (responseCode == 200 || responseCode == 201) {
                //请求成功
                warningActionRecord.setOutputStatus(1);
                return warningActionRecord;
            } else {
                //请求失败
                warningActionRecord.setFailReason("HTTP请求失败,状态码为 "+responseCode);
                warningActionRecord.setOutputStatus(0);
                return warningActionRecord;
            }
        } catch (Exception e) {
            warningActionRecord.setFailReason("HTTP请求失败");
            warningActionRecord.setOutputStatus(0);
            //e.printStackTrace();
            log.error("HTTP请求失败:{}",e.getMessage());
            return warningActionRecord;
        }
    }

    public static WarningActionRecord sendPut(HTTPDO httpdo, JSONObject jsonInput) {
        WarningActionRecord warningActionRecord = new WarningActionRecord();
        try {
            URL obj = new URL(httpdo.getRequestPath());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("PUT");
            con.setDoOutput(true);

            //设置请求头
            List<HashMap<String, Object>> requestHeaders = httpdo.getRequestHeaders();
            for (HashMap<String, Object> header : requestHeaders) {
                con.setRequestProperty(header.get("key").toString(), header.get("value").toString());
            }
            con.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = con.getOutputStream()) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                if (jsonInput != null) {
                    writer.write(jsonInput.toString());
                } else {
                    writer.write("{}");
                }
                //writer.write(jsonInput.toString());
                writer.flush();
                writer.close();
            }

            int responseCode = con.getResponseCode();

            con.disconnect();

            if (responseCode == 200 || responseCode == 201) {
                //请求成功
                warningActionRecord.setOutputStatus(1);
                return warningActionRecord;
            } else {
                //请求失败
                warningActionRecord.setFailReason("HTTP请求失败,状态码为 "+responseCode);
                warningActionRecord.setOutputStatus(0);
                return warningActionRecord;
            }
        } catch (Exception e) {
            warningActionRecord.setFailReason("HTTP请求失败");
            warningActionRecord.setOutputStatus(0);
            //e.printStackTrace();
            log.error("HTTP请求失败:{}",e.getMessage());
            return warningActionRecord;
        }
    }

    public static String testHttp(HTTPDO httpdo) {
        try {
            URL obj = new URL(httpdo.getRequestPath());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod(httpdo.getRequestMethod());
            con.setDoOutput(true);

            //设置请求头
            List<HashMap<String, Object>> requestHeaders = httpdo.getRequestHeaders();
            for (HashMap<String, Object> header : requestHeaders) {
                con.setRequestProperty(header.get("key").toString(), header.get("value").toString());
            }
            con.setRequestProperty("Content-Type", "application/json");

            if (!"GET".equals(httpdo.getRequestMethod())){
                try (OutputStream os = con.getOutputStream()) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    if (httpdo.getRequestBody() != null) {
                        writer.write(httpdo.getRequestBody().toString());
                    } else {
                        writer.write("{}");
                    }
                    writer.flush();
                    writer.close();
                }
            }

            int responseCode = con.getResponseCode();
            InputStream inputStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String result = "";
            while ( (line = reader.readLine()) != null){
                result += line;
            }

            con.disconnect();

            if (responseCode == 200 || responseCode == 201) {
                //请求成功
                return result;
            } else {
                //请求失败
                return result;
            }
        } catch (Exception e) {
            log.error("HTTP请求失败:{}",e.getMessage());
            return e.getMessage();
        }
    }

    /*public static void main(String[] args) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        HTTPDO httpdo = mapper.readValue("{\"requestHeaders\":[{\"value\":\"Bearer bc4605cf6d2c422ea3f096dd7062fb4a\",\"key\":\"authorization\"}],\"requestBody\":\"{\\n    \\\"ccc\\\":\\\"bbb\\\"\\n}\",\"requestMethod\":\"GET\",\"requestParams\":[{\"value\":\"aaaa\",\"key\":\"cccc\"},{\"value\":\"\",\"key\":\"\"}],\"requestPath\":\"http://192.168.1.249:48080/admin-api/warning/action/page?pageNo=1&pageSize=12\"}", HTTPDO.class);
        testHttp(httpdo);
    }
*/

}