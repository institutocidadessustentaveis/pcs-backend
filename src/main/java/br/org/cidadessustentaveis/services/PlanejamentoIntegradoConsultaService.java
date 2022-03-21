package br.org.cidadessustentaveis.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.FiltroCidadesComBoasPraticas;
import br.org.cidadessustentaveis.dto.FiltroIndicadoresPorMunicipios;
import br.org.cidadessustentaveis.dto.FiltroVariaveisPorMunicipios;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ConsultaBoaPratica;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ConsultaIndicador;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ConsultaVariavel;
import br.org.cidadessustentaveis.repository.PlanejamentoIntegradoConsultaBoaPraticaRepository;
import br.org.cidadessustentaveis.repository.PlanejamentoIntegradoConsultaIndicadorRepository;
import br.org.cidadessustentaveis.repository.PlanejamentoIntegradoConsultaVariavelRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Service
public class PlanejamentoIntegradoConsultaService {
	
	@Autowired
	private PlanejamentoIntegradoConsultaBoaPraticaRepository consultaBoaPraticaRepository;
	
	@Autowired
	private PlanejamentoIntegradoConsultaVariavelRepository consultaVariavelRepository;
	
	@Autowired
	private PlanejamentoIntegradoConsultaIndicadorRepository consultaIndicadorRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	EntityManager em;
	
	public ConsultaBoaPratica buscarConsultaBoaPraticaPorId(Long id) {
		Optional<ConsultaBoaPratica> obj = consultaBoaPraticaRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Consulta Boa Prática não encontrada!"));
	}
	
	
	public ConsultaBoaPratica inserirConsultaBoaPratica(FiltroCidadesComBoasPraticas filtroCidadesComBoasPraticas) {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmail(user);


		ConsultaBoaPratica consultaBoaPratica = ConsultaBoaPratica.builder()
				.nome(filtroCidadesComBoasPraticas.getNomeConsulta())
				.idCidade(filtroCidadesComBoasPraticas.getIdCidade())
				.idEstado(filtroCidadesComBoasPraticas.getIdEstado())
				.idPais(filtroCidadesComBoasPraticas.getIdPais())
				.continente(filtroCidadesComBoasPraticas.getContinente())
				.idEixo(filtroCidadesComBoasPraticas.getIdEixo())
				.idOds(filtroCidadesComBoasPraticas.getIdOds())
				.idMetaOds(filtroCidadesComBoasPraticas.getIdMetaOds())
				.idIndicador(filtroCidadesComBoasPraticas.getIdIndicador())
				.popuMin(filtroCidadesComBoasPraticas.getPopuMin())
				.popuMax(filtroCidadesComBoasPraticas.getPopuMax())
				.visualizarComoPontos(filtroCidadesComBoasPraticas.isVisualizarComoPontos())
				.usuario(usuario)
				.build();

		
		consultaBoaPratica = consultaBoaPraticaRepository.save(consultaBoaPratica);

		return consultaBoaPratica;
	}
	
	
	public List<ConsultaBoaPratica> buscarConsultasBoaPratica() {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmail(user);

		return consultaBoaPraticaRepository.findByUsuarioId(usuario.getId());
	}
	
	
	public void deletarConsultaBoaPratica(Long id) {
		try {
			consultaBoaPraticaRepository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}	
	}
	
	public ConsultaVariavel buscarConsultaVariavelPorId(Long id) {
		Optional<ConsultaVariavel> obj = consultaVariavelRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Consulta Variável não encontrada!"));
	}
	
	
	public ConsultaVariavel inserirConsultaVariavel(FiltroVariaveisPorMunicipios filtroVariaveisPorMunicipios) {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmail(user);


		ConsultaVariavel consultaVariavel = ConsultaVariavel.builder()
				.nome(filtroVariaveisPorMunicipios.getNomeConsulta())
				.idVariavel(filtroVariaveisPorMunicipios.getIdVariavelSelecionada())
				.valorPreenchido(filtroVariaveisPorMunicipios.getValorPreenchido())
				.anoSelecionado(filtroVariaveisPorMunicipios.getAnoSelecionado())
				.visualizarComoPontos(filtroVariaveisPorMunicipios.isVisualizarComoPontos())
				.usuario(usuario)
				.build();

		
		consultaVariavel = consultaVariavelRepository.save(consultaVariavel);

		return consultaVariavel;
	}
	
	
	public List<ConsultaVariavel> buscarConsultasVariavel() {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmail(user);

		return consultaVariavelRepository.findByUsuarioId(usuario.getId());
	}
	
	
	public void deletarConsultaVariavel(Long id) {
		try {
			consultaVariavelRepository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}	
	}

	
	public ConsultaIndicador buscarConsultaIndicadorPorId(Long id) {
		Optional<ConsultaIndicador> obj = consultaIndicadorRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Consulta Indicador	 não encontrada!"));
	}
	
	public ConsultaIndicador inserirConsultaIndicador(FiltroIndicadoresPorMunicipios filtroIndicadoresPorMunicipios) {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmail(user);


		ConsultaIndicador consultaIndicador = ConsultaIndicador.builder()
				.nome(filtroIndicadoresPorMunicipios.getNomeConsulta())
				.idIndicador(filtroIndicadoresPorMunicipios.getIdIndicador())
				.idVariavel(filtroIndicadoresPorMunicipios.getIdVariavelSelecionada())
				.idEixo(filtroIndicadoresPorMunicipios.getIdEixo())
				.idOds(filtroIndicadoresPorMunicipios.getIdOds())
				.idCidade(filtroIndicadoresPorMunicipios.getIdCidade())
				.valorPreenchido(filtroIndicadoresPorMunicipios.getValorPreenchido())
				.anoSelecionado(filtroIndicadoresPorMunicipios.getAnoSelecionado())
				.popuMin(filtroIndicadoresPorMunicipios.getPopuMin())
				.popuMax(filtroIndicadoresPorMunicipios.getPopuMax())
				.visualizarComoPontos(filtroIndicadoresPorMunicipios.isVisualizarComoPontos())
				.usuario(usuario)
				.build();
		
		consultaIndicador = consultaIndicadorRepository.save(consultaIndicador);

		return consultaIndicador;
	}
	
	public List<ConsultaIndicador> buscarConsultasIndicador() {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmail(user);

		return consultaIndicadorRepository.findByUsuarioId(usuario.getId());
	}
	
	public void deletarConsultaIndicador(Long id) {
		try {
			consultaIndicadorRepository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}	
	}


}
