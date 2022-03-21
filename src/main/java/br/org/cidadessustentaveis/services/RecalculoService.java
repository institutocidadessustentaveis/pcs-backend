package br.org.cidadessustentaveis.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.repository.IndicadorPreenchidoRepository;
import br.org.cidadessustentaveis.repository.VariavelPreenchidaRepository;
import br.org.cidadessustentaveis.util.CalculadoraFormulaUtil;

@Deprecated
@Service
public class RecalculoService {
	@Autowired
	CidadeService cidadeService;
	@Autowired
	IndicadorService indicadorService;
	@Autowired
	PrefeituraService prefeituraService;
	@Autowired
	private VariavelPreenchidaRepository variavelPreenchidaRepository;
	@Autowired
	private CalculadoraFormulaUtil calculadora;
	@Autowired
	private IndicadorPreenchidoRepository indicadorPreenchidorepository;
	@Autowired
	private EntityManager em;

	@Transactional
	public void recalcular(Long idIndicador, int anoInicial, Prefeitura prefeitura) {
		if(idIndicador != null) {
			removerIndicadoresPreenchidos(idIndicador);
		}
	}

	@Transactional
	public List<Long> recalcularTudo(boolean calcularPcs, boolean calcularPrefeitura, boolean numerico, boolean texto ) {
		List<Long> listaIdIndicadores = new ArrayList<>();
		List<Long> indicadoresRecalculo = new ArrayList<>();
		if(calcularPcs && calcularPrefeitura) {
			listaIdIndicadores = indicadorService.listarApenasId();
		} else if( calcularPcs && ! calcularPrefeitura) {
			listaIdIndicadores = indicadorService.listarApenasIdPcs();
		} else if( !calcularPcs && calcularPrefeitura) {
			listaIdIndicadores = indicadorService.listarApenasIdPrefeituras();
		}
		for(Long id : listaIdIndicadores) {
			Indicador indicador = indicadorService.listarById(id);
			boolean ehNumerico = indicador.isNumerico(); 
			boolean add = false;
			if(numerico && ehNumerico) {
				indicadoresRecalculo.add(id);
				add= true;
			}
			if(texto && !ehNumerico && !add) {
				indicadoresRecalculo.add(id);
			}			
		}

		for(Long id : indicadoresRecalculo) {
			recalcular(id, 2005, null);
		}
		return indicadoresRecalculo;

	}
	@Async("threadRecalculo")
	@Transactional
	public void preencherNovosIndicadores(Long idIndicador) {
		
		List<Long> cidades = new ArrayList<>(); 
		System.out.println("Iniciei: "+ idIndicador);
		System.out.println("Executando: "+ idIndicador);
		cidades = cidadeService.buscarIdCidadesPreencheramUmaDasVariaveisDoIndicador(idIndicador); 
		Indicador indicador = indicadorService.listarById(idIndicador);
		for(Long idCidade: cidades) {
			Prefeitura prefeitura = prefeituraService.buscarAtualPorCidade(idCidade);
			List<Short> anos = variavelPreenchidaRepository.findAnoQueCidadeJaPreencheuUmaDasVariaveisDoIndicador(indicador.getId(), idCidade);
			if (anos != null) {
				for(Short ano: anos) {
					try {
						List<VariavelPreenchida> vps = variavelPreenchidaRepository.findByIndicadorIdCidadeidAno(idIndicador, idCidade, ano);
						int quantidadeVP = vps.size();
						int quantidadeV = indicador.getVariaveis().size();
						if( quantidadeV == quantidadeVP ) {
							IndicadorPreenchido ip = new IndicadorPreenchido();				
							ip.setAno(ano);
							ip.setVariaveisPreenchidas(vps);				
							ip.setPrefeitura(prefeitura);
							ip.setIndicador(indicador);
							ip.setDataPreenchimento(LocalDateTime.now());
							calculadora.calcularResultado(ip);
							indicadorPreenchidorepository.save(ip);
							vps = null;
							ip = null;
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		System.out.println("Terminei: "+ idIndicador);

	}

	private void removerIndicadoresPreenchidos(Long idIndicador) {
		Query q = em.createQuery("delete from IndicadorPreenchido where indicador.id = :id");
		q.setParameter("id" , idIndicador);
		q.executeUpdate();
	}
}
