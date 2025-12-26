package com.supnum.middle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Configuration du client SOAP pour consommer le service SOAP existant.
 * 
 * Cette classe configure :
 * - Jaxb2Marshaller pour marshaller/unmarshaller les objets SOAP
 * - WebServiceTemplate pour effectuer les appels au service SOAP
 */
@Configuration
public class SoapClientConfig {

	/**
	 * URL du service SOAP à consommer.
	 * Récupérée depuis application.yml via la propriété soap.service.url
	 */
	@Value("${soap.service.url}")
	private String soapServiceUrl;

	/**
	 * Configuration du Jaxb2Marshaller pour marshaller/unmarshaller les objets SOAP.
	 * Le contextPath doit correspondre au package des classes générées depuis le XSD.
	 * 
	 * @return Jaxb2Marshaller configuré
	 */
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// Le package doit correspondre aux classes générées depuis le XSD
		// Les classes sont générées dans le package com.supnum.td1.servers
		marshaller.setContextPath("com.supnum.td1.servers");
		return marshaller;
	}

	/**
	 * Configuration du WebServiceTemplate pour appeler le service SOAP.
	 * 
	 * @param marshaller Le marshaller/unmarshaller JAXB injecté
	 * @return WebServiceTemplate configuré avec l'URL du service SOAP
	 */
	@Bean
	public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) {
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		webServiceTemplate.setMarshaller(marshaller);
		webServiceTemplate.setUnmarshaller(marshaller);
		webServiceTemplate.setDefaultUri(soapServiceUrl);
		return webServiceTemplate;
	}
}

