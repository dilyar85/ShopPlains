package bean;

/**
 * Created by tianyunchen on 16/4/18.
 */
public class ProductionDetailBean {
    private String storename;
    private String productname;
    private String productoldprice;
    private String productionnewprice;
    private String productionimageaddress;

    public ProductionDetailBean(String storename, String productname, String productoldprice, String productionnewprice, String productionimageaddress) {
        this.storename = storename;
        this.productname = productname;
        this.productoldprice = productoldprice;
        this.productionnewprice = productionnewprice;
        this.productionimageaddress = productionimageaddress;

    }

    public String getStorename() {
        return storename;
    }

    public String getProductname() {
        return productname;
    }

    public String getProductoldprice() {
        return productoldprice;
    }

    public String getProductionnewprice() {
        return productionnewprice;
    }

    public String getProductionimageaddress() {
        return productionimageaddress;
    }
}
