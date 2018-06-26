package it.tim.dashboard.transformer;

import it.tim.dashboard.domain.Credit;
import it.tim.dashboard.web.model.CreditoResponse;
import org.springframework.http.HttpStatus;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility class to build credito Response
 */
public class CreditoTransformer {

    private CreditoTransformer() {
        //empty constructor
    }

    public static CreditoResponse transform(Credit credit, String lineNumber){

        return CreditoResponse.builder()
                .bonus1(format(credit.getBonus1()))
                .credito(format(credit.getCredito()))
                .dataUltimoAggiornamento(credit.getDataAggiornamento())
                .numLinea(lineNumber)
                .status(HttpStatus.OK.name())
                .build();

    }

    private static String format(Double value){  	
    	DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.ITALIAN));
    	return formatter.format(value);
    } 
}
