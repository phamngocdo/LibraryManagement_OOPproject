package database;

import app.base.Admin;
import app.base.Member;
import app.database.DatabaseManagement;
import app.database.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    @BeforeEach
    public void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    public void testGetAdminFromSignIn_Valid() {
        Admin admin = UserDAO.getAdminFromSignIn("admin", "admin");
        assertNotNull(admin);//kiểm tra xem có null k nếu null testcase sai
        //kiểm tra username lấy đưược có bằng expected k
        assertEquals("admin", admin.getUsername());
    }

    @Test
    public void testGetAdminFromSignIn_Invalid() {
        Admin admin = UserDAO.getAdminFromSignIn("admin", "wrongPassword");
        assertNull(admin);//null đúng
    }

    @Test
    public void testGetAdminFromSignIn_NonExistingAdmin() {
        Admin admin = UserDAO.getAdminFromSignIn("non-admin", "admin");
        assertNull(admin);
    }

    @Test
    public void testGetMemberFromId_ValidId() {
        Member member = UserDAO.getMemberFromId("OJIK98JHNTMT");
        assertNotNull(member);
        assertEquals("member1", member.getUsername());
    }

    @Test
    public void testGetMemberFromId_InvalidId() {
        Member member = UserDAO.getMemberFromId("invalid-id");
        assertNull(member);
    }

    @Test
    public void testGetMemberFromId_NonExistingMember() {
        Member member = UserDAO.getMemberFromId("non-member");
        assertNull(member);
    }

    @Test
    public void testGetMemberFromSignIn_Valid() {
        Member member = UserDAO.getMemberFromSignIn("member2", "member2");
        assertNotNull(member);
        assertEquals("member2", member.getUsername());
    }

    @Test
    public void testGetMemberFromSignIn_Invalid() {
        Member member = UserDAO.getMemberFromSignIn("member2", "wrongPassword");
        assertNull(member);
    }

    @Test
    public void testGetMemberFromSignIn_NonExistingAdmin() {
        Admin admin = UserDAO.getAdminFromSignIn("non-menber", "member2");
        assertNull(admin);
    }

    @Test
    public void testGetAllMembers() {
        ArrayList<Member> members = UserDAO.getAllMember();
        assertNotNull(members);
        //kiểm tra danh sách member có trống k
        assertFalse(members.isEmpty());
    }

    @Test
    public void testAddMember() {
        Member member = new Member(
                "newmemberid", "member3", "member3",
                null, null,
                LocalDate.of(2024, 6, 10),
                null, null);
        UserDAO.addMember(member);
        Member retrievedMember = UserDAO.getMemberFromId("OIKM891R111T");
        assertNotNull(retrievedMember);//k null đúng
    }

    @Test
    public void testRemoveMember() {
        Member memberToRemove = new Member(
                "newmemberid1", "member3", "member3",
                null, null,
                LocalDate.of(2024, 6, 10),
                null, null);
        UserDAO.addMember(memberToRemove);
        UserDAO.removeMember("newmemberid1");
        Member member = UserDAO.getMemberFromId("newmemberid1");
        assertNull(member);
    }

    @Test
    public void testUpdateMember() {
        //lấy id member2 từ db
        Member member = new Member("MJ2394NMUBO", "member4", "member4",
                null, null,
                null,
                null, null);
        UserDAO.updateMember(member);
        Member updatedMember = UserDAO.getMemberFromId("MJ2394NMUBO");
        assertNotNull(updatedMember);
        assertEquals("member4", updatedMember.getUsername());
    }

    @Test
    public void testCheckUsernameExist_ExistingUsername() {
        boolean exists = UserDAO.checkUsernameExist("OIKM891R138T");
        assertTrue(exists); //true vì username đã tồn tại
    }

    @Test
    public void testCheckUsernameExist_NonExistingUsername() {
        boolean exists = UserDAO.checkUsernameExist("non-username");
        assertFalse(exists); // Kết quả phải là false vì username không tồn tại
    }
}
