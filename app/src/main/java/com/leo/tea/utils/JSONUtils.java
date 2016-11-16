package com.leo.tea.utils;

import com.alibaba.fastjson.JSON;
import com.leo.tea.beans.HeaderImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by my on 2016/11/11.
 */
public class JSONUtils {
    public static List<HeaderImage> parseData(String data) {

        try {
            JSONObject jsonObject=new JSONObject(data);

            JSONArray jsonArray = jsonObject.optJSONArray("data");
            List<HeaderImage> headerImageList=JSON.parseArray(jsonArray.toString(),HeaderImage.class);
            return headerImageList;
        } catch (JSONException e) {
            e.printStackTrace();
        }





        return null;
    }

}
