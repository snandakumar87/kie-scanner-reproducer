package com.fraud.analysis.CepRules;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.dmn.api.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

@SpringBootApplication


public class CepRulesApplication {


	public static void main(String[] args) {

		SpringApplication.run(CepRulesApplication.class, args);
		CepRulesApplication cepRulesApplication = new CepRulesApplication();
		cepRulesApplication.invokeCEPRule();
		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Bean
	public KieContainer kieContainer() {
		System.setProperty("kie.maven.settings.custom","https://raw.githubusercontent.com/snandakumar87/rtp-case-mgmt/master/settings.xml");
		System.out.println("set prop here"+System.getProperty("kie.maven.settings.custom"));
		return null;

	}



	public String invokeCEPRule() {

		try {
			System.out.println(System.getProperty("kie.maven.settings.custom")+"kie.maven.settings.custom");
			KieServices kieServices = KieServices.Factory.get();


			ReleaseId releaseId = kieServices.newReleaseId("com.myspace", "CEP", "1.0.0");
			KieContainer kContainer = KieServices.Factory.get().newKieContainer(releaseId);
			KieScanner kScanner = kieServices.newKieScanner(kContainer);

			kScanner.start(3000);

			DMNRuntime dmnRuntime = kContainer.newKieSession().getKieRuntime(DMNRuntime.class);

			String namespace = "https://kiegroup.org/dmn/_3688199A-611D-4DBE-BE5B-404510BA8F39";
			String modelName = "TransactionAnalysis";

			DMNModel dmnModel = dmnRuntime.getModel(namespace, modelName);

			DMNContext dmnContext = dmnRuntime.newContext();


			dmnContext.set("Transaction Type", "Web");
			dmnContext.set("Merchant Type", "Veternary");


			DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
			DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName("Transaction Validation");
			boolean txnValid = (boolean) decisionResult.getResult();
			System.out.println(txnValid);
			return String.valueOf(txnValid);

		}catch(Exception e) {
			return "exception";
		}


	}



}
