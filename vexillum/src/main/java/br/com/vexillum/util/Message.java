package br.com.vexillum.util;

public class Message {

	private String associated;
	private String message;
	
	/**
	 * @param associated, o campo(field) associado na visão
	 * @param message, a mensagem de erro
	 */
	public Message(String associated, String message) {
		this.associated = associated;
		this.message = message;
	}

	public String getAssociated() {
		return associated;
	}

	public void setAssociated(String associated) {
		this.associated = associated;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
