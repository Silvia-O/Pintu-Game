package pintu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pintu.entity.TimeOrder;

public class HandleDB {

	private Connection conn = null;
	private PreparedStatement stat = null;  //Ԥ����������
	private ResultSet rs = null;            //�����������
	
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
		conn = DBUtil.getConn();    //��̬����ͨ�� ����.���� ����
		
		String sql = "select count(*) from timeorder where [time]<="+time;    //��ѯ��䣺ͳ�Ʋ�ѯ��������
		try {
			stat = conn.prepareStatement(sql);
			rs = stat.executeQuery();   //�Ѳ�ѯ��䷢�͵�sqlserverִ�У�������ص������
		    if(rs.next()) {
		    	int n = rs.getInt(1);   //��ȡ��������
		    	if(n<=4) {  //��ǰʱ���ܹ�������ʷǰ��
		    		sql = "insert into timeorder values('"+name+"', "+time+")";//��������ʱ����뵽���ݿ�
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
