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

    @org.junit.Test
    @Test
    public void testGetAdminFromSignIn_Valid() {
        Admin admin = UserDAO.getAdminFromSignIn("admin1", "password123");
        assertNotNull(admin);
        assertEquals("admin1", admin.getUsername());
    }

    @Test
    public void testGetAdminFromSignIn_Invalid() {
        Admin admin = UserDAO.getAdminFromSignIn("admin1", "wrongPassword");
        assertNull(admin);
    }

    @Test
    public void testGetAdminFromSignIn_NonExistingAdmin() {
        Admin admin = UserDAO.getAdminFromSignIn("nonAdmin", "password123");
        assertNull(admin);
    }

    @Test
    public void testGetMemberFromId_ValidId() {
        Member member = UserDAO.getMemberFromId("member123");
        assertNotNull(member);
        assertEquals("member123", member.getId());
    }

    @Test
    public void testGetMemberFromId_InvalidId() {
        Member member = UserDAO.getMemberFromId("invalidId");
        assertNull(member);
    }

    @Test
    public void testGetMemberFromId_NonExistingMember() {
        Member member = UserDAO.getMemberFromId("nonMember");
        assertNull(member);
    }

    @Test
    public void testGetMemberFromSignIn_Valid() {
        Member member = UserDAO.getMemberFromSignIn("user1", "password123");
        assertNotNull(member);
        assertEquals("user1", member.getUsername());
    }

    @Test
    public void testGetMemberFromSignIn_Invalid() {
        Member member = UserDAO.getMemberFromSignIn("user1", "wrongPassword");
        assertNull(member);
    }

    @Test
    public void testGetMemberFromSignIn_NonExistingAdmin() {
        Admin admin = UserDAO.getAdminFromSignIn("nonMenber", "password123");
        assertNull(admin);
    }

    @Test
    public void testGetAllMembers() {
        ArrayList<Member> members = UserDAO.getAllMember();
        assertNotNull(members);
        assertTrue(members.size() > 0);
    }

    @Test
    public void testAddMember() {
        Member member = new Member(
                "newMember", "newUser", "newPassword",
                "newFirstName", "newLastName",
                LocalDate.of(2024, 6, 10),
                "newEmail", "newPhoneNumber");
        UserDAO.addMember(member);
        Member retrievedMember = UserDAO.getMemberFromId("newMember");
        assertNotNull(retrievedMember);
    }

    @Test
    public void testRemoveMember() {
        UserDAO.removeMember("newMember");
        Member member = UserDAO.getMemberFromId("newMember");
        assertNull(member);
    }

    @Test
    public void testUpdateMember() {
        Member member = new Member("memberToUpdate", "newUser", "newPassword",
                "newFirstName", "newLastName",
                LocalDate.of(2024, 6, 10),
                "newEmail", "newPhoneNumber");
        UserDAO.updateMember(member);
        Member updatedMember = UserDAO.getMemberFromId("memberToUpdate");
        assertNotNull(updatedMember);
        assertEquals("newUser", updatedMember.getUsername());
    }
}
