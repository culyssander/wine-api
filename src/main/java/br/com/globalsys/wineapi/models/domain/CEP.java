package br.com.globalsys.wineapi.models.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cep")
public class CEP {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Loja é obrigatório, não pode ser vazio")
	@Column(name = "codigo_loja")
	private String loja;
	
	@NotNull(message = "Faixa inicio é obrigatório, não pode ser vazio")
	@Min(value = 10000000)
	@Column(unique = true)
	private Integer faixaInicio;
	
	@NotNull(message = "Faixa fim é obrigatório, não pode ser vazio")
	@Min(value = 10000001)
	@Column(unique = true)
	private Integer faixaFim;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoja() {
		return loja;
	}

	public void setLoja(String loja) {
		this.loja = loja;
	}

	public Integer getFaixaInicio() {
		return faixaInicio;
	}

	public void setFaixaInicio(Integer faixaInicio) {
		this.faixaInicio = faixaInicio;
	}

	public Integer getFaixaFim() {
		return faixaFim;
	}

	public void setFaixaFim(Integer faixaFim) {
		this.faixaFim = faixaFim;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CEP other = (CEP) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CEP [id=" + id + ", loja=" + loja + ", faixaInicio=" + faixaInicio + ", faixaFim=" + faixaFim + "]";
	}
	
}
