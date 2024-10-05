package app.database;

import app.base.Admin;
import app.base.Member;
import java.sql.*;
import java.util.ArrayList;

public class UserDAO {
    public static final String MAIN_TABLE = "users";

    public static Admin getAdminFromSignIn(String username, String password) {
        //

    }

    public static Member getMemberFromId(String id) {
        //kiểm tra id + role

    }

    public static Member getMemberFromSignIn(String username, String password) {
        //Kiểm tra username + password + role

    }

    public static ArrayList<Member> getAllMember() {
        //Kiểm tra role

    }

    public static void addMember(Member member) {
        member.setId(DatabaseManagement.createRandomIdInTable(MAIN_TABLE, "user_id"));
        //thêm các thuộc tính vào DB, chú ý đổi birthday sang String
    }

    public static void removeMember(String memberId) {
        //Xóa member có id tương ứng và xóa các rating và receipt
    }

    public static void updateMember(Member member) {
        //Cập nhật member bằng cách gọi hàm remove(id) sau đó addMember cho member mới
    }
}
