package br.com.vexillum.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateCalc {
	private static Long DAY = 24L * 60L * 60L * 1000L;

	/**
	 * Método que retorna diferença de dias entre datas
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Long difDates(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return null;
		}
		Long dif = date1.getTime() - date2.getTime();
		return dif / DAY;
	}

	/**
	 * Método que soma quantidade de dias a uma data
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date incrementDateInDays(Date date, Integer days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}

	/**
	 * Método que soma quantidade de anos em uma data
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date incrementDateInYears(Date date, Integer years) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, years);
		return calendar.getTime();
	}

	// Calcula a Idade baseado em java.util.Date

	public static int calculateAge(java.util.Date birthDate) {
		if(birthDate == null) return 0;
		
		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(birthDate);

		// Cria um objeto calendar com a data atual
		Calendar today = Calendar.getInstance();

		// Obtém a idade baseado no ano
		int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
		dateOfBirth.add(Calendar.YEAR, age);

		// se a data de hoje é antes da data de Nascimento, então diminui 1(um)
		if (today.before(dateOfBirth)) {
			age--;
		}
		return age;
	}

	/**
	 * Método que soma o mes
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date incrementMonth(Date date, Integer month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}
}
