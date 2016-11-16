package com.leo.tea.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.tea.R;
import com.leo.tea.beans.HeaderImage;
import com.leo.tea.uri.Uri;
import com.leo.tea.utils.IsNetConntectedUtils;
import com.leo.tea.utils.JSONUtils;
import com.leo.tea.utils.MyLruCache;
import com.leo.tea.utils.SDUtils;

import java.io.File;
import java.util.List;

import static android.R.attr.value;
import static com.leo.tea.utils.HttpUtils.parseUriContent;

/**
 * Created by my on 2016/11/11.
 */
public class ImageHeaderFragment extends Fragment {

    private MyLruCache mMyLruCache;

    public ImageHeaderFragment() {
        int maxSize= (int) (Runtime.getRuntime().maxMemory()/8);
        mMyLruCache=new MyLruCache(maxSize);
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case 192:

                    Bitmap bitmap= (Bitmap) msg.obj;
                    Bundle bundle=msg.getData();

                    String title = (String) bundle.get("title");
                    Log.d("flag", "----------->得到的数据为头布局:"+title);
                    mImageView.setImageBitmap(bitmap);
                    mTextView.setText(title);

                    break;





            }






        }
    };
    private ImageView mImageView;
    private TextView mTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret=null;

        ret=inflater.inflate(R.layout.headerimage_item,container,false);

        initView(ret);

        initData();



        return ret;
    }

    private void initData() {
        String rootHeader=getContext().getExternalCacheDir().getAbsolutePath();
        String fileNameHeader=Uri.HEADERIMAGE_URL.replaceAll("/","").replaceAll(":","");
        byte[] bt = SDUtils.getByte(rootHeader +File.separator+ fileNameHeader);
        if(bt!=null){

            List<HeaderImage> headerImageLists= JSONUtils.parseData(new String(bt));
            for (int i = 0; i < headerImageLists.size(); i++) {
                String ImagePath=headerImageLists.get(i).getImage();
                Bitmap bitmap = mMyLruCache.get(ImagePath.replaceAll("/", "").replaceAll(":", ""));
                Message msg= Message.obtain();
                msg.what=192;
                Bundle bundle=new Bundle();
                bundle.putString("title",headerImageLists.get(i).getTitle());
                msg.setData(bundle);
                if (bitmap != null) {

                    msg.obj= bitmap;
                    mHandler.sendMessage(msg);

                }else{
                    String root=getContext().getExternalCacheDir().getAbsolutePath();

                    String fileName=ImagePath.replaceAll("/","").replaceAll(":","");

                    byte[] bt1=SDUtils.getByte(root+ File.separator+fileName);
                    if (bt1 != null) {

                        msg.obj= BitmapFactory.decodeByteArray(bt1, 0, bt1.length);
                        mHandler.sendMessage(msg);
                    }
                }
            }

        }else {
            if (IsNetConntectedUtils.isNetConntected(getContext())) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        byte[] res = parseUriContent(Uri.HEADERIMAGE_URL);
                        List<HeaderImage> headerImageList = JSONUtils.parseData(new String(res));

                        String rootHeader = getContext().getExternalCacheDir().getAbsolutePath();
                        String fileNameHeader = Uri.HEADERIMAGE_URL.replaceAll("/", "").replaceAll(":", "");
                        SDUtils.saveFile(rootHeader, fileNameHeader, res);


                        for (int i = 0; i < headerImageList.size(); i++) {

                            String ImagePath = headerImageList.get(i).getImage();

                            byte[] ImageBt = parseUriContent(ImagePath);

                            Bitmap value = BitmapFactory.decodeByteArray(ImageBt, 0, ImageBt.length);
                            mMyLruCache.put(ImagePath.replaceAll("/", "").replaceAll(":", ""), value);
                            String root = getContext().getExternalCacheDir().getAbsolutePath();
                            String fileName = ImagePath.replaceAll("/", "").replaceAll(":", "");
                            SDUtils.saveFile(root, fileName, ImageBt);

                            Message msg = Message.obtain();

                            msg.what = 192;

                            Bundle bundle = new Bundle();

                            bundle.putString("title", headerImageList.get(i).getTitle());

                            msg.obj = value;
                            msg.setData(bundle);


                            mHandler.sendMessage(msg);

                        }


                    }
                }).start();
            }
        }



    }

    private void initView(View ret) {

        mImageView = (ImageView) ret.findViewById(R.id.imageHeader);

        mTextView = (TextView) ret.findViewById(R.id.textHeader);


    }
}
