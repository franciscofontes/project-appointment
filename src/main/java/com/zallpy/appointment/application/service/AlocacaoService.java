package com.zallpy.appointment.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.zallpy.appointment.application.domain.entity.Alocacao;
import com.zallpy.appointment.application.domain.entity.Colaborador;
import com.zallpy.appointment.application.domain.entity.Projeto;
import com.zallpy.appointment.application.dto.AlocacaoDTO;
import com.zallpy.appointment.application.repository.AlocacaoRepository;

@Service
public class AlocacaoService implements CrudService<Alocacao, Long> {

	@Autowired
	private AlocacaoRepository repository;

	public List<AlocacaoDTO> buscarAlocacoesPorProjeto(Long idProjeto) {
		List<Alocacao> alocacoes = repository.findByProjeto(new Projeto(idProjeto));
		return alocacoes.stream().map(alocacao -> new AlocacaoDTO(alocacao)).collect(Collectors.toList());
	}

	public List<AlocacaoDTO> buscarAlocacoesPorColaborador(Long idColaborador) {
		List<Alocacao> alocacoes = repository.findByColaborador(new Colaborador(idColaborador));
		return alocacoes.stream().map(alocacao -> new AlocacaoDTO(alocacao)).collect(Collectors.toList());
	}

	@Override
	public JpaRepository<Alocacao, Long> getRepository() {
		return repository;
	}

}
