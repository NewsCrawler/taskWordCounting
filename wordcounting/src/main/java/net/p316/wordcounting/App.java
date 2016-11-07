package net.p316.wordcounting;

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
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        StringBuffer buffer = new StringBuffer();
		String replace = new String("");
        
        MySQLConnector mycon = new MySQLConnector();
        CountingWord mycount = new CountingWord();
        
        List<String> list = mycon.getTitles();
		
        for(String title : list){
        	System.out.println(title);
        	buffer.append(title);
        }
        
        // word counting  
    	replace = mycount.StringReplace(buffer.toString());
    	
    	mycount.splitAndAdd(replace);
    	

        //mycount.split();
      
        

    }
}
