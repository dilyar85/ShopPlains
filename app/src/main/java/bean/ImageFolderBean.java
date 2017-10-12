package bean;
/**
 * Created by Dilyar on 4/9/16.
 */
public class ImageFolderBean {
    private String dirPath;
    private String firstImagePath;
    private String dirName;
    private int count;



    public int getCount() {

        return count;
    }



    public void setCount(int count) {

        this.count = count;
    }



    public String getFirstImagePath() {

        return firstImagePath;
    }



    public void setFirstImagePath(String firstImagePath) {

        this.firstImagePath = firstImagePath;
    }



    public String getDirName() {

        return dirName;
    }




    public String getDirPath() {

        return dirPath;
    }



    public void setDirPath(String dirPath) {

        this.dirPath = dirPath;

        int lastIndex = dirPath.lastIndexOf("/");
        this.dirName = dirPath.substring(lastIndex);
    }
}
