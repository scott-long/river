package org.scottlong.util;

public class StringUtil {

	public static String capitalizePhrase(String s) {
		if (s == null)
			return s;
		String[] words = s.split("\\s");
		StringBuffer sb = new StringBuffer();
		for (String word : words) {
			sb.append(capitalizeWord(word));
			sb.append(" ");
		}
		return sb.toString().trim();
	}

	public static String capitalizeWord(String s) {
		if (s == null)
			return s;
		s = s.trim();
		if (s.length() == 0)
			return s;
		s = s.toLowerCase();
		
		if (s.equals("at"))
			return "at";
		if (s.equals("us"))
			return "US";
		if (s.equals("ds"))
			return "Due South";
		if (s.equals("dn"))
			return "Due North";
		if (s.equals("nr"))
			return "Near";
		if (s.equals("fy"))
			return "Ferry";
		if (s.equals("r"))
			return "River";

		StringBuffer sb = new StringBuffer();
		boolean cap = false;
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			
			if (!cap) {
				if (Character.isLetter(c)) { 
					c = Character.toUpperCase(c);
					cap = true;
				}
			}
			
			sb.append(c);
		}
		
		return sb.toString();
	}

	public static boolean isEmpty(String state) {
		if (state == null)
			return true;
	    return state.length() == 0;
    }
}
