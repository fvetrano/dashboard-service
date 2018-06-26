package it.tim.dashboard.web.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.tim.dashboard.domain.Consistenza;
import it.tim.dashboard.domain.Credit;
import it.tim.dashboard.domain.Offertum;
import it.tim.dashboard.domain.StatoLinea;
import it.tim.dashboard.integration.model.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
public class MockController {

	@GetMapping("/clienti/{rifCliente}/consistenze/{numLinea}/dashboard")
	ResponseEntity<SDPOffersResponse> getOffers(@PathVariable(name = "rifCliente") String rifCliente,
			@PathVariable(name = "numLinea") String numLinea, @RequestHeader HttpHeaders headers) {

		log.info("***************** Start offers MOCK - Headers: " + headers);

		SDPOffersResponse off = new SDPOffersResponse();
		Opscdata opscdata = new Opscdata();

		Activeoffers actOff = new Activeoffers();
		List<Offer> offers = new ArrayList<Offer>();

		if (numLinea.endsWith("00")) { // Offerte attive con boundle credito >0
			opscdata.setOfferBase("278");
			opscdata.setDebitCO("150");

			Offer offer = new Offer();

			Section sec = new Section();
			sec.setFT_FlagAggregation("Y");
			sec.setFT_AggregationType("VOCE");
			sec.setFT_BasketConsumeType("A");
			sec.setQuantity("101");
			sec.setFT_StartErosionValue("1100");
			sec.setFT_NextUsageDate("20180605235959");
			sec.setFT_StopAccumulationValue("200");
			offer.setSection(sec);
			offer.setLimitDate("20180618235959");
			offer.setOfferState("A");
			offer.setOfferCode("ODTO3");
			offers.add(offer);

			Offer offer00 = new Offer();

			offer00.setLimitDate("20180618235959");
			Section sec00 = new Section();
			sec00.setFT_FlagAggregation("Y");
			sec00.setFT_AggregationType("DATI");
			sec00.setFT_BasketConsumeType("A");
			sec00.setQuantity("39999900000000");
			sec00.setFT_StartErosionValue("399999000000003");
			sec00.setFT_NextUsageDate("20180619235959");
			offer00.setSection(sec00);
			offer00.setOfferState("A");
			offer00.setOfferCode("ODTO3");
			sec00.setFT_NoLimitBasket("N"); // FT_NoLimitBasket()
			sec00.setFT_StopAccumulationValue("200");
			offers.add(offer00);

			Offer offer01 = new Offer();

			Section sec01 = new Section();
			sec01.setFT_AggregationType("SMS");
			sec01.setFT_FlagAggregation("Y");
			sec01.setFT_BasketConsumeType("A");
			sec01.setFT_StopAccumulationValue("200");
			sec01.setQuantity("103");
			sec01.setFT_StartErosionValue("1300");
			sec01.setFT_NextUsageDate("20180609235959");
			offer01.setOfferState("A");
			offer01.setSection(sec01);
			offer01.setOfferCode("ODTO3");
			offers.add(offer01);

			// Seconda offerta
			Offer offer2 = new Offer();

			Section sec2 = new Section();
			sec2.setFT_FlagAggregation("Y");
			sec2.setFT_AggregationType("VOCE");
			sec2.setFT_BasketConsumeType("E");
			sec2.setQuantity("101");
			sec2.setFT_StartErosionValue("1100");
			sec2.setFT_NextUsageDate("20180619235959");
			offer2.setSection(sec2);
			offer2.setOfferState("A");
			offer2.setOfferCode("ODTMZ");
			offers.add(offer2);

			Offer offer3 = new Offer();

			Section sec3 = new Section();
			sec3.setFT_AggregationType("DATI");
			sec3.setFT_FlagAggregation("Y");
			sec3.setFT_BasketConsumeType("A");
			sec3.setFT_StopAccumulationValue("200");
			sec3.setQuantity("29999900000000");
			sec3.setFT_StartErosionValue("299999000000003");
			sec3.setFT_NextUsageDate("20180610235959");
			offer3.setSection(sec3);
			offer3.setOfferState("A");
			offer3.setOfferCode("ODTMZ");
			sec3.setFT_NoLimitBasket("S"); // FT_NoLimitBasket()
			offers.add(offer3);

			Offer offer31 = new Offer();

			Section sec31 = new Section();
			sec31.setFT_AggregationType("SMS");
			sec31.setFT_FlagAggregation("Y");
			sec31.setFT_BasketConsumeType("E");
			sec31.setFT_StopAccumulationValue("200");
			sec31.setQuantity("103");
			sec31.setFT_StartErosionValue("1300");
			sec31.setFT_NextUsageDate("20180609235959");
			offer31.setOfferState("A");
			offer31.setSection(sec31);
			offer31.setOfferCode("ODTO3");
			offers.add(offer31);

		} else if (numLinea.endsWith("07")) { // un'offerta attiva con boundle
			opscdata.setOfferBase("278");
			opscdata.setDebitCO("150");

			Offer offer14 = new Offer();

			Section sec14 = new Section();
			sec14.setFT_AggregationType("VOCE");
			sec14.setFT_FlagAggregation("Y");
			sec14.setFT_BasketConsumeType("S");
			sec14.setQuantity("101");
			sec14.setFT_StartErosionValue("1100");
			sec14.setFT_NextUsageDate("20180611235959");
			offer14.setSection(sec14);
			offer14.setLimitDate("20180618235959");
			offer14.setOfferState("A");
			offer14.setOfferCode("ODTO3");
			offers.add(offer14);

			Offer offer13 = new Offer();

			offer13.setLimitDate("20180618235959");
			Section sec13 = new Section();
			sec13.setFT_FlagAggregation("Y");
			sec13.setFT_AggregationType("DATI");
			sec13.setFT_BasketConsumeType("A");
			sec13.setFT_StopAccumulationValue("200");
			sec13.setQuantity("39999900000000");
			sec13.setFT_StartErosionValue("399999000000003");
			sec13.setFT_NextUsageDate("20180610235959");
			offer13.setSection(sec13);
			offer13.setOfferState("A");
			offer13.setOfferCode("ODTO3");
			sec13.setFT_NoLimitBasket("N"); // FT_NoLimitBasket()
			offers.add(offer13);

			Offer offer12 = new Offer();

			Section sec12 = new Section();
			sec12.setFT_FlagAggregation("Y");
			sec12.setFT_AggregationType("SMS");
			sec12.setFT_BasketConsumeType("A");
			sec12.setFT_StopAccumulationValue("200");
			sec12.setQuantity("103");
			sec12.setFT_StartErosionValue("1300");
			sec12.setFT_NextUsageDate("20180609235959");
			offer12.setOfferState("A");
			offer12.setSection(sec12);
			offer12.setOfferCode("ODTO3");
			offers.add(offer12);

			// Seconda offerta
			Offer offer11 = new Offer();

			Section sec11 = new Section();
			sec11.setFT_FlagAggregation("Y");
			sec11.setFT_AggregationType("VOCE");
			sec11.setFT_BasketConsumeType("E");
			sec11.setQuantity("101");
			sec11.setFT_StartErosionValue("1100");
			sec11.setFT_NextUsageDate("20180611235959");
			offer11.setSection(sec11);
			offer11.setOfferState("A");
			offer11.setOfferCode("ODTO3");
			offers.add(offer11);

			Offer offer10 = new Offer();

			Section sec10 = new Section();
			sec10.setFT_FlagAggregation("Y");
			sec10.setFT_AggregationType("DATI");
			sec10.setFT_BasketConsumeType("A");
			sec10.setFT_StopAccumulationValue("200");
			sec10.setQuantity("29999900000000");
			sec10.setFT_StartErosionValue("299999000000003");
			sec10.setFT_NextUsageDate("20180620235959");
			offer10.setSection(sec10);
			offer10.setOfferState("A");
			offer10.setOfferCode("ODTO3");
			sec10.setFT_NoLimitBasket("S"); // FT_NoLimitBasket()
			offers.add(offer10);

		} else if (numLinea.endsWith("02")) {// Offerte sospese con boundle
			opscdata.setOfferBase("278");
			opscdata.setDebitCO("150");

			Offer offer14 = new Offer();

			Section sec14 = new Section();
			sec14.setFT_FlagAggregation("Y");
			sec14.setFT_AggregationType("VOCE");
			sec14.setFT_BasketConsumeType("S");
			sec14.setQuantity("101");
			sec14.setFT_StartErosionValue("1100");
			sec14.setFT_NextUsageDate("20180611235959");
			offer14.setSection(sec14);
			offer14.setLimitDate("20180618235959");
			offer14.setOfferState("S");
			offer14.setOfferCode("ODTO3");
			offers.add(offer14);

			Offer offer13 = new Offer();

			offer13.setLimitDate("20180618235959");

			Section sec13 = new Section();
			sec13.setFT_FlagAggregation("Y");
			sec13.setFT_AggregationType("DATI");
			sec13.setFT_BasketConsumeType("A");
			sec13.setFT_StopAccumulationValue("200");
			sec13.setQuantity("39999900000000");
			sec13.setFT_StartErosionValue("399999000000003");
			sec13.setFT_NextUsageDate("20180610235959");
			offer13.setSection(sec13);
			offer13.setOfferState("S");
			offer13.setOfferCode("ODTO3");
			sec13.setFT_NoLimitBasket("N"); // FT_NoLimitBasket()
			offers.add(offer13);

			Offer offer12 = new Offer();

			Section sec12 = new Section();
			sec12.setFT_FlagAggregation("Y");
			sec12.setFT_AggregationType("SMS");
			sec12.setFT_BasketConsumeType("A");
			sec12.setFT_StopAccumulationValue("200");
			sec12.setQuantity("103");
			sec12.setFT_StartErosionValue("1300");
			sec12.setFT_NextUsageDate("20180609235959");
			offer12.setOfferState("S");
			offer12.setSection(sec12);
			offer12.setOfferCode("ODTO3");
			offers.add(offer12);

			// Seconda offerta
			Offer offer11 = new Offer();
			offer11.setFT_FlagAggregation("Y");
			Section sec11 = new Section();
			sec11.setFT_FlagAggregation("Y");
			sec11.setFT_AggregationType("VOCE");
			sec11.setFT_BasketConsumeType("E");
			sec11.setQuantity("101");
			sec11.setFT_StartErosionValue("1100");
			sec11.setFT_NextUsageDate("20180611235959");
			offer11.setSection(sec11);
			offer11.setOfferState("S");
			offer11.setOfferCode("ODTMZ");
			offers.add(offer11);

			Offer offer10 = new Offer();

			Section sec10 = new Section();
			sec10.setFT_FlagAggregation("Y");
			sec10.setFT_AggregationType("DATI");
			sec10.setFT_BasketConsumeType("A");
			sec10.setFT_StopAccumulationValue("200");
			sec10.setQuantity("29999900000000");
			sec10.setFT_StartErosionValue("299999000000003");
			sec10.setFT_NextUsageDate("20180610235959");
			offer10.setSection(sec10);
			offer10.setOfferState("S");
			offer10.setOfferCode("ODTMZ");
			sec10.setFT_NoLimitBasket("S"); // FT_NoLimitBasket()
			offers.add(offer10);

		} else if (numLinea.endsWith("03")) { // Offerta sospesa senza boundle
			opscdata.setOfferBase("278");
			opscdata.setDebitCO("150");

			Offer offer15 = new Offer();

			Section sec15 = new Section();
			sec15.setFT_FlagAggregation("N");
			sec15.setFT_AggregationType("VOCE");
			sec15.setFT_BasketConsumeType("S");
			sec15.setQuantity("101");
			sec15.setFT_StartErosionValue("1100");
			sec15.setFT_NextUsageDate("20180611235959");
			offer15.setSection(sec15);
			offer15.setLimitDate("20180618235959");
			offer15.setOfferState("S");
			offer15.setOfferCode("ODTO3");
			offers.add(offer15);

			Offer offer16 = new Offer();

			offer16.setLimitDate("20180618235959");
			Section sec16 = new Section();
			sec16.setFT_FlagAggregation("N");
			sec16.setFT_AggregationType("DATI");
			sec16.setFT_BasketConsumeType("A");
			sec16.setFT_StopAccumulationValue("200");
			sec16.setQuantity("39999900000000");

			sec16.setFT_StartErosionValue("399999000000003");
			sec16.setFT_NextUsageDate("20180610235959");
			offer16.setSection(sec16);
			offer16.setOfferState("S");
			offer16.setOfferCode("ODTO3");
			sec16.setFT_NoLimitBasket("N"); // FT_NoLimitBasket()
			offers.add(offer16);

			Offer offer17 = new Offer();

			Section sec17 = new Section();
			sec17.setFT_FlagAggregation("N");
			sec17.setFT_AggregationType("SMS");
			sec17.setFT_BasketConsumeType("A");
			sec17.setFT_StopAccumulationValue("200");
			sec17.setQuantity("103");
			sec17.setFT_StartErosionValue("1300");
			sec17.setFT_NextUsageDate("20180609235959");
			offer17.setOfferState("S");
			offer17.setSection(sec17);
			offer17.setOfferCode("ODTO3");
			offers.add(offer17);

		} else if (numLinea.endsWith("04")) { // Offerta sospesa con un boundle
			opscdata.setOfferBase("278");
			opscdata.setDebitCO("150");

			Offer offer15 = new Offer();

			Section sec15 = new Section();
			sec15.setFT_FlagAggregation("Y");
			sec15.setFT_AggregationType("VOCE");
			sec15.setFT_BasketConsumeType("S");
			sec15.setQuantity("101");
			sec15.setFT_StartErosionValue("1100");
			sec15.setFT_NextUsageDate("20180611235959");
			sec15.setFT_TimeInterval("FASCIA1");

			offer15.setSection(sec15);
			offer15.setLimitDate("20180618235959");
			offer15.setOfferState("S");
			offer15.setOfferCode("ODTO3");

			offers.add(offer15);

			Offer offer16 = new Offer();

			offer16.setLimitDate("20180618235959");
			Section sec16 = new Section();
			sec16.setFT_FlagAggregation("N");
			sec16.setFT_AggregationType("DATI");
			sec16.setFT_BasketConsumeType("A");
			sec16.setFT_StopAccumulationValue("200");
			sec16.setQuantity("39999900000000");

			sec16.setFT_StartErosionValue("399999000000003");
			sec16.setFT_NextUsageDate("20180610235959");
			offer16.setSection(sec16);
			offer16.setOfferState("S");
			offer16.setOfferCode("ODTO3");
			sec16.setFT_NoLimitBasket("N"); // FT_NoLimitBasket()
			offers.add(offer16);

			Offer offer17 = new Offer();

			Section sec17 = new Section();
			sec17.setFT_FlagAggregation("N");
			sec17.setFT_AggregationType("SMS");
			sec17.setFT_BasketConsumeType("A");
			sec17.setFT_StopAccumulationValue("200");
			sec17.setQuantity("103");
			sec17.setFT_StartErosionValue("1300");
			sec17.setFT_NextUsageDate("20180609235959");
			offer17.setOfferState("S");
			offer17.setSection(sec17);
			offer17.setOfferCode("ODTO3");
			offers.add(offer17);

		} else if (numLinea.endsWith("05")) { // Solo profilo
			opscdata.setOfferBase("278");
			opscdata.setDebitCO("150");

			Offer offer18 = new Offer();
			// Deve far visualizzare solo il profilo

			Section sec18 = new Section();
			sec18.setFT_FlagAggregation("N");
			sec18.setFT_AggregationType("VOCE");
			sec18.setFT_BasketConsumeType("S");
			sec18.setQuantity("101");
			sec18.setFT_StartErosionValue("1100");
			sec18.setFT_NextUsageDate("20180611235959");
			offer18.setSection(sec18);
			offer18.setLimitDate("20180618235959");
			offer18.setOfferState("P");
			offer18.setOfferCode("ODTO3");
			offers.add(offer18);

		} else if (numLinea.endsWith("06")) { // Offerta attiva senza boundle
												// aggregabili
			opscdata.setOfferBase("278");
			opscdata.setDebitCO("150");

			Offer offer15 = new Offer();

			Section sec15 = new Section();
			sec15.setFT_FlagAggregation("N");
			sec15.setFT_AggregationType("VOCE");
			sec15.setFT_BasketConsumeType("S");
			sec15.setQuantity("101");
			sec15.setFT_StartErosionValue("1100");
			sec15.setFT_NextUsageDate("20180611235959");
			offer15.setSection(sec15);
			offer15.setLimitDate("20180618235959");
			offer15.setOfferState("A");
			offer15.setOfferCode("ODTO3");
			offers.add(offer15);

			Offer offer16 = new Offer();

			offer16.setLimitDate("20180618235959");
			Section sec16 = new Section();
			sec16.setFT_FlagAggregation("N");
			sec16.setFT_AggregationType("DATI");
			sec16.setFT_BasketConsumeType("A");
			sec16.setFT_StopAccumulationValue("200");
			sec16.setQuantity("39999900000000");

			sec16.setFT_StartErosionValue("399999000000003");

			sec16.setFT_NextUsageDate("20180610235959");
			offer16.setSection(sec16);
			offer16.setOfferState("A");
			offer16.setOfferCode("ODTO3");
			sec16.setFT_NoLimitBasket("N"); // FT_NoLimitBasket()
			offers.add(offer16);

			Offer offer17 = new Offer();

			Section sec17 = new Section();
			sec17.setFT_FlagAggregation("N");
			sec17.setFT_AggregationType("SMS");
			sec17.setFT_BasketConsumeType("A");
			sec17.setFT_StopAccumulationValue("200");
			sec17.setQuantity("103");
			sec17.setFT_StartErosionValue("1300");
			sec17.setFT_NextUsageDate("20180609235959");
			offer17.setOfferState("A");
			offer17.setSection(sec17);
			offer17.setOfferCode("ODTO3");
			offers.add(offer17);
		}

		else if (numLinea.endsWith("01")) { // un'offerta attiva con boundle
											// Michelina

			opscdata.setOfferBase("278");
			opscdata.setDebitCO("150");

			Offer offer33 = new Offer();
			offer33.setFT_FlagAggregation("Y");

			Section sec33 = new Section();
			sec33.setFT_FlagAggregation("Y");
			sec33.setFT_AggregationType("VOCE");
			sec33.setFT_BasketConsumeType("E");
			sec33.setFT_StopAccumulationValue("0");
			sec33.setQuantity("5010");
			sec33.setFT_StartErosionValue("26100"); //FT_StartErosionValue =26100
			sec33.setFT_NextUsageDate("20180711235959");
			sec33.setFT_NoLimitBasket("N");
			sec33.setFT_TimeInterval("");
			offer33.setSection(sec33);
			offer33.setLimitDate("20180711235959");
			offer33.setOfferState("A");
			offer33.setOfferCode("ODH15");
			offers.add(offer33);

			Offer offer34 = new Offer();

			Section sec34 = new Section();
			sec34.setFT_FlagAggregation("Y");
			sec34.setFT_AggregationType("DATI");
			sec34.setFT_BasketConsumeType("E");
			sec34.setFT_StopAccumulationValue("0");
			sec34.setQuantity("2e+9"); // 2GB
			sec34.setFT_StartErosionValue("4724463616"); // FT_StartErosionValue = 4.2GB
			sec34.setFT_NextUsageDate("20180711235959");
			sec34.setFT_NoLimitBasket("N");
			sec34.setFT_TimeInterval("");
			offer34.setSection(sec34);
			offer34.setOfferState("A");
			offer34.setOfferCode("ODH15");

			offers.add(offer34);

			Offer offer35 = new Offer();
			
			Section sec35 = new Section();
			sec35.setFT_FlagAggregation("Y");
			sec35.setFT_AggregationType("SMS");
			sec35.setFT_BasketConsumeType("E");
			sec35.setFT_StopAccumulationValue("0");
			sec35.setQuantity("10");
			sec35.setFT_StartErosionValue("761"); // FT_StartErosionValue = 50
			sec35.setFT_NextUsageDate("20180711235959");
			offer35.setOfferState("A");
			offer35.setSection(sec35);
			offer35.setOfferCode("ODH15");
			offers.add(offer35);

			Offer offer36 = new Offer();
			
			Section sec36 = new Section();
			sec36.setFT_FlagAggregation("Y");
			sec36.setFT_AggregationType("SMS");
			sec36.setFT_BasketConsumeType("E");
			sec36.setFT_StopAccumulationValue("0");
			sec36.setQuantity("85");
			sec36.setFT_StartErosionValue("326"); // FT_StartErosionValue = 50
			sec36.setFT_NextUsageDate("20180711235959");
			offer36.setOfferState("A");
			offer36.setSection(sec36);
			offer36.setOfferCode("ODH15");
			offers.add(offer36);

		} else {// Offerte in collaudo in collaudo
			opscdata.setOfferBase("278");
			opscdata.setDebitCO("150");

			Offer offer20 = new Offer();

			Section sec20 = new Section();
			sec20.setFT_AggregationType("VOCE");
			sec20.setFT_FlagAggregation("N");
			sec20.setFT_BasketConsumeType("A");
			sec20.setFT_StopAccumulationValue("200");
			sec20.setQuantity("0");
			sec20.setFT_StartErosionValue("0");
			sec20.setFT_NextUsageDate("20180611235959");
			offer20.setSection(sec20);
			offer20.setLimitDate("20180618235959");
			offer20.setOfferState("A");
			offer20.setOfferCode("ODT13");
			offers.add(offer20);

			Offer offer21 = new Offer();

			Section sec21 = new Section();
			sec21.setFT_AggregationType("VOCE");
			sec21.setFT_FlagAggregation("N");
			sec21.setFT_BasketConsumeType("A");
			sec21.setFT_StopAccumulationValue("200");
			sec21.setQuantity("0");
			sec21.setFT_StartErosionValue("0");
			sec21.setFT_NextUsageDate("20180611235959");
			offer21.setSection(sec21);
			offer21.setLimitDate("20180618235959");
			offer21.setOfferState("A");
			offer21.setOfferCode("SL057");
			offers.add(offer21);

			Offer offer22 = new Offer();

			Section sec22 = new Section();
			sec22.setFT_AggregationType("VOCE");
			sec22.setFT_FlagAggregation("Y");
			sec22.setFT_BasketConsumeType("E");
			sec22.setQuantity("0");
			sec22.setFT_StartErosionValue("0");
			sec22.setFT_NextUsageDate("20180611235959");
			offer22.setSection(sec22);
			offer22.setLimitDate("20180618235959");
			offer22.setOfferState("A");
			offer22.setOfferCode("ODTDS");
			offers.add(offer22);

			Offer offer23 = new Offer();

			Section sec23 = new Section();
			sec23.setFT_FlagAggregation("Y");
			sec23.setFT_AggregationType("VOCE");
			sec23.setFT_BasketConsumeType("E");
			sec23.setQuantity("700");
			sec23.setFT_StartErosionValue("26100");
			sec23.setFT_NextUsageDate("20180611235959");
			offer23.setSection(sec23);
			offer23.setLimitDate("20180618235959");
			offer23.setOfferState("A");
			offer23.setOfferCode("ODH15");
			offers.add(offer23);

			Offer offer24 = new Offer();

			Section sec24 = new Section();
			sec24.setFT_FlagAggregation("Y");
			sec24.setFT_AggregationType("SMS");
			sec24.setFT_BasketConsumeType("E");
			sec24.setQuantity("11");
			sec24.setFT_StartErosionValue("761");
			sec24.setFT_NextUsageDate("20180611235959");
			offer24.setSection(sec24);
			offer24.setLimitDate("20180618235959");
			offer24.setOfferState("A");
			offer24.setOfferCode("ODH15");
			offers.add(offer24);

			Offer offer25 = new Offer();

			Section sec25 = new Section();
			sec25.setFT_FlagAggregation("Y");
			sec25.setFT_AggregationType("SMS");
			sec25.setFT_BasketConsumeType("E");
			sec25.setQuantity("100");
			sec25.setFT_StartErosionValue("326");
			sec25.setFT_NextUsageDate("20180611235959");
			offer25.setSection(sec25);

			offer25.setLimitDate("20180618235959");
			offer25.setOfferState("A");
			offer25.setOfferCode("ODH15");
			offers.add(offer25);

			Offer offer26 = new Offer();

			Section sec26 = new Section();
			sec26.setFT_FlagAggregation("Y");
			sec26.setFT_AggregationType("DATI");
			sec26.setFT_BasketConsumeType("E");
			sec26.setQuantity("472446");
			sec26.setFT_StartErosionValue("4724463616");
			sec26.setFT_NextUsageDate("20180611235959");
			offer26.setSection(sec26);
			offer26.setLimitDate("20180618235959");
			offer26.setOfferState("A");
			offer26.setOfferCode("ODH15");
			offers.add(offer26);

			Offer offer27 = new Offer();

			Section sec27 = new Section();
			sec27.setFT_FlagAggregation("Y");
			sec27.setFT_AggregationType("DATI");
			sec27.setFT_BasketConsumeType("E");
			sec27.setQuantity("472446");
			sec27.setFT_StartErosionValue("4724463616");
			sec27.setFT_NextUsageDate("20180611235959");
			offer27.setSection(sec27);
			offer27.setLimitDate("20180618235959");
			offer27.setOfferState("A");
			offer27.setOfferCode("ODTEM");
			offers.add(offer27);

			Offer offer28 = new Offer();

			Section sec28 = new Section();
			sec28.setFT_FlagAggregation("N");
			sec28.setFT_AggregationType("VOCE");
			sec28.setFT_BasketConsumeType("E");
			sec28.setQuantity("0");
			sec28.setFT_StartErosionValue("0");
			sec28.setFT_NextUsageDate("20180611235959");
			offer28.setSection(sec28);
			offer28.setLimitDate("20180618235959");
			offer28.setOfferState("A");
			offer28.setOfferCode("ODTEM");
			offers.add(offer28);

			Offer offer29 = new Offer();
			Section sec29 = new Section();
			sec29.setFT_FlagAggregation("Y");
			sec29.setFT_AggregationType("VOCE");
			sec29.setFT_BasketConsumeType("E");
			sec29.setQuantity("45000");
			sec29.setFT_StartErosionValue("45660");
			sec29.setFT_NextUsageDate("20180611235959");
			offer29.setSection(sec29);
			offer29.setLimitDate("20180618235959");
			offer29.setOfferState("A");
			offer29.setOfferCode("ODTEM");
			offers.add(offer29);

			Offer offer30 = new Offer();

			Section sec30 = new Section();
			sec30.setFT_FlagAggregation("Y");
			sec30.setFT_AggregationType("VOCE");
			sec30.setFT_BasketConsumeType("E");
			sec30.setQuantity("100");
			sec30.setFT_StartErosionValue("19560");
			sec30.setFT_NextUsageDate("20180611235959");
			offer30.setSection(sec30);
			offer30.setLimitDate("20180618235959");
			offer30.setOfferState("A");
			offer30.setOfferCode("ODTEM");
			offers.add(offer30);

			Offer offer31 = new Offer();

			Section sec31 = new Section();
			sec31.setFT_FlagAggregation("Y");
			sec31.setFT_AggregationType("DATI");
			sec31.setFT_BasketConsumeType("E");
			sec31.setQuantity("708669644");
			sec31.setFT_StartErosionValue("7086696448");
			sec31.setFT_NextUsageDate("20180611235959");
			offer31.setSection(sec31);
			offer31.setLimitDate("20180618235959");
			offer31.setOfferState("A");
			offer31.setOfferCode("ODTEM");
			offers.add(offer31);

		}

		actOff.setOffer(offers);
		off.setActiveoffers(actOff);
		off.setOpscdata(opscdata);

		return ResponseEntity.ok(off);
	}

