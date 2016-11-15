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
public class App {
    public static void main( String[] args ) throws IOException{
        System.out.println( "operate the program..." );
        List<String> currentTitle = new ArrayList<String>();
        
        MySQLConnector mycon = new MySQLConnector();
        CountingWord mycount = new CountingWord();
        
        // 수정일자 : 11월 13일 일요일
        // 수정내역 : 아래 주석을 단 내용
        
        // DB에서 크롤링해온 제목들과 예상 키워드로 등록한 단어 테이블을 불러온다.
        mycon.getWords();
        mycon.getTitles();
        
        // 불러온 제목 맵의 처음부터 끝까지 하나씩 읽어들인다.
        for(Integer key : mycon.titleMap.keySet()){
        	// 제목을 단어 분리기로 나누어 String List에 추가한다.
        	currentTitle = mycount.newSplit(mycount.StringReplace(mycon.titleMap.get(key)));
        	// 분리된 단어 리스트의 처음부터 끝까지 하나씩 읽어들인다.
        	for(String value : currentTitle){
        		// 읽어온 단어가 단어 테이블에 있다면
        		if(mycon.wordMap.containsValue(value)){
        			// 테이블 상의 해당 단어가 있는 index를 찾는다.
        			for(Integer wordTableSize : mycon.wordMap.keySet()){
        				// 단어가 위치한 index를 찾으면
        				if(mycon.wordMap.get(wordTableSize).equals(value)){
        					// 의미망 분석에 쓰일 테이블에 제목의 키값, 단어의 키값, 가중치(1)을 삽입한다.
        					mycon.insertWordToDB(key, wordTableSize, 1);
        					// 하기 내용은 테이블에 들어가고 있는지 확인하는 출력부분으로 삭제해도 무방하다.
        					System.out.println("DB입력! 입력한 단어 : " + value + " 제목 위치 : " + key);
        				}
        			}        			
        		}
        	}
        	// 한번 제목을 불러올 때마다 단어 분리기를 거친 단어 리스트는 초기화 해주어야 한다.
        	currentTitle.clear();
        }
        
		// 11월 13일 branch로 나눈 부분에서 
        // 모든 제목을 하나의 긴 스트링으로 옮기는 부분은 현재 코드에서 제외되었으나
        // 하지만 단어 테이블 확장에 필요한 데이터를 모으는 과정에서 
        // 어떤 단어가 많이 나왔는지 분석하는 과정에서 필요하므로 github에 저장해 두었다.

		// 수정일자 : 2016년 11월 9일 수요일
		// SQLException: URLDecoder: Incomplete trailing escape (%) pattern
    	// 무슨 오류인가...
        System.out.println( "finished..." );
        // db connection close
        mycon.close();
    }
}
