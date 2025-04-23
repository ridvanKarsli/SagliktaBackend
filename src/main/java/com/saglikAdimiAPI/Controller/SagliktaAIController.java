package com.saglikAdimiAPI.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.saglikAdimiAPI.Abstraction.SagliktaAIActionable;
import com.saglikAdimiAPI.Service.SagliktaAIService;

@RestController
@RequestMapping("/sagliktaAI")
public class SagliktaAIController implements SagliktaAIActionable{
	
	private final SagliktaAIService sagliktaAIService;	

	@Autowired
	public SagliktaAIController(SagliktaAIService sagliktaAIService) {
		this.sagliktaAIService = sagliktaAIService;
	}		


	@PostMapping("/ask")
	@ResponseStatus(HttpStatus.OK)
	@Override
	public String askSagliktaAI(@RequestParam String message, @RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub
		return sagliktaAIService.askSagliktaAI(message, token);
	}

}
