package br.com.vexillum.control.manager;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;

/**
 * @author Fernando Augusto Silva Lopes
 *Class de gerenciamento de exceção
 */
public class ExceptionManager implements IManager {
	
	private Properties prop;
	private Exception e;
	
	/**
	 * Construtodo gerenciador de exceção, iniciado a o properties de exceções.
	 * @param e Exce��o a ser gerenciada
	 */
	public ExceptionManager(Exception e){
		prop = SpringFactory.getInstance().getBean("exceptionProperties", Properties.class);
		this.e = e;
	}
	
	/**
	 * M�todo que trata a exce��o, exibindo seu Stack Trace e adicionando sua respectiva mensagem de erro ao retorno.
	 * @return Objeto Return contendo a validade false e a respectica mensagem de erro da Exception
	 */
	public Return treatException(){		
		e.printStackTrace();	   
		String errorMessage = prop.getKey(getNameException()) ;
		errorMessage = (errorMessage == null || errorMessage.isEmpty()) ? e.getMessage() : errorMessage;
		if(errorMessage.isEmpty()) errorMessage = e.getLocalizedMessage();
		return new Return(false, new Message(null, errorMessage));
	}	
	
	/**
	 * @return Retorna o nome da exce��o
	 */
	private String getNameException(){
		return e.getClass().getSimpleName();
	}
}
