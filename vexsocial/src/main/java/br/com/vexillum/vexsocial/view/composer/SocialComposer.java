package br.com.vexillum.vexsocial.view.composer;

import org.zkoss.zk.ui.Executions;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;
import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.vexsocial.core.SocialConnections;
import br.com.vexillum.view.CRUDComposer;
import facebook4j.Facebook;
import facebook4j.FacebookException;

@SuppressWarnings("serial")
public abstract class SocialComposer<E extends ICommonEntity, G extends GenericControl<E>> extends CRUDComposer<E, G> {

	private Facebook facebook;
	
	private Twitter twitter;
	
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
/* ------------------------------ Métodos relacionados ao Facebook ------------------------------*/
	
	protected Facebook getFacebookInstance(){
		SocialConnections connection = SpringFactory.getInstance().getBean("socialConnections", SocialConnections.class);
		return connection.getFacebook();
	}
	
	protected Boolean authenticateFacebook(){
		Facebook facebook = getFacebook();
		try {
			if(facebook != null && facebook.getOAuthAccessToken() != null){
				return true;
			}
		} catch (IllegalStateException e) {
			String url = getRequestUrlFacebook();
			Executions.sendRedirect(facebook.getOAuthAuthorizationURL(url));
			return false;
		}
		return false;
	}
	
	public void postStatusMessageFacebook(String message){
		try {
			if(authenticateFacebook()){
				getFacebook().postStatusMessage(message);
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}
	}
	
	private String getRequestUrlFacebook(){
		String url = getContextPath() +  "/callbackFacebook"; //?callbackurl=" + Executions.getCurrent().getDesktop().getRequestPath() + "/";
		return url;
	}
/* ------------------------------ Métodos relacionados ao Twitter ------------------------------*/
	
	protected Twitter getTwitterInstance(){
		SocialConnections connection = SpringFactory.getInstance().getBean("socialConnections", SocialConnections.class);
		return connection.getTwitter();
	}
	
	protected Boolean authenticateTwitter(){
		Twitter twitter = getTwitter();
		try {
			if(twitter != null && twitter.verifyCredentials() != null){
				return true;
			} else {
				SocialConnections connection = SpringFactory.getInstance().getBean("socialConnections", SocialConnections.class);
				String url = getRequestUrlTwitter();
				RequestToken requestToken = connection.getRequestToken(url);
				Executions.sendRedirect(requestToken.getAuthenticationURL());
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		} 
		return false;
	}
	
	public void updateStatusTwitter(String message){
		try {
			if(authenticateTwitter()){
				getTwitter().updateStatus(message);
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	private String getRequestUrlTwitter(){
		String url = getContextPath() +  "/callbackTwitter"; 
		return url;
	}
	
}
