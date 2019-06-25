package com.example.demo.controller;

import com.example.demo.service.TagService.TagAreaService;
import com.example.demo.service.TagService.TagCircleService;
import com.example.demo.service.TagService.TagOverallService;
import com.example.demo.service.TagService.TagQueryService;
import com.example.demo.service.TaskService.TaskOperationService;
import com.example.demo.service.TaskService.TaskQueryService;
import com.example.demo.serviceImpl.AI.AreaAI;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/webwork")
public class WebWorkController {

    @Autowired
    private TagOverallService tagoverallservice;

    @Autowired
    private TagAreaService tagAreaService;

    @Autowired
    private TagCircleService tagCircleService;

    @Autowired
    private TaskOperationService taskOperationService;

    @Autowired
    private TaskQueryService taskQueryService;

    @Autowired
    private TagQueryService tagQueryService;


    @GetMapping("/getAreaAI")
    public String getAreaAI(HttpServletRequest request){
        String pictureurl=request.getParameter("pictureurl");
        AreaAI areaAI=new AreaAI();
        return areaAI.getAI(pictureurl).toString();
    }

    /**
     * @return : res
     * @Author : GSY
     * @Date : 23:56 2018/5/24
     * @Desciption : 一句话描述
     */
    @GetMapping("/getdivision/{AreaTagId}")
    public String getDivision(@PathVariable("AreaTagId")String AreaTagId) {
        String result="";
        int areatagid=Integer.parseInt(AreaTagId);
        JsonObject j1=tagQueryService.getAreaTag(areatagid);
        String division_name=j1.get("label").toString();
        division_name=division_name.replaceAll("\"","");
        String division_description=j1.get("description").toString();
        division_description=division_description.replaceAll("\"","");
        String beginX=j1.get("beginX").toString();
        beginX=beginX.replaceAll("\"","");
        String beginY=j1.get("beginY").toString();
        beginY=beginY.replaceAll("\"","");
        String endX=j1.get("endX").toString();
        endX=endX.replaceAll("\"","");
        String endY=j1.get("endY").toString();
        endY=endY.replaceAll("\"","");
        String DrawColor=j1.get("drawColor").toString();
        DrawColor=DrawColor.replaceAll("\"","");
        String DrawSize=j1.get("drawSize").toString();
        DrawSize=DrawSize.replaceAll("\"","");
        String fontSize=j1.get("fontSize").toString();
        fontSize=fontSize.replaceAll("\"","");
        String fontFamily=j1.get("fontFamily").toString();
        fontFamily=fontFamily.replaceAll("\"","");
        result=division_name+";"+division_description+";"+beginX+";"+beginY+";"+endX+";"+endY+";"
                +DrawColor+";"+DrawSize+";"+fontSize+";"+fontFamily;
        return result;
    }


    /**
     * @param userID
     * @return : res
     * @Author : GSY
     * @Date : 23:57 2018/5/24
     * @Desciption : 一句话描述
     */
    @GetMapping("/getwholeextra/{userID}/{recordID}")
    public String getWholeExtra(@PathVariable("userID") String userID,@PathVariable("recordID") String recordID,
                                HttpServletRequest request) {
        String pictureurl=request.getParameter("pictureurl");
        JsonArray jsonArray = tagQueryService.getTag(userID, recordID, pictureurl.trim());
        //先将whole拿出来
        JsonObject j1 = new JsonObject();
        j1 = jsonArray.get(0).getAsJsonObject();
        String Font_Size=j1.get("fontSize").toString();
        String Font_Family=j1.get("fontFamily").toString();
        String result=Font_Size+","+Font_Family;
        return result;
    }

    /**
     * @return : res
     * @Author : GSY
     * @Date : 23:57 2018/5/24
     * @Desciption : 一句话描述
     */
    @GetMapping("/getlabel/{CircleTagId}")
    public String getLabel(@PathVariable("CircleTagId")String CircleTagId) {
        int circletagId=Integer.parseInt(CircleTagId);
        String result="";
        JsonObject j1=tagQueryService.getCircleTag(circletagId);
        String name=j1.get("label").toString();
        name=name.replaceAll("\"","");
        String description=j1.get("description").toString();
        description=description.replaceAll("\"","");
        String x=j1.get("x").toString();
        x=x.replaceAll("\"","");
        String y=j1.get("y").toString();
        y=y.replaceAll("\"","");
        String DrawColor=j1.get("drawColor").toString();
        DrawColor=DrawColor.replaceAll("\"","");
        String DrawSize=j1.get("drawSize").toString();
        DrawSize=DrawSize.replaceAll("\"","");
        String FontFamily=j1.get("fontFamily").toString();
        FontFamily=FontFamily.replaceAll("\"","");
        String FontSize=j1.get("fontSize").toString();
        FontSize=FontSize.replaceAll("\"","");
        result=name+";"+description+";"+x+";"+y+";"+DrawColor+";"+DrawSize+";"+FontFamily+";"+FontSize;
        return result;
    }


