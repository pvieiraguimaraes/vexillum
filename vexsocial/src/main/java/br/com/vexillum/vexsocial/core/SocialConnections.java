package br.com.vexillum.vexsocial.core;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import br.com.vexillum.configuration.Properties;
import br.com.vexillum.util.SpringFactory;
import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;

public class SocialConnections {

	private Facebook facebook;
	
	private Twitter twitter;
	private RequestToken requestToken;
	
	private Properties socialProperties;
	
	public Facebook getFacebook() {
		if(facebook == null){
			facebook = getFacebookInstance();
		}
		return facebook;
	}

	public void setFacebook(Facebook facebook) {
		this.facebook = facebook;
	}

	public Twitter getTwitter() {
		if(twitter == null){
			twitter = getTwitterInstance();
		}
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}

	public RequestToken getRequestToken() {
		return requestToken;
	}
	
	public RequestToken getRequestToken(String url) throws TwitterException {
		setRequestToken(getTwitter().getOAuthRequestToken(url));
		return requestToken;
	}

	public void setRequestToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}

	public Properties getSocialProperties() {
		return socialProperties;
	}

	public void setSocialProperties(Properties socialProperties) {
		this.socialProperties = socialProperties;
	}

	public SocialConnections() {
		socialProperties = SpringFactory.getInstance().getBean("socialProperties", Properties.class);
	}
	
	public Facebook getFacebookInstance(){
		Configuration configuration =  createConfigurationFacebook();
        FacebookFactory facebookFactory = new FacebookFactory(configuration);
        Facebook facebookClient = facebookFactory.getInstance();
        facebookClient.setOAuthPermissions(getSocialProperties().getKey("permissions_facebook"));
		
        return facebookClient;
	}
	
	private Configuration createConfigurationFacebook() {
        ConfigurationBuilder confBuilder = new ConfigurationBuilder();

        confBuilder.setDebugEnabled(true);
        confBuilder.setOAuthAppId(getSocialProperties().getKey("oauth_appid_facebook"));
        confBuilder.setOAuthAppSecret(getSocialProperties().getKey("oauth_appsecret_facebook"));
        confBuilder.setJSONStoreEnabled(true);

        Configuration configuration = confBuilder.build();
        return configuration;
    }
	
	private Twitter getTwitterInstance() {
		twitter4j.conf.Configuration configuration =  createConfigurationTwitter();
        TwitterFactory twitterFactory = new TwitterFactory(configuration);
        Twitter twitterClient = twitterFactory.getInstance();
        
        return twitterClient;
	}
	
	private twitter4j.conf.Configuration createConfigurationTwitter() {
        twitter4j.conf.ConfigurationBuilder confBuilder = new twitter4j.conf.ConfigurationBuilder();

        confBuilder.setDebugEnabled(true);
        confBuilder.setOAuthConsumerKey(getSocialProperties().getKey("oauth_appid_twitter"));
        confBuilder.setOAuthConsumerSecret(getSocialProperties().getKey("oauth_appsecret_twitter"));
        confBuilder.setOAuthAccessToken(getSocialProperties().getKey("oauth_accesstoken_twitter"));
        confBuilder.setOAuthAccessTokenSecret(getSocialProperties().getKey("oauth_accesstokensecret_twitter"));
        confBuilder.setJSONStoreEnabled(true);

        twitter4j.conf.Configuration configuration = confBuilder.build();
        return configuration;
    }
	
}
