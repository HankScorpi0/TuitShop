package businessTwitter.android.util.hashtag;

public class HashTagHelper {
	
	public static String convertStringToStringHashtag(String str){
		if(str == null || str.equals(""))
			return "";
		
		String query = "";
		String[] palabras = str.split(" ");
		
		for (String palabra : palabras) {
			query += "#" + palabra + " ";
		}
		
		return query;
	}

}
