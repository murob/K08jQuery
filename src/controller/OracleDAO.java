package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class OracleDAO {
	
	public Connection con;
	public Statement stmt;
	public PreparedStatement psmt;
	public ResultSet rs;
	
	public OracleDAO() {
		try {
			
			Context initCtx = new InitialContext();
			Context ctx = (Context)initCtx.lookup("java:comp/env");
			DataSource source = (DataSource)initCtx.lookup("java:comp/env/dbcp_myoracle");
			con = source.getConnection();
			System.out.println("DB ConnectionPool 연결성공");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB ConnectionPool 연결실패");
		}
	}
	
	public void close() {
		try {
			if(rs!=null) rs.close();
			if(stmt!=null) stmt.close();
			if(psmt!=null) psmt.close();
			if(con!=null) con.close();
		}
		catch(Exception e) {
			System.out.println("자원반납시 예외발생");
			e.printStackTrace();
		}
	}
	
	public boolean isMember(String id, String pass) {
		
		String sql = "SELECT COUNT(*) FROM member WHERE id=? AND pass=?";
		int isMember = 0;
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, pass);
			rs = psmt.executeQuery();//쿼리문 실행 및 결과반환
			rs.next();//결과를 읽기 위해 커서 이동
			isMember = rs.getInt(1);
			System.out.println("affected:"+isMember);
			if(isMember==0) return false;
		} 
		catch (Exception e) {
			System.out.println("게시물 카운트중 예외발생");
			e.printStackTrace();
		}
		return true;
	}
}
