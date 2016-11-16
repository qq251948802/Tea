package com.leo.tea.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.leo.tea.utils.HttpUtils;
/**
 * Created by my on 2016/11/12.
 */
public class ImageAsyncTask extends AsyncTask<String,Void,byte[]>{
    private ImageCallback mCallback;

    public ImageAsyncTask(ImageCallback callback) {
        mCallback = callback;
    }

    @Override
    protected byte[] doInBackground(String... params) {

        byte[] bytes = HttpUtils.parseUriContent(params[0]);
        return bytes;
    }

    @Override
    protected void onPostExecute(byte[] bitmap) {
        super.onPostExecute(bitmap);
        mCallback.callback(bitmap);

    }
    public interface ImageCallback{

        void callback(byte[] bitmap);


    }
}