    /**
     * @Author : GSY
     * @Date : 23:57 2018/5/24
     */
    @GetMapping("/delete/{flag}/{TagId}")
    public void deleteRecord(@PathVariable("flag")String flag
            ,@PathVariable String TagId) {
        int Id= Integer.parseInt(TagId);
        if (flag.equals("2")) {
            //删除区域划分的一个AreaTag
            tagAreaService.deleteAreaTag(Id);
        } else if (flag.equals("3")) {
            //删除局部标注的一个CircleTag
            tagCircleService.deleteCircleTag(Id);
        }
    }

    /**
     * @param request
     * @Author : GSY
     * @Date : 0:03 2018/5/25
     * @Desciption : 保存全局标注
     */
    @PostMapping("/savewhole")
    public void saveWhole(HttpServletRequest request) {
        String userID = request.getParameter("userID");
        String RecordID = request.getParameter("RecordID");
        String pictureurl = request.getParameter("pictureurl");
        String whole_name = request.getParameter("whole_name");
        String whole_description = request.getParameter("whole_description");
        String Font_Size = request.getParameter("Font_Size");
        String Font_Family = request.getParameter("Font_Family");
        if (Font_Size.equals("\"\"")) {
            Font_Size = "2px";
        }
        if (Font_Family.equals("\"\"")) {
            Font_Family = "仿宋";
        }
        Font_Size.replace("\"", "");
        Font_Family.replace("\"", "");
        //调用增加的
        tagoverallservice.modifyOverall(userID, RecordID, pictureurl.trim(), whole_name, whole_description, Font_Size, Font_Family);
    }

    /**
     * @param request
     * @Author : GSY
     * @Date : 0:03 2018/5/25
     * @Desciption : 保存区域划分
     */
    @PostMapping("/savedivision")
    public int saveDivision(HttpServletRequest request) {
        String isAddable = request.getParameter("isAddable");
        String startxlist=request.getParameter("startxlist");
        String startylist=request.getParameter("startylist");
        String endxlist=request.getParameter("endxlist");
        String endylist=request.getParameter("endylist");
        String Draw_Color = request.getParameter("Draw_Color");
        String Draw_Size = request.getParameter("Draw_Size");
        String Font_Size = request.getParameter("Font_Size");
        String Font_Family = request.getParameter("Font_Family");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String option=request.getParameter("option");
        startxlist = getList(startxlist);
        startylist = getList(startylist);
        endxlist = getList(endxlist);
        endylist = getList(endylist);
        if (Font_Size.equals("\"\"")) {
            Font_Size = "2px";
        }
        if (Font_Family.equals("\"\"")) {
            Font_Family = "仿宋";
        }
        Font_Size.replace("\"", "");
        Font_Family.replace("\"", "");
        Draw_Color.replace("\"", "");
        Draw_Size.replace("\"", "");
        //根据isAddable的值进行保存取出需要的值
        if(isAddable.equals("0")){
            String userID = request.getParameter("userID");
            String RecordID = request.getParameter("RecordID");
            String pictureurl = request.getParameter("pictureurl");
            int result=0;
            result=tagAreaService.insertAreaTag(userID,RecordID,pictureurl.trim(),name,description,startxlist,
                    startylist,endxlist,endylist,Draw_Color,Draw_Size,Font_Size,Font_Family,option);
            return result;
        }else{
            String TagId=request.getParameter("areaTagId");
            int AreaTagId=Integer.parseInt(TagId);
            tagAreaService.modifyAreaTag(AreaTagId,name,description,startxlist,startylist,endxlist
                    ,endylist,Draw_Color,Draw_Size,Font_Size,Font_Family,option);
            return -1;
        }
    }

    /**
     * @param request
     * @Author : GSY
     * @Date : 0:03 2018/5/25
     * @Desciption : 保存局部标注
     */
    @PostMapping("/savelabel")
    public int saveLabel(HttpServletRequest request) {
        String isAddable = request.getParameter("isAddable");
        String option=request.getParameter("option");
        String xlist = request.getParameter("xlist");
        String ylist = request.getParameter("ylist");
        String Draw_Color = request.getParameter("Draw_Color");
        String Draw_Size = request.getParameter("Draw_Size");
        String Font_Size = request.getParameter("Font_Size");
        String Font_Family = request.getParameter("Font_Family");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        xlist = getList(xlist);
        ylist = getList(ylist);
        if (Font_Size.equals("\"\"")) {
            Font_Size = "2px";
        }
        if (Font_Family.equals("\"\"")) {
            Font_Family = "仿宋";
        }
        Font_Size.replace("\"", "");
        Font_Family.replace("\"", "");
        Draw_Color.replace("\"", "");
        Draw_Size.replace("\"", "");
        if(isAddable.equals("0")){
            String userID = request.getParameter("userID");
            String RecordID = request.getParameter("RecordID");
            String pictureurl = request.getParameter("pictureurl");
            int result=0;
            result=tagCircleService.insertCircleTag(userID, RecordID, pictureurl.trim(), name,
                    description, xlist, ylist, Draw_Color, Draw_Size,
                    Font_Size, Font_Family,option);
            return result;
        }else{
            String TagId=request.getParameter("CircleTagId");
            int circleTagId=Integer.parseInt(TagId);
            tagCircleService.modifyCircleTag(circleTagId, name, description, xlist, ylist, Draw_Color, Draw_Size, Font_Size, Font_Family,option);
            return -1;
        }
    }

