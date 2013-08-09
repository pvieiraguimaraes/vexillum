package br.com.vexillum.control.manager;

import java.util.List;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;

public class EmailManager extends Thread implements IManager {

	private final String HOSTNAME;
	private final String SMTPPORT;
	private final String AUTHENTICATIONLOGIN;
	private final String AUTHENTICATIONPASSWORD;
	private final String FROMEMAIL;
	private final String FROMNAME;
	
	private Email email;
	private Properties prop;
	
	/**
	 * Construtor do gerenciador de email, inicando o tipo do email e inicializando as vari�veis finais do gerenciador.
	 * @param type Tipo do email. SimpleEmail, HtmlEmail.
	 */
	public EmailManager(String type){
		prop = SpringFactory.getInstance().getBean("emailProperties", Properties.class);
		
		email = getTypeEmail(type);
		
		HOSTNAME = prop.getKey("hostname");
		SMTPPORT = prop.getKey("smtpport");
		AUTHENTICATIONLOGIN = prop.getKey("authenticationLogin");
		AUTHENTICATIONPASSWORD = prop.getKey("authenticationPassword");
		FROMEMAIL = prop.getKey("fromemail");
		FROMNAME = prop.getKey("fromname");
	}
	
	/**
	 * M�todo respons�vel por iniciar a o email.
	 * @param type Tipo do email
	 * @return Retorna um email do tipo especificado.
	 */
	private Email getTypeEmail(String type){
		String path = "org.apache.commons.mail." + type;
		try{
			return (Email) Class.forName(path).newInstance();
		} catch(Exception e){
			return new SimpleEmail();
		} 
	}
	
	/**
	 * M�todo respons�vel por enviar a mensagem de ativa��o a um email.
	 * @param emailAdress Email a qual a mensagem deve ser enviada.
	 * @return Retorna se o email foi mandado com sucesso.
	 */
	public Return sendActivationEmail(String emailAdress) {
		String subject = prop.getKey("activationsubject");
		String message = "Mensagem de ativação da Vexillum Architeture";
	   try{
		   return sendEmail(emailAdress, subject, message);
	   } catch(Exception e){
		   return new ExceptionManager(e).treatException();
	   }
	}
	
	/**
	 * M�todo respons�vel por mandar um email de car�ter gen�rico a um email espec�fico.
	 * @param emailAdress Email a qual a mensagem deve ser enviada. 
	 * @param subject Assunto da mensagem.
	 * @param message Mensagem do email.
	 * @return
	 */
	public Return sendEmail(String emailAdress, String subject, String message){
		   try{
			   email.setHostName(HOSTNAME); //Utilize o hostname do seu provedor de email
			   email.setSmtpPort(Integer.parseInt(SMTPPORT)); //Quando a porta utilizada n�o � a padr�o (gmail = 465)
			   email.setFrom(FROMEMAIL,FROMNAME); //Configure o seu email do qual enviar�
			   email.addTo(emailAdress); //Adicione os destinat�rios
			   email.setSubject(subject); //Adicione um assunto
			   if(email.getClass().getSimpleName().equals("HtmlEmail")){
				   ((HtmlEmail)email).setHtmlMsg(message);
			   } else {
				   email.setMsg(message); //Adicione a mensagem do email
			   }
			   this.start();
			   return new Return(true);
		   } catch(Exception e){
			   return new ExceptionManager(e).treatException();
		   }
	}
	
	/**
	 * M�todo respons�vel por mandar um email de car�ter gen�rico a uma lista de emails.
	 * @param emailAdress Lista de email a qual a mensagem ser� enviada.
	 * @param subject Assuntos da mensagem.
	 * @param message Mensagem do email.
	 * @return
	 */
	public Return sendEmail(List<String> emailAdress, String subject, String message){
		   try{
			   email.setHostName(HOSTNAME); //Utilize o hostname do seu provedor de email
			   email.setSmtpPort(Integer.parseInt(SMTPPORT)); //Quando a porta utilizada n�o � a padr�o (gmail = 465)
			   email.setFrom(FROMEMAIL,FROMNAME); //Configure o seu email do qual enviar�
			   for (String email : emailAdress) {
				   this.email.addTo(email); //Adicione os destinat�rios
			   }
			   email.setSubject(subject); //Adicione um assunto
			   email.setMsg(message); //Adicione a mensagem do email
			   this.start();
			   return new Return(true);
		   } catch(Exception e){
			   return new ExceptionManager(e).treatException();
		   }
	}

	@Override
	public void run() {
		 try {
			email.setAuthentication(AUTHENTICATIONLOGIN, AUTHENTICATIONPASSWORD); //Para autenticar no servidor � necess�rio chamar os dois m�todos abaixo
			email.setSSLOnConnect(true);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	
}
