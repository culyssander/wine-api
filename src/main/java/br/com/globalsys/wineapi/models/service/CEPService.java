package br.com.globalsys.wineapi.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.globalsys.wineapi.models.domain.CEP;
import br.com.globalsys.wineapi.models.exception.EntidadeNaoEncontradaException;
import br.com.globalsys.wineapi.models.exception.NegocioException;
import br.com.globalsys.wineapi.models.repository.CEPRepository;

@Service
public class CEPService {
	
	@Autowired
	private CEPRepository cepRepository;
	
	public List<CEP> listarTodos() {
		return cepRepository.findAll();
	}
	
	public CEP buscarPeloCEP(Integer cep) {
		CEP cepEncontrado = cepRepository.findByFaixaInicioLessThanEqualAndFaixaFimGreaterThanEqual(cep, cep)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Nao encontrado uma loja que atende ao CEP %d", cep)));
		return cepEncontrado;
	}
	
	public CEP salvar(CEP cep) {
		
		validacaoFaixaInicioMaiorQueFaixaFim(cep);
		
		validacaoDeConflitoDeFaixa(cep);
		
		return cepRepository.save(cep);
	}
	
	public CEP editar(CEP cep) {
		cepRepository.findById(cep.getId())
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Nao encontrado uma loja que atende ao CEP %d", cep.getId())));
				
		validacaoFaixaInicioMaiorQueFaixaFim(cep);
		
		validacaoDaFaixaExistente(cep);
		
		return cepRepository.save(cep);
	}
	
	private void validacaoFaixaInicioMaiorQueFaixaFim(CEP cep) {
		if(cep.getFaixaInicio() >= cep.getFaixaFim()) {
			throw new NegocioException("Faixa inicio nao pode ser maior que faixa fim");
		}
	}
	
	private void validacaoDeConflitoDeFaixa(CEP cep) {
		List<CEP> ceps = cepRepository.findByFaixa(cep.getFaixaInicio(), cep.getFaixaFim());
		
		listaCEPFiltrado(ceps);
	}
	
	private void validacaoDaFaixaExistente(CEP cep) {
		System.out.println(cep);
		List<CEP> ceps = cepRepository.findByFaixa(cep.getFaixaInicio(), cep.getFaixaFim());
		
		List<CEP> cepsFiltrados = ceps.stream().filter(c -> c.getId() != cep.getId()).collect(Collectors.toList());
		
		listaCEPFiltrado(cepsFiltrados);
	}
	
	
	private void listaCEPFiltrado(List<CEP> cepsFiltrados) {
		if(!cepsFiltrados.isEmpty()) {
			String lojas = cepsFiltrados.stream()
					.map(CEP::getLoja)
					.collect(Collectors.joining(", "));
			throw new NegocioException(String.format("Erro! Essa faixa de CEP conflita com a faixa de CEP da loja %s", lojas));
		}
	}

	public boolean remover(Long id) {
		if(cepRepository.existsById(id)) {
			cepRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
