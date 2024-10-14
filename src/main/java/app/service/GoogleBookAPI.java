package app.service;

import java.util.HashMap;

public class GoogleBookAPI {
    private static final String GG_BOOK_API_URL = "https://www.googleapis.com/books/v1/volumes/";

    public static HashMap<String, String> getDocFromId(String id) {
        //Không sử dụng try, catch mà sử dụng throw để class khác xử lí
        //Sử dụng thư viện java.net và org.json
        //Truy xuất các thông tin sau và đưa vào hashmap:
        //publisher, publishedDate, description, pageCount, language
        //Chú ý đổi hết về String, nếu thông tin rỗng thì trả về N/A
        HashMap<String, String> information = new HashMap<>();

        return information;
    }
}
