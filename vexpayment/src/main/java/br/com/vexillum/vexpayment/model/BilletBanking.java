package br.com.vexillum.vexpayment.model;

import java.util.Date;
import java.util.HashMap;

import br.com.vexillum.model.CommonEntity;

/**
 * @author pedro.vieira
 * 
 */
@SuppressWarnings("serial")
public class BilletBanking extends CommonEntity {

	private String monthReference;
	private Date maturityDate;
	private Date emissionDate;
	private Date periodInitial;
	private Date periodFinal;
	private Double value;
	private Double juros;
	private String numberBillet;
	private String numberDocument;
	private boolean withJuros;

	private String nameCedente;
	private String cgcCedente;

	private String localePayment;
	private String instructionForSacado;

	/**
	 * Por padrão a quantidade de instruções para o boleto é limitada em 8, as
	 * quais deverão ser inseridas na sequência 1 a 8;
	 */
	private HashMap<Integer, String> generalInstructions;

	/**
	 * Contém o mapa dos parâmetros adicionais para o boleto, sendo necessário a
	 * existência de um campo com o mesmo nome do parâmetro no template.
	 */
	private HashMap<String, String> params;

	// TODO Verificar como será tratado o nome dos SACADO, ENDEREÇO e CEDENTE

	public String getMonthReference() {
		return monthReference;
	}

	public void setMonthReference(String monthReference) {
		this.monthReference = monthReference;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public Date getEmissionDate() {
		return emissionDate;
	}

	public void setEmissionDate(Date emissionDate) {
		this.emissionDate = emissionDate;
	}

	public Date getPeriodInitial() {
		return periodInitial;
	}

	public void setPeriodInitial(Date periodInitial) {
		this.periodInitial = periodInitial;
	}

	public Date getPeriodFinal() {
		return periodFinal;
	}

	public void setPeriodFinal(Date periodFinal) {
		this.periodFinal = periodFinal;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getJuros() {
		return juros;
	}

	public void setJuros(Double juros) {
		this.juros = juros;
	}

	public String getNumberBillet() {
		return numberBillet;
	}

	public void setNumberBillet(String numberBillet) {
		this.numberBillet = numberBillet;
	}

	public String getNumberDocument() {
		return numberDocument;
	}

	public void setNumberDocument(String numberDocument) {
		this.numberDocument = numberDocument;
	}

	public boolean isWithJuros() {
		return withJuros;
	}

	public void setWithJuros(boolean withJuros) {
		this.withJuros = withJuros;
	}

	public String getNameCedente() {
		return nameCedente;
	}

	public void setNameCedente(String nameCedente) {
		this.nameCedente = nameCedente;
	}

	public String getCgcCedente() {
		return cgcCedente;
	}

	public void setCgcCedente(String cgcCedente) {
		this.cgcCedente = cgcCedente;
	}

	public String getLocalePayment() {
		return localePayment;
	}

	public void setLocalePayment(String localePayment) {
		this.localePayment = localePayment;
	}

	public String getInstructionForSacado() {
		return instructionForSacado;
	}

	public void setInstructionForSacado(String instructionForSacado) {
		this.instructionForSacado = instructionForSacado;
	}

	public HashMap<Integer, String> getGeneralInstructions() {
		return generalInstructions;
	}

	public void setGeneralInstructions(
			HashMap<Integer, String> generalInstructions) {
		this.generalInstructions = generalInstructions;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

}
