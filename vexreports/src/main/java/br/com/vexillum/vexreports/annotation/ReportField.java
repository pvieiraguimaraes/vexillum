package br.com.vexillum.vexreports.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para o módulo de relatórios que especifíca o nome para o campo
 * anotado que deverá aparecer no relatório, bem como sua ordem de aparição,
 * devendo esta começar com 1.
 * 
 * @author Pedro
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ReportField {
	/**
	 * Ordem do campo para aparecer no relatório. Obs: Começar a contagem por 1.
	 * 
	 * @return
	 */
	int order() default 0;

	/**
	 * Nome do campo que deverá aparecer no lugar do nome do Field, se null ou
	 * "" então o próprio nome do Field será colocado no relatório
	 * 
	 * @return
	 */
	String name() default "";
}
