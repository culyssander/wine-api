package br.com.globalsys.wineapi.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.globalsys.wineapi.models.domain.CEP;
import br.com.globalsys.wineapi.models.exception.NegocioException;
import br.com.globalsys.wineapi.models.service.CEPService;

@RestController
@RequestMapping("/cep")
public class CEPController {
	
	@Autowired
	private CEPService cepService;
	
	@GetMapping
	public List<CEP> listarTodos() {
		return cepService.listarTodos();
	}
	
	@GetMapping("{cep}")
	public CEP encontrarPorCEP(@PathVariable Integer cep) {
		return cepService.buscarPeloCEP(cep);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public CEP salvar(@Validated @RequestBody CEP cep, BindingResult result) {
		result.getFieldErrors().forEach(System.out::println);
		validacaoDeErro(result);
		cep.setId(null);
		return cepService.salvar(cep);
	}
	
	@PutMapping("/{id}")
	public CEP editar(@Validated @RequestBody CEP cep, BindingResult result, @PathVariable Long id) {
		result.getFieldErrors().forEach(System.out::println);
		validacaoDeErro(result);
		cep.setId(id);
		return cepService.editar(cep);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		if(cepService.remover(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	private void validacaoDeErro(BindingResult result) {
		if(result.hasErrors()) {
			FieldError error = result.getFieldError();
			throw new NegocioException(String.format("%s", error.getDefaultMessage()));
		}
	}
}
