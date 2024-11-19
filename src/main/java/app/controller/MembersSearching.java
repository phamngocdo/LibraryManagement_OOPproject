package app.controller;

import app.base.Admin;
import app.base.Member;
import app.run.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
        // Ẩn thông báo "Không tìm thấy thông tin"
        notFoundLabel.setVisible(false);

        // Lấy danh sách thành viên từ Admin
        allMembers = ((Admin) App.currentUser).seeAllMemberInfo();

        if (allMembers == null || allMembers.isEmpty()) {
            notFoundLabel.setVisible(true);
        } else {
            addMembersToVbox(allMembers);
        }

        setUpSearchField();
    }

    private void setUpSearchField() {
        // Lắng nghe sự kiện Enter trong thanh tìm kiếm
        searchingField.setOnAction(event -> {
            String searchText = searchingField.getText().trim().toLowerCase();

            // Lọc danh sách thành viên theo ID hoặc tên
            ArrayList<Member> filteredMembers = filterMembers(searchText);

            // Xóa nội dung VBox và cập nhật danh sách
            vBox.getChildren().clear();
            if (filteredMembers.isEmpty()) {
                notFoundLabel.setVisible(true);
            } else {
                notFoundLabel.setVisible(false);
                addMembersToVbox(filteredMembers);
            }
        });
    }

    private ArrayList<Member> filterMembers(String searchText) {
        // Lọc danh sách thành viên theo ID hoặc tên (không phân biệt hoa/thường)
        return (ArrayList<Member>) allMembers.stream()
                .filter(member -> member.getId().toLowerCase().contains(searchText) ||
                        (member.getFirstName() + " " + member.getLastName()).toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }

    private void addMembersToVbox(ArrayList<Member> members) {
        // Tính số lượng hàng (mỗi hàng tối đa 3 thành viên)
        int rowCount = (int) Math.ceil(members.size() / 3.0);

        int memberIndex = 0;
        for (int i = 0; i < rowCount; i++) {
            HBox hBox = new HBox();
            hBox.setSpacing(10);

            for (int j = 0; j < 3; j++) {
                if (memberIndex >= members.size()) break;

                Member member = members.get(memberIndex);
                Pane memberPane = createMemberPane(member);

                hBox.getChildren().add(memberPane);
                memberIndex++;
            }

            vBox.getChildren().add(hBox);
        }
    }

    private Pane createMemberPane(Member member) {
        // Tạo Pane hiển thị thông tin của một thành viên
        Pane pane = new Pane();
        pane.setPrefSize(282, 166);
        pane.getStyleClass().add("member-pane"); // Có thể thêm CSS class

        Label nameLabel = new Label("Họ và Tên: " + member.getFirstName() + " " + member.getLastName());
        nameLabel.setLayoutX(14);
        nameLabel.setLayoutY(14);

        Label idLabel = new Label("Mã Thành Viên: " + member.getId());
        idLabel.setLayoutX(14);
        idLabel.setLayoutY(38);

        Label birthdayLabel = new Label("Ngày Sinh: " + member.getBirthday());
        birthdayLabel.setLayoutX(14);
        birthdayLabel.setLayoutY(64);

        Label emailLabel = new Label("Email: " + member.getEmail());
        emailLabel.setLayoutX(14);
        emailLabel.setLayoutY(92);

        Label phoneLabel = new Label("Số Điện Thoại: " + member.getPhoneNumber());
        phoneLabel.setLayoutX(14);
        phoneLabel.setLayoutY(121);

        pane.getChildren().addAll(nameLabel, idLabel, birthdayLabel, emailLabel, phoneLabel);

        return pane;
    }
}
