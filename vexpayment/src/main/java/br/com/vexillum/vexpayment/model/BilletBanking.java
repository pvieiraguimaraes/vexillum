package br.com.vexillum.vexpayment.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jrimum.domkee.comum.pessoa.endereco.Endereco;

import br.com.vexillum.model.CommonEntity;

/**
 * @author pedro.vieira
 * 
 */
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

	private String nameSacado;
	private String cgcSacado;

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

	private String templatePath;

	private List<Endereco> enderecos;

	private Integer agencyCode;
	private String agencyDigit;

	private String carteiraCode;

	private Integer numberAccount;
	private String digityAccount;

	private String codeBank;
	private String nameBank;

	// TODO Verificar como será tratado o nome dos SACADO, ENDEREÇO e CEDENTE

	public BilletBanking() {
		if (enderecos == null)
			enderecos = new ArrayList<Endereco>();
	}

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

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public String getNameSacado() {
		return nameSacado;
	}

	public void setNameSacado(String nameSacado) {
		this.nameSacado = nameSacado;
	}

	public String getCgcSacado() {
		return cgcSacado;
	}

	public void setCgcSacado(String cgcSacado) {
		this.cgcSacado = cgcSacado;
	}

	public Integer getAgencyCode() {
		return agencyCode;
	}

	public void setAgencyCode(Integer agencyCode) {
		this.agencyCode = agencyCode;
	}

	public String getAgencyDigit() {
		return agencyDigit;
	}

	public void setAgencyDigit(String agencyDigit) {
		this.agencyDigit = agencyDigit;
	}

	public String getCarteiraCode() {
		return carteiraCode;
	}

	public void setCarteiraCode(String carteiraCode) {
		this.carteiraCode = carteiraCode;
	}

	public Integer getNumberAccount() {
		return numberAccount;
	}

	public void setNumberAccount(Integer numberAccount) {
		this.numberAccount = numberAccount;
	}

	public String getDigityAccount() {
		return digityAccount;
	}

	public void setDigityAccount(String digityAccount) {
		this.digityAccount = digityAccount;
	}

	public String getCodeBank() {
		return codeBank;
	}

	public void setCodeBank(String codeBank) {
		this.codeBank = codeBank;
	}

	public String getNameBank() {
		return nameBank;
	}

	public void setNameBank(String nameBank) {
		this.nameBank = nameBank;
	}

}
