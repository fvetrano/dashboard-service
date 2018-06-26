package it.tim.dashboard.transformer;

import it.tim.dashboard.domain.Consistenza;
import it.tim.dashboard.transformer.ConsistenzeTransformer;
import it.tim.dashboard.web.model.ConsistenzeResponse;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConsistenzeTransformerTest {

    @Test
    public void WhenMSPSourceSystemIsProvidedSimIsOfTypePP(){

        Consistenza consistenza = Consistenza.builder()
                .numLinea("123")
                .tiidOwner("123")
                .sourceSystem("MSP")
                .build();

        List<Consistenza> consistenze = Collections.singletonList(consistenza);
        ConsistenzeResponse transform = ConsistenzeTransformer.transform(consistenze);

        assertNotNull(transform);
        assertNotNull(transform.getSim());
        assertEquals(1,transform.getSim().size());
        assertEquals("123",transform.getSim().get(0).getMsisdn());
        assertEquals(ConsistenzeTransformer.SimType.PP.getValue(),transform.getSim().get(0).getType());

    }

    @Test
    public void WhenMSCourceSystemIsProvidedSimIsOfTypeABB(){


        Consistenza consistenza = Consistenza.builder()
                .numLinea("123")
                .tiidOwner("123")
                .sourceSystem("MSC")
                .build();

        List<Consistenza> consistenze = Collections.singletonList(consistenza);
        ConsistenzeResponse transform = ConsistenzeTransformer.transform(consistenze);

        assertNotNull(transform);
        assertNotNull(transform.getSim());
        assertEquals(1,transform.getSim().size());
        assertEquals("123",transform.getSim().get(0).getMsisdn());
        assertEquals(ConsistenzeTransformer.SimType.ABB.getValue(),transform.getSim().get(0).getType());

    }

    @Test
    public void WhenMSSSourceSystemIsProvidedSimIsOfTypePP(){

        Consistenza consistenza = Consistenza.builder()
                .numLinea("123")
                .tiidOwner("123")
                .sourceSystem("MSS")
                .build();

        List<Consistenza> consistenze = Collections.singletonList(consistenza);
        ConsistenzeResponse transform = ConsistenzeTransformer.transform(consistenze);

        assertNotNull(transform);
        assertNotNull(transform.getSim());
        assertEquals(1,transform.getSim().size());
        assertEquals("123",transform.getSim().get(0).getMsisdn());
        assertEquals("123",transform.getTiid());
        assertEquals(ConsistenzeTransformer.SimType.PP.getValue(),transform.getSim().get(0).getType());
    }

    @Test
    public void WhenUnknownSourceSystemIsProvidedSimIsOfTypeUnknown(){

        Consistenza consistenza = Consistenza.builder()
                .numLinea("123")
                .tiidOwner("123")
                .sourceSystem("something")
                .build();

        List<Consistenza> consistenze = Collections.singletonList(consistenza);
        ConsistenzeResponse transform = ConsistenzeTransformer.transform(consistenze);
        assertEquals("123",transform.getSim().get(0).getMsisdn());
        assertNotNull(transform);
        assertNotNull(transform.getSim());
        assertEquals(1,transform.getSim().size());

        assertEquals(ConsistenzeTransformer.SimType.UNKNOWN.getValue(),transform.getSim().get(0).getType());
    }

    @Test
    public void WhenEmptySourceSystemIsProvidedSimIsOfTypeUnknown(){

        Consistenza consistenza = Consistenza.builder()
                .numLinea("123")
                .tiidOwner("123")
                .sourceSystem(null)
                .build();

        List<Consistenza> consistenze = Collections.singletonList(consistenza);
        ConsistenzeResponse transform = ConsistenzeTransformer.transform(consistenze);
        assertEquals("123",transform.getSim().get(0).getMsisdn());
        assertNotNull(transform);
        assertNotNull(transform.getSim());
        assertEquals(1,transform.getSim().size());

        assertEquals(ConsistenzeTransformer.SimType.UNKNOWN.getValue(),transform.getSim().get(0).getType());
    }

}