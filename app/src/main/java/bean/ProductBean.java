package bean;
/**
 * Created by Dilyar on 5/22/16.
 */
public class ProductBean {

    private String thumbnailUrl;
    private String name;
    private String size;
    private String color;
    private String price;
    private String objectId;



    public void setObjectId(String objectId) {

        this.objectId = objectId;
    }



    public String getObjectId() {

        return objectId;
    }



    public String getPrice() {

        return price;
    }



    public void setPrice(String price) {

        this.price = price;
    }



    public String getThumbnailUrl() {

        return thumbnailUrl;
    }



    public void setThumbnailUrl(String thumbnailUrl) {

        this.thumbnailUrl = thumbnailUrl;
    }



    public String getName() {

        return name;
    }



    public void setName(String name) {

        this.name = name;
    }



    public String getSize() {

        return size;
    }



    public void setSize(String size) {

        this.size = size;
    }



    public String getColor() {

        return color;
    }



    public void setColor(String color) {

        this.color = color;
    }
}
