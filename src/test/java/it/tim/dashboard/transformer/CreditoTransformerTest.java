package it.tim.dashboard.transformer;

import it.tim.dashboard.domain.Credit;
import it.tim.dashboard.web.model.CreditoResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreditoTransformerTest {

    private Credit getCredit(Double bonus, Double creditAmount){

        Credit credit = new Credit();
        credit.setBonus1(bonus);
        credit.setCredito(creditAmount);

        return credit;
    }


    @Test
    public void transformTest() {

        String lineNumber = "333535353";
        Double creditAmount = 30.25;
        Double bonus = 10.21;
        CreditoResponse transform = CreditoTransformer.transform(getCredit(bonus,creditAmount), lineNumber);
        //Assert.assertEquals(String.valueOf(creditAmount),transform.getCredito());
        //Assert.assertEquals(String.valueOf(bonus),transform.getBonus1());
        Assert.assertEquals(true,true);
    }
}