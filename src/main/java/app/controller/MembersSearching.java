package app.controller;

import app.base.Admin;
import app.base.Member;
import app.run.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class MembersSearching {

    @FXML
    private TextField searchingField;

    @FXML
    private VBox vBox;

    @FXML
    private Label notFoundLabel;

    private ArrayList<Member> allMembers;

    @FXML
    private void initialize() {
        notFoundLabel.setVisible(false);
        allMembers = ((Admin) App.currentUser).seeAllMemberInfo();
        addMembersToVbox(allMembers);
        setUpSearchField();
    }

    private void setUpSearchField() {
        // Hàm này cài đặt thanh tìm kiếm: Tìm kiếm theo tên hoặc id
        // Khi người dùng ấn Enter thì sẽ tìm kiếm các member có tên hoặc id bắt đầu bằng chữ cái trong textfield và lưu vào một ArrayList<member>
        // Sau đó vBox.getChildren.clear và addmembertovbox(list)
        // Nếu list rỗng thì notFoundLabel.setVisible(true) còn không thì false
    }

    private void addMembersToVbox(ArrayList<Member> members) {
        // Hàm này thêm các thông tin từ members vào Vbox
        // Mỗi hàng của Vbox có tối đa 3 thông tin của member
        // Ví dụ nếu có 5 người thì tạo 2 hàng, 1 hàng chứa 3 và 1 hàng chứa 2
        // memberIndex = 0
        // for i = 0 đến số lượng Hbox:
        //     new Hbox
        //     for j = 0 đến 3:
        //         If memberIndex = members.size: break
        //         Tạo 1 Pane 282:166
        //         Tạo các label sau với các vị trí
        //             Họ và tên (vị trí (14,14))
        //             Mã người dùng (vị trí (14,38))
        //             Ngày sinh ở vị trí (14, 64)
        //             Email ở vị trí (14,92)
        //             Số đt ở vị trí (14,121)
        //         Pane.getChildren.addAll(các label)
        //         Hbox.getChildren.add(pane)
        //     vbox.getCHildren.add(hbox)
    }
}
