package br.com.vexillum.control.aspects;

import br.com.vexillum.control.logger.ExceptionLogger;

public class LoggerAspect { }
//
//public aspect LoggerAspect {
//	
//	 pointcut exceptionHandler(Exception e): handler(Exception+) && args(e);  
//	 
//	 before(Exception e): exceptionHandler(e){  
//		 ExceptionLogger ex = new ExceptionLogger(e);
//		 ex.addMessages(e.getLocalizedMessage());
//		 ex.addMessages("[" + thisJoinPoint.getSourceLocation().toString() + "]");
//		 ex.resgisterLog();
//	 }  
//}

	 


