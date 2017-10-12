package application;
import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import server.LeanCloudServer;

public class myApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        LeanCloudServer.getInstance().initLeanCloud(this);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }
}