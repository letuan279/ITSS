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
//    public static int getGroupLeader(int groupId) {
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        int leaderId = -1;
//        try {
//            Connection conn = Database.getConnection();
//            stmt = conn.prepareStatement("SELECT m.idUser FROM member m WHERE m.idGroup = ? AND m.isLeader = 1;");
//            stmt.setInt(1, groupId);
//            rs = stmt.executeQuery();
//            if (rs.next()) {
//                leaderId = rs.getInt("idUser");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return leaderId;
//    }

//    public static getGroupMembers(int groupId) {
//
//    }

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

//    public static void main(String[] args) {
//        int idUser = 1; // Thay đổi idUser để kiểm tra với các giá trị khác nhau
//        List<Group> groups = getListGroups(idUser);
//        for (Group group : groups) {
//            System.out.println(group.getName());
//        }
//    }
}
