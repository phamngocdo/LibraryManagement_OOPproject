package app.controller;

public class ReceiptsSearching {
    // Class này hiển thị tất cả các phiếu mượn cho admin xem (sử dụng (admin) app.currentUser.seeAllReceipts)
    // Một số các chức năng cần viết:

    // Hiển thị các thông tin của từng phiếu vào bảng tableView,
    //              nếu không có thì hiển thị không tìm thấy thông tin ở giữa bằng cách nào đó (có thể cho vào bảng càng tốt)
    // Trong bảng này các cột từ 1 đến 5 là kiểu String, riêng cột 6 là JFXCheckBox (để text là"") sử dụng để Admin đánh dấu là đã trả sách
    //                                                                              (nhớ sử dụng (admin) app.currentUser.confirmreturnDoc)


    // Xử lí thanh tìm kiếm: Khi người dùng nhập một chuỗi thì hiển thị phiếu có id phiếu hoặc id doc hoặc id member
    //                       bắt đầu bằng chuỗi đấy

    // Lọc thông tin phiếu: Sử dụng JFXComboBox để chọn thông tin cần lọc: Tất cả (mặc định), Đã trả, Chưa trả, Quá hạn

    // Tìm kiếm thông tin bằng QR: nhúng event mouseclick cho cái ảnh QRcode, sử dụng FileChooser để chọn ảnh và hiển thị
}
