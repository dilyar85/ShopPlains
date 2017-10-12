package utility;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by Dilyar on 4/8/16.
 */
public class ImageLoader {

    private static final String LOG_TAG = ImageLoader.class.getSimpleName();

    private static ImageLoader mInstance;

    private LruCache<String, Bitmap> mLruCache;
//    private LruCache<String, Bitmap> mLruCacheUrl;

    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 3;
    private Semaphore mThreadPoolSemaphore;

    private Type mType = Type.LIFO;

    public enum Type {
        LIFO, FIFO
    }

    private LinkedList<Runnable> mTaskQueue;

    private Thread mPollingThread;
    private Handler mPollingThreadHandler;
    private Semaphore mPollingThreadHandlerSemaphore = new Semaphore(0);
    private static final int GET_IMAGE_MESSAGE = 0;

    //UI Thread handler
    private Handler mUIhandlerPath;
    private Handler mUIhandlerUrl;



    private ImageLoader(int threadCount, Type type) {

        init(threadCount, type);

    }



    private void init(int threadCount, Type type) {

        initLruCaches();

        //Init the Task Queue and its carrying type
        mTaskQueue = new LinkedList<>();
        mType = type;

        //Init Thread Pool and the corresponding semaphore
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mThreadPoolSemaphore = new Semaphore(threadCount);

        //Backstage polling thread
        mPollingThread = new Thread() {

            public void run() {

                Looper.prepare();
                mPollingThreadHandler = new Handler() {

                    public void handleMessage(Message message) {

                        //Get the task by the type and execute it.
                        mThreadPool.execute(getTask());

                        try {
                            mThreadPoolSemaphore.acquire();
                        } catch (InterruptedException e) {
                            Log.d(LOG_TAG, "Thread run out in the pool. Wait...");
                        }

                    }
                };
                mPollingThreadHandlerSemaphore.release();
                Looper.loop();
            }
        };
        mPollingThread.start();

    }



    private void initLruCaches() {

        //Init LruCache
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemoryPath = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheMemoryPath) {

            @Override
            protected int sizeOf(String key, Bitmap value) {

                return value.getByteCount();
            }
        };

//        int cacheMemoryUrl = maxMemory / 4;
//        mLruCacheUrl = new LruCache<String, Bitmap>(cacheMemoryUrl) {
//            @Override
//            protected int sizeOf(String key, Bitmap value) {
//                return value.getByteCount();
//            }
//        };
    }



    private Runnable getTask() {

        if (mType == Type.FIFO) {
            return mTaskQueue.removeFirst();
        } else if (mType == Type.LIFO) {
            return mTaskQueue.removeLast();
        }
        return null;
    }



    public void loadImageWithPath(final String path, final ImageView imageView) {

        //Prevent mess of images
        imageView.setTag(path);
        //Init mUIhandler
        if (mUIhandlerPath == null) {
            mUIhandlerPath = new Handler(Looper.getMainLooper()) {

                //Get the image and set it to the imageView
                @Override
                public void handleMessage(Message msg) {

                    ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
                    ImageView imageView1 = holder.imageView;
                    Bitmap bm = holder.bitmap;
                    String path = holder.path;

                    //Check if it is the original imageview
                    if (imageView1.getTag().equals(path)) {
                        imageView1.setImageBitmap(bm);
                    }
                }
            };
        }
        //First try to get the image from cache
        Bitmap bm = mLruCache.get(path);
        if (bm != null) {
            //Send the message to mUIhandler with correct bitmap and imageView.
            refreshPathBitmapOnUI(path, bm, imageView);
        }
        //If the image not in the cache, add a task to TaskQueue
        else {
            //A task that will compress and add the image to LruCache
            addTask(
                    new Runnable() {

                        @Override
                        public void run() {
                            //get the displayed width and height of image
                            ImageViewSize imageViewSize = getImageSize(imageView);
                            //Compress the image using above sizes and get the bitmap
                            Bitmap bm = decodeCompressedBitmapFromPath(path, imageViewSize.width, imageViewSize.height);
                            //Add it to the cache
                            addBitmapToLruCache(path, bm);
                            //Send the message to mUIhandler with correct bitmap and imageView.
                            refreshPathBitmapOnUI(path, bm, imageView);

                            //Release a semaphore when finish a task
                            mThreadPoolSemaphore.release();
                        }
                    });
        }
    }



    public void loadImageWithUrl(final String imgUrl, final ImageView imageView) {


        imageView.setTag(imgUrl);

        if (mUIhandlerUrl == null) {
            mUIhandlerUrl = new Handler(Looper.getMainLooper()) {

                @Override
                public void handleMessage(Message msg) {

                    ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
                    ImageView holderImageView = holder.imageView;
                    Bitmap bm = holder.bitmap;
                    String imgUrl = holder.url;
                    if (holderImageView.getTag().equals(imgUrl)) {
                        holderImageView.setImageBitmap(bm);
                    }
                }
            };
        }

        Bitmap bm = mLruCache.get(imgUrl);
        if (bm != null) {
            refreshUrlBitmapOnUI(imgUrl, bm, imageView);
        } else {
            addTask(new Runnable() {

                @Override
                public void run() {

                    ImageViewSize imageViewSize = getImageSize(imageView);
                    Bitmap bm = decodeCompressedBitmapFromUrl(imgUrl, imageViewSize.width, imageViewSize.height);
                    addBitmapToLruCache(imgUrl, bm);
                    refreshUrlBitmapOnUI(imgUrl, bm, imageView);

                    mThreadPoolSemaphore.release();

                }
            });
        }
    }



    private void refreshPathBitmapOnUI(String path, Bitmap bm, ImageView imageView) {

        Message message = Message.obtain();
        ImageBeanHolder holder = new ImageBeanHolder();
        holder.bitmap = bm;
        holder.imageView = imageView;
        holder.path = path;
        message.obj = holder;
        mUIhandlerPath.sendMessage(message);
    }



    private void refreshUrlBitmapOnUI(String url, Bitmap bm, ImageView imageView) {

        Message message = Message.obtain();
        ImageBeanHolder holder = new ImageBeanHolder();
        holder.bitmap = bm;
        holder.imageView = imageView;
        holder.url = url;
        message.obj = holder;
        mUIhandlerUrl.sendMessage(message);
    }



    //A class that holds the corresponding image and its imageview
    private class ImageBeanHolder {
        ImageView imageView;
        Bitmap bitmap;
        String path;
        String url;
    }



    //Add the task to mTaskQueue and notify polling thread
    private synchronized void addTask(Runnable runnable) {

        mTaskQueue.add(runnable);
        if (mPollingThreadHandler == null) {
            try {

                mPollingThreadHandlerSemaphore.acquire();

            } catch (InterruptedException e) {
                Log.d(LOG_TAG, "mPollingThreadHandler is not ready. Wait...");
            }
        }
        mPollingThreadHandler.sendEmptyMessage(GET_IMAGE_MESSAGE);

    }



    private void addBitmapToLruCache(String path, Bitmap bm) {

        if (mLruCache.get(path) == null) {
            if (bm != null) {
                mLruCache.put(path, bm);
            }
        }

    }

