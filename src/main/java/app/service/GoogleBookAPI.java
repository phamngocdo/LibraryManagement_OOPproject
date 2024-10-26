package app.service;

import app.base.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class GoogleBookAPI {
    private static final String GG_BOOK_API_URL = "https://www.googleapis.com/books/v1/volumes/";
    private static final String API_KEY = "AIzaSyApmeiKeZsc5BFbGFqAHJa3kAUybkoQVjY";

    public static Document getDocFromId(String id) throws Exception {
        //Không sử dụng try, catch mà sử dụng throw để class khác xử lí
        //Sử dụng thư viện java.net và org.json
        //Truy xuất các thông tin sau và đưa vào hashmap:
        //publisher, publishedDate, description, pageCount, language
        //Chú ý đổi hết về String, nếu thông tin rỗng thì trả về N/A

        String apiUrl = GG_BOOK_API_URL + id + "?key=" + API_KEY;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            // Đọc phản hồi lỗi từ API
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorResponse.append(line);
            }
            errorReader.close();

            // Phân tích JSON lỗi
            JSONObject errorJson = new JSONObject(errorResponse.toString());
            String errorMessage = errorJson.getJSONObject("error")
                    .getJSONArray("errors")
                    .getJSONObject(0)
                    .getString("message");

            throw new Exception("Error: " + responseCode + " - " + errorMessage);
        }

        // Đọc dữ liệu từ API
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        // Chuyển đổi chuỗi JSON thành đối tượng JSONObject
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONObject volumeInfo = jsonResponse.getJSONObject("volumeInfo");

        // Truy xuất và lưu thông tin vào HashMap
        HashMap<String, String> information = new HashMap<>();
        information.put("publisher", getStringOrNA(volumeInfo, "publisher"));
        information.put("publishedDate", getStringOrNA(volumeInfo, "publishedDate"));
        information.put("description", getStringOrNA(volumeInfo, "description"));
        information.put("pageCount", String.valueOf(volumeInfo.optInt("pageCount", 0)));
        information.put("language", getStringOrNA(volumeInfo, "language"));

    }

    //lấy String hoặc trả về "N/A" nếu không có giá trị
    private static String getStringOrNA(JSONObject obj, String key) {
        try {
            return obj.optString(key, "N/A");
        } catch (JSONException e) {
            return "N/A";
        }
    }
}
