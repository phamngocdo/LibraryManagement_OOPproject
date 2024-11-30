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
        String apiUrl = GG_BOOK_API_URL + "?q=isbn:" + isbn;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorResponse.append(line);
            }
            errorReader.close();

            JSONObject errorJson = new JSONObject(errorResponse.toString());
            String errorMessage = errorJson.getJSONObject("error")
                    .getJSONArray("errors")
                    .getJSONObject(0)
                    .getString("message");

            throw new Exception("Error: " + responseCode + " - " + errorMessage);
        }
        
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(response.toString());

        if (!jsonResponse.has("items")) {
            throw new Exception("Không tìm thấy tài liệu với ISBN: " + isbn);
        }

        JSONObject volumeInfo = jsonResponse.getJSONArray("items")
                .getJSONObject(0).getJSONObject("volumeInfo");

        String id = jsonResponse.getJSONArray("items").getJSONObject(0).getString("id");
        String title = volumeInfo.optString("title", "N/A");
        String publisher = volumeInfo.optString("publisher", "N/A");
        String publishedDate = volumeInfo.optString("publishedDate", "N/A");
        String description = volumeInfo.optString("description", "N/A");
        int pageCount = volumeInfo.optInt("pageCount", 0);
        double averageScore = volumeInfo.optDouble("averageRating", 0);
        int ratingCount = volumeInfo.optInt("ratingsCount", 0);

        JSONArray authorsJSON = volumeInfo.optJSONArray("authors");
        ArrayList<Author> authors = new ArrayList<>();
        if (authorsJSON != null) {
            for (int i = 0; i < authorsJSON.length(); i++) {
                authors.add(new Author("", authorsJSON.getString(i)));
            }
        }

        JSONArray categoriesJSON = volumeInfo.optJSONArray("categories");
        ArrayList<Category> categories = new ArrayList<>();
        if (categoriesJSON != null) {
            for (int i = 0; i < categoriesJSON.length(); i++) {
                categories.add(new Category("", categoriesJSON.getString(i)));
            }
        }

        String imageUrl = volumeInfo.optJSONObject("imageLinks") != null
                ? volumeInfo.getJSONObject("imageLinks").optString("thumbnail", "")
                : "N/A";

        return new Document(id, title, isbn, 0, 0, ratingCount, averageScore, pageCount,
                description, publisher, publishedDate, imageUrl, authors, categories, null);
    }
}
