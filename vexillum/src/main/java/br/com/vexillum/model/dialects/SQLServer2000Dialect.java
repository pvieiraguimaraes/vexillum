package br.com.vexillum.model.dialects;

import java.sql.Types;

import org.hibernate.dialect.SQLServerDialect;

public class SQLServer2000Dialect extends SQLServerDialect {
	
	public SQLServer2000Dialect(){
		super();
		registerColumnType(Types.BOOLEAN, "bit");
	}
}
