package br.org.cidadessustentaveis.services;

import java.math.BigInteger;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import br.org.cidadessustentaveis.dto.CidadeDTO;
import br.org.cidadessustentaveis.dto.PainelCidadeIndicadoresDTO;
import br.org.cidadessustentaveis.dto.VariacaoReferenciasDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.repository.CidadeRepository;
import br.org.cidadessustentaveis.repository.IndicadorPreenchidoRepository;
import br.org.cidadessustentaveis.repository.IndicadorRepository;
import br.org.cidadessustentaveis.util.VariavelPreenchidaUtil;

@Service
public class PainelIndicadorService {


	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private ProvinciaEstadoService estadoService;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private IndicadorService indicadorService;

	@Autowired
	private IndicadorPreenchidoService preenchidoService;

	@Autowired
	private IndicadorPreenchidoRepository preenchidoRepository;
	@Autowired
	private VariavelPreenchidaService variavelPreenchidaService;
	
	@Autowired
	private VariavelService variavelService;
	
	@Autowired
	private IndicadorRepository indicadorRepository;


	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<ProvinciaEstado> buscarEstadosSignatarias() {
		return cidadeRepository.findEstadosSignatarios();
	}

	public List<Cidade> buscarCidadesSignatarias(Long idEstado) {
		ProvinciaEstado estado = estadoService.buscarPorId(idEstado);
		return cidadeRepository.findCidadesSignataria(estado);
	}


	public List<CidadeDTO> buscarCidadesPorIndicador(Long idIndicador) {
		return cidadeRepository.findCidadesPorIndicador(idIndicador);
	}

	public List<Cidade> buscarCidadesPorIndicadorSemDTO(Long indicador) {
		return cidadeRepository.findCidadesPorIndicadorSemDTO(indicador);
	}

	public List<Cidade> buscarCidadesPorIndicadorEPorNome(Long indicador, String nome) {
		return cidadeRepository.findCidadesPorIndicadorEPorNome(indicador, nome);
	}

	public PainelCidadeIndicadoresDTO buscarIndicadoresPorCidade(Long idCidade) {
		Cidade cidade = cidadeService.buscarPorId(idCidade);
		PainelCidadeIndicadoresDTO dto = new PainelCidadeIndicadoresDTO(cidade, null, null);
		dto.setPossuiIndicadoresCadastrados(indicadorService.cidadePossuiIndicadoresCadastrados(idCidade));
		return dto;
	}
	
