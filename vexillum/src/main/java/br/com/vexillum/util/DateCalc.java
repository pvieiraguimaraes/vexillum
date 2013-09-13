package br.com.vexillum.util;

import java.util.Calendar;
import java.util.Date;

public class DateCalc {
	private static Long DAY = 24L * 60L * 60L * 1000L;
	
	/** Método que retorna diferença de dias entre datas
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Long difDates(Date date1, Date date2){
		Long dif = date1.getTime() - date2.getTime();
		return dif/DAY;
	}
	
	/**Método que soma quantidade de dias a uma data
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date incrementDate(Date date, Integer days){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}
}
