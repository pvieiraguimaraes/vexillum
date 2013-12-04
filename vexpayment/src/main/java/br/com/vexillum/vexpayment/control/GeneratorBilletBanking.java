package br.com.vexillum.vexpayment.control;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.financeiro.banco.ParametrosBancariosMap;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Banco;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.CodigoDeCompensacaoBACEN;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.vexpayment.model.BilletBanking;

@Service
@Scope("prototype")
public class GeneratorBilletBanking extends GenericControl<BilletBanking> {
	
	private BoletoViewer boletoViewer;

	public GeneratorBilletBanking() {
		super(null);
	}
	
	public byte[] generateBillet(BilletBanking billet){
		constructBillet(billet);
		return boletoViewer.getPdfAsByteArray();
	}
	
	@SuppressWarnings("static-access")
	public byte[] generateSeveralBillets(List<BilletBanking> billets, String pathBoleto){
		List<Boleto> boletosJrimum = constructSeveralBillets(billets);
		return boletoViewer.groupInOnePdfWithTemplate(boletosJrimum, pathBoleto);
	}

	private List<Boleto> constructSeveralBillets(List<BilletBanking> billets) {
		List<Boleto> boletos = new ArrayList<Boleto>();
		for (BilletBanking billetBanking : billets) {
			boletos.add(constructBillet(billetBanking));
		}
		return boletos;
	}

	private Boleto constructBillet(BilletBanking billet) {
		Boleto boleto = new Boleto(getTitleBillet(billet));
		boleto.setLocalPagamento(billet.getLocalePayment());
		boleto.setInstrucaoAoSacado(billet.getInstructionForSacado());
		boleto = putGeneralInstructionsInBillet(boleto,
				billet.getGeneralInstructions());
		boleto = overrideInformations(boleto, billet.getParams());
		boletoViewer = new BoletoViewer(boleto, billet.getTemplatePath());
		return boleto;
	}

	private Boleto overrideInformations(Boleto boleto,
			HashMap<String, String> params) {
		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			boleto.addTextosExtras(key, params.get(key));
		}
		return boleto;
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
		Titulo titulo = new Titulo(getContaBancaria(billet), getSacado(billet),
				getCedente(billet));
		titulo.setParametrosBancarios(getParamentrosBancarios());
		titulo.setAceite(null);
		titulo.setDataDoDocumento(billet.getEmissionDate());
		titulo.setDataDoVencimento(getDateMaturity(billet.getMaturityDate()));
		titulo.setNossoNumero(getOurTitleNumber(billet));
		titulo.setDigitoDoNossoNumero(getCheckDigitOurTitleNumber(billet));
		titulo.setNumeroDoDocumento(getNumberDocumentTitle(billet));
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
	protected String getOurTitleNumber(BilletBanking billet) {
		return billet.getOurTitleNumber();
	}

	/**
	 * @return Só serve para impressão, não aparece na linha digitável e no cód.
	 *         barras
	 */
	protected String getCheckDigitOurTitleNumber(BilletBanking billet) {
		return null;
	}

	/**
	 * @return Serve somente para o cedente o banco não faz nenhum controle
	 *         sobre ele exceto na maneira de ser impresso, que pode variar de
	 *         banco pra banco.
	 */
	protected String getNumberDocumentTitle(BilletBanking billet) {
		return billet.getNumberBillet();
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

	private Sacado getSacado(BilletBanking billet) {
		Sacado sacado = new Sacado(billet.getNameSacado(),
				billet.getCgcSacado());
		sacado.setEnderecos(billet.getEnderecos());
		return sacado;
	}

	private ContaBancaria getContaBancaria(BilletBanking billet) {
		ContaBancaria contaBancaria = new ContaBancaria();
		contaBancaria.setAgencia(getAgencia(billet));
		contaBancaria.setBanco(getBanco(billet));
		contaBancaria.setNumeroDaConta(getNumeroDaConta(billet));
		return contaBancaria;
	}

	private NumeroDaConta getNumeroDaConta(BilletBanking billet) {
		return new NumeroDaConta(billet.getNumberAccount(), billet.getDigityAccount());
	}

	private Banco getBanco(BilletBanking billet) {
		CodigoDeCompensacaoBACEN codigoBACEN = new CodigoDeCompensacaoBACEN(billet.getCodeBank());
		return new Banco(codigoBACEN, billet.getNameBank());
	}

	private Agencia getAgencia(BilletBanking billet) {
		return new Agencia(billet.getAgencyCode(), billet.getAgencyDigit());
	}

}
