package untils;

import android.content.Context;

/**
 * Created by tianyunchen on 16/4/21.
 */
public class DisPlayUtils {

    public static  int pxToDip(Context context,float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }
    public static int DipToPx(Context context,float DpVaule)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(DpVaule*scale+0.5f);

    }
}
