package br.com.vexillum.vexpayment.control;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jrimum.bopepo.Boleto;
import org.jrimum.domkee.financeiro.banco.ParametrosBancariosMap;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.control.GenericControl;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.util.Return;
import br.com.vexillum.vexpayment.model.BilletBanking;

public class GeneratorBilletBanking extends GenericControl<BilletBanking> {

	private Properties billetProperties;

	private DecimalFormat formaterDouble;
	private SimpleDateFormat formaterData;

	// private Date dateAfter;

	public GeneratorBilletBanking() {
		super(null);
	}

	private List<Boleto> constructSeveralBillets(List<BilletBanking> billets) {
		return null;
	}

	private Boleto constructBillet(BilletBanking billet) {
		Boleto boleto = new Boleto(getTitleBillet(billet));
		boleto.setLocalPagamento(billet.getLocalePayment());
		boleto.setInstrucaoAoSacado(billet.getInstructionForSacado());
		boleto = putGeneralInstructionsInBillet(boleto,
				billet.getGeneralInstructions());
		boleto = overrideInformations(boleto, billet.getParams());
		return boleto;
	}

	private Boleto overrideInformations(Boleto boleto,
			HashMap<String, String> params) {
		return null;
	}

	private Boleto putGeneralInstructionsInBillet(Boleto boleto,
			HashMap<Integer, String> generalInstructions) {
		for (int i = 0; i < 8; i++) {
			try {
				Boleto.class.getMethod("setInstrucao" + i,
						new Class[] { String.class }).invoke(boleto,
						new Object[] { generalInstructions.get(i) });
			} catch (IllegalAccessException e) {
				new ExceptionManager(e).treatException();
			} catch (IllegalArgumentException e) {
				new ExceptionManager(e).treatException();
			} catch (InvocationTargetException e) {
				new ExceptionManager(e).treatException();
			} catch (NoSuchMethodException e) {
				new ExceptionManager(e).treatException();
			} catch (SecurityException e) {
				new ExceptionManager(e).treatException();
			}
		}
		return boleto;
	}

	private Titulo getTitleBillet(BilletBanking billet) {
		Titulo titulo = new Titulo(getContaBancaria(), getSacado(),
				getCedente(billet));
		titulo.setParametrosBancarios(getParamentrosBancarios());
		titulo.setAceite(null);
		titulo.setDataDoDocumento(billet.getEmissionDate());
		titulo.setDataDoVencimento(getDateMaturity(billet.getMaturityDate()));
		titulo.setNossoNumero(getOurTitleNumber());
		titulo.setDigitoDoNossoNumero(getCheckDigitOurTitleNumber());
		titulo.setNumeroDoDocumento(getNumberDocumentTitle());
		titulo.setValor(getValueBillet(billet));
		titulo.setTipoDeDocumento(getTypeDocBoleto());
		return titulo;
	}

	private TipoDeTitulo getTypeDocBoleto() {
		return TipoDeTitulo.DS_DUPLICATA_DE_SERVICO;
	}

	private BigDecimal getValueBillet(BilletBanking billet) {
		BigDecimal valueFinal = BigDecimal.valueOf(billet.getValue());
		if (billet.isWithJuros())
			valueFinal = BigDecimal.valueOf(billet.getValue()
					+ billet.getJuros());
		valueFinal = valueFinal.setScale(2, BigDecimal.ROUND_HALF_UP);
		return valueFinal;
	}

	/**
	 * Ao implementar esse método deve ser retornado o nosso numero de acordo
	 * com cada especifiação do banco, sendo que esse nosso numero é o que irá
	 * para o cód. de barras e também para a linha digitável.
	 * 
	 * @return
	 */
	protected String getOurTitleNumber() {
		return null;
	}

	/**
	 * @return Só serve para impressão, não aparece na linha digitável e no cód.
	 *         barras
	 */
	protected String getCheckDigitOurTitleNumber() {
		return null;
	}

	/**
	 * @return Serve somente para o cedente o banco não faz nenhum controle
	 *         sobre ele exceto na maneira de ser impresso, que pode variar de
	 *         banco pra banco.
	 */
	protected String getNumberDocumentTitle() {
		return null;
	}

	private Date getDateMaturity(Date maturityDate) {
		Date now = new Date();
		if (maturityDate.before(now)) {
			now = br.com.vexillum.util.DateCalc.incrementDateInDays(now, null);
		} else if (maturityDate.after(now))
			return maturityDate;
		return now;
	}

	private ParametrosBancariosMap getParamentrosBancarios() {
		return null;
	}

	private Cedente getCedente(BilletBanking billet) {
		return new Cedente(billet.getNameCedente(), billet.getCgcCedente());
	}

	private Sacado getSacado() {
		// TODO Auto-generated method stub
		return null;
	}

	private ContaBancaria getContaBancaria() {
		// TODO Auto-generated method stub
		return null;
	}

}
