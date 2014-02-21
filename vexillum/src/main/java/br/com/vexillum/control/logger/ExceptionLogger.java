package br.com.vexillum.control.logger;


/**
 * @author Fernando Augusto Silva Lopes
 *Classe respons�vel pelo log de Exceções
 */
public class ExceptionLogger extends Logging<Exception> {

	/**
	 * Construtor iniciando o log da Exception e.
	 * @param e
	 */
	public ExceptionLogger(Exception e) {
		super(e);
	}
	
	/* (non-Javadoc)
	 * @see br.com.vexillum.control.logger.Logging#resgisterLog()
	 */
	public void resgisterLog(){		
	    logger.error(getMessage());
	}	
	
}