	public void linkarVariaveisPreenchidas(IndicadorPreenchido preenchido) {
		if(preenchido.getVariaveisPreenchidas() == null) {
			preenchido.setVariaveisPreenchidas(new ArrayList<>());
		}
		if(preenchido.getVariaveisPreenchidas().isEmpty()) {

			for(Variavel variavel : preenchido.getIndicador().getVariaveis()) {
				try {
					Optional<VariavelPreenchida> vp = variavelPreenchidaService.buscarVariavel(variavel, preenchido.getAno(), preenchido.getPrefeitura().getCidade());
					if(vp.isPresent()) {
						preenchido.getVariaveisPreenchidas().add(vp.get());
					}
				}catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
			if(preenchido.getVariaveisPreenchidas() != null) {
				//preenchidoService.salvar(preenchido);
			}
		}
	}


	public List<CidadeDTO> buscarCidadesPorNomeComIndicadoresPreenchidos(String nome) {
		List<CidadeDTO> lista = cidadeRepository.findCidadesPorNomeComIndicadoresPreenchidos(nome);
		return lista;
	}

	public List<Short> buscarAnosIndicadoresPorPrefeitura(Long idPrefeitura){
		List<Short> periodo = preenchidoRepository.buscarAnosIndicadoresPorPrefeitura(idPrefeitura); 
		return periodo;
	}
	
	public List<VariacaoReferenciasDTO> buscarReferenciasComVariacao(Long idIndicador){
		List<VariacaoReferenciasDTO> referenciasComVariacao = preenchidoRepository.buscarReferenciasComVariacao(idIndicador);
		return referenciasComVariacao;
	}
	
	public List<List<String>> buscarTabelaIndicadores(Long idEixo, Long idCidade,Short anoInicial, Short anoFinal) {
		List<Indicador> indicadores = indicadorService.buscarIndicadoresPCSporEixo(idEixo);
		List<List<String>> tabela = new ArrayList<>();
		for(Indicador indicador : indicadores) {
			String[] linhaS = new String[17];
			List<String> linha = Arrays.asList(linhaS);
			linha.set(0,indicador.getId()+"");
			linha.set(1,indicador.getNome());
			linha.set(12, indicador.getDescricao());
			linha.set(6,null); // Controla se vai carregar variável ou não no FRONTEND,			
			tabela.add(linha);
		}
		List<IndicadorPreenchido> indicadoresPreenchidos = preenchidoService.buscarPorCidadeEixoInicioFim(idCidade,idEixo,anoInicial,anoFinal);
		for(List<String> linha : tabela) {
			Long idIndicador = Long.parseLong(linha.get(0));
			
			for(Short ano = anoInicial; ano <= anoFinal; ano++) {
				for(int index = 0; index < indicadoresPreenchidos.size(); index++) {
					IndicadorPreenchido ip = indicadoresPreenchidos.get(index);
					if(ip.getIndicador().getId().equals(idIndicador)) {
						if(ip.getAno().equals(ano)) {
							int posicao = (int) (2+(ano - anoInicial));
							linha.set(posicao, ip.getResultadoApresentacao());
							int posicaoReferencia = (int) (7+(ano - anoInicial));
							linha.set(posicaoReferencia, ip.getReferencia());
							int posicaoParaReferencia = (int) (13+(ano - anoInicial));
							linha.set(posicaoParaReferencia, ip.getReferenciaDescritiva());
						}
					}
				}
			}
		}
		
		return tabela;
	}
	
	public List<List<String>> buscarTabelaIndicadoresDaCidade(Long idCidade,Short anoInicial, Short anoFinal) {
		List<Indicador> indicadores = indicadorService.buscarIndicadoresDaCidade(idCidade);
		List<List<String>> tabela = new ArrayList<>();
		for(Indicador indicador : indicadores) {
			String[] linhaS = new String[11];
			List<String> linha = Arrays.asList(linhaS);
			linha.set(0,indicador.getId()+"");
			linha.set(1,indicador.getNome());
			linha.set(6,null); // Controla se vai carregar variável ou não no FRONTEND,			
			tabela.add(linha);
		}
		List<IndicadorPreenchido> indicadoresPreenchidos = preenchidoService.buscarPorCidadeInicioFim(idCidade,anoInicial,anoFinal);
		for(List<String> linha : tabela) {
			Long idIndicador = Long.parseLong(linha.get(0));
			
			for(Short ano = anoInicial; ano <= anoFinal; ano++) {
				for(int index = 0; index < indicadoresPreenchidos.size(); index++) {
					IndicadorPreenchido ip = indicadoresPreenchidos.get(index);
					if(ip.getIndicador().getId().equals(idIndicador)) {
						if(ip.getAno().equals(ano)) {
							int posicao = (int) (2+(ano - anoInicial));
							linha.set(posicao, ip.getResultadoApresentacao());
							int posicaoReferencia = (int) (7+(ano - anoInicial));
							linha.set(posicaoReferencia, ip.getReferencia());
						}
					}
				}
			}
		}
		
		return tabela;
	}

	public List<List<String>> buscarTabelaVariaveis(Long idIndicador, Long idCidade, Short anoInicial, Short anoFinal, Long idSubdivisao) {
		Indicador indicador = indicadorService.listarById(idIndicador);
		List<List<String>> tabela = new ArrayList<>();
		for(Variavel variavel : indicador.getVariaveis()) {
			String[] linhaS = new String[6];
			List<String> linha = Arrays.asList(linhaS);
			linha.set(0,variavel.getId()+"");
			linha.set(1,variavel.getNome());			
			tabela.add(linha);
		}
		if (idSubdivisao == null) {
			List<VariavelPreenchida> vps = variavelPreenchidaService.buscarPorIndicadorCidadeAnoInicialEFinal(idIndicador, idCidade, anoInicial, anoFinal);
			for(List<String> linha : tabela) {
				Long idVariavel = Long.parseLong(linha.get(0));
				
				for(Short ano = anoInicial; ano <= anoFinal; ano++) {
					for(int index = 0; index < vps.size(); index++) {
						VariavelPreenchida vp = vps.get(index);
						if(vp.getVariavel().getId().equals(idVariavel)) {
							if(vp.getAno().equals(ano)) {
								int posicao = (int) (2+(ano - anoInicial));
								linha.set(posicao, VariavelPreenchidaUtil.valorApresentacao(vp));
								break;
							}
						}
					}
				}
			}
		} else {
			List<SubdivisaoVariavelPreenchida> vps = variavelPreenchidaService.buscarPorIndicadorCidadeAnoInicialEFinalSubdivisao(idIndicador, idCidade, anoInicial, anoFinal,idSubdivisao);
			for(List<String> linha : tabela) {
				Long idVariavel = Long.parseLong(linha.get(0));
				
				for(Short ano = anoInicial; ano <= anoFinal; ano++) {
					for(int index = 0; index < vps.size(); index++) {
						SubdivisaoVariavelPreenchida vp = vps.get(index);
						if(vp.getVariavel().getId().equals(idVariavel)) {
							if(vp.getAno().equals(ano)) {
								int posicao = (int) (2+(ano - anoInicial));
								linha.set(posicao, VariavelPreenchidaUtil.valorApresentacao(vp));
								break;
							}
						}
					}
				}
			}
		}
		
		return tabela;
	}
	
	public Long buscarIndicadoresPorNomeEstadoCidade(String siglaEstado, String nomeCidade) {
		String siglaEstadoFormatada = formatarNomeCidade(siglaEstado);
		String nomeCidadeFormatado = formatarNomeCidade(nomeCidade);
		Long idCidade = cidadeService.buscarIdPorNomeSigla(nomeCidadeFormatado, siglaEstadoFormatada);
		return idCidade;
	}
	
	public static String formatarNomeCidade(String str) {
	    return Normalizer.normalize(str.toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("-", "");
	}
	
	public List<Long> buscarIdsIndicadoresPorVariavel(Long idVariavel) {
		Variavel variavel = variavelService.buscarVariavelById(idVariavel);
		List<Long> ids = null;
		if(variavel != null) {
			ids = indicadorRepository.buscarIdsIndicadoresPorVariavel(variavel);
		}
		return ids;
	}
	
	public List<Long> buscarIdsIndicadoresPorNomeVariavel(String nome) {
		List<Long> ids  = variavelService.buscarVariavelById(nome);
		List<BigInteger> idsIndicadoresAux = null;
		List<Long> idsIndicadores = null;
		if(ids != null && !ids.isEmpty()) {
			idsIndicadoresAux = indicadorRepository.buscarIdsIndicadoresPorVariaveis(ids);
			idsIndicadores = idsIndicadoresAux.stream().map(s -> s.longValue()).collect(Collectors.toList());
		}

		return idsIndicadores;
	}
	


}
