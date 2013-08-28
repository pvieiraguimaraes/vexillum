package br.com.vexillum.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CloneUtils {
	
	public static Object cloneSerializable(Serializable obj) {  
		  treatBeforeCopy(obj);
		
		  ObjectOutputStream out = null;  
		  ObjectInputStream in = null;  
		  
		  try {  
		    ByteArrayOutputStream bout = new ByteArrayOutputStream();  
		    out = new ObjectOutputStream(bout);  
		              
		    out.writeObject(obj);  
		    out.close();  
		              
		    ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());  
		    in = new ObjectInputStream(bin);              
		    Object copy = in.readObject();  
		  
		    in.close();  
		              
		    return copy;  
		  } catch (Exception ex) {  
		    ex.printStackTrace();  
		  } finally {  
		    try {  
		      if(out != null) {  
		        out.close();  
		      }  
		                  
		      if(in != null) {  
		        in.close();  
		      }  
		    } catch (IOException ignore) {}  
		  }  
		          
		  return null;  
		}

	private static void treatBeforeCopy(Serializable obj) {
		HibernateUtils.initialize(obj);
	}  
}
