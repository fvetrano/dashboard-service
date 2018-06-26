package it.tim.dashboard.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import it.tim.dashboard.cms.CMSConfiguration;
import it.tim.dashboard.configuration.ApplicationConfiguration;
import it.tim.dashboard.exception.ExceptionUtil;
import it.tim.dashboard.exception.ServiceException;
import it.tim.dashboard.integration.model.Offer;
import it.tim.dashboard.integration.model.SDPOffersResponse;
import it.tim.dashboard.integration.proxy.SDPProxyOffers;
import it.tim.dashboard.utils.FormatterUtils;
import it.tim.dashboard.web.model.Action;
import it.tim.dashboard.web.model.Aggregateoffer;
import it.tim.dashboard.web.model.CardAction;
import it.tim.dashboard.web.model.Detailinfo;
import it.tim.dashboard.web.model.OfferResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OffersService {

	
	private static final String SMS_TYPE = "SMS";
	private static final String DATI_TYPE = "DATI";
	private static final String VOCE_TYPE = "VOCE";
	private static final String OFFER_TYPE = "OFFERTA";
	

	private SDPProxyOffers sdpProxyOffers;
	private ApplicationConfiguration config;
	private CMSConfiguration cmsConf;

	@Autowired
	public OffersService(SDPProxyOffers sdpProxyOffers, ApplicationConfiguration config) {
		this.sdpProxyOffers = sdpProxyOffers;
		this.config = config;

		try {
			cmsConf = CMSConfiguration.getinstance(config.getPromotionsConfigFile(), config.getProfilesConfigFile());
		} catch (Exception ex) {
			log.error("Error building CMSConfiguration " + ExceptionUtil.getStackTrace(ex));
		}

	}

	public ResponseEntity<OfferResponse> getOffers(String rifCliente, String numLinea, HttpHeaders headers)
			throws ServiceException {

		int iActive = 0;
		int iActiveBoundle = 0;

		int iSuspended = 0;
		int iSuspendedBoundle = 0;
		double credit = 0D;

		OfferResponse offerResponse = new OfferResponse();

		List<Aggregateoffer> aggregateOfferList = new ArrayList<Aggregateoffer>();
		String codeOfferAct = "";
		String codeOfferActB = "";
		String codeOfferSusp = "";
		String codeOfferSuspB = "";

		try {

			// HttpHeaders headers = buildHttpHeaders();

			log.debug("Calling SDP ...");
			long startSDP = System.currentTimeMillis();
			SDPOffersResponse sdpResponse = sdpProxyOffers.getOffers(rifCliente, numLinea, headers);
			long endSDP = System.currentTimeMillis();
			log.info("SDP response in [" + (endSDP - startSDP) + "] ms - Result [" + sdpResponse + "]");

			if (sdpResponse == null) {
				log.debug("errore nella request/response verso SDP");
				throw new Exception("Richiesta SDP null");
			}

			if (sdpResponse.getOpscdata() != null && sdpResponse.getOpscdata().getDebitCO() != null) {
				credit = Double.parseDouble(sdpResponse.getOpscdata().getDebitCO());
			}
			if (sdpResponse.getActiveoffers() != null && sdpResponse.getActiveoffers().getOffer() != null) {
				for (Offer offer : sdpResponse.getActiveoffers().getOffer()) {

					if (offer.getOfferState() != null && (offer.getOfferState().equals("A")||offer.getOfferState().equals("I")) ) {
						// Ci sono offerte attive

						if (!codeOfferAct.equals(offer.getOfferCode())) {
							iActive++;
							codeOfferAct = (offer.getOfferCode());
						}
						

						if (offer.getSection().getFT_FlagAggregation() != null && offer.getSection().getFT_FlagAggregation().equalsIgnoreCase("Y")
								&& offer.getSection() != null && offer.getSection().getFT_AggregationType() != null) {
							// Ci sono offerte attive con boundle

							if (!codeOfferActB.equals(offer.getOfferCode())
									&& checkTimeInterval(offer.getSection().getFT_TimeInterval())) {
								if (checkTimeInterval(offer.getSection().getFT_TimeInterval()))
									iActiveBoundle++;
								codeOfferActB = (offer.getOfferCode());
							}

						}

					} else if (offer.getOfferState() != null && offer.getOfferState().equals("S")) {
						if (!codeOfferSusp.equals(offer.getOfferCode())) {
							iSuspended++;
							codeOfferSusp = (offer.getOfferCode());
						}
						// controllo se ci sono offerte sospese con boundle

						if (offer.getSection().getFT_FlagAggregation() != null && offer.getSection().getFT_FlagAggregation().equalsIgnoreCase("Y")
								&& offer.getSection() != null && offer.getSection().getFT_AggregationType() != null) {
							// ci sono offerte sospese con boundle
							if (!codeOfferSuspB.equals(offer.getOfferCode())) {
								if (checkTimeInterval(offer.getSection().getFT_TimeInterval()))
									iSuspendedBoundle++;
								codeOfferSuspB = (offer.getOfferCode());
							}

						}
					}

				} // end for check su attive e sospese
			}

			if (iActiveBoundle > 0) {
				offerResponse = getActiveOfferBoundleResponse(sdpResponse, credit);

			} else if (iActive > 0 && iActiveBoundle <= 0) {
				offerResponse = getActiveOfferNOBoundleResponse(sdpResponse, credit);
				// Offerta attiva senza boundle

			} else if (iSuspendedBoundle > 0) {
				offerResponse = getSuspeOfferBoundleResponse(sdpResponse, credit);

			} else if (iSuspended > 0) {
				offerResponse = getASuspOfferNOBoundleResponse(sdpResponse);

			} else {
				// profilo tariffario
				offerResponse = getBaseOfferResponse(sdpResponse);
			}

			if (offerResponse != null && !aggregateOfferList.isEmpty())
				offerResponse.setAggregateoffers(aggregateOfferList);

			ResponseEntity<OfferResponse> responseEntity = new ResponseEntity<OfferResponse>(offerResponse,
					HttpStatus.OK);
			return responseEntity;

		} catch (Exception ex) {
			log.error("Error in getUserProfileInfo " + ExceptionUtil.getStackTrace(ex));

			throw new ServiceException("Temporary Error", "ERR001");
		}

	}

	private double getAggregateValue(double valuBasket, String quantity, String stopAccumulationValue,
			String basketConsumeType) throws Exception {

		if (!FormatterUtils.isStringNullOrEmpty(quantity)) {
			if (!(FormatterUtils.isStringNullOrEmpty(stopAccumulationValue))
					&& !(FormatterUtils.isStringNullOrEmpty(basketConsumeType)) && (basketConsumeType.equals("A"))) {

				// In caso di accumulo
				valuBasket = valuBasket + (Double.valueOf(stopAccumulationValue) - Double.valueOf(quantity));
			} else
				//// in caso di erosione o non definito
				valuBasket = valuBasket + Double.valueOf(quantity);
		}

		return valuBasket;

	}

	private double getAggregateTotalValue(double totBasketValue, String startErosionValue, String stopAccumulationValue,
			String basketConsumeType) throws Exception {
		if (!FormatterUtils.isStringNullOrEmpty(startErosionValue)) {
			if (!(FormatterUtils.isStringNullOrEmpty(stopAccumulationValue))
					&& !(FormatterUtils.isStringNullOrEmpty(basketConsumeType)) && (basketConsumeType.equals("A"))) {
				// In caso di accumulo
				totBasketValue = totBasketValue + Double.valueOf(stopAccumulationValue);
			} else
				//// in caso di erosione o non definito
				totBasketValue = totBasketValue + Double.valueOf(startErosionValue);
		}
		return totBasketValue;

	}

	private OfferResponse getActiveOfferBoundleResponse(SDPOffersResponse sdpResponse, double credit) throws Exception {

		double totalBasketVoce = 0D;
		double valueBasketSms = 0D;
		double totalBasketSms = 0D;

		double valueBasketDati = 0D;
		double totalBasketDati = 0D;
		double valueBasketVoce = 0D;
		boolean flagSms = false;
		boolean flagDati = false;
		boolean flagVoce = false;

		String endUsageDate = null;
		String codeOffer = "";
		OfferResponse offerResponse = new OfferResponse();
		List<Aggregateoffer> aggregateOfferList = new ArrayList<Aggregateoffer>();
		boolean isUnlimitedDati = false;
		boolean isUnlimitedVoce = false;
		boolean isUnlimitedSms = false;
		int countActive = 0;
		for (Offer offer : sdpResponse.getActiveoffers().getOffer()) {
			if (offer.getSection().getFT_FlagAggregation() != null && offer.getSection().getFT_FlagAggregation().equalsIgnoreCase("Y")
					&& (offer.getOfferState() != null && (offer.getOfferState().equals("A")|| offer.getOfferState().equals("I")))) {

				if (!checkTimeInterval(offer.getSection().getFT_TimeInterval()))
					continue;

				if (offer.getSection().getFT_NextUsageDate() != null
						&& offer.getSection().getFT_NextUsageDate().length() > 0) {
					String newEndUsageDate = offer.getSection().getFT_NextUsageDate();
					endUsageDate = FormatterUtils.getLastDate(newEndUsageDate, endUsageDate, "yyyyMMddHHmmSS");
				}
				if (!codeOffer.equals(offer.getOfferCode())) {
					countActive++;
					codeOffer = (offer.getOfferCode());
				}

				if (offer.getSection().getFT_AggregationType() != null
						&& offer.getSection().getFT_AggregationType().equalsIgnoreCase(SMS_TYPE)) {
					flagSms = true;

					valueBasketSms = getAggregateValue(valueBasketSms, offer.getSection().getQuantity(),
							offer.getSection().getFT_StopAccumulationValue(),
							offer.getSection().getFT_BasketConsumeType());

					// Se il traffico è illimitato non calcolo il totale
					if (isUnlimitedSms || (offer.getSection().getFT_NoLimitBasket() != null
							&& offer.getSection().getFT_NoLimitBasket().equals("S"))) {
						isUnlimitedSms = true;
					} else {

						totalBasketSms = getAggregateTotalValue(totalBasketSms, offer.getSection().getFT_StartErosionValue(),
								offer.getSection().getFT_StopAccumulationValue(),
								offer.getSection().getFT_BasketConsumeType());
					}

				} else if (offer.getSection().getFT_AggregationType() != null
						&& offer.getSection().getFT_AggregationType().equalsIgnoreCase(DATI_TYPE)) {
					flagDati = true;

					valueBasketDati = getAggregateValue(valueBasketDati, offer.getSection().getQuantity(),
							offer.getSection().getFT_StopAccumulationValue(),
							offer.getSection().getFT_BasketConsumeType());
					// Se il traffico è illimitato non calcolo il totale
					if (isUnlimitedDati || (offer.getSection().getFT_NoLimitBasket() != null
							&& offer.getSection().getFT_NoLimitBasket().equals("S"))) {
						isUnlimitedDati = true;

					} else {
						totalBasketDati = getAggregateTotalValue(totalBasketDati,
								offer.getSection().getFT_StartErosionValue(),
								offer.getSection().getFT_StopAccumulationValue(),
								offer.getSection().getFT_BasketConsumeType());
					}

				} else if (offer.getSection().getFT_AggregationType() != null
						&& offer.getSection().getFT_AggregationType().equalsIgnoreCase(VOCE_TYPE)) {
					flagVoce = true;

					valueBasketVoce = getAggregateValue(valueBasketVoce, offer.getSection().getQuantity(),
							offer.getSection().getFT_StopAccumulationValue(),
							offer.getSection().getFT_BasketConsumeType());
					// Se il traffico è illimitato non calcolo il totale
					if (isUnlimitedVoce || (offer.getSection().getFT_NoLimitBasket() != null
							&& offer.getSection().getFT_NoLimitBasket().equals("S"))) {
						isUnlimitedVoce = true;

					} else {
						totalBasketVoce = getAggregateTotalValue(totalBasketVoce,
								offer.getSection().getFT_StartErosionValue(),

								offer.getSection().getFT_StopAccumulationValue(),
								offer.getSection().getFT_BasketConsumeType());

					}

				}
				

			}

		}

		String valueDatiDescription = null;
		String valueSmsDescription = null;
		String valueVoceDescription = null;

	

		Aggregateoffer offerVoce = getAggregateOffer(flagVoce, isUnlimitedVoce, valueBasketVoce, totalBasketVoce,
				"MINUTI", VOCE_TYPE);
		if (offerVoce != null && offerVoce.getValue() != null) {
			valueVoceDescription = offerVoce.getMindescription();
			aggregateOfferList.add(offerVoce);
		}

		Aggregateoffer offerDati = getAggregateOffer(flagDati, isUnlimitedDati, valueBasketDati, totalBasketDati, "GB",
				DATI_TYPE);
		if (offerDati != null && offerDati.getValue() != null) {
			valueDatiDescription = offerDati.getMindescription();
			aggregateOfferList.add(offerDati);
		}

		Aggregateoffer offerSms = getAggregateOffer(flagSms, isUnlimitedSms, valueBasketSms, totalBasketSms, "SMS",
				SMS_TYPE);
		if (offerSms != null && offerSms.getValue() != null) {
			valueSmsDescription = offerSms.getMindescription();
			aggregateOfferList.add(offerSms);
		}

		if (aggregateOfferList.size() > 0) {
			String diffDay = "";
			if (endUsageDate != null && endUsageDate.length() > 0)
				diffDay = FormatterUtils.getDayUntilToday(endUsageDate, "yyyyMMddHHmmSS");

			StringBuilder buffDescription = new StringBuilder();

			if (credit <= 0) {
				buffDescription.append(
						"Non puoi utilizzare le tue offerte per credito Insufficiente. Effettua una ricarica per continuare a utilizzare le tue offerte.");
				offerResponse.setAction(new Action("path", "Ricarica"));
			} else {
				offerResponse.setAction(new Action("path", "Linea"));
				buffDescription.append("Hai a disposizione");
				if (valueDatiDescription != null && valueDatiDescription.length() > 0)
					buffDescription.append(" <b>").append(valueDatiDescription).append("</b>");
				if (valueVoceDescription != null && valueVoceDescription.length() > 0)
					buffDescription.append(" <b>").append(valueVoceDescription).append("</b>");
				if (valueSmsDescription != null && valueSmsDescription.length() > 0)
					buffDescription.append(" <b>").append(valueSmsDescription).append("</b>");
				buffDescription.append(". Prossimo rinnovo tra <b>" + diffDay + " giorni</b>.");
			}

			offerResponse.setDescription(buffDescription.toString());
			offerResponse.setScadenzaGG(String.valueOf(diffDay));

			if (countActive > 1) {

				offerResponse.setScadenzaGG(String.valueOf(diffDay));
				offerResponse.setType("AGGREGATO");
				offerResponse.setTitle("Panoramica");
			} else if (countActive == 1) {

				offerResponse.setTitle(cmsConf.getPromotionById(codeOffer).getName()); // Da
																						// catalogo
																						// tramite
				// il code offerta
				offerResponse.setType(OFFER_TYPE);
			}

			offerResponse.setStatus("OK");
			offerResponse.setAggregateoffers(aggregateOfferList);
		}

		return offerResponse;

	}

	private Aggregateoffer getAggregateOffer(boolean flagType, boolean isUnlimited, double valueBasket,
			double totalBasket, String measure, String BasketType) throws Exception {
		Aggregateoffer offerAgg = null;

		if (flagType && (totalBasket > 0 || valueBasket > 0)) {
			offerAgg = new Aggregateoffer();
			offerAgg.setMeasure(measure);
			// se infinito settare solo il value e il campo illimitated

			offerAgg.setType(BasketType);
			if (isUnlimited) {
				offerAgg.setUnlimited(isUnlimited);
			} else {
				offerAgg.setTotal(FormatterUtils.formatBasketValue(totalBasket, BasketType));
			}

			offerAgg.setValue(FormatterUtils.formatBasketValue(valueBasket, BasketType));

			if (totalBasket > 0 && valueBasket > 0 && !isUnlimited)
				offerAgg.setPercent((long) (valueBasket / totalBasket * 100));
			else
				offerAgg.setPercent(100);

			offerAgg.setMin_percent(5);// da configurazione
			offerAgg.setImageLink("/img/" + BasketType.toLowerCase() + ".jpg");// da

			offerAgg.setMindescription(offerAgg.getValue() + " " + offerAgg.getMeasure());

		}
		return offerAgg;

	}

	private OfferResponse getActiveOfferNOBoundleResponse(SDPOffersResponse sdpResponse, double credit)
			throws Exception {
		// Controllare il credito in questo caso? Non è specificato
		OfferResponse offerResponse = null;
		for (Offer offer : sdpResponse.getActiveoffers().getOffer()) {

			if (offer.getOfferState() != null && (offer.getOfferState().equals("A")|| offer.getOfferState().equals("I"))) {
				// So che ci sono solo offerta attive senza boundle e prendo
				// la prima
				String offerCode = offer.getOfferCode();
				offerResponse = new OfferResponse();
				offerResponse.setType(OFFER_TYPE); // DA CONFIGURAZIONE

				offerResponse.setTitle(cmsConf.getPromotionById(offerCode).getName()); // preso
																						// tramite
			
				offerResponse.setDetailDescription(cmsConf.getPromotionById(offerCode).getDescription()); // preso
			

				if (offer.getLimitDate() != null && offer.getLimitDate().length() > 0) {
					String newEndUsageDate = offer.getLimitDate();

					String diffDay = FormatterUtils.getDayUntilToday(newEndUsageDate, "yyyyMMddHHmmSS");

					offerResponse.setDescription("La tua offerta si rinnova tra <b>" + diffDay + " giorni</b>");
				}

				offerResponse.setStatus("OK");

				break;
			}

		}

		return offerResponse;
	}

	private OfferResponse getASuspOfferNOBoundleResponse(SDPOffersResponse sdpResponse) throws Exception {
		// Controllare il credito in questo caso? Non è specificato
		OfferResponse offerResponse = null;
		for (Offer offer : sdpResponse.getActiveoffers().getOffer()) {

			if (offer.getOfferState() != null && offer.getOfferState().equals("S")) {
				// So che ci sono solo offerta attive senza boundle e prendo
				// la prima
				String offerCode = offer.getOfferCode();
				offerResponse = new OfferResponse();
				offerResponse.setType(OFFER_TYPE); // DA CONFIGURAZIONE

				offerResponse.setTitle(cmsConf.getPromotionById(offerCode).getName()); // preso
																						// tramite
				// offerCode nel
				// catalogo
				offerResponse.setDetailDescription(cmsConf.getPromotionById(offerCode).getDescription()); // preso
				// tramite
				// offerCode
				// nel
				// catalogo

				offerResponse.setDescription("La tua offerta e' sospesa");
				offerResponse.setCardAction(new CardAction("path", "Ricarica")); // preso
																					// da
																					// configurazione

				offerResponse.setStatus("OK");

				break;
			}

		}

		return offerResponse;
	}

	private OfferResponse getSuspeOfferBoundleResponse(SDPOffersResponse sdpResponse, double credit) throws Exception {
		double valueBasketDati = 0D;
		double totalBasketDati = 0D;
		double valueBasketVoce = 0D;
		double totalBasketVoce = 0D;
		double valueBasketSms = 0D;
		double totalBasketSms = 0D;
		boolean flagDati = false;
		boolean flagVoce = false;
		boolean flagSms = false;

		OfferResponse offerResponse = new OfferResponse();
		List<Aggregateoffer> aggregateOfferList = new ArrayList<Aggregateoffer>();
		boolean isUnlimitedDati = false;
		boolean isUnlimitedVoce = false;
		boolean isUnlimitedSms = false;
		int countSosp = 0;
		String codeOfferta = "";

		for (Offer offer : sdpResponse.getActiveoffers().getOffer()) {
			if (offer.getSection().getFT_FlagAggregation() != null && offer.getSection().getFT_FlagAggregation().equalsIgnoreCase("Y")
					&& (offer.getOfferState() != null && offer.getOfferState().equals("S"))) {
				if (!checkTimeInterval(offer.getSection().getFT_TimeInterval()))
					continue;
				
				if (!codeOfferta.equals(offer.getOfferCode())) {
					countSosp++;
					codeOfferta = (offer.getOfferCode());
				}

				if (offer.getSection().getFT_AggregationType() != null
						&& offer.getSection().getFT_AggregationType().equalsIgnoreCase(SMS_TYPE)) {
					flagSms = true;
					

					valueBasketSms = getAggregateValue(valueBasketSms, offer.getSection().getQuantity(),
							offer.getSection().getFT_StopAccumulationValue(),
							offer.getSection().getFT_BasketConsumeType());

					// Se il traffico è illimitato non calcolo il totale
					if (isUnlimitedSms || (offer.getSection().getFT_NoLimitBasket() != null
							&& offer.getSection().getFT_NoLimitBasket().equals("S"))) {
						isUnlimitedSms = true;
					} else {

						totalBasketSms = getAggregateTotalValue(totalBasketSms, offer.getSection().getFT_StartErosionValue(),
								offer.getSection().getFT_StopAccumulationValue(),
								offer.getSection().getFT_BasketConsumeType());
					}

				} else if (offer.getSection().getFT_AggregationType() != null
						&& offer.getSection().getFT_AggregationType().equalsIgnoreCase(DATI_TYPE)) {
					flagDati = true;

					valueBasketDati = getAggregateValue(valueBasketDati, offer.getSection().getQuantity(),
							offer.getSection().getFT_StopAccumulationValue(),
							offer.getSection().getFT_BasketConsumeType());
					// Se il traffico è illimitato non calcolo il totale
					if (isUnlimitedDati || (offer.getSection().getFT_NoLimitBasket() != null
							&& offer.getSection().getFT_NoLimitBasket().equals("S"))) {
						isUnlimitedDati = true;

					} else {
						totalBasketDati = getAggregateTotalValue(totalBasketDati,
								offer.getSection().getFT_StartErosionValue(),
								offer.getSection().getFT_StopAccumulationValue(),
								offer.getSection().getFT_BasketConsumeType());
					}

				} else if (offer.getSection().getFT_AggregationType() != null
						&& offer.getSection().getFT_AggregationType().equalsIgnoreCase(VOCE_TYPE)) {
					flagVoce = true;

					valueBasketVoce = getAggregateValue(valueBasketVoce, offer.getSection().getQuantity(),
							offer.getSection().getFT_StopAccumulationValue(),
							offer.getSection().getFT_BasketConsumeType());
					// Se il traffico è illimitato non calcolo il totale
					if (isUnlimitedVoce || (offer.getSection().getFT_NoLimitBasket() != null
							&& offer.getSection().getFT_NoLimitBasket().equals("S"))) {
						isUnlimitedVoce = true;

					} else {
						totalBasketVoce = getAggregateTotalValue(totalBasketVoce,
								offer.getSection().getFT_StartErosionValue(),

								offer.getSection().getFT_StopAccumulationValue(),
								offer.getSection().getFT_BasketConsumeType());

					}

				}
			
			}

		}

		// Prendere questi valori da conf "SMS",SMS_TYPE .. etc

		Aggregateoffer offerVoce = getAggregateOffer(flagVoce, isUnlimitedVoce, valueBasketVoce, totalBasketVoce, "MIN",
				VOCE_TYPE);
		if (offerVoce != null && offerVoce.getValue() != null) {

			aggregateOfferList.add(offerVoce);
		}

		Aggregateoffer offerDati = getAggregateOffer(flagDati, isUnlimitedDati, valueBasketDati, totalBasketDati, "GB",
				DATI_TYPE);
		if (offerDati != null && offerDati.getValue() != null) {

			aggregateOfferList.add(offerDati);
		}

		Aggregateoffer offerSms = getAggregateOffer(flagSms, isUnlimitedSms, valueBasketSms, totalBasketSms, "SMS",
				SMS_TYPE);
		if (offerSms != null && offerSms.getValue() != null) {

			aggregateOfferList.add(offerSms);
		}

		if (aggregateOfferList.size() > 0) {

			offerResponse.setCardAction(new CardAction("path", "Ricarica")); // da
			// configurazione

			StringBuilder buffDescription = new StringBuilder();

			if (credit <= 0) {
				buffDescription.append(
						"Non puoi utilizzare le tue offerte per credito Insufficiente. Effettua una ricarica per continuare a utilizzare le tue offerte.");

			}
			if (countSosp > 1) {
				buffDescription.append(
						"Tutte le tue offerte sono sospese! Effettua una ricarica per tornare a utilizzare le tue offerte"); // prendere
																																// da
																																// conf
				offerResponse.setDescription(buffDescription.toString());
				offerResponse.setTitle("Panoramica");
			} else if (countSosp == 1) {
				// c'è solo un'offerta sospesa
				buffDescription.append("La tua offerta e' sospesa"); // prendere
																		// da
																		// conf
				offerResponse.setDescription(buffDescription.toString());
				offerResponse.setTitle(cmsConf.getPromotionById(codeOfferta).getName());
				
				offerResponse.setType("OFFERTA SOSPESA");

				offerResponse.setDetailDescription(cmsConf.getPromotionById(codeOfferta).getDescription()); 
		
			}

			offerResponse.setStatus("OK");
			offerResponse.setAggregateoffers(aggregateOfferList);
		}

		return offerResponse;

	}

	private OfferResponse getBaseOfferResponse(SDPOffersResponse sdpResponse) throws Exception {
		// Controllare il credito in questo caso? Non è specificato
		OfferResponse offerResponse = null;
		String offerBase = null;
		if (sdpResponse.getOpscdata() != null && sdpResponse.getOpscdata().getOfferBase() != null)
			offerBase = sdpResponse.getOpscdata().getOfferBase();

		// So che ci sono solo offerta attive senza boundle e prendo
		// la prima

		offerResponse = new OfferResponse();
		offerResponse.setType("PROFILO"); // DA CONFIGURAZIONE

		offerResponse.setTitle(cmsConf.getProfileById(offerBase).getName()); // preso
																				// tramite
	
		offerResponse.setDescription(cmsConf.getProfileById(offerBase).getDescription()); // preso
		

		offerResponse.setCardAction(new CardAction("path", "Attiva offerta")); // preso
		
		offerResponse.setAction(new Action("path", "Attiva offerta")); // preso
		
		List<Detailinfo> detailInfoList = new ArrayList<Detailinfo>();

		Detailinfo detailInfo = new Detailinfo();
		detailInfo.setImageLink("/img/sms.jpg"); // PRESO DA CONF
		detailInfo.setType("SMS"); // PRESO DA CONF
		detailInfo.setValue("29 CENT");// PRESO DA CATALOGO O CONF?
		detailInfoList.add(detailInfo);
		detailInfo = new Detailinfo();
		detailInfo.setImageLink("/img/voce.jpg"); // PRESO DA CONF
		detailInfo.setType("VOCE"); // PRESO DA CONF
		detailInfo.setValue("2  MIN");// PRESO DA CATALOGO O CONF?
		detailInfoList.add(detailInfo);
		detailInfo = new Detailinfo();
		detailInfo.setImageLink("/img/dati.jpg"); // PRESO DA CONF
		detailInfo.setType("DATI"); // PRESO DA CONF
		detailInfo.setValue("2  GB");// PRESO DA CATALOGO O CONF?
		detailInfoList.add(detailInfo);

		offerResponse.setDetailinfo(detailInfoList);

		offerResponse.setStatus("OK");

		return offerResponse;
	}

	private boolean checkTimeInterval(String timeInterval) throws Exception {
		boolean flagInTime = true;
		if (timeInterval != null && !timeInterval.isEmpty() && timeInterval.equals("FASCIA1")) {

			String compareStringOne = "8:00";
			String compareStringTwo = "23:30";
			SimpleDateFormat fm = new SimpleDateFormat("HH:mm");

			Calendar today = Calendar.getInstance(); // set this up however you
														// need it.
			int dow = today.get(Calendar.DAY_OF_WEEK);
			

			int hour = today.get(Calendar.HOUR_OF_DAY);
			int minute = today.get(Calendar.MINUTE);
			Date date = fm.parse(hour + ":" + minute);
			Date dateCompareOne = fm.parse(compareStringOne);
			Date dateCompareTwo = fm.parse(compareStringTwo);

			if (!(date.compareTo(dateCompareTwo) <= 0 && date.compareTo(dateCompareOne) >= 0)
					|| !((dow >= 1) && (dow <= 6))) {
				flagInTime = false;
			}
		}

		return flagInTime;
	}
	/*
	 * public static void main(String[] args) { try{
	 * System.out.println(checkTimeInterval("FASCIA1") ); }catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * 
	 * }
	 */

}
