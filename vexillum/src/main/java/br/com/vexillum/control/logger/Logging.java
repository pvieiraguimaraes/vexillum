package br.com.vexillum.control.logger;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fernando Augusto Silva Lopes
 *Classe responsável pelo log da aplicação
 * @param <T> Classe a qual o logger irá se aplicar.
 */
public abstract class Logging<T> {
	
	protected Object obj;
	protected ArrayList<String> messages;
	
	Logger logger;
	
	/**
	 * @param e
	 * Inicia o logger de um objeto..
	 */
	public Logging(Object e){
		this.obj = e;
		logger = LoggerFactory.getLogger(e.getClass());
	}
	
	/**
	 * Sobrep�e a lista de mensagens que serão usadas na geraçãoo do log.
	 * @param messages Lista de mensagens
	 */
	public void addMessages(ArrayList<String> messages){
		this.messages = messages;
	}
	
	/**
	 * Adiciona uma mensagem a lista de mensagens que serão usadas no log.
	 * @param message Mensagem a ser adicionada
	 */
	public void addMessages(String message){
		if(messages == null){
			messages = new ArrayList<String>();
		}
		messages.add(message);
	}
	
	/**
	 * Cria��o da lista de mensagens a ser usada no log.
	 * @return Um lista de mensagens formatadas no padrão de uma mensagem por linha.
	 */
	protected String getMessage(){
		String message = "";
		for (String item : this.messages) {
			message += "\t" + item;
		}
		return message;
	}
		
	/**
	 * M�todo abstrato que define a forma como o log será feito, dever ser especificado em toda classe herdada.
	 */
	public abstract void resgisterLog();	
	
}
