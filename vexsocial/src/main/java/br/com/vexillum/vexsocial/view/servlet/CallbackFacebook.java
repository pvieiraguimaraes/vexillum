package br.com.vexillum.vexsocial.view.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.vexsocial.core.SocialConnections;
import facebook4j.Facebook;
import facebook4j.FacebookException;

public class CallbackFacebook extends HttpServlet {
    private static final long serialVersionUID = 6305643034487441839L;

    private Properties socialProperties = SpringFactory.getInstance().getBean("socialProperties", Properties.class);
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Facebook facebook = SpringFactory.getInstance().getBean("socialConnections", SocialConnections.class).getFacebook();
        String oauthCode = request.getParameter("code");
        try {
            facebook.getOAuthAccessToken(oauthCode);
        } catch (FacebookException e) {
            throw new ServletException(e);
        }
        socialProperties = SpringFactory.getInstance().getBean("socialProperties", Properties.class);
        response.sendRedirect(request.getContextPath() + socialProperties.getKey("initialpage_facebook"));
    }
}
