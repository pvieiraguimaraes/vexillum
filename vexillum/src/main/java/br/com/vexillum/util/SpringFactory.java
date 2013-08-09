package br.com.vexillum.util;

import java.util.HashMap;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.request.SessionScope;

import br.com.vexillum.control.GenericControl;

/**
 * Classe que carrega as configuraçães do Spring
 * Através dela que se faz a injeção de dependéncias
 * @author Diego
 *
 */
public class SpringFactory extends ClassPathXmlApplicationContext {

        private static ClassPathXmlApplicationContext instance = null;
        
        /**
         * @return Instancia do Spring com informações do xml lidas
         */
        public static ClassPathXmlApplicationContext getInstance() {
                if (instance == null) {
                        instance = new ClassPathXmlApplicationContext("br/com/vexillum/configuration/applicationContext.xml");
                        instance.getBeanFactory().registerScope("session", new SessionScope());
                } 
                return instance;
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
		public static <T> T  getController(String beanName, Class<T> classBean, HashMap<String, Object> data){
        	T controller = getInstance().getBean(beanName, classBean);      
        	((GenericControl) controller).initControl(data);
        	return controller;
        }
        
        private SpringFactory() {}      
        
}