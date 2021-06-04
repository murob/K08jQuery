package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
 

public class ZipcodeDAO   {
	
	Connection con;
	PreparedStatement psmt;
	ResultSet rs;
	
	//기본생성자에서 DBCP(커넥션풀)을 통해 DB연결
	public ZipcodeDAO() {
		try {
			Context ctx = new InitialContext();						 
			DataSource source = (DataSource)ctx.lookup("java:comp/env/dbcp_myoracle");
			con = source.getConnection();
			System.out.println("DBCP연결성공");
		}
		catch(Exception e) {
			System.out.println("DBCP연결실패");
			e.printStackTrace();
		}		
	}
	
	public void close() {
		try {
			//연결을 해제하는것이 아니고 풀에 다시 반납한다. 
			if(rs!=null) rs.close();
			if(psmt!=null) psmt.close();
			if(con!=null) con.close();
		}
		catch (Exception e) {
			System.out.println("자원반납시 예외발생");
		}
	}
	//우편번호 테이블에서 시/도를 추출
	public ArrayList<String> getSido()
	{
		ArrayList<String> sidoAddr = new ArrayList<String>();
		
		//시/도의 중복을 제거한 상태로 레코드를 가져옴.
		String sql = "SELECT distinct sido FROM zipcode "
				+ " WHERE 1=1"				
				+ " ORDER BY sido ASC";
		try {
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();
			while(rs.next()) {
				//가져온 레코드는 List컬렉션에 저장함. 
				sidoAddr.add(rs.getString(1));
			}
		}
		catch(Exception e) {}
		
		return sidoAddr;
	}
	//시/도를 인수로 받아 해당 구/군을 추출
	public ArrayList<String> getGugun(String sido){
		
		ArrayList<String> gugunAddr = 
				new ArrayList<String>();
		
		String sql = "SELECT DISTINCT gugun FROM zipcode "
				+ " WHERE sido=?"				
				+ " ORDER BY gugun DESC";
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, sido);			
			rs = psmt.executeQuery();
			while(rs.next()) {
				gugunAddr.add(rs.getString(1));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return gugunAddr;		
	}	
}











