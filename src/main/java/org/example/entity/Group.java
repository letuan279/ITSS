package org.example.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Group extends BaseEntity{
    private String name;

    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Group() {}
    public Group(int id, String name, String desc) {
        super(id);
        this.name = name;
        this.desc = desc;
    }

    public static List<Group> getListGroups(int idUser) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Group> groups = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM `group` WHERE id IN (SELECT idGroup FROM `member` WHERE idUser = ?)");
            stmt.setInt(1, idUser);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String desc = rs.getString("desc");
                groups.add(new Group(id, name, desc));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public static void updateGroup(int id, String name, String desc) {
        PreparedStatement stmt = null;
        try {
            Connection conn = Database.getConnection();
            stmt = conn.prepareStatement("UPDATE `group` SET name = ?, `desc` = ? WHERE id = ?");
            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getMembers(int idCreateUser) {
        PreparedStatement stmt = null;
        List<User> members = new ArrayList<>();
        ResultSet rs = null;
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT id, username FROM user WHERE id <> ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCreateUser);
            rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                members.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public static Group addGroup(String name, String desc, List<Integer> userIds, int idLeader) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Group group = null;
        try {
            Connection conn = Database.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO `group` (name, `desc`) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.executeUpdate();

            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            int idGroup = 0;
            if (rs.next()) {
                idGroup = rs.getInt(1);
            }
            sql = "INSERT INTO member (idUser, idGroup, isLeader) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            for (int idUser : userIds) {
                stmt.setInt(1, idUser);
                stmt.setInt(2, idGroup);
                stmt.setInt(3, idUser == idLeader ? 1 : 0);
                stmt.executeUpdate();
            }
            conn.commit();
            conn.setAutoCommit(true);
            group = new Group(idGroup, name, desc);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    public static List<User> getMemberInGroup(int idGroup) {
        List<User> members = new ArrayList<>();
        PreparedStatement stmt = null;
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT u.id, u.username FROM member m INNER JOIN user u ON m.idUser = u.id WHERE m.idGroup = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idGroup);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                User user = new User(id, username);
                members.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public static boolean isLeader(int idUser, int idGroup) {
        boolean result = false;
        PreparedStatement stmt = null;
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT isLeader FROM member WHERE idUser = ? AND idGroup = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUser);
            stmt.setInt(2, idGroup);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int isLeader = rs.getInt("isLeader");
                result = (isLeader == 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void deleteMember(int idMember, int idGroup) {
        PreparedStatement stmt = null;
        try {
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM member WHERE idUser = ? AND idGroup = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idMember);
            stmt.setInt(2, idGroup);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getMemberAddToGroup(int idGroup) {
        PreparedStatement stmt = null;
        List<User> users = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT user.id, user.username FROM user LEFT JOIN member ON user.id = member.idUser AND member.idGroup = ? WHERE member.idUser IS NULL";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idGroup);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                User user = new User(id, username);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void addMembers(int idGroup, List<Integer> idMembers) {
        PreparedStatement stmt = null;
        try {
            Connection conn = Database.getConnection();
            String sql = "INSERT INTO member (idGroup, idUser, isLeader) VALUES (?, ?, 0)";
            stmt = conn.prepareStatement(sql);
            for (int idMember : idMembers) {
                stmt.setInt(1, idGroup);
                stmt.setInt(2, idMember);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        List<User> members = Group.getMemberAddToGroup(1);
//        for(User member : members){
//            System.out.println(member.getUsername());
//        }
//    }
}