    /**
     * @param userID
     * @param recordID
     * @Author : GSY
     * @Date : 0:07 2018/5/25
     * @Desciption : 根据userID和recordID提交任务
     */
    @PostMapping("/submit/{userID}/{recordID}")
    public void submitTask(@PathVariable("userID") String userID, @PathVariable("recordID") String recordID) {
        taskOperationService.finishTask(userID, recordID);
    }

    /**
     * @param recordID
     * @return : res
     * @Author : GSY
     * @Date : 0:15 2018/5/25
     * @Desciption : 初始化拿到所有的base64位图片内容
     */
    @GetMapping("/init/{recordID}")
    public String queryALLURL(@PathVariable("recordID") String recordID) {
        //TaskQueryService t=new TaskQueryServiceImpl();
        //得到所有的3个TagSet,TagSet里面包含了图片路径以及每个Tag的具体信息
        System.out.println(recordID);
        return taskQueryService.getAllPictures(recordID).toString();

    }

    //拿到全部的分类
    @GetMapping("/getOptions/{recordID}")
    public String getOptions(@PathVariable("recordID")String recordID){
        String result=tagQueryService.queryOptions(recordID).toString();
        result=result.substring(1,result.length()-1);
        return result;
    }

    //拿全部对应分类的divisionname.divisiondescription,AreaTagId,CircleName,CircleDescription,CircleId
    @GetMapping("/getLabelAndId/{userID}/{recordID}/{option}")
    public String getLabelAndId(@PathVariable("userID")String userID,@PathVariable("recordID") String recordID
            ,@PathVariable("option")String option,HttpServletRequest request){
        String pictureurl =request.getParameter("pictureurl");
        JsonArray j1=tagQueryService.queryTags(userID,recordID,pictureurl.trim(),1,option);
        JsonArray j2=tagQueryService.queryTags(userID,recordID,pictureurl.trim(),2,option);
        String result1="";
        String result2="";
        //解析j1
        String division_name="";
        String division_description="";
        String AreaTagId="";
        if(j1.size()>0){
            for (int i = 0; i < j1.size(); i++) {
                JsonObject Area = j1.get(i).getAsJsonObject();
                String temp=Area.get("label").toString();
                temp=temp.substring(1, temp.length() - 1);
                division_name=division_name+","+temp;
                temp=Area.get("description").toString();
                temp=temp.substring(1,temp.length()-1);
                division_description=division_description+","+temp;
                temp=Area.get("areaTagId").toString();
                AreaTagId=AreaTagId+","+temp;
            }
            division_name=division_name.substring(1,division_name.length());
            division_description=division_description.substring(1,division_description.length());
            AreaTagId=AreaTagId.substring(1,AreaTagId.length());
            result1=division_name+";"+division_description+";"+AreaTagId;
        }else {
            result1=result1+"disA"+";"+"disAd"+";"+"disAT";
        }
        //解析j2
        String label_name="";
        String label_description="";
        String CircleTagId="";
        if(j2.size()>0){
            for(int i=0;i<j2.size();i++){
                JsonObject Circle=j2.get(i).getAsJsonObject();
                String temp=Circle.get("label").toString();
                temp=temp.substring(1,temp.length()-1);
                label_name=label_name+","+temp;
                temp=Circle.get("description").toString();
                temp=temp.substring(1,temp.length()-1);
                label_description=label_description+","+temp;
                temp=Circle.get("circleTagId").toString();
                CircleTagId=CircleTagId+","+temp;
            }
            label_name=label_name.substring(1,label_name.length());
            label_description=label_description.substring(1,label_description.length());
            CircleTagId=CircleTagId.substring(1,CircleTagId.length());
            result2=label_name+";"+label_description+";"+CircleTagId;
        }else{
            result2=result2+"disL"+";"+"disLd"+";"+"disLT";
        }
        String result="";
        result=result1+";"+result2;
        return result;
    }

    //进行解析String的list,得到解析后的["123","1234","1"]的String
    public String getList(String mlist){
        String a[];
        a=mlist.split(",");
        String result="";
        for(int i=0;i<(a.length);i++) {
            String temp = a[i];
            String tresult = "";
            for (int j = 0; j < (temp.length()); j++) {
                if (temp.charAt(j) == '\"') {

                } else {
                    tresult = tresult + temp.charAt(j);
                }
            }
            a[i] = tresult;
        }
        for(int i=0;i<(a.length);i++){
            result=result+","+a[i];
        }
        result=result.substring(1,result.length());
        return result;
    }

}
