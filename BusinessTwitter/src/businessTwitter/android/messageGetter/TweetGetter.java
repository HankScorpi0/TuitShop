package businessTwitter.android.messageGetter;

import java.util.ArrayList;
import java.util.List;

import businessTwitter.android.util.search.SearchHelper;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TweetGetter {
	
	public List<Tweet> getTweetsBySearch(String tipoBusqueda, String titulo, String ciudad, int numPagina, 
			boolean conLinks, double longitude, double latitude){
		List<Tweet> list = new ArrayList<Tweet>();
		List<Query> querys = SearchHelper.contruirQuery(tipoBusqueda, ciudad, titulo, conLinks, longitude, latitude);			
		Twitter twitter = new TwitterFactory().getInstance();
		
		try {
			for (Query query : querys) {
				query.setPage(numPagina);
				QueryResult result = twitter.search(query);
				list.addAll(result.getTweets());
			}
			
		} catch (TwitterException e) {
			return new ArrayList<Tweet>();
		}
		
		return list;
	}

}
