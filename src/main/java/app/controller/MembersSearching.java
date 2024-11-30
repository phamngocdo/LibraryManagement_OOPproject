package app.controller;

import app.base.Admin;
import app.base.Member;
import app.run.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.Optional;
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
        notFoundLabel.setVisible(false);
        allMembers = ((Admin) App.currentUser).seeAllMemberInfo();

        if (allMembers == null || allMembers.isEmpty()) {
            notFoundLabel.setVisible(true);
        } else {
            addMembersToVbox(allMembers);
        }

        setUpSearchField();
    }

    private void setUpSearchField() {
        searchingField.setOnAction(event -> {String searchText = searchingField.getText().trim().toLowerCase();
            ArrayList<Member> filteredMembers = filterMembers(searchText);
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
        return (ArrayList<Member>) allMembers.stream()
                .filter(member -> member.getId().toLowerCase().contains(searchText) ||
                        (member.getFirstName() + " " + member.getLastName()).toLowerCase().contains(searchText) ||
                        (member.getEmail()).toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }

    private void addMembersToVbox(ArrayList<Member> members) {
        int rowCount = (int) Math.ceil(members.size() / 3.0);

        int memberIndex = 0;
        for (int i = 0; i < rowCount; i++) {
            HBox hBox = new HBox();
            hBox.setSpacing(15);

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
        Pane pane = new Pane();
        pane.setPrefSize(282, 200);
        pane.getStyleClass().add("member-pane");

        TextFlow nameFlow = getTextFlow("Họ và tên: ", member.getFirstName()+ " " + member.getLastName());
        nameFlow.setLayoutX(14);
        nameFlow.setLayoutY(14);

        TextFlow idFlow = getTextFlow("Mã thành viên: ", member.getId());
        idFlow.setLayoutX(14);
        idFlow.setLayoutY(38);

        TextFlow birthdayFlow = getTextFlow("Ngày sinh: ", member.getBirthday());
        birthdayFlow.setLayoutX(14);
        birthdayFlow.setLayoutY(64);

        TextFlow emailFlow = getTextFlow("Email: ", member.getEmail());
        emailFlow.setLayoutX(14);
        emailFlow.setLayoutY(90);

        TextFlow phoneFlow = getTextFlow("Số điện thoại: ", member.getPhoneNumber());
        phoneFlow.setLayoutX(14);
        phoneFlow.setLayoutY(116);

        TextFlow numberOfBorrowedDocFlow = getTextFlow("Số sách đã mượn: ",
                String.valueOf(member.getReceipts().size()));
        numberOfBorrowedDocFlow.setLayoutX(14);
        numberOfBorrowedDocFlow.setLayoutY(142);

        int numberOfReturnedDoc = member.getReceipts().stream()
                                .filter(receipt -> "returned".equals(receipt.getStatus()))
                                .toList().size();
        TextFlow numberOfReturnedDocFlow = getTextFlow("Số sách đã trả: ", String.valueOf(numberOfReturnedDoc));
        numberOfReturnedDocFlow.setLayoutX(14);
        numberOfReturnedDocFlow.setLayoutY(168);

        ImageView trashImage = new ImageView();
        trashImage.getStyleClass().add("trash-icon");
        trashImage.setLayoutX(253);
        trashImage.setLayoutY(170);
        trashImage.setFitWidth(20);
        trashImage.setFitHeight(20);
        trashImage.setOnMouseClicked(event -> removeMember(member.getId(), pane));

        pane.getChildren().addAll(nameFlow, idFlow, emailFlow, birthdayFlow, phoneFlow,
                numberOfReturnedDocFlow, numberOfBorrowedDocFlow, trashImage);
        pane.getStyleClass().add("member-info-pane");
        return pane;
    }

    private TextFlow getTextFlow(String title, String content) {
        TextFlow textFlow = new TextFlow();
        textFlow.setStyle("-fx-font-size: 13");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold");

        Label contentLabel = new Label(content);

        textFlow.getChildren().addAll(titleLabel, contentLabel);
        return textFlow;
    }

    private void removeMember(String memberId, Pane pane) {
        ButtonType buttonType = new ButtonType("OK");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xóa thành viên");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc muốn xóa thành viên có số ID là " + memberId + " không?");
        alert.getButtonTypes().setAll(buttonType);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonType) {
            ((Admin) App.currentUser).removeMember(memberId);
        }
        vBox.getChildren().remove(pane);
    }
}
