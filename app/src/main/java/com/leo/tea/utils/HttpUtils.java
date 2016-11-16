package com.leo.tea.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by my on 2016/11/11.
 */
public class HttpUtils {
    public static byte[] parseUriContent(String path) {

        InputStream is=null;

        ByteArrayOutputStream boas=null;
        try {
            URL url=new URL(path);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5000);

            if(conn.getResponseCode()==200){

                is=conn.getInputStream();

                boas=new ByteArrayOutputStream();


                byte[] bt=new byte[1024*8];
                int len=0;

                while((len=is.read(bt))!=-1){



                    boas.write(bt,0,len);




                }

                return boas.toByteArray();





            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            if (boas != null) {
                try {
                    boas.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


        return null;
    }
}
