// 새로 만들기 위해 생성한 클래스.... 그러나 의미 없다 제대로 돌아가버렸다!
package net.p316.wordcounting.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class newCountingWord {
	public static final int N = 100;
	// 수정일자 : 2016년 11월 9일 수요일
	// 단어 제외 테이블 1차 확장
	// 테스트용 단어 제외 테이블
	private static String[] cancel = {"까지", "를" ,"을", "가화", "것",
			"왜", "아", "어", "할", "에게", "곳", "했다", "했다가", "포토",
			"포토뉴스", "못", "없었다", "없이", "충격", "알고보니", "경악",
			"섹시", "속보", "뉴스라인", "전", "차", "말", "고", "종합", "격", // '격' 좀더 확인 필요
			"OK저축은행", "TV특종", "간다", "았", "한겨레", "주요뉴스초미니",
			"치어리더", "사설", "V앱", "티아라", "미녀", "아닐까", "시끄러운",
			"내일", "위해", "등", "WORLD", "헤드라인", "2명", "초미니입은", "SS",
			"포토S", "오늘의", "VALUES", "사실상", "드나들", "거스", "거스를",
			"힘빼기", "재벌들도", "팩트체크", "Never", "있다", "의문의", "설마",
			"단신", "초미니", "어머", "열애설", "헉", "폭소", "입이", "쩍", "노출",
			"결국", "멘붕", "발칵", "무슨일", "이럴수가", "화들짝", "살아있네",
			"몸매", "미모", "미모의", "숨막히는", "물오른", "얼짱", "신경쓰여",
			"최근한온라인게시판", "화제다", "네티즌", "네티즌들은", "누구", "하네요",
			"세련된", "심화에", "씽씽", "쌩쌩", "생생", "활활", "부릅", "왈칵",
			"단독", "없다", "없", "마친", "의", "하는", "아냐", "때", "더",
			"향하는", "찾은", "vs", "없는"};
	
	// 수정일자 : 2016년 11월 9일 수요일
	// DB에 넣기 위한 string을 선언해봄
	public List<String> word = new ArrayList<String>();
	public List<Integer> count = new ArrayList<Integer>();
	public Map<String, Integer> map = new TreeMap<String, Integer>();
	
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ){
		//*** 빈도수 검사를 마친 단어를 사전순 정렬에서 빈도 순 정렬로 변환 ***//
		//*** Map -> LinkedList 캐스팅한 뒤 빈도값 비교 ***//
		//*** 다시 LinkedList -> Map 캐스팅 ***//
		
		//** external parameters **//
		//** 1. map : 사전 순서로 정렬한 단어 트리 **//
		
		//* 작성자 : 2015112088 김경민 *//
		//* 작성일자 : 2016년 11월 06일 일요일 *//
		//* 최종 수정자 : 2015112088 김경민 *//
		//* 최종 수정일 : 2016년 11월 07일 월요일 *//
    	
	    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	    
	    Collections.sort(
	    	list, new Comparator<Map.Entry<K, V>>(){
	    		public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ){
	    			return (o1.getValue()).compareTo( o2.getValue() );
	    			// 내림차순
	    			// return (o1.getValue()).compareTo( o2.getValue() ) * -1;
	    		}
	    	}
	    );
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    
	    for (Map.Entry<K, V> entry : list){
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}
    
	// 단어 분리, 빈도수 분석
	// 수정일자 : 2016년 11월 9일 수요일
	// App의 countedList에 넣기 위해 return하도록 수정해봄. (public void -> public List<String>)
	public List<String> splitAndAdd(String str){
		String[] arr = str.split(" ");
		
		// 사전순으로 정렬하기 위한 tree 선언

		// 10개 단어를 파일에 쓸 때마다 줄을 바꾸기 위한 변수
		int lineCount = 0;
		
		for(int i=0; i<arr.length; i++){
            if(map.containsKey(arr[i])){
                map.put(arr[i], map.get(arr[i])+1);
            }else{
                map.put(arr[i], 1);
            }
        }// 더이상 처리할 단어가 없을때까지 반복
		
		// 사전순으로 정리된 단어를 빈도순서로 재정리
		map = sortByValue(map);
		
		// 결과를 콘솔에 출력하는 부분
		// 빈도 N 이하의 단어는 출력하지 않는다.
		for(String resultStr : map.keySet()){
			if(map.get(resultStr) < N){
				continue;
			}
			else{
				lineCount++;
				System.out.print(resultStr + " : " + map.get(resultStr) + "회   | ");
				// 수정일자 : 2016년 11월 9일 수요일
				// DB에 넣기 위한 string을 선언하고 넣어봄.
				word.add(resultStr);
				count.add(map.get(resultStr));
				if(lineCount == 10){
					System.out.println();
					lineCount = 0;
				}
			}
        }
		// 수정일자 : 2016년 11월 9일 수요일
		// App의 countedList에 넣기 위해 return하도록 수정해봄. 
		return word;
	}

	// 특수문자 제거(정규 표현식 이용)
	public String StringReplace(String str){
		//*** 정규표현식을 통해 특수문자를 제거한다. ***//
		//*** 한글, 영어, 한자, 숫자는 남는다. ***//
		//*** 이중 띄어쓰기는 띄어쓰기 하나로 치환한다.(치환하지 않으면 띄어쓰기가 압도적으로 많이 나온 단어가 됨) ***//
		
		//** external parameters **//
		//** 1. str : DB statement 실행시 읽어들인 모든 문자열 **//
		
		//* 작성자 : 2015112088 김경민 *//
		//* 작성일자 : 2016년 11월 05일 토요일 *//
		//* 최종 수정자 : 2015112088 김경민 *//
		//* 최종 수정일 : 2016년 11월 07일 월요일 *//
		
		//Pattern namePattern = Pattern.compile("[가-힣]+.*");
		
		String match1 = "[^\u4E00-\u9FBF\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
		str = str.replaceAll(match1, " ");
		String match2 = "\\s{2,}";
		str = str.replaceAll(match2, " ");
		str = StringReplace2(str);
		return str;
	}
	
	// 필요없는 단어 제거(단어 테이블 이용)
	public String StringReplace2(String str){
		//*** 정규표현식을 통해 필요없는 단어를 제거한다. ***//
		//*** 임의로 설정한 단어테이블을 이용한다. ***//
		//*** 이중 띄어쓰기는 띄어쓰기 하나로 치환한다. ***//
		
		//** external parameters **//
		//** 1. str : StringReplace에서 한번 걸러진 문자열 **//
		
		//* 작성자 : 2015112088 김경민 *//
		//* 작성일자 : 2016년 11월 05일 토요일 *//
		//* 최종 수정자 : 2015112088 김경민 *//
		//* 최종 수정일 : 2016년 11월 07일 월요일 *//
		for(int i = 0; i < cancel.length; i++){
			String match1 = "(" + cancel[i] + ")\\s";
			str = str.replaceAll(match1, " ");
			String match2 = "\\s{2,}";
			str = str.replaceAll(match2, " ");
		}
		return str;
	}
}
