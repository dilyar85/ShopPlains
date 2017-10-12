package bean;

/**
 * Created by tianyunchen on 16/4/13.
 */
public class StorePreViewBean {
    private String storename;
    private String storedes;
    private String currentprice;
    private String oldprice;
    private String imageaddress;


    public StorePreViewBean(String storename, String storedes, String currentprice, String oldprice,String imageaddress) {
        this.storename = storename;
        this.storedes = storedes;
        this.currentprice = currentprice;
        this.oldprice = oldprice;
        this.imageaddress = imageaddress;
    }

    public String getImageaddress() {
        return imageaddress;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public void setStoredes(String storedes) {
        this.storedes = storedes;
    }

    public void setCurrentprice(String currentprice) {
        this.currentprice = currentprice;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public String getStorename() {
        return storename;
    }

    public String getStoredes() {
        return storedes;
    }

    public String getCurrentprice() {
        return currentprice;
    }

    public String getOldprice() {
        return oldprice;
    }
}
