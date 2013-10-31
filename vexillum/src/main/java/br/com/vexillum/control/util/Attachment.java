package br.com.vexillum.control.util;

import java.io.File;

import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.Return;

public interface Attachment<T, E extends ICommonEntity> {

		public Return uploadAttachment(T file, String name, E entity);
		public Return deleteAttachment(String name, E entity);
		public File getAttachment(String name, E entity);
}
