//package org.bala;
import opennlp.tools.stemmer.PorterStemmer;
class Porter{
	public static void main(String args[]){
		System.out.println("Hello");
		String token0 = "diversity";
		PorterStemmer stemmer = new PorterStemmer();
		String token1 = stemmer.stem(token0);
		System.out.println(token1);
		System.out.println(stemmer.stem("divers"));
	}
}
