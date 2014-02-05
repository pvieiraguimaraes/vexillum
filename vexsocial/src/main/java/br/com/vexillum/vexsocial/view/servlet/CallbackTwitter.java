package br.com.vexillum.vexsocial.view.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;
import br.com.vexillum.configuration.Properties;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.vexsocial.core.SocialConnections;

public class CallbackTwitter extends HttpServlet {
    private static final long serialVersionUID = 6305643034487441839L;

    private Properties socialProperties = SpringFactory.getInstance().getBean("socialProperties", Properties.class);
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	SocialConnections connection = SpringFactory.getInstance().getBean("socialConnections", SocialConnections.class);
        Twitter twitter = connection.getTwitter();
        RequestToken requestToken = connection.getRequestToken();
        
        String verifier = request.getParameter("oauth_verifier");
        try {
            twitter.getOAuthAccessToken(requestToken, verifier);
        } catch (TwitterException e) {
            throw new ServletException(e);
        }
        response.sendRedirect(request.getContextPath() + socialProperties.getKey("initialpage_twitter"));
    }
}
