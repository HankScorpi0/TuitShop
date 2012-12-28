package businessTwitter.android.util.search;

import java.util.ArrayList;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import businessTwitter.android.datasource.TipoBusquedaDS;

public class SearchHelper {
	
	public static String construirStringORHashtag(List<String> keywords){
		String query = "";
		
		//montamos el string con las keywords y los hashtags
		for(int i=0;i<=keywords.size()-1;i++)
			query += keywords.get(i) + " OR #" + keywords.get(i) + " OR " ;		
		
		//quitamos el œltimo OR
		query = query.substring(0, query.length()-3);
		
		return query;
	}
	
	public static String construirStringHashtagFrase(String frase){
		String query = "";
		String[] palabras = frase.split(" ");
		
		for (String palabra : palabras) {
			query += palabra + " OR #" + palabra + " ";
		}
		
		return query;
	}
	
	public static List<Query> contruirQuery(String tipoBusqueda, String ciudad, String articulo, boolean conLinks,
			double longitude, double latitude){
		List<Query> querys = new ArrayList<Query>();	
		Query q1 = new Query();
		Query q2 = new Query();
		querys.add(q1);
		querys.add(q2);
		List<String> keywords = TipoBusquedaDS.getKeyWordsByTipoBusqueda(tipoBusqueda);
		String q = "";

		q += construirStringORHashtag(keywords);
		q += construirStringHashtagFrase(articulo);
		
		String qCiudad = "";
		if(ciudad != null && ciudad.equals("") == false){
			q1.setGeoCode(new GeoLocation(latitude, longitude), 50, "km");
			qCiudad += construirStringHashtagFrase(ciudad);
		}					
		
		q1.setQuery(q);
		q2.setQuery(q + qCiudad);
		
		if(conLinks)
			q += " filter:links ";
		
		q1.setResultType(Query.MIXED);
		q2.setResultType(Query.MIXED);
		
		return querys;
	}

}
