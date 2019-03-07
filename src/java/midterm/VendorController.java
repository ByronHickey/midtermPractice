package midterm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * The Vendor Controller Class
 *
 * @author <ENTER YOUR NAME HERE>
 */
@Named
@ApplicationScoped
public class VendorController {

    private List<Vendor> vendors = new ArrayList<>();
    private Vendor thisVendor = new Vendor();

    public VendorController() {
        getVendorsFromDB();

    }

    private void getVendorsFromDB() {
        try {
            Connection conn = DBUtils.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Vendors");
            vendors.clear();
            while (rs.next()) {
                Vendor v = new Vendor();
                v.setName(rs.getString("Name"));
                v.setVendorId(rs.getInt("VendorId"));
                v.setContactName(rs.getString("ContactName"));
                v.setPhone(rs.getString("PhoneNumber"));
                vendors.add(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Vendor getById(int id) {
        for (Vendor v : vendors) {
            if (v.getVendorId() == id) {
                return v;
            }
        }
        return null;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public Vendor getThisVendor() {
        return thisVendor;
    }

    public String editById(int id) {
        thisVendor = getById(id);
        return "addVendor";
    }

    public String add() {
        try {
            Connection conn = DBUtils.getConnection();
            
            if ( thisVendor.getVendorId() >= vendors.size()) {
                String sql = "INSERT INTO Vendors (VendorId, Name, ContactName, PhoneNumber) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, thisVendor.getVendorId());
                pstmt.setString(2, thisVendor.getName());
                pstmt.setString(3, thisVendor.getContactName());
                pstmt.setString(4, thisVendor.getPhone());
                pstmt.executeUpdate();
            } else {
                String sql = "UPDATE Vendors SET Name = ?, ContactName = ?, PhoneNumber = ? WHERE VendorId = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, thisVendor.getName());
                pstmt.setString(2, thisVendor.getContactName());
                pstmt.setString(3, thisVendor.getPhone());
                pstmt.setInt(4, thisVendor.getVendorId());
                pstmt.executeUpdate();
            }
            vendors.add(thisVendor);
            getVendorsFromDB();
            thisVendor = new Vendor();
            return "index";
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public String removeById(int id) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "DELETE FROM Vendors WHERE VendorId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            getVendorsFromDB();
            thisVendor = new Vendor();
            return "index";
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

}
