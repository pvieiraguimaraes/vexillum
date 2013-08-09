package br.com.vexillum.control.util;

import java.io.File;

import br.com.vexillum.model.UserBasic;
import br.com.vexillum.util.Return;

public interface Attachment<T> {

		public Return uploadAttachment(T file, String name, UserBasic user);
		public Return deleteAttachment(String name, UserBasic user);
		public File getAttachment(String name, UserBasic user);
}
