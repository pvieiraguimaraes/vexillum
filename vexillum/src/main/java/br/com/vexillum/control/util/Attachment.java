package br.com.vexillum.control.util;

import java.io.File;
import java.io.InputStream;

import org.zkoss.util.media.Media;

import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.Return;

/**
 * Interface para a manipulação de arquivos do servidor.
 * 
 * @author fotorious
 *
 * @param <T> Entidade que presenta ao que o arquivo estará ligado.
 * @param <E> Entidade do tipo de arquivo, como {@link InputStream} ou {@link Media}.
 */
public interface Attachment<T, E extends ICommonEntity> {

		/**
		 * Responsável pelo upload do arquivo ao servidor.
		 * @param file Arquivo a ser gravado.
		 * @param name Nome do arquivo, contendo extensão.
		 * @param entity Entidade ao qual estará associado.
		 * @return {@link Return}
		 */
		public Return uploadAttachment(T file, String name, E entity);
		
		/**
		 * Responsável por deletar um arquivo do servidor.
		 * @param name Nome do arquivo.
		 * @param entity Entidade ao qual está associado.
		 * @return {@link Return}
		 */
		public Return deleteAttachment(String name, E entity);
		
		/**
		 * Responsável por pegar um arquivo do servido.
		 * @param name Nome do arquivo.
		 * @param entity Entidade ao qual está associado.
		 * @return Arquivo.
		 */
		public File getAttachment(String name, E entity);
}
