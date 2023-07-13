package org.example.entity;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MarketItem extends BaseEntity{
    private int quantity;
    private String unit;
    private String name;
    private int type;
    private int idGroup;
    private LocalDate dayToBuy;
    private User buyer;
    private int expirationDate;
    private int state;
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    public MarketItem(int id, int quantity, String unit, String name, int type, int idGroup, LocalDate dayToBuy, User buyer, int expirationDate, int state) {
        super(id);
        this.quantity = quantity;
        this.unit = unit;
        this.name = name;
        this.type = type;
        this.idGroup = idGroup;
        this.dayToBuy = dayToBuy;
        this.buyer = buyer;
        this.expirationDate = expirationDate;
        this.state = state;
    }

    public MarketItem(int id, int quantity, String unit, String name, int type, int idGroup, LocalDate dayToBuy, int state) {
        super(id);
        this.quantity = quantity;
        this.unit = unit;
        this.name = name;
        this.type = type;
        this.idGroup = idGroup;
        this.dayToBuy = dayToBuy;
        this.state = state;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public LocalDate getDayToBuy() {
        return dayToBuy;
    }

    public void setDayToBuy(LocalDate dayToBuy) {
        this.dayToBuy = dayToBuy;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public int getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(int expirationDate) {
        this.expirationDate = expirationDate;
    }

    public static List<MarketItem> getAllInGroup(int idGroup) {
        List<MarketItem> items = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT * FROM marketitem WHERE idGroup = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idGroup);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int quantity = rs.getInt("quantity");
                String unit = rs.getString("unit");
                String name = rs.getString("name");
                int type = rs.getInt("type");
                int _idGroup = rs.getInt("idGroup");
                LocalDate dayToBuy = rs.getDate("dayToBuy").toLocalDate();
                Integer idUser = rs.getInt("idUser");
                int expirationDate = rs.getInt("expirationTime");
                int state = rs.getInt("state");
                User buyer = null;
                if (idUser != null) {
                    buyer = User.getById(idUser);
                }
                MarketItem item = new MarketItem(id, quantity, unit, name, type, _idGroup, dayToBuy, buyer, expirationDate, state);
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    public static void buyAMarketItem(int idUser, int idMarketItem) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);
            String sql = "SELECT idUser FROM marketitem WHERE id = ? FOR UPDATE";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idMarketItem);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int currentUserId = rs.getInt("idUser");

                if (currentUserId == 0) {
                    sql = "UPDATE marketitem SET idUser = ? WHERE id = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, idUser);
                    stmt.setInt(2, idMarketItem);
                    stmt.executeUpdate();
                    conn.commit(); // Kết thúc transaction
                } else {
                    conn.rollback();
                    System.out.println("Sản phẩm đã được mua bởi một người dùng khác!");
                }
            } else {
                conn.rollback();
                System.out.println("Không tìm thấy sản phẩm!");
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static MarketItem add(int idGroup, String name, int quantity, int type, String unit, LocalDate date) {
        MarketItem marketItem = null;
        try {
            Connection conn = Database.getConnection();
            String sql = "INSERT INTO marketitem (state, name, quantity, type, unit, dayToBuy, idGroup) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 0);
            pstmt.setString(2, name);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, type);
            pstmt.setString(5, unit);
            pstmt.setDate(6, java.sql.Date.valueOf(date));
            pstmt.setInt(7, idGroup);
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM marketitem");
            if (rs.next()) {
                int id = rs.getInt(1);
                marketItem = new MarketItem(id, quantity, unit, name, type, idGroup, date, 0);
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marketItem;
    }

    public static void main(String[] args) {
        MarketItem marketItem = MarketItem.add(1, "a", 1, 0, "b", LocalDate.now());
        System.out.println(marketItem.getId());
    }
}