	@GetMapping("/clienti/{rifCliente}/consistenze")
	ResponseEntity<List<Consistenza>> getConsistenze(@PathVariable(name = "rifCliente") String rifCliente) {
		log.info("Start consistenze MOCK");

		List<Consistenza> li = new ArrayList<Consistenza>();

		Consistenza co1 = new Consistenza();
		co1.setSourceSystem("MSP");
		co1.setNumLinea("3332226740");
		StatoLinea statoLinea1 = new StatoLinea();
		statoLinea1.setDescrizione(StatoLinea.LINE_ACTIVE);
		co1.setStatoLinea(statoLinea1);
		Offertum off1 = new Offertum();
		off1.setAmbito("Mobile");
		List<Offertum> offerta1 = new ArrayList<Offertum>();
		offerta1.add(off1);
		co1.setOfferta(offerta1);
		li.add(co1);

		Consistenza co2 = new Consistenza();
		co2.setSourceSystem("MSP");
		co2.setNumLinea("3332226770");
		StatoLinea statoLinea2 = new StatoLinea();
		statoLinea2.setDescrizione(StatoLinea.LINE_ACTIVE);
		co2.setStatoLinea(statoLinea2);
		Offertum off2 = new Offertum();
		off2.setAmbito("Mobile");
		List<Offertum> offerta2 = new ArrayList<Offertum>();
		offerta2.add(off2);
		co2.setOfferta(offerta2);
		li.add(co2);

		Consistenza co3 = new Consistenza();
		co3.setSourceSystem("MSP");
		co3.setNumLinea("3332226780");
		StatoLinea statoLinea3 = new StatoLinea();
		statoLinea3.setDescrizione(StatoLinea.LINE_ACTIVE);
		co3.setStatoLinea(statoLinea3);
		Offertum off3 = new Offertum();
		off3.setAmbito("Mobile");
		List<Offertum> offerta3 = new ArrayList<Offertum>();
		offerta3.add(off3);
		co3.setOfferta(offerta3);
		li.add(co3);

		return ResponseEntity.ok(li); 
    }

	
    
    @GetMapping("/clienti/{rifCliente}/consistenze/{numLinea}/credito")
    ResponseEntity<Credit> getCredito(	@PathVariable(name = "rifCliente") String rifCliente, 
    									@PathVariable(name = "numLinea") String numLinea){
    	
     	log.info("Start credito MOCK");
    	
    	Credit c = new Credit();
    	
    	Double b = 100.00;
    	Double cr = 99.90;
    	
    	c.setBonus1(b);
    	c.setCredito(cr);
    	c.setDataAggiornamento("2020-12-10T23:59:59-04:00Z");
    	
    	return ResponseEntity.ok(c);
    }



}
