package app.database;

import app.base.Admin;
import app.base.Member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UserDAO {
    public static final String MAIN_TABLE = "users";

    public static Admin getAdminFromSignIn(String username, String password) {
        //Kiểm tra username + password + role
        String query = String.format("SELECT * FROM %s WHERE username = '%s' AND password = '%s' " +
                "AND role = 'admin'", MAIN_TABLE, username, password);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            if (resultSet.next()) {//kiểm tra result có hàng nào k
                //lấy birthday từ db chuyển thành LocalDate
                String birthdayString = resultSet.getString("birthday");
                LocalDate birthday = LocalDate.parse(
                        birthdayString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                // Chuyển đổi ResultSet thành đối tượng Admin
                return new Admin(
                        resultSet.getString("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        birthday,
                        resultSet.getString("email"),
                        resultSet.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;//k tìm thấy trả về null
    }

    public static Member getMemberFromId(String id) {
        //kiểm tra id + role
        String query = String.format("SELECT * FROM %s WHERE user_id = '%s' AND role = 'member'",
                MAIN_TABLE, id);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            if (resultSet.next()) {
                //lấy birthday từ db chuyển thành LocalDate
                String birthdayString = resultSet.getString("birthday");
                LocalDate birthday = LocalDate.parse(
                        birthdayString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                // Chuyển đổi ResultSet thành đối tượng Menber
                return new Member(
                        resultSet.getString("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        birthday,
                        resultSet.getString("email"),
                        resultSet.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static Member getMemberFromSignIn(String username, String password) {
        //Kiểm tra username + password + role
        String query = String.format("SELECT * FROM %s WHERE username = '%s' AND password = '%s' " +
                "AND role = 'member'", MAIN_TABLE, username, password);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            if (resultSet.next()) {//kiểm tra result có hàng nào k
                //lấy birthday từ db chuyển thành LocalDate
                String birthdayString = resultSet.getString("birthday");
                LocalDate birthday = LocalDate.parse(
                        birthdayString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                // Chuyển đổi ResultSet thành đối tượng Admin
                return new Member(
                        resultSet.getString("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        birthday,
                        resultSet.getString("email"),
                        resultSet.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;//k tìm thấy trả về null
    }

    public static ArrayList<Member> getAllMember() {
        //Kiểm tra role
        ArrayList<Member> members = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE role = 'member'", MAIN_TABLE);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            while (resultSet.next()) {
                //lấy birthday từ db chuyển thành LocalDate
                String birthdayString = resultSet.getString("birthday");
                LocalDate birthday = LocalDate.parse(
                        birthdayString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                //thêm member vào danh sách
                members.add(
                        new Member(
                                resultSet.getString("user_id"),
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                birthday,
                                resultSet.getString("email"),
                                resultSet.getString("phone_number")
                        ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return members;
    }

    public static void addMember(Member member) {
        member.setId(DatabaseManagement.createRandomIdInTable(MAIN_TABLE, "user_id"));
        //thêm các thuộc tính vào DB, chú ý đổi birthday sang String
        // Chuyển LocalDate birthday sang string dạng dd/MM/yyyy
        String birthdayString = member.getBirthday().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String query = String.format(
                "INSERT INTO %s (user_id, username, password, first_name, last_name, birthday, " +
                        "email, phone_number, role) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', 'member')",
                MAIN_TABLE,
                member.getId(),
                member.getUsername(),
                member.getPassword(),
                member.getFirstName(),
                member.getLastName(),
                birthdayString,
                member.getEmail(),
                member.getPhoneNumber()
        );

        DatabaseManagement.executeUpdate(query);
    }

    public static void removeMember(String memberId) {
        //Xóa member có id tương ứng và xóa các rating và receipt
        //Xóa ratings
        String deleteRatingsQuery = String.format("DELETE FROM ratings WHERE user_id = '%s'", memberId);
        DatabaseManagement.executeUpdate(deleteRatingsQuery);

        //Xóa receipts
        String deleteReceiptsQuery = String.format("DELETE FROM receipts WHERE user_id = '%s'", memberId);
        DatabaseManagement.executeUpdate(deleteReceiptsQuery);

        //xóa member
        String deleteMemberQuery = String.format("DELETE FROM %s WHERE user_id = '%s'", MAIN_TABLE, memberId);
        DatabaseManagement.executeUpdate(deleteMemberQuery);
    }

    public static void updateMember(Member member) {
        //Cập nhật member bằng cách gọi hàm remove(id) sau đó addMember cho member mới
        removeMember(member.getId());
        addMember(member);
    }

    public static boolean checkUsernameExist(String username) {
        //Kiểm tra username
        String query = String.format("SELECT * FROM %s WHERE username = '%s'", MAIN_TABLE, username);
        ResultSet resultSet = DatabaseManagement.getResultSetFromQuery(query);
        try {
            //nếu kết quả username đã tồn tại
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false; //Username không tồn tại
    }
}
