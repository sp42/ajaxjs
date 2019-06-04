package com.ajaxjs.javatools.task.task2;

import java.io.File;  
import java.util.ArrayList;  
import java.util.List;  

//import com.ajaxjs.util.XML;
  
public class XmlReader {  
    public static void main(String[] args) {  
        XmlReader.getTasks();  
    }  
  
    public static List<TaskModel> getTasks() {  
        List<TaskModel> tasks = new ArrayList<>();  
        System.out.println("load task config start...");  
         
        File file = new File("/work/TaskManager/conf/taskconfig.xml");  
  
        if (file.exists() && !file.isDirectory()) {
        	// TODO替换为 XML 
//        	XML xml = new XML();
//            SAXBuilder sx = new SAXBuilder();  
//            Document doc = sx.build(file);  
//            Element rootelement = doc.getRootElement();  
//              
//            List<Element> childs = rootelement.getChildren();  
//            for (int i = 0; i < childs.size(); i++) {  
//                TaskModel tModel = new TaskModel();  
//                tModel.setClassName(childs.get(i).getChildText("class"));  
//                System.out.println(childs.get(i).getChildText("class"));  
//                tModel.setMethodName(childs.get(i).getChildText("method"));  
//                System.out.println(childs.get(i).getChildText("method"));  
//                String initialDelay = childs.get(i).getChildText("initialDelay");  
//                  
//                tModel.setInitialDelay((Long.valueOf(initialDelay)));  
//                System.out.println("距离首次运行还差" + initialDelay + "秒！");  
//                tModel.setPeriod(Integer.valueOf(childs.get(i).getChildText("period")));  
//                System.out.println(childs.get(i).getChildText("period"));  
//                tasks.add(tModel);  
//            }  
        } else {  
            System.out.println("file no exist!");  
        }  
        System.out.println("load task config end !");  
        return tasks;  
    }  
}  
