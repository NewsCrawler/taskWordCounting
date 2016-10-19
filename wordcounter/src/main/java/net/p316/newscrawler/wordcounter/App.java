package net.p316.newscrawler.wordcounter;

import com.twitter.penguin.korean.TwitterKoreanProcessorJava;
import com.twitter.penguin.korean.tokenizer.KoreanTokenizer;

import scala.collection.Seq;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String text = "한국어를 처리하는 예시입니닼ㅋㅋㅋㅋㅋ #한국어 이명박의 문재인";
    	
        System.out.println( text );
        
        CharSequence normalized = TwitterKoreanProcessorJava.normalize(text);
        System.out.println(normalized);
        
        Seq<KoreanTokenizer.KoreanToken> tokens = TwitterKoreanProcessorJava.tokenize(normalized);
        System.out.println(TwitterKoreanProcessorJava.tokensToJavaStringList(tokens));
        
        System.out.println(TwitterKoreanProcessorJava.tokensToJavaKoreanTokenList(tokens));
        
        Seq<KoreanTokenizer.KoreanToken> stemmed = TwitterKoreanProcessorJava.stem(tokens);
        System.out.println(TwitterKoreanProcessorJava.tokensToJavaStringList(stemmed));
        
    }
}
