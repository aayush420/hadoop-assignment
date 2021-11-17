//package org.bala;
import opennlp.tools.stemmer.PorterStemmer;
class Porter{
	public static void main(String args[]){
		System.out.println("Hello");
		String token0 = "diversity";
		PorterStemmer stemmer = new PorterStemmer();
		String token1 = stemmer.stem(token0);
		System.out.println(token1);
		System.out.println(stemmer.stem("diversity"));
		System.out.println(stemmer.stem("diversit"));
		System.out.println(stemmer.stem("diversi"));
		System.out.println(stemmer.stem("divers"));
		System.out.println(stemmer.stem("diversY"));
		System.out.println(stemmer.stem("divers1"));
		System.out.println(stemmer.stem("diver"));
		System.out.println(stemmer.stem("dive"));
		System.out.println(stemmer.stem("div"));
		System.out.println(stemmer.stem("di"));
		System.out.println(stemmer.stem("d"));
		System.out.println(stemmer.stem("foolishness"));
	}
}
