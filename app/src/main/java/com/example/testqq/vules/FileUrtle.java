package com.example.testqq.vules;

import com.example.testqq.activity.PrivateMessageActivity;
import com.example.testqq.bean.Pictrue;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * Created by 宋宝春 on 2017/4/11.
 */

public class FileUrtle {
    private static String[] img = new String[]{".jpg", ".png", ".gif", ".bmp"};
    public void  getPicture(final List<Pictrue> list, final File file){
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                //获取文件的名字
                String name = file.getName();
                //判断文件名字中的“.”在名字中出现的最后一次的下标
                int i = name.indexOf('.');
                //如果下标存在
                if (i!=-1){
                    //截取下标以后的字符串 下标算
                    name=name.substring(i);
                    //判断如果下标是".jpg", ".png", ".gif", ".bmp"的文件就获取值并赋给实体类
                    if (name.equalsIgnoreCase(img[0])||
                            name.equalsIgnoreCase(img[1])
                            ||name.equalsIgnoreCase(img[2])
                            ||name.equalsIgnoreCase(img[3])){
                        //实例化实体类
                        Pictrue pic=new Pictrue();
                        //获取文件名字并赋值给实体类
                        pic.setName(file.getName());
                        //获取文件的路径并赋值
                        pic.setPath(file.getPath());
                        //添加到list集合
                        list.add(pic);
                        if (list.size()%50==0){

                        }
                        return true;
                    }
                    //判断是否是文件夹
                }else if(file.isDirectory()){
                    //是文件夹就递归调用继续执行
                    getPicture(list,file);
                }
                return false;
            }
        });
    }
}