//
//    private void addBitmapToLruCacheUrl(String url, Bitmap bm) {
//        if (mLruCache.get(url) == null) {
//            if (bm != null) {
//                mLruCache.put(url, bm);
//            }
//        }
//    }

    private class ImageViewSize {
        int width;
        int height;
    }



    //Get the compressed image using imageView (Required API 16)
    protected ImageViewSize getImageSize(ImageView imageView) {

        ImageViewSize imageViewSize = new ImageViewSize();

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        int width = imageView.getWidth();
        //ImageView has not been placed into the layout
        if (width <= 0) {
            width = lp.width;
        }
        if (width <= 0) {
            width = getImageViewFieldValue(imageView, "mMaxWidth");
        }
        if (width <= 0) {
            width = displayMetrics.widthPixels;
        }

        //Same logic as "width".
        int height = imageView.getHeight();
        if (height <= 0) {
            height = lp.height;
        }
        if (height <= 0) {
            height = getImageViewFieldValue(imageView, "mMaxHeight");
        }
        if (height <= 0) {
            height = displayMetrics.heightPixels;
        }

        imageViewSize.width = width;
        imageViewSize.height = height;

        return imageViewSize;
    }



    //Using reflection to get the imageView filed value
    private static int getImageViewFieldValue(Object object, String filedName) {

        int value = 0;

        try {
            Field field = ImageView.class.getDeclaredField(filedName);
            field.setAccessible(true);

            int filedValue = field.getInt(object);
            if (filedValue > 0 && filedValue < Integer.MAX_VALUE) {
                value = filedValue;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return value;
    }



    //Get and compress bitmap image by specific with and height
    private Bitmap decodeCompressedBitmapFromPath(String path, int width, int height) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        //Avoid allocating memory and get the actual image size
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }



    private Bitmap decodeCompressedBitmapFromUrl(String url, int width, int height) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            URL imgUrl = new URL(url);
//            InputStream inputStream = imgUrl.openStream();
            BitmapFactory.decodeStream(imgUrl.openStream(), null, options);

            options.inSampleSize = calculateInSampleSize(options, width, height);
            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeStream(imgUrl.openStream(), null, options);

            return bm;

        } catch (IOException e) {
            Log.e(LOG_TAG, "Cannot decode bitmap from this url: " + url);
            return null;
        }





    }



    //Calculate the compressed proportion
    private int calculateInSampleSize(BitmapFactory.Options options, int requiredWidth, int requiredHeight) {

        int actualWidth = options.outWidth;
        int actualHeight = options.outHeight;

        int inSampleSize = 1;

        if (actualWidth > requiredWidth || actualHeight > requiredHeight) {
            int widthRatio = Math.round(actualWidth * 1.0f / requiredWidth);
            int heightRation = Math.round(actualHeight * 1.0f / requiredHeight);
            //Try to save memory best
            inSampleSize = Math.max(widthRatio, heightRation);
        }
        return inSampleSize;
    }



    public static ImageLoader getInstance() {

        //Double check null to improve efficiency.
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(DEFAULT_THREAD_COUNT, Type.LIFO);
                }
            }
        }
        return mInstance;

    }



    public static ImageLoader getInstance(int threadCount, Type type) {

        //Double check null to improve efficiency.
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;

    }

}
