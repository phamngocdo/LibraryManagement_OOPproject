package database;

import app.base.Admin;
import app.base.Member;
import app.database.DatabaseManagement;
import app.database.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//All Test are correct

public class UserDAOTest {

    @BeforeEach
    public void setUp() {
        DatabaseManagement.setConnection();
    }

    @Test
    public void testGetAdminFromSignIn_Valid() {
        Admin admin = (Admin) UserDAO.getUserFromLogin("admin", "admin");
        assertNotNull(admin);//kiểm tra xem có null k nếu null testcase sai
        //kiểm tra username lấy đưược có bằng expected k
        assertEquals("admin", admin.getUsername());
    }

    @Test
    public void testGetAdminFromSignIn_Invalid() {
        Admin admin = (Admin) UserDAO.getUserFromLogin("admin", "wrongPassword");
        assertNull(admin);//null đúng
    }

    @Test
    public void testGetAdminFromSignIn_NonExistingAdmin() {
        Admin admin = (Admin) UserDAO.getUserFromLogin("non-admin", "admin");
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
        Member member = (Member) UserDAO.getUserFromLogin("member1", "member1");
        assertNotNull(member);
        assertEquals("member1", member.getUsername());
    }

    @Test
    public void testGetMemberFromSignIn_Invalid() {
        Member member = (Member) UserDAO.getUserFromLogin("member1", "wrongPassword");
        assertNull(member);
    }

    @Test
    public void testGetMemberFromSignIn_NonExistingAdmin() {
        Admin admin = (Admin) UserDAO.getUserFromLogin("non-menber", "member1");
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
                "", "member3", "member3",
                "null", "null",
                "09-09-2004",
                "null", "null");
        UserDAO.addMember(member);
        Member retrievedMember = UserDAO.getMemberFromId(member.getId());
        assertNotNull(retrievedMember);//k null đúng
    }

    @Test
    public void testRemoveMember() {
        Member memberToRemove = new Member(
                "", "member3", "member3",
                "null", "null",
                "09-09-2004",
                "null", "null");
        UserDAO.addMember(memberToRemove);
        UserDAO.removeMember(memberToRemove.getId());
        Member member = UserDAO.getMemberFromId(memberToRemove.getId());
        assertNull(member);
    }

    @Test
    public void testUpdateMember() {
        //lấy id member2 từ db
        Member member = new Member("61K9MOF2IWG9", "member4", "member4",
                "null", "null",
                "09/09/2004",
                "null", "null");
        UserDAO.updateMember(member);
        Member updatedMember = UserDAO.getMemberFromId("61K9MOF2IWG9");
        assertNotNull(updatedMember);
        assertEquals("member4", updatedMember.getUsername());
    }

    @Test
    public void testCheckUsernameExist_ExistingUsername() {
        boolean exists = UserDAO.checkUsernameExist("member1");
        assertTrue(exists); //true vì username đã tồn tại
    }

    @Test
    public void testCheckUsernameExist_NonExistingUsername() {
        boolean exists = UserDAO.checkUsernameExist("non-username");
        assertFalse(exists); // Kết quả phải là false vì username không tồn tại
    }
}
