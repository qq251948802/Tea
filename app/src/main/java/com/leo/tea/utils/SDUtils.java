package com.leo.tea.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Created by my on 2016/11/15.
 */
public class SDUtils {



    public static void saveFile(String root, String fileName, byte[] res) {


        File file=new File(root,fileName);
        FileOutputStream fos=null;
        try {
            fos = new FileOutputStream(file);
            fos.write(res,0,res.length);





        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static byte[] getByte(String filePath) {
        FileInputStream fis=null;
        ByteArrayOutputStream baos=null;
        try {
            fis=new FileInputStream(new File(filePath));
            baos=new ByteArrayOutputStream();
            byte[] bt=new byte[1024*8];
            int len=0;
            while ((len=fis.read(bt))!=-1){
                baos.write(bt,0,bt.length);
            }
            return baos.toByteArray();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;
    }
}
