package app.service;

import app.base.Author;
import app.base.Category;
import app.base.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GoogleBookAPI {
    private static final String GG_BOOK_API_URL = "https://www.googleapis.com/books/v1/volumes/";

    public static Document getDocFromIsbn(String isbn) throws Exception {
        // Không sử dụng try-catch mà sử dụng throw để lớp khác xử lý
        // Sử dụng thư viện java.net và org.json

        String apiUrl = GG_BOOK_API_URL + "?q=isbn:" + isbn;
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

        // Kiểm tra nếu có tài liệu nào trả về
        if (!jsonResponse.has("items")) {
            throw new Exception("Không tìm thấy tài liệu với ISBN: " + isbn);
        }

        // Lấy thông tin từ volumeInfo của cuốn sách đầu tiên
        JSONObject volumeInfo = jsonResponse.getJSONArray("items")
                .getJSONObject(0).getJSONObject("volumeInfo");

        // Truy xuất thông tin từ JSON và xử lý nếu thiếu giá trị
        String id = jsonResponse.getJSONArray("items").getJSONObject(0).getString("id");
        String title = volumeInfo.optString("title", "N/A");
        String publisher = volumeInfo.optString("publisher", "N/A");
        String publishedDate = volumeInfo.optString("publishedDate", "N/A");
        String description = volumeInfo.optString("description", "N/A");
        int pageCount = volumeInfo.optInt("pageCount", 0);
        double averageScore = volumeInfo.optDouble("averageRating", 0);
        int ratingCount = volumeInfo.optInt("ratingsCount", 0);

        // Lấy danh sách tác giả
        JSONArray authorsJSON = volumeInfo.optJSONArray("authors");
        ArrayList<Author> authors = new ArrayList<>();
        if (authorsJSON != null) {
            for (int i = 0; i < authorsJSON.length(); i++) {
                authors.add(new Author("", authorsJSON.getString(i)));
            }
        }

        // Lấy danh sách thể loại
        JSONArray categoriesJSON = volumeInfo.optJSONArray("categories");
        ArrayList<Category> categories = new ArrayList<>();
        if (categoriesJSON != null) {
            for (int i = 0; i < categoriesJSON.length(); i++) {
                categories.add(new Category("", categoriesJSON.getString(i)));
            }
        }

        // Lấy ảnh bìa (thumbnail) nếu có
        String imageUrl = volumeInfo.optJSONObject("imageLinks") != null
                ? volumeInfo.getJSONObject("imageLinks").optString("thumbnail", "")
                : "N/A";

        // Tạo đối tượng Document và trả về
        return new Document(id, title, isbn, 0, 0, ratingCount, averageScore, pageCount,
                description, publisher, publishedDate, imageUrl, authors, categories, null);
    }
}
