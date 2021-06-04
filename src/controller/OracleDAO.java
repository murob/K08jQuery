package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class OracleDAO {
	Connection con;
	PreparedStatement psmt;
	ResultSet rs;
	
	public OracleDAO() {
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
			if(rs!=null) rs.close();
			if(psmt!=null) psmt.close();
			if(con!=null) con.close();
		}
		catch (Exception e) {
			System.out.println("자원반납시 예외발생");
		}
	}
	
	public boolean isMember(String id, String pass) {
		
		//쿼리문 작성
		String sql = "SELECT COUNT(*) FROM member "
				+ " WHERE id=? AND pass=?";
		
		int isMember = 0;
		boolean isFlag = false;

		try {
			//prepare객체를 통해 쿼리문을 전송한다. 
			//생성자에서 연결정보를 저장한 커넥션 객체를 사용함.
			psmt = con.prepareStatement(sql);
			//쿼리문의 인파라미터 설정(DB의 인덱스는 1부터 시작)
			psmt.setString(1, id);
			psmt.setString(2, pass);
			//쿼리문 실행후 결과는 ResultSet객체를 통해 반환받음
			rs = psmt.executeQuery();
			//실행결과를 가져오기 위해 next()를 호출한다. 
			rs.next();
			//select절의 첫번째 결과값을 얻어오기위해 getInt()사용함.
			isMember = rs.getInt(1);
			System.out.println("affected:"+isMember);
			if(isMember==0) //회원이 아닌경우
				isFlag = false;
			else //회원인 경우(해당 아이디,패스워드가 일치함)
				isFlag = true; 
		}
		catch(Exception e) {
			//예외가 발생한다면 확인이 불가능하므로 무조건 false를 반환한다.
			isFlag = false;
			e.printStackTrace();
		}
		return isFlag;
	}
	
	
}
