package server;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import bean.ProductBean;

/**
 * Created by tianyunchen on 16/4/9.
 */
public class LeanCloudServer {

    public static final String LOG_TAG = LeanCloudServer.class.getSimpleName();

    public static final String AV_LIST_USER_AVATAR = "avatar";
    public static final String AV_USER_GENDER_KEY = "gender";

    final static String EXTRA_PRODUCT_OBJECT_ID = "product_object_id_extra";
    final static String EXTRA_PRODUCT_NAME = "product_object_name_extra";
    final static String EXTRA_PRODUCT_PRICE = "product_object_price_extra";

    //Leancloud tables' names
    final static String QUERY_TABLE_NAME = "AdidasShoes";
    final static String COLUMN_THUMBNAIL_URL = "thumbnail_url";
    final static String COLUMN_ITEM_TITLE = "item_title";
    final static String COLUMN_ITEM_PRICE = "price";

    private static LeanCloudServer LEANCLOUDSERVER;

    private AVQuery<AVObject> query;
    private List<AVObject> dataList;
    private CallbackListener mCallbackListener;
    private TableQueryListener mTableQueryListener;



    public void updateUserGender(String extraValues) {

        AVUser user = AVUser.getCurrentUser();
        if (user != null) {
            user.put(AV_USER_GENDER_KEY, extraValues);
            user.saveInBackground();
        }
    }

    public String getUserGenderSelection() {
        AVUser user = AVUser.getCurrentUser();
        if (user != null) {
            return user.getString(AV_USER_GENDER_KEY);
        } else {
            return null;
        }



    }





    public interface CallbackListener {
        void getDataDone(List<AVObject> data);
        void getUserNameAndAvatarDone(String username, String url);

    }

    public interface TableQueryListener {
        void getProductBeansDone(HashSet<ProductBean> beans);

    }


    public void initLeanCloud(Context context) {

        AVOSCloud.initialize(context, "RX1cqn5oekgS9aUYOAU022Sc-MdYXbMMI", "iaUkc8Jl4Tqro5bUra4IfwKn");
        AVOSCloud.useAVCloudUS();

    }



    public void setCallbackListener(CallbackListener callbackListener) {

        this.mCallbackListener = callbackListener;
    }

    public void setTableQueryListener(TableQueryListener tableQueryListener) {

        this.mTableQueryListener = tableQueryListener;

    }



    public void getDataByRequirement(final CallbackListener callbackListener, String columnName, String value) {

        if (query == null) {
            throw new IllegalArgumentException("query is an null object");
        }
        query.whereEqualTo(columnName, value);
        query.findInBackground(new FindCallback<AVObject>() {

            @Override
            public void done(List<AVObject> list, AVException e) {

                if (e == null) {
                    Log.d(LOG_TAG, "Succeed");
                    // dataList = list;
                    callbackListener.getDataDone(list);

                } else {
                    Log.d(LOG_TAG, "Failed");
                }
            }
        });

    }



    public void downloadDataFromTable(String tableName) {


        AVQuery<AVObject> query = new AVQuery<>(tableName);
        query.findInBackground(new FindCallback<AVObject>() {

            @Override
            public void done(List<AVObject> list, AVException e) {
                 HashSet<ProductBean> beans = new HashSet<>();

                for (AVObject avObject : list) {
                    if (avObject != null) {
                        String thumbnailUrl = avObject.getString(COLUMN_THUMBNAIL_URL);
                        String itemName = avObject.getString(COLUMN_ITEM_TITLE);
                        String itemPrice = avObject.getString(COLUMN_ITEM_PRICE);
                        String objectId = avObject.getObjectId();

                        ProductBean productBean = new ProductBean();
                        productBean.setThumbnailUrl(thumbnailUrl);
                        productBean.setPrice(itemPrice);
                        productBean.setName(itemName);
                        productBean.setObjectId(objectId);

                        beans.add(productBean);
                    }
                }
                mTableQueryListener.getProductBeansDone(beans);
            }
        });
    }



    public LeanCloudServer setQuery(String tableName) {

        query = new AVQuery<>(tableName);
        return LEANCLOUDSERVER;
    }



    /**
     * Upload the user avatar to the cloud, and call downloadUserInfoFromCloud() when it's done.
     *
     * @param imgAbsPath the Image path of user avatar.
     */

    public void uploadUserAvatar(String imgAbsPath) {

        Log.e(LOG_TAG, "Start uploading img path to the cloud");

        String imgName = imgAbsPath.substring(imgAbsPath.lastIndexOf("/"));

        AVUser currentUser = AVUser.getCurrentUser();

        if (currentUser != null) {

            try {
                AVFile userAvatarFile = AVFile.withAbsoluteLocalPath(imgName, imgAbsPath);
                currentUser.put(AV_LIST_USER_AVATAR, userAvatarFile);
                currentUser.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(AVException e) {

                        Log.d(LOG_TAG, "finish uploading, start to download the newest avatar from cloud.");
                        downloadUserInfoFromCloud();

                    }
                });

            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to upload user avatar.");
            }
        } else {
            Log.e(LOG_TAG, "Current user is null.");
        }

    }



    /**
     * Download the user info from cloud, and call interface method when its down.
     */
    public void downloadUserInfoFromCloud() {

        AVUser currentUser = AVUser.getCurrentUser();

        if (currentUser != null) {

            //Pass the user avatar byte[]
            AVFile userAvatarFile = currentUser.getAVFile(AV_LIST_USER_AVATAR);
            if (userAvatarFile != null) {
                String avatarUrl = userAvatarFile.getUrl();
                if (avatarUrl != null) {
                    //Pass the user name and avatar url
                    Log.d(LOG_TAG, "Pass the url to listener: " + avatarUrl);
                    mCallbackListener.getUserNameAndAvatarDone(currentUser.getUsername(), avatarUrl);

                } else {
                    Log.e(LOG_TAG, "avatar url is empty in the cloud");
                }

            }
        }
    }



    public static LeanCloudServer getInstance() {

        if (LEANCLOUDSERVER == null) {
            LEANCLOUDSERVER = new LeanCloudServer();
        }
        return LEANCLOUDSERVER;
    }

//            userAvatarFile.getDataInBackground(new GetDataCallback() {
//
//                @Override
//                public void done(byte[] bytes, AVException e) {
//                    mCallbackListener.getAvatarDone(bytes);
//                    // byte[] cache = new byte[1024];
////                    Bitmap newBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
////                    avatarView.setImageBitmap(newBitmap);
//
//                }
//            });

}
