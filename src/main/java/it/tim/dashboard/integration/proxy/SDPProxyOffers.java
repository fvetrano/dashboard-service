package it.tim.dashboard.integration.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import it.tim.dashboard.integration.model.SDPOffersResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SDPProxyOffers extends ProxyTemplate {

	static final String SUBSYSTEM_NAME = "Offers SDP Service";

	private SDPProxyOffersDelegate sdpProxyDelegate;

	@Autowired
	public SDPProxyOffers(SDPProxyOffersDelegate sdpProxyDelegate) {
		this.sdpProxyDelegate = sdpProxyDelegate;
	}

	public SDPOffersResponse getOffers(String rifCliente, String numLinea, HttpHeaders headers) {

		log.debug("SDPProxy.getProfileInfo (" + rifCliente + ")");
		return getBody(sdpProxyDelegate.getOffers(rifCliente, numLinea, headers));
	}

	@Override
	String getSubsystemName() {
		return SUBSYSTEM_NAME;
	}
}
