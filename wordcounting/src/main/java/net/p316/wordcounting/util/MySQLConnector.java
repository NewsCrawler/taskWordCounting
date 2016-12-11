package net.p316.wordcounting.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

public class MySQLConnector {
	//*** 데이터베이스를 이용하기 위해 쓸 각종 변수를 저장한다 ***//
	//*** 크롤러가 받아온 값을 처리하기 위한 필드를 만든다 ***//
	//*** DB를 이용하는 과정에서 공통으로 있는 클래스 ***//
	
	//** external parameters **//
	//** NONE **//
	
	//* 작성자 : 2015112088 김경민 *//
	//* 작성일자 : - *//
	//* 최종 수정자 : 2015112088 김경민 *//
	//* 최종 수정일 : 2016년 12월 10일 토요일 *//
	
   //string형 변수 DB_URL에 DB연동을 할 주소(URL)저장
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://news.p316.net/news_crawler";

   //String형 변수 USER에 아이디 crawler저장   
   static final String USER = "word";
   //String형 변수 PASS에 비밀번호 저장
   static final String PASS = "s:7Nq*?2AEEQ~h+%";

   //제목과 단어리스트를 맵에 등록하기 위한 변수
   public HashMap<Integer, String> titleMap = new HashMap<Integer, String>();
   public HashMap<Integer, String> wordMap = new HashMap<Integer, String>();
   
   private DriverManager driverManager;
   private Connection conn = null;
   
   public MySQLConnector(){
	   try{
		   conn = driverManager.getConnection(DB_URL
				   + "?characterEncoding=utf8"
				   + "&user=" + USER + "&password=" + PASS);
	   } catch(Exception ex){
		   System.out.println("SQLException: " + ex.getMessage());
	   }
	   
   }
   // 이 부분이 없으면 DB에 연결되어있는 사람이 많아질 때 터진다...
   public void close(){
	   if(conn != null){ try{ conn.close(); }catch(SQLException ex){} }
   }
   
   // DB삽입 테스트용
   public void simpleInsertTitle(String _href, String _title, String _comp, String _day){
	   Statement stmt = null;
	   ResultSet rs = null;
	   try{
		   stmt = conn.createStatement();
		   stmt.execute("INSERT INTO `nc_title`"
		   		+ "(`idx`, `idx_category`, `url`, `title`, `company`, `date`)"
		   		+ "VALUES (NULL,"
		   		+ "'1',"
		   		+ "'" + _href + "',"
		   		+ "'" + _title + "',"
		   		+ "'" + _comp + "',"
		   		+ "'" + _day + "')");
	   } catch(Exception ex){
		   
	   } finally {
			if (rs != null) try { rs.close(); } catch(SQLException ex) {}
	        if (stmt != null) try { stmt.close(); } catch(SQLException ex) {}
		}
   }
   
   public void insertWordToDB(int idx_title, int idx_word, int cnt) {
	   Statement stmt = null;
	   ResultSet rs = null;
	   try{
		   stmt = conn.createStatement();
		   stmt.execute("INSERT INTO `nc_counter_title_word`"
		   		+ "(`idx`, `idx_title`, `idx_word`, `cnt`)"
		   		+ "VALUES (NULL,"
		   		+ "'" + idx_title + "',"
		   		+ "'" + idx_word + "',"
		   		+ "'" + cnt + "');");
		   stmt.close();
	   } catch(Exception ex){
		   System.out.println("문제발생!");
	   } finally {
			if (rs != null) try { rs.close(); } catch(SQLException ex) {}
	        if (stmt != null) try { stmt.close(); } catch(SQLException ex) {}
		}
   }
   
   // 쿼리 실행 테스트용
   public void testConn(){
	   Statement stmt = null;
	   ResultSet rs = null;
	   try{
		   stmt = conn.createStatement();
		   stmt.execute("SELECT idx, `word` FROM `nc_word_table`");
	   } catch(Exception ex){
		   
	   } finally {
		   
	   }
   }
   
   // 제목을 DB에서 불러와 map에 저장(key : index, value : title)
   public void getTitles(){
		//*** 위에서 정의한 DB경로에서 제목을 불러온다 ***//
		//*** 불러온 테이블의 내용은 MAP에 저장된다 ***//
		//*** MAP은 이후 단어 빈도에 따라 정리해 보는 메소드에서 이용될 수 있다 ***//
		
		//** external parameters **//
		//** 1. DB 테이블 **//
		
		//* 작성자 : 2015112088 김경민 *//
		//* 작성일자 : 2016년 11월 12일 토요일 *//
		//* 최종 수정자 : 2015112088 김경민 *//
		//* 최종 수정일 : 2016년 11월 13일 일요일 *//
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   try{
		   Class.forName("com.mysql.jdbc.Driver");
		   
		   conn = DriverManager.getConnection(DB_URL, USER, PASS);
		   
		   stmt = conn.createStatement();
		   String sql = "SELECT idx, `title` FROM `nc_title`";
		   rs = stmt.executeQuery(sql);
		   while(rs.next()){
			   titleMap.put(rs.getRow(), rs.getString("title"));
		   }
		   
		   
	   } catch(Exception ex){
		   
	   } finally {
			if (rs != null) try { rs.close(); } catch(SQLException ex) {}
	        if (stmt != null) try { stmt.close(); } catch(SQLException ex) {}
		}
   }
   
   // 단어을 DB에서 불러와 map에 저장(key : index, value : word)
   public void getWords(){
		//*** 위에서 정의한 DB경로에서 단어를 불러온다 ***//
		//*** 불러온 테이블의 내용은 MAP에 저장된다 ***//
		//*** MAP은 이후 단어 빈도에 따라 정리해 보는 메소드에서 이용될 수 있다 ***//
		
		//** external parameters **//
		//** 1. DB 테이블 **//
		
		//* 작성자 : 2015112088 김경민 *//
		//* 작성일자 : 2016년 11월 13일 일요일 *//
		//* 최종 수정자 : 2015112088 김경민 *//
		//* 최종 수정일 : 2016년 11월 14일 월요일 *//
	   Statement stmt = null;
	   ResultSet rs = null;
	   
	   try{
		   Class.forName("com.mysql.jdbc.Driver");
		   
		   conn = DriverManager.getConnection(DB_URL, USER, PASS);
		   
		   stmt = conn.createStatement();
		   String sql = "SELECT idx, `word` FROM `nc_word_table`";
		   rs = stmt.executeQuery(sql);
		   while(rs.next()){
			   wordMap.put(rs.getRow(), rs.getString("word"));
		   }
		   
		   
	   } catch(Exception ex){
		   
	   } finally {
			if (rs != null) try { rs.close(); } catch(SQLException ex) {}
	        if (stmt != null) try { stmt.close(); } catch(SQLException ex) {}
	   }
   }
}