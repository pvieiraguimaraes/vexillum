package br.com.vexillum.vexpayment.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.vexpayment.model.BilletBanking;

@Service
@Scope("prototype")
public class GeneratorUnicred extends GeneratorBilletBanking {
	
	@Override
	protected String getCheckDigitOurTitleNumber(BilletBanking billet) {
		Integer soma = 0, peso = 9, base = 2, digito;
		String valor = billet.getOurTitleNumber();
		for (Integer i = valor.length(); i > 0; i--) {
			soma += Integer.parseInt(valor.substring(i - 1, i)) * peso;
			if (peso < base) {
				peso = 9;
			} else {
				peso -= 1;
			}
		}
		digito = (soma % 11);
		if (digito == 10)
			return "X";
		else
			return digito.toString();
	}

}
