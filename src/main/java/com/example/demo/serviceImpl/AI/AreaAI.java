package com.example.demo.serviceImpl.AI;



import net.sf.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 这个类用来自动化标注图片中的重点部分
 */
public class AreaAI {

    public JSONObject getAI(String url){
        JSONObject jsonObject=new JSONObject();
        //url换成服务器的图片地址
        String path=url.replace("http://localhost:9900/","c:/images/").trim();
        File picture=new File(path.trim());
        BufferedImage sourceImg= null;
        try {
            sourceImg = ImageIO.read(new FileInputStream(picture));

            byte[] imgData = FileUtil.readFileByBytes(path.trim());
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam + "&with_face=" + 1;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.abbebbeb72eb0abc01e0c6e80aff421e.2592000.1530023911.282335-11306594";

            String link = "https://aip.baidubce.com/rest/2.0/image-classify/v1/object_detect";
            String result = HttpUtil.post(link, accessToken, param);
//            System.out.println(result);
//            System.out.println(JSONObject.fromObject(result).get("result"));
            jsonObject.put("result",JSONObject.fromObject(result).get("result"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double width=sourceImg.getWidth();

        double height=sourceImg.getHeight();

        jsonObject.put("originWidth",width);
        jsonObject.put("originHeight",height);
        System.out.println(jsonObject);
        return jsonObject;
    }


    public String DishUtil(String url){
        String link = "https://aip.baidubce.com/rest/2.0/image-classify/v2/dish";
       return Util(url,link);

    }

    public String CarUtil(String url){
        String link = "https://aip.baidubce.com/rest/2.0/image-classify/v1/car";
       return Util(url,link);
    }

    public String AnimalUtil(String url){
        String link="https://aip.baidubce.com/rest/2.0/image-classify/v1/animal";
        return Util(url,link);
    }
    public String PlantUtil(String url){
        String link="https://aip.baidubce.com/rest/2.0/image-classify/v1/plant";
        return Util(url,link);
    }

    private String Util(String url,String link){
        JSONObject jsonObject=new JSONObject();
        try {
            BufferedImage sourceImg= null;
            // 本地文件路径
            String filePath = url.replace("http://localhost:9900/","c:/images/");

            File picture=new File(filePath.trim());
            sourceImg = ImageIO.read(new FileInputStream(picture));
            double width=sourceImg.getWidth();
            double height=sourceImg.getHeight();


            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam + "&top_num=" + 5;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.abbebbeb72eb0abc01e0c6e80aff421e.2592000.1530023911.282335-11306594";

            String result = HttpUtil.post(link, accessToken, param);
            System.out.println(result);
            jsonObject.put("result",JSONObject.fromObject(result).get("result"));
            jsonObject.put("originalwidth",width);
            jsonObject.put("originalheight",height);

            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String args[]){
        new AreaAI().PlantUtil("C:\\Users\\hp\\Pictures\\Saved Pictures\\flower.jpg");
    }
}
