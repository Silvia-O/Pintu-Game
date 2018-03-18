package pintu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pintu.entity.TimeOrder;

public class HandleDB {

	private Connection conn = null;
	private PreparedStatement stat = null;  //预处理语句对象
	private ResultSet rs = null;            //结果集语句对象
	
	public ArrayList<TimeOrder> selectInfo() {
		ArrayList<TimeOrder> al = new ArrayList<TimeOrder>();
		String sql = "select top 5 * from timeorder order by [time] asc";
		conn = DBUtil.getConn();
		try {
			stat = conn.prepareStatement(sql);
			rs = stat.executeQuery();
			while(rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				int time = rs.getInt(3);
				
				TimeOrder to = new TimeOrder(id, name, time);
				al.add(to);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return al;
	}
	public void insertInfo(String name, int time) {
		conn = DBUtil.getConn();    //静态方法通过 类名.方法 调用
		
		String sql = "select count(*) from timeorder where [time]<="+time;    //查询语句：统计查询到的行数
		try {
			stat = conn.prepareStatement(sql);
			rs = stat.executeQuery();   //把查询语句发送到sqlserver执行，结果返回到程序端
		    if(rs.next()) {
		    	int n = rs.getInt(1);   //获取整形数据
		    	if(n<=4) {  //当前时间能够排入历史前五
		    		sql = "insert into timeorder values('"+name+"', "+time+")";//把姓名和时间插入到数据库
		    		stat = conn.prepareStatement(sql);
		    		stat.executeUpdate();
		    	}
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(sql);
	}
	public static void main(String[] args) {
		HandleDB hdb = new HandleDB();
		hdb.insertInfo("ww", 998);

		
	}

}
