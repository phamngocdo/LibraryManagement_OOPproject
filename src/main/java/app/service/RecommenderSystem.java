package app.service;

import app.base.Document;
import app.base.Member;

import java.util.ArrayList;

public class RecommenderSystem {
    public static ArrayList<Document> getRecommendDocFromMember(Member member) {
        // Hàm này dựa vào danh sách receipts của member để gợi ý ra 5 sách cho họ.
        // Nếu chưa mượn sách nào thì lấy ngẫu nhiên 5 sách từ member.seeTopRatingDoc(50)
        // Nếu mượn rồi thì thực hiện việc sau:
        // Tạo một list gồm: (xem các class author và category dao để sử dụng)
        //       các sách cùng thể loại với các từng sách trong receipts
        //       các sách cùng tác giả với....
        // Lấy ngẫu nhiên 5 sách từ list trên (có thể ưu tiên các sách rating cao)
        //     nếu list trên < 5 thì lấy ngẫu nhiên từ seeTopRatingDoc()
        // Trả về 5 sách được đề xuất
        return new ArrayList<>();
    }
}
