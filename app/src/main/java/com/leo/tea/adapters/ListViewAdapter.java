package com.leo.tea.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.tea.R;
import com.leo.tea.asynctasks.ImageAsyncTask;
import com.leo.tea.beans.HeaderLines;
import com.leo.tea.utils.MyLruCache;
import com.leo.tea.utils.SDUtils;

import java.io.File;
import java.util.List;

import static android.R.attr.bitmap;
import static android.os.Build.VERSION.SDK;

/**
 * Created by my on 2016/11/11.
 */
public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<HeaderLines> headerLines;

    private MyLruCache mCache;

    public ListViewAdapter(Context context, List<HeaderLines> headerLines) {
        this.context=context;
        this.headerLines=headerLines;
        int MaxSize= (int) (Runtime.getRuntime().maxMemory()/8);//内存的8分之1；
        mCache=new MyLruCache(MaxSize);
    }

    @Override
    public int getCount() {
        return headerLines!=null?headerLines.size():0;
    }

    @Override
    public Object getItem(int position) {
        return headerLines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret=null;
        HolderView holderView=null;
        HolderViewTwo holderViewTwo=null;
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case 0:

                if(convertView!=null){
                    ret=convertView;
                    holderViewTwo= (HolderViewTwo) ret.getTag();
                }else{
                    ret=LayoutInflater.from(context).inflate(R.layout.list_itemtwo,parent,false);
                    holderViewTwo=new HolderViewTwo();
                    holderViewTwo.listViewTitleTwo= (TextView) ret.findViewById(R.id.listViewTwoTitle);
                    holderViewTwo.listViewNicknameTwo= (TextView) ret.findViewById(R.id.listViewTwoNickname);
                    holderViewTwo.listViewSourceTwo= (TextView) ret.findViewById(R.id.listViewTwoSource);
                    holderViewTwo.listViewTimeTwo= (TextView) ret.findViewById(R.id.listViewTwoTime);
                    ret.setTag(holderViewTwo);
                }
                holderViewTwo.listViewTitleTwo.setText(headerLines.get(position).getTitle());
                holderViewTwo.listViewSourceTwo.setText(headerLines.get(position).getSource());
                holderViewTwo.listViewNicknameTwo.setText(headerLines.get(position).getNickname());
                holderViewTwo.listViewTimeTwo.setText(headerLines.get(position).getCreate_time());

                break;
            case 1:

                if(convertView!=null){


                    ret=convertView;

                    holderView= (HolderView) ret.getTag();

                }else{

                    ret= LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
                    holderView=new HolderView();
                    holderView.listViewPic= (ImageView) ret.findViewById(R.id.listViewPic);
                    holderView.listViewTitle= (TextView) ret.findViewById(R.id.listViewTitle);
                    holderView.listViewNickname= (TextView) ret.findViewById(R.id.listViewNickname);
                    holderView.listViewSource= (TextView) ret.findViewById(R.id.listViewSource);
                    holderView.listViewTime= (TextView) ret.findViewById(R.id.listViewTime);
                    ret.setTag(holderView);

                }

                holderView.listViewTitle.setText(headerLines.get(position).getTitle());
                holderView.listViewSource.setText(headerLines.get(position).getSource());
                holderView.listViewNickname.setText(headerLines.get(position).getNickname());
                holderView.listViewTime.setText(headerLines.get(position).getCreate_time());

                Bitmap bm1 = Bitmap.createBitmap(80, 80, Bitmap.Config.ALPHA_8);
                holderView.listViewPic.setImageBitmap(bm1);

                final String wap_thumb = headerLines.get(position).getWap_thumb();
                final HolderView holder=holderView;
                if(wap_thumb==null){

                    holderView.listViewPic.setVisibility(View.INVISIBLE);
                }else {


                    Log.d("flag", "----------->得到的数据为getView:wap_thumb图片地址" +wap_thumb);
                    Bitmap bitmap=getCache(wap_thumb);

                    if (bitmap != null) {

                        holder.listViewPic.setImageBitmap(bitmap);


                    }else{
                        holder.listViewPic.setTag(wap_thumb);
                        getNetBitmap(wap_thumb,holder.listViewPic);
                    }

                }

                break;

        }


        return ret;
    }

    private Bitmap getCache(String wap_thumb) {

            String file=wap_thumb.replaceAll("/","").replaceAll(":","");
            Bitmap bitmap=mCache.get(file);
        if (bitmap != null) {
            Log.d("flag", "----------->得到的数据为getCache:内存");
            return bitmap;


        }else{

            String root=context.getExternalCacheDir().getAbsolutePath();
            Log.d("flag", "----------->得到的数据为getCache:ListAdapter" +root);
            Log.d("flag", "----------->得到的数据为getCache:ListAdapter" +file);
            byte[] bt=SDUtils.getByte(root+ File.separator+file);
            if (bt!=null){
                Log.d("flag", "----------->得到的数据为getCache:SD卡");
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(bt, 0, bt.length);
                mCache.put(file,bitmap1);
                return bitmap1;

            }

        }
        return null;
    }

    private void getNetBitmap(final String wap_thumb, final ImageView listViewPic) {
        new ImageAsyncTask(new ImageAsyncTask.ImageCallback() {
            @Override
            public void callback(byte[] res) {
                if(wap_thumb.equals(listViewPic.getTag())&&res!=null){
                    Log.d("flag", "----------->得到的数据为callback: 联网" );
                    Bitmap bitmap = BitmapFactory.decodeByteArray(res, 0, res.length);
                    listViewPic.setImageBitmap(bitmap);

                    //存到内存；
                    mCache.put(wap_thumb.replaceAll("/","").replaceAll(":",""),bitmap);
                    //存入SD卡
                    String root=context.getExternalCacheDir().getAbsolutePath();
                    String fileName=wap_thumb.replaceAll("/","").replaceAll(":","");
                    Log.d("flag", "----------->得到的数据为getCache:ListAdapter" +root);
                    Log.d("flag", "----------->得到的数据为getCache:ListAdapter" +fileName);
                    SDUtils.saveFile(root,fileName,res);
                }

            }
        }).execute(wap_thumb);
    }


    public static class HolderView{

        ImageView listViewPic;
        TextView listViewTitle,listViewSource,listViewNickname,listViewTime;

    }
    public static class HolderViewTwo{

        TextView listViewTitleTwo,listViewSourceTwo,listViewNicknameTwo,listViewTimeTwo;

    }


    @Override
    public int getViewTypeCount() {


        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        String wap_thumb = headerLines.get(position).getWap_thumb();
        if(wap_thumb==null){

            return 0;

        }else{
            return 1;
        }
    }
}
