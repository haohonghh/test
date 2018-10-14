package com.test;

import java.sql.SQLException;

public class QueryFx {
	public static void receiveNumber(String finalFwNumber) throws SQLException {
		String fwNumber = mapping2Oppo(finalFwNumber);
		//分割防伪码，分为三部分
		String piciNumber = fwNumber.substring(0, 6);
		String fwQvjian = fwNumber.substring(6, 15);
		String trait = fwNumber.substring(15,20);
		
		//从fwQuery中进行参数查询和判断
		DBUtil.fwQuery(piciNumber, fwQvjian, trait);
	}
	
	/*
	 * mapping2的逆映射
	 */
	public static String mapping2Oppo(String num) {
		StringBuffer sBuffer = new StringBuffer(); // 储存
		String lastNum = num.substring(9, num.length()); // 9~末尾（保留完全无规律数）
		String theLastNum = num.substring(num.length() - 1, num.length()); // 最后一位规则数
		int theLastNumInt = Integer.parseInt(theLastNum); // 规则数的整形

		for (int i = 0; i < 9; i++) {
			String preNum = num.substring(i, i + 1); // 截取第i位数字
			if (theLastNumInt + i > 9) {
				theLastNumInt = -i; // 使映射满10回0
			}
			int result = Integer.parseInt(preNum) - i - theLastNumInt; // 第i位按照i+1的规则进行映射
			if (result < 0) {
				result = 10 + result; // 少于0的映射其实是与10的补数（这里的result是负数哦）
			}
			sBuffer.append(Integer.toString(result)); // 存入缓存区域
		}
		sBuffer.append(lastNum);
		return sBuffer.toString();//返回密2
	}
	
	//测试用main
	public static void main(String[] args) throws Exception {
		receiveNumber("12356698898898823433");
	}
}
