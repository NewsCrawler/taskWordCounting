package net.p316.wordcounting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.p316.wordcounting.util.CountingWord;
import net.p316.wordcounting.util.MySQLConnector;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        System.out.println( "Hello World!" );
        //야호오
        StringBuffer buffer = new StringBuffer();
		String replace = new String("");
        
        MySQLConnector mycon = new MySQLConnector();
        CountingWord mycount = new CountingWord();
        
        List<String> list = mycon.getTitles();
		//List<String> countedList = new ArrayList<String>();
		
        for(String title : list){
        	System.out.println(title);
        	buffer.append(title);
        }
        
        // word counting  
    	replace = mycount.StringReplace(buffer.toString());
    	
    	List<String> countedList = mycount.splitAndAdd(replace);
    	
		// 수정일자 : 2016년 11월 9일 수요일
		// SQLException: URLDecoder: Incomplete trailing escape (%) pattern
    	// DB입력을 모르겠소 도와주시오. 프로그램 자체가 실행되지 않는 오류는 아니지만 뭔가 진행되지는 않고있소.
    	for(String strArr : countedList){
        	mycon.insertWordToDB(strArr, "0");
    	}

    	//mycount.writeOnDB();
        //mycount.split();
      
        

    }
}
