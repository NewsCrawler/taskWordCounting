package net.p316.wordcounting.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class CountingWord {
	public static final int N = 4;
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
			"세련된", "심화에", "씽씽", "쌩쌩", "생생", "활활", "부릅", "왈칵"};
	
	// 수정일자 : 2016년 11월 9일 수요일
	// DB에 넣기 위한 string을 선언해봄
	public List<String> word = new ArrayList<String>();
	public List<Integer> count = new ArrayList<Integer>();
	
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
		//*** 문자열을 공백으로 분리해 단어로 만들어 map에 저장한다. (이미 공백으로 분리되어 있지만 한번 더 시도)***//
		//*** 생성한 단어는 사전순으로 정렬되며 이미 map에 있는 단어는 출현횟수를 갱신한다. ***//
		//*** 마지막으로  저장한 모든 단어와 그 빈도수를 사전순으로 출력한다. ***//
		
		//** external parameters **//
		//** 1. str : DB statement 실행시 읽어들인 모든 문자열 **//
		
		//* 작성자 : 2015112088 김경민 *//
		//* 작성일자 : 2016년 11월 05일 토요일 *//
		//* 최종 수정자 : 2015112088 김경민 *//
		//* 최종 수정일 : 2016년 11월 07일 월요일 *//
		
		// str을 공백을 기준으로 분리
		String[] arr = str.split(" ");
		
		// 사전순으로 정렬하기 위한 tree 선언
		Map<String, Integer> map = new TreeMap<String, Integer>();
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
	
	// 결과를 파일로 출력 (추후 DB출력으로 변경)
	public void writeOnDB() throws IOException{
		//*** 콘솔에 출력된 단어와 빈도수를 *.txt파일에 옮겨 적는다. ***//
		
		//** external parameters **//
		//** NONE **//
		
		//* 작성자 : 2015112088 김경민 *//
		//* 작성일자 : 2016년 11월 05일 토요일 *//
		//* 최종 수정자 : 2015112088 김경민 *//
		//* 최종 수정일 : 2016년 11월 07일 월요일 *//
		FileWriter writer = new FileWriter("D:\\eclass download\\2016_02\\주니어디자인프로젝트\\result.txt", false);
		//writer.write(buffer.toString());
		
	}
	
	// 임의로 정한 조사, 의성어, 의태어, 필요없는 단어 제외 테이블
		//private static String[] cancel = {"아", "휴", "아이구", "아이쿠", "아이고", "어", "나", "우리", "저희", "따라", "의해", "을", "를", "에", "의", "가", "으로", "로", "에게", "뿐이다", "의거하여", "근거하여", "입각하여", "기준으로", "예하면", "예를", "들면", "들자면", "저", "소인", "소생", "저희", "지말고", "하지마", "하지마라", "다른", "물론", "또한", "그리고", "비길수", "없다", "해서는", "해서", "안된다", "뿐만", "아니라", "만이", "아니다", "만은", "아니다", "막론하고", "관계없이", "그치지", "않다", "그러나", "그런데", "하지만", "든간에", "논하지", "않다", "따지지", "않다", "설사", "비록", "더라도", "아니면", "만", "못하다", "하는", "편이", "낫다", "불문하고", "향하여", "향해서", "향하다", "쪽으로", "틈타", "이용하여", "타다", "오르다", "제외하고", "이", "외에", "이", "밖에", "하여야", "비로소", "한다면", "몰라도", "외에도", "이곳", "여기", "부터", "기점으로", "따라서", "할", "생각이다", "하려고하다", "이리하여", "그리하여", "그렇게", "함으로써", "하지만", "일때", "할때", "앞에서", "중에서", "보는데서", "으로써", "로써", "까지", "해야한다", "일것이다", "반드시", "할줄알다", "할수있다", "할수있어", "임에", "틀림없다", "한다면", "등", "등등", "제", "겨우", "단지", "다만", "할뿐", "딩동", "댕그", "대해서", "대하여", "대하면", "훨씬", "얼마나", "얼마만큼", "얼마큼", "남짓", "여", "얼마간", "약간", "다소", "좀", "조금", "다수", "몇", "얼마", "지만", "하물며", "또한", "그러나", "그렇지만", "하지만", "이외에도", "대해", "말하자면", "뿐이다", "다음에", "반대로", "반대로", "말하자면", "이와", "반대로", "바꾸어서", "말하면", "바꾸어서", "한다면", "만약", "그렇지않으면", "까악", "툭", "딱", "삐걱거리다", "보드득", "비걱거리다", "꽈당", "응당", "해야한다", "에", "가서", "각", "각각", "여러분", "각종", "각자", "제각기", "하도록하다", "와", "과", "그러므로", "그래서", "고로", "한", "까닭에", "하기", "때문에", "거니와", "이지만", "대하여", "관하여", "관한", "과연", "실로", "아니나다를가", "생각한대로", "진짜로", "한적이있다", "하곤하였다", "하", "하하", "허허", "아하", "거바", "와", "오", "왜", "어째서", "무엇때문에", "어찌", "하겠는가", "무슨", "어디", "어느곳", "더군다나", "하물며", "더욱이는", "어느때", "언제", "야", "이봐", "어이", "여보시오", "흐흐", "흥", "휴", "헉헉", "헐떡헐떡", "영차", "여차", "어기여차", "끙끙", "아야", "앗", "아야", "콸콸", "졸졸", "좍좍", "뚝뚝", "주룩주룩", "솨", "우르르", "그래도", "또", "그리고", "바꾸어말하면", "바꾸어말하자면", "혹은", "혹시", "답다", "및", "그에", "따르는", "때가", "되어", "즉", "지든지", "설령", "가령", "하더라도", "할지라도", "일지라도", "지든지", "몇", "거의", "하마터면", "인젠", "이젠", "된바에야", "된이상", "만큼 어찌됏든", "그위에", "게다가", "점에서", "보아", "비추어", "보아", "고려하면", "하게될것이다", "일것이다", "비교적", "좀", "보다더", "비하면", "시키다", "하게하다", "할만하다", "의해서", "연이서", "이어서", "잇따라", "뒤따라", "뒤이어", "결국", "의지하여", "기대여", "통하여", "자마자", "더욱더", "불구하고", "얼마든지", "마음대로", "주저하지", "않고", "곧", "즉시", "바로", "당장", "하자마자", "밖에", "안된다", "하면된다", "그래", "그렇지", "요컨대", "다시", "말하자면", "바꿔", "말하면", "즉", "구체적으로", "말하자면", "시작하여", "시초에", "이상", "허", "헉", "허걱", "바와같이", "해도좋다", "해도된다", "게다가", "더구나", "하물며", "와르르", "팍", "퍽", "펄렁", "동안", "이래", "하고있었다", "이었다", "에서", "로부터", "까지", "예하면", "했어요", "해요", "함께", "같이", "더불어", "마저", "마저도", "양자", "모두", "습니다", "가까스로", "하려고하다", "즈음하여", "다른", "다른", "방면으로", "해봐요", "습니까", "했어요", "말할것도", "없고", "무릎쓰고", "개의치않고", "하는것만", "못하다", "하는것이", "낫다", "매", "매번", "들", "모", "어느것", "어느", "로써", "갖고말하자면", "어디", "어느쪽", "어느것", "어느해", "어느", "년도", "라", "해도", "언젠가", "어떤것", "어느것", "저기", "저쪽", "저것", "그때", "그럼", "그러면", "요만한걸", "그래", "그때", "저것만큼", "그저", "이르기까지", "할", "줄", "안다", "할", "힘이", "있다", "너", "너희", "당신", "어찌", "설마", "차라리", "할지언정", "할지라도", "할망정", "할지언정", "구토하다", "게우다", "토하다", "메쓰겁다", "옆사람", "퉤", "쳇", "의거하여", "근거하여", "의해", "따라", "힘입어", "그", "다음", "버금", "두번째로", "기타", "첫번째로", "나머지는", "그중에서", "견지에서", "형식으로", "쓰여", "입장에서", "위해서", "단지", "의해되다", "하도록시키다", "뿐만아니라", "반대로", "전후", "전자", "앞의것", "잠시", "잠깐", "하면서", "그렇지만", "다음에", "그러한즉", "그런즉", "남들", "아무거나", "어찌하든지", "같다", "비슷하다", "예컨대", "이럴정도로", "어떻게", "만약", "만일", "위에서", "서술한바와같이", "듯하다", "하지", "않는다면", "만약에", "무엇", "무슨", "어느", "어떤", "아래윗", "조차", "한데", "그럼에도", "불구하고", "여전히", "심지어", "까지도", "조차도", "하지", "않도록", "않기", "위하여", "때", "시각", "무렵", "시간", "동안", "어때", "어떠한", "하여금", "네", "예", "우선", "누구", "누가", "알겠는가", "아무도", "줄은모른다", "줄은", "몰랏다", "하는", "김에", "겸사겸사", "하는바", "그런", "까닭에", "한", "이유는", "그러니", "그러니까", "때문에", "그", "너희", "그들", "너희들", "타인", "것", "것들", "너", "위하여", "공동으로", "동시에", "하기", "위하여", "어찌하여", "무엇때문에", "붕붕", "윙윙", "나", "우리", "엉엉", "휘익", "윙윙", "오호", "아하", "어쨋든", "만", "못하다    하기보다는", "차라리", "하는", "편이", "낫다", "흐흐", "놀라다", "상대적으로", "말하자면", "마치", "아니라면", "쉿", "그렇지", "않으면", "그렇지", "않다면", "안", "그러면", "아니었다면", "하든지", "아니면", "이라면", "좋아", "알았어", "하는것도", "그만이다", "어쩔수", "없다", "하나", "일반적으로", "일단", "한켠으로는", "오자마자", "이렇게되면", "이와같다면", "전부", "한마디", "한항목", "근거로", "하기에", "아울러", "하지", "않도록", "않기", "위해서", "이르기까지", "이", "되다", "로", "인하여", "까닭으로", "이유만으로", "이로", "인하여", "그래서", "이", "때문에", "그러므로", "그런", "까닭에", "알", "수", "있다", "결론을", "낼", "수", "있다", "으로", "인하여", "있다", "어떤것", "관계가", "있다", "관련이", "있다", "연관되다", "어떤것들", "에", "대해", "이리하여", "그리하여", "여부", "하기보다는", "하느니", "하면", "할수록", "운운", "이러이러하다", "하구나", "하도다", "다시말하면", "다음으로", "에", "있다", "에", "달려", "있다", "우리", "우리들", "오히려", "하기는한데", "어떻게", "어떻해", "어찌됏어", "어때", "어째서", "본대로", "자", "이", "이쪽", "여기", "이것", "이번", "이렇게말하자면", "이런", "이러한", "이와", "같은", "요만큼", "요만한", "것", "얼마", "안", "되는", "것", "이만큼", "이", "정도의", "이렇게", "많은", "것", "이와", "같다", "이때", "이렇구나", "것과", "같이", "끼익", "삐걱", "따위", "와", "같은", "사람들", "부류의", "사람들", "왜냐하면", "중의하나", "오직", "오로지", "에", "한하다", "하기만", "하면", "도착하다", "까지", "미치다", "도달하다", "정도에", "이르다", "할", "지경이다", "결과에", "이르다", "관해서는", "여러분", "하고", "있다", "한", "후", "혼자", "자기", "자기집", "자신", "우에", "종합한것과같이", "총적으로", "보면", "총적으로", "말하면", "총적으로", "대로", "하다", "으로서", "참", "그만이다", "할", "따름이다", "쿵", "탕탕", "쾅쾅", "둥둥", "봐", "봐라", "아이야", "아니", "와아", "응", "아이", "참나", "년", "월", "령", "영", "팔", "구", "이천육", "이천칠", "이천팔", "이천구", "하나", "둘", "셋", "넷", "다섯", "여섯", "일곱", "여덟", "아홉", "령", "영"};//new ArrayList<String>("aa", "bb");
}
