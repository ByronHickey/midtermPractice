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
 * The Product Controller Class
 *
 * @author <ENTER YOUR NAME HERE>
 */
@Named
@ApplicationScoped
public class ProductController {

    private List<Product> products = new ArrayList<>();
    private Product thisProduct = new Product();

    /**
     * Basic Constructor for Products - Retrieves from DB
     */
    public ProductController() {
        getProductsFromDB();
   
    }
    
    private void getProductsFromDB(){
         try {
            Connection conn = DBUtils.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Products");
            products.clear();
            while (rs.next()) {
                Product p = new Product();
                p.setName(rs.getString("Name"));
                p.setProductId(rs.getInt("ProductId"));
                p.setVendorId(rs.getInt("VendorId"));
                products.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
}

    /**
     * Retrieve the full list of Products
     *
     * @return the List of Products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Retrieve the Product Model used in Forms
     *
     * @return the Product Model used in Forms
     */
    public Product getThisProduct() {
        return thisProduct;
    }

    /**
     * Set the Product Model used in Forms
     *
     * @param thisProduct the Product Model used in Forms
     */
    public void setThisProduct(Product thisProduct) {
        this.thisProduct = thisProduct;
    }
      public Product getById(int id){
        for(Product p : products){
            if(p.getVendorId() == id){
                return p;
            }
        }
        return null;
    }
    /**
     * 
     * @param id
     * @return 
     */
    public String editById(int id){
        thisProduct = getById(id);
        return "addProduct";
    }

    /**
     * Add a new Product to the Database and List
     */
    public String add() {
        try {
            Connection conn = DBUtils.getConnection();
            if(thisProduct.getProductId() == 0){
            String sql = "INSERT INTO Products (Name, VendorId) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, thisProduct.getName());
            pstmt.setInt(2, thisProduct.getVendorId());
            pstmt.executeUpdate();
            }else{
                    String sql = "UPDATE Products SET Name = ?, VendorId = ? WHERE ProductId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, thisProduct.getName());
            pstmt.setInt(2, thisProduct.getVendorId());
            pstmt.setInt(3, thisProduct.getProductId());
            pstmt.executeUpdate();
            }               
            products.add(thisProduct);
            getProductsFromDB();
            thisProduct = new Product();
            return "index";
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    public String removeById(int id){
               try {
            Connection conn = DBUtils.getConnection();
            String sql = "DELETE FROM Products WHERE ProductId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            getProductsFromDB();
            thisProduct = new Product();
            return "index";
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        
    }
}
