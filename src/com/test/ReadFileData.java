package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

//读取文件取出合适的步长
public class ReadFileData {
    public static long txt2String(long pace){
    	File file = new File("D:\\Java\\ideaworkspace\\test\\src\\com\\test\\primeNumber.txt");//我的txt文本存放目录，根据自己的路径修改即可
        String s = null;  //s是当前行的值
        String h = null;  //h是前一行的值
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            	if(Long.valueOf(s)>=pace) {
            		return Long.valueOf(h);
            	}
            	h=s;
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return Long.valueOf(h);
    }
    
    //测试用main
    public static void main(String[] args) {
		System.out.println(txt2String(100000));
	}
}
