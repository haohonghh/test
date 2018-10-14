package com.test;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;

public class GetFx {
	static long k = 1000000000L; //9位的防伪区间的长度 
	static HashSet<String> hs = new HashSet<>(); // 定义一个set用来存储防伪码
	static ArrayList<String> list = new ArrayList<>(); // 定义一个array用来存储防伪码，这两个集合是用来判断生成出来的防伪码个数对不对， 是否有重复的。
	
	private static long n1;    			 //要取得防伪码个数
	private static int piciNumber1;      //批次号
	private static int trait1;        	 //特征值
	private static int pace1;			 //步长
	private static int jieju1;			 //截距，k=0时的第一个防伪码，必须小于步长
	private static String msg1;			 //其它信息，如商品，出厂日期等，自行定义
	
	//通过合适的数量取出质数步长（为了充分利用各个位，用100000000除以防伪码个数n1，得到可以取得最大步长，然后取小于的，最接近最大步长的质数，用来当步长）
	public static long findPrimeNumber(long x) {
		double shang = k/x;
		long pace = (long) Math.floor(shang);
		long primeNumberPace = ReadFileData.txt2String(pace);
		return primeNumberPace;
	}
	
	//取出防伪码
	public static void get(long n,int piciNumber,int trait,int pace,int jieju) throws Exception {
		if (n <= 0) {
			System.out.println("请输入正整数"); // 数量不能为0，负数
			return;
		}
		
		DecimalFormat df = new DecimalFormat("000000000"); // 前缀补0函数
		
		//迭代出码
			for(long i = 0;i < n;i++) {
				long y = jieju + pace*i ;
				String fwNumber = piciNumber1+df.format(y)+trait1;
				String finalFwNumber = mapping2(fwNumber);    //用mapping2进行映射打乱
				System.out.println(fwNumber+" , "+finalFwNumber);   //输出初始防伪码和最终防伪码

			}
		//将参数输入到数据库里面
	//	DBUtil.insertPici(piciNumber, trait, pace, jieju);
	//	DBUtil.insertQvjian(piciNumber,1);
	//	DBUtil.insertQvjian(piciNumber,-n);
		
		return;
	}
	
	//设定各项参数
	public static void setValue(long n,int piciNumber,int trait,int jieju,String msg) {
		n1 = n;
		piciNumber1 = piciNumber;
		trait1 = trait;						//特征值为1到5位，若想缩短防伪码，就缩短特征值位数
		pace1 = (int) findPrimeNumber(n);   //步长为自行生成，也可自己从质数表中选取
		jieju1 = jieju;
		msg1 = msg;
	}
	
	/*
	 * mapping2： 根据最后一位的数字作映射 eg：最后一位3 则第一位3映射，第二位4映射，第5位... 就是第一位使用---第（最后一位）个规则
	 * 之后位数使用---第（前一位+1）个规则 无0规则10规则变成0规则
	 */
	public static String mapping2(String num) { // 获取16位数
		StringBuffer sBuffer = new StringBuffer(); // 储存
		String lastNum = num.substring(9, num.length()); // 9~末尾（保留完全无规律数）
		String theLastNum = num.substring(num.length() - 1, num.length()); // 最后一位规则数
		int theLastNumInt = Integer.parseInt(theLastNum); // 规则数的整形

		for (int i = 0; i < 9; i++) {
			String preNum = num.substring(i, i + 1); // 截取第i位数字

			if (theLastNumInt + i > 9) {
				theLastNumInt = -i; // 使映射满10回0
			}
			int result = Integer.parseInt(preNum) + i + theLastNumInt; // 第i位按照i+1的规则进行映射
			if (result > 9) {
				result = result % 10; // 满10的映射值其实是余数
			}
			sBuffer.append(Integer.toString(result)); // 存入缓存区域
		}
		sBuffer.append(lastNum);
		return sBuffer.toString();
	}
	
	//测试用main
	public static void main(String[] args) throws Exception {
		setValue(100,100045,0,5,"");
		
	//	DBUtil.insertPici(piciNumber1, trait1, pace1, jieju1);
		
		get(n1,piciNumber1,trait1,pace1,jieju1);
	}
	
	
}
