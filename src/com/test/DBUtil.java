package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//链接数据库，需要改
public class DBUtil {

    private static final String URL="jdbc:mysql://localhost:3306/zhilian?useSSL=false";
    private static final String USER="root";
    private static final String PASSWORD="root";
    
    private static Connection conn=null;
    
    static {
        try {
            //1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            //2.获得数据库的连接
            conn=DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //将获得的数据库与java的链接返回（返回的类型为Connection）
    public static Connection getConnection(){
        return conn;
    }
    
	//数据库操作，插入批次
	public static void insertPici(int piciNumber,int trait,int pace,int jieju) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String sql = "INSERT INTO main(pici_number,trait,pace,jieju,msg)"
				+ "VALUES (?,?,?,?,?)";
		PreparedStatement pst=conn.prepareStatement(sql);
		pst.setLong(1, piciNumber);
		pst.setLong(2, trait);
		pst.setLong(3, pace);
		pst.setLong(4, jieju);
		pst.setString(5, "");
		boolean rs=pst.execute();
		System.out.println("插入批次操作："+rs);
		
        pst.close();
        conn.close();
	}
	
	//插入区间
	public static void insertQvjian(int piciNumber,long k) throws Exception {
		Connection conn = DBUtil.getConnection();
		String sql = "INSERT INTO switch(on_used,pici_number)"
				+ "VALUES (?,?)";
		PreparedStatement pst=conn.prepareStatement(sql);
		pst.setLong(1, k);
		pst.setInt(2, piciNumber);
		boolean rs=pst.execute();
		System.out.println("开关表插入区值："+rs);
	}
	
	//数据库操作，防伪查询
	public static void fwQuery(String piciNumber, String fwQvjian, String trait) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String sql = "SELECT * FROM main WHERE pici_number = ?";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setLong(1, Long.valueOf(piciNumber));
		ResultSet rs = pst.executeQuery();
		long useTrait = 0;
		long usePace = 0;
		long useJieju = 0;
		while (rs.next()) {
			useTrait = Long.valueOf(rs.getString("trait"));
			usePace = Long.valueOf(rs.getString("pace"));
			useJieju = Long.valueOf(rs.getString("jieju"));
		}

		// 判断特征值是否相同
		if (!(Long.valueOf(trait) == useTrait)) {
			System.out.println("不是防伪码");
			return;
		}

		// 函数判断
		if (Long.valueOf(fwQvjian) % usePace == useJieju) {
			String sql2 = "SELECT on_used FROM switch WHERE pici_number = ?";
			PreparedStatement pst2 = conn.prepareStatement(sql2);
			pst2.setLong(1, Long.valueOf(piciNumber));
			ResultSet rs2 = pst2.executeQuery();
			long k = (Long.valueOf(fwQvjian)-useJieju) / usePace;
			
			//区间是否为整数判断，不为整数则不通过
			if((int)k-k!=0) {
				System.out.println("函数验证不通过，进入独立表查询");
			}else {
				// 区间开闭验证，读取开关表
				int upstair = 0;
				while (rs2.next()) {
					int a1 = rs2.getInt("on_used");
					if (a1 < 0) {
						int a2 = -a1;
						if (upstair <= k) {
							if (k <= a2) {
								System.out.println("成功，查询次数1");
								return;
							}
						}
					}
	
					if (k == Long.valueOf(a1)) {
						System.out.println("成功，查询次数1");
						return;
					}
	
					upstair = (int) (a1);
				}
			}
		}

		//独立表查询判断方法，待完成
		
		
		//独立表中查询次数超过限制，冻结防伪码方法，待完成
		
		
		//所有都不通过，则失败
		System.out.println("不是防伪码");
		return;
	}
	
	//测试用main
	public static void main(String[] args) throws Exception {
		System.out.println(conn);
	//	insertPici(100045,0,0,5);
	//	fwQuery("100045","0","5");
		insertQvjian(123333,99);
	}
	
	/**
	 * @author jiangzh
	 * 开关表读取部分需要改进
	 */
	//未使用方法，读取开关表新方法
	public static Map switchQuery(String piciNumber) throws Exception {
		Connection conn = DBUtil.getConnection();
		String sql2 = "SELECT on_used FROM switch WHERE pici_number = ?";
		PreparedStatement pst2 = conn.prepareStatement(sql2);
		pst2.setLong(1, Long.valueOf(piciNumber));
		ResultSet rs2 = pst2.executeQuery();
		
		//treemap存on_used的值，用来排序
		Map<Integer, Integer> map = new TreeMap();
		//list用来存a，标记某些负数的on_used的值，排序完后取出时加上负号
		List list = new ArrayList();
		//自增数字
		int a = 1;
		while (rs2.next()) {
			int a1 = rs2.getInt("on_used");
			if (a1>=0) {
				map.put(a1, a);
				a++;
			}else {
				map.put(-a1, -a);
				a++;
			}
		}
		return map;
	}
	
	//未使用方法，关闭k值存进数据库开关表，关闭单个值
	public static void insertK() {
		
	}
	//未使用方法，关闭k值存进数据库开关表，关闭一段的值
	public static void insertKs() {
		
	}
	
}
