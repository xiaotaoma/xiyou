package com.util;
import java.util.Random;


public class CardStr {
	private static CardStr cardStr;
	public static CardStr getCardStr() {
		if (cardStr == null) {
			cardStr = new CardStr();
		}
		return cardStr;
	}
	public String getStr(int n) {
		String s=new String();
		switch (n) {
		case 0:
			s="0";
			break;
		case 1:
			s="1";
			break;
		case 2:
			s="2";
			break;
		case 3:
			s="3";
			break;
		case 4:
			s="4";
			break;
		case 5:
			s="5";
			break;
		case 6:
			s="6";
			break;
		case 7:
			s="7";
			break;
		case 8:
			s="8";
			break;
		case 9:
			s="9";
			break;
		case 11:
			s="a";
			break;
		case 12:
			s="b";
			break;
		case 13:
			s="c";
			break;
		case 14:
			s="d";
			break;
		case 15:
			s="e";
			break;
		case 16:
			s="f";
			break;
		case 17:
			s="g";
			break;
		case 18:
			s="h";
			break;
		case 19:
			s="i";
			break;
		case 20:
			s="j";
			break;
		case 21:
			s="k";
			break;
		case 22:
			s="l";
			break;
		case 23:
			s="m";
			break;
		case 24:
			s="n";
			break;
		case 25:
			s="o";
			break;
		case 26:
			s="p";
			break;
		case 27:
			s="q";
			break;
		case 28:
			s="r";
			break;
		case 29:
			s="s";
			break;
		case 30:
			s="t";
			break;
		case 31:
			s="u";
			break;
		case 32:
			s="v";
			break;
		case 33:
			s="w";
			break;
		case 34:
			s="x";
			break;
		case 35:
			s="y";
			break;
		case 36:
			s="z";
			break;
		case 37:
			s="B";
			break;
		case 38:
			s="C";
			break;
		case 39:
			s="D";
			break;
		case 40:
			s="E";
			break;
		case 41:
			s="F";
			break;
		case 42:
			s="G";
			break;
		case 43:
			s="H";
			break;
		case 44:
			s="I";
			break;
		case 45:
			s="J";
			break;
		case 46:
			s="K";
			break;
		case 47:
			s="L";
			break;
		case 48:
			s="M";
			break;
		case 49:
			s="N";
			break;
		case 50:
			s="O";
			break;
		case 51:
			s="P";
			break;
		case 52:
			s="Q";
			break;
		case 53:
			s="R";
			break;
		case 54:
			s="S";
			break;
		case 55:
			s="T";
			break;
		case 56:
			s="U";
			break;
		case 57:
			s="V";
			break;
		case 58:
			s="W";
			break;
		case 59:
			s="X";
			break;
		case 60:
			s="Y";
			break;
		case 61:
			s="Z";
			break;
		case 10:
			s="A";
			break;
		default:
			System.out.println(n);
			return null;
		}
		return s;
	}
	public String getString(int n) {
		StringBuffer sb = new StringBuffer();
		Random random= new Random();
		for (int i = 0; i < n; i++) {
			int nextInt = random.nextInt(62);
			String str = getStr(nextInt);
			sb.append(str);
		}
		return sb.toString();
	}
}