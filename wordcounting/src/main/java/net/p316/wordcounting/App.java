package net.p316.wordcounting;

import java.util.ArrayList;
import java.util.List;

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
        
        MySQLConnector mycon = new MySQLConnector();
		
        List<String> list = mycon.getTitles();
		
        for(String title : list){
        	System.out.println(title);
        }
        
        // word counting
    }
}
