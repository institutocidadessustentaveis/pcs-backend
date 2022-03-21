package br.org.cidadessustentaveis.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.org.cidadessustentaveis.dto.DadosDownloadDTO;
import br.org.cidadessustentaveis.dto.SubdivisaoDTO;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.InstanciaOrgao;
import br.org.cidadessustentaveis.model.administracao.InstituicaoFonte;
import br.org.cidadessustentaveis.model.administracao.Orgao;
import br.org.cidadessustentaveis.model.administracao.SubdivisaoCidade;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.VariaveisOpcoes;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.util.CalculadoraFormulaUtil;
import br.org.cidadessustentaveis.util.NumeroUtil;
import br.org.cidadessustentaveis.util.SenhaUtil;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class ImportacaoVariaveisService {

	@Autowired 
	private VariavelService variavelService;
	@Autowired 
	private VariavelPreenchidaService variavelPreenchidaService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private IndicadorPreenchidoService indicadorPreenchidoService;
	@Autowired
	private InstituicaoFonteService serviceFonte;
	@Autowired
	private InstanciaOrgaoService instanciaOrgaoService;
	@Autowired
	private OrgaoService serviceOrgao;
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;
	@Autowired
	private SubdivisaoService subdivisaoService;

	public List<String> importar(MultipartFile arquivoOriginal) throws Exception {
		List<VariavelPreenchida> listPreenchidas = new ArrayList<VariavelPreenchida>();
		List<SubdivisaoVariavelPreenchida> listPreenchidasSubdivisao = new ArrayList<SubdivisaoVariavelPreenchida>();
		ArrayList<String> listaDados = new ArrayList<String>();
		StringBuilder errosBuilder = new StringBuilder();
		if(arquivoOriginal.getOriginalFilename().toLowerCase().endsWith("xlsx")) {
			listPreenchidas = importarXLSX(arquivoOriginal,errosBuilder);
			listPreenchidasSubdivisao = importarXLSXSubdivisao(arquivoOriginal,errosBuilder);
		} else if( arquivoOriginal.getOriginalFilename().toLowerCase().endsWith("xls")){
			listPreenchidas = importarXLS(arquivoOriginal,errosBuilder);
		} else {
			throw new IOException("Arquivo inválido!");
		}
		variavelPreenchidaService.inserir(listPreenchidas);
		variavelPreenchidaService.inserirComSubdivisao(listPreenchidasSubdivisao);
		
		Integer qtdPreenchida = listPreenchidas.toArray().length;
		Integer qtdPreenchidaSubdivisao = listPreenchidasSubdivisao.toArray().length;
		
		listaDados.add(qtdPreenchida.toString());
		listaDados.add(qtdPreenchidaSubdivisao.toString());
		listaDados.add(errosBuilder.toString());
		
		preencherIndicadores(listPreenchidas,errosBuilder);
		preencherIndicadoresSubdivisao(listPreenchidasSubdivisao,errosBuilder);
		
		return listaDados;
	}
	
	private void preencherIndicadoresSubdivisao(List<SubdivisaoVariavelPreenchida> listPreenchidasSubdivisao,
			StringBuilder errosBuilder) {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		Cidade cidade = usuario.getPrefeitura().getCidade();
		
		for(SubdivisaoVariavelPreenchida subdivisaoVP: listPreenchidasSubdivisao) {
			
			try {
				indicadorPreenchidoService.recalcularIndicadoresPorVariavelCidadeAnoSubdivisao(subdivisaoVP.getVariavel(), 
						cidade, subdivisaoVP.getAno(), subdivisaoVP.getSubdivisao().getId());
			} catch (Exception e) {
				errosBuilder.append("Não foi possível preencher os indicadores da variável "+subdivisaoVP.getVariavel().getNome());
				e.printStackTrace();
			}
		}
		
		
		
	}

	private void preencherIndicadores(List<VariavelPreenchida> listPreenchidas, StringBuilder errosBuilder) throws Exception {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		if(usuario == null) {
			throw new Exception("Usuário não encontrado");
		}
		Cidade cidade = usuario.getPrefeitura().getCidade();
		
		for(VariavelPreenchida vp: listPreenchidas) {
			
			try {
				indicadorPreenchidoService.recalcularIndicadoresPorVariavelCidadeAno(vp.getVariavel(), cidade, vp.getAno());
			} catch (Exception e) {
				errosBuilder.append("Não foi possível preencher os indicadores da variável "+vp.getVariavel().getNome());
				e.printStackTrace();
			}
		}
		
		
	}

	private List<VariavelPreenchida> importarXLSX(MultipartFile arquivoOriginal, StringBuilder stringBuilder) throws Exception {
		List<VariavelPreenchida> listPreenchidas = new ArrayList<VariavelPreenchida>();
		XSSFWorkbook workbook = new XSSFWorkbook (arquivoOriginal.getInputStream());
		XSSFSheet sheet = workbook.getSheetAt(0);
		Usuario usuario = usuarioContextUtil.getUsuario();
		Long idCidade = null;
		if(null != usuario && null != usuario.getPrefeitura()) {
			idCidade = usuario.getPrefeitura().getCidade().getId();
		}else {
			idCidade = Long.parseLong("0");
		}
		
		if(sheet != null) {
			Iterator<Row> rowIterator = sheet.iterator();
			int rownum = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if( rownum != 0) {
					VariavelPreenchida variavelPreenchida = geraVariavelPreenchida(row,stringBuilder,idCidade);
					if(variavelPreenchida != null) {
						listPreenchidas.add(variavelPreenchida);
					}
				}
				rownum++;
			}
		}
		
		arquivoOriginal.getInputStream().close();	
		workbook.close();
		return listPreenchidas;
	}
	
	private List<SubdivisaoVariavelPreenchida> importarXLSXSubdivisao(MultipartFile arquivoOriginal, StringBuilder stringBuilder) throws Exception {
		List<SubdivisaoVariavelPreenchida> listPreenchidas = new ArrayList<SubdivisaoVariavelPreenchida>();
 		XSSFWorkbook workbook = new XSSFWorkbook (arquivoOriginal.getInputStream());
 		Usuario usuario = usuarioContextUtil.getUsuario();
		Long idCidade = null;
		
		if(null != usuario && null != usuario.getPrefeitura()) {
			idCidade = usuario.getPrefeitura().getCidade().getId();
		}else {
			idCidade = Long.parseLong("0");
		}
		for(int i = 0; i < workbook.getNumberOfSheets() ; i ++) {
			XSSFSheet sheetSub = workbook.getSheetAt(i);
			String sheetName = sheetSub.getSheetName();
			SubdivisaoCidade subdivisao = subdivisaoService.findBySheetName(sheetName);
			if(subdivisao != null) {
				System.out.println(subdivisao.getNome());
				Iterator<Row> rowIterator = sheetSub.iterator();
				int rownum = 0;
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if( rownum != 0) {
						SubdivisaoVariavelPreenchida variavelPreenchida;
						variavelPreenchida = geraVariavelPreenchidaSubdivisao(row, stringBuilder, idCidade, subdivisao, usuario);
						if(variavelPreenchida != null) {
							listPreenchidas.add(variavelPreenchida);
						}
					}
					rownum++;
				}
			}
		}
		
		
		arquivoOriginal.getInputStream().close();	
		workbook.close();
		return listPreenchidas;
	}

	private List<VariavelPreenchida> importarXLS(MultipartFile arquivoOriginal, StringBuilder stringBuilder) throws Exception {
		List<VariavelPreenchida> listPreenchidas = new ArrayList<VariavelPreenchida>();
		HSSFWorkbook workbook = new HSSFWorkbook(arquivoOriginal.getInputStream());
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		Usuario usuario = usuarioContextUtil.getUsuario();
		Long idCidade = null;
		if(null != usuario && null != usuario.getPrefeitura()) {
			idCidade = usuario.getPrefeitura().getCidade().getId();
		}else {
			idCidade = Long.parseLong("0");
		}
		
		int rownum = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if( rownum != 0) {
				VariavelPreenchida variavelPreenchida = geraVariavelPreenchida(row,stringBuilder,idCidade);
				if(variavelPreenchida != null) {
					listPreenchidas.add(variavelPreenchida);
				}
			}
			rownum++;
		}
		arquivoOriginal.getInputStream().close();	
		workbook.close();
		return listPreenchidas;
	}
	
	//Aqui recebe excel
	private VariavelPreenchida geraVariavelPreenchida(Row row, StringBuilder stringBuilder, Long idCidade) throws Exception {
		VariavelPreenchida variavelPreenchida = null;
		try {
			
			String id = getCellValue(row.getCell(0));
			String nome = getCellValue(row.getCell(1));
			String ano = getCellValue(row.getCell(2));
			String valor = getCellValue(row.getCell(3));
			String fonte = getCellValue(row.getCell(4));
			String nomeOutraFonte = getCellValue(row.getCell(5));
			String nomeInstanciaOrgao = getCellValue(row.getCell(6));
			String nomeOrgaoFederal = getCellValue(row.getCell(7));
			String nomeOrgaoEstadual = getCellValue(row.getCell(8));
			String nomeOrgaoMunicipal = getCellValue(row.getCell(9));
			String observacao = getCellValue(row.getCell(10));
			
			validarLinha(id, nome, ano, valor, fonte, nomeOutraFonte, nomeInstanciaOrgao, nomeOrgaoFederal, nomeOrgaoEstadual, nomeOrgaoMunicipal);

			Variavel variavel =  variavelService.listarById(NumeroUtil.toLong(id));
			if(!variavel.getNome().equals(nome)) {
				throw new Exception("Variável não identificada");
			}
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
			if(usuario == null) {
				throw new Exception("Usuário não encontrado");
			}
			
			Optional<VariavelPreenchida> optional = variavelPreenchidaService.buscarVariavel(variavel, NumeroUtil.toShort(ano),usuario.getPrefeitura());
			variavelPreenchida = optional.orElse(null);
			if(variavelPreenchida == null) {
				variavelPreenchida = new VariavelPreenchida();
				variavelPreenchida.setVariavel(variavel);
				variavelPreenchida.setAno(NumeroUtil.toShort(ano));
				variavelPreenchida.setPrefeitura(usuario.getPrefeitura());
			}
			variavelPreenchida.setDataPreenchimento(new Date());
			
			// Inicia o objeto fonte
			InstituicaoFonte instituicaofonte = null;
			
			// Cria um novo Orgao
			Orgao orgao = null;
			
			if ( fonte.equals("Outra fonte")) {
				instituicaofonte = serviceFonte.getByNomeAndCidadeId(nomeOutraFonte, idCidade);
				if(null == instituicaofonte) {
					instituicaofonte = new InstituicaoFonte();
					instituicaofonte.setNome(nomeOutraFonte);
					instituicaofonte.setCidade(usuario.getPrefeitura().getCidade());
					
					
					switch (nomeInstanciaOrgao) {
						case "Outro Órgão Federal":
							orgao = serviceOrgao.buscarPorNome(nomeOrgaoFederal, nomeInstanciaOrgao);
							if(orgao == null) {
								orgao = serviceOrgao.buscarPorId(68l);
							}						
							break;
						case "Outro Órgão Estadual":
							orgao = serviceOrgao.buscarPorNome(nomeOrgaoEstadual, nomeInstanciaOrgao);
							if(orgao == null) {
								orgao = serviceOrgao.buscarPorId(69l);
							}
							break;
						case "Outro Órgão Municipal":
							orgao = serviceOrgao.buscarPorNome(nomeOrgaoMunicipal, nomeInstanciaOrgao);						
							if(orgao == null) {
								orgao = serviceOrgao.buscarPorId(70l); 
							}
							break;
						case "Ministério Federal":
							orgao = serviceOrgao.buscarPorId(Long.parseLong("68"));						
							break;
						case "Secretaria Estadual":
							orgao = serviceOrgao.buscarPorId(Long.parseLong("69"));						
							break;
						case "Secretaria Municipal":
							orgao = serviceOrgao.buscarPorId(Long.parseLong("70"));						
							break;
	
						default:
							throw new Exception("Nome da Instancia do orgão não foi informado");
					}
					
					instituicaofonte.setOrgao(orgao);
					serviceFonte.inserirInstituicaoFonte(instituicaofonte);
				}
				variavelPreenchida.setInstituicaoFonte(instituicaofonte);
			} else {
				instituicaofonte = serviceFonte.getByNomeAndCidadeId(fonte, idCidade);
				variavelPreenchida.setInstituicaoFonte(instituicaofonte);
			}
			
			variavelPreenchida.setObservacao(observacao);
			atribuirValor(variavelPreenchida,valor);	
		} catch (Exception e) {
			stringBuilder.append("Aba: Variáveis, Linha: "
					+(row.getRowNum()+1)+": "+e.getMessage()+"\n");
			return null;
		}


		return variavelPreenchida;
	}
	
	private SubdivisaoVariavelPreenchida geraVariavelPreenchidaSubdivisao(Row row, StringBuilder stringBuilder, Long idCidade, SubdivisaoCidade subdivisao, Usuario usuario) throws Exception {
		SubdivisaoVariavelPreenchida subdivisaoVP = null;
		try {
			
			String id = getCellValue(row.getCell(0));
			String nome = getCellValue(row.getCell(1));
			String ano = getCellValue(row.getCell(2));
			String valor = getCellValue(row.getCell(3));
			String fonte = getCellValue(row.getCell(4));
			String nomeOutraFonte = getCellValue(row.getCell(5));
			String nomeInstanciaOrgao = getCellValue(row.getCell(6));
			String nomeOrgaoFederal = getCellValue(row.getCell(7));
			String nomeOrgaoEstadual = getCellValue(row.getCell(8));
			String nomeOrgaoMunicipal = getCellValue(row.getCell(9));
			String observacao = getCellValue(row.getCell(10));
			
			validarLinha(id, nome, ano, valor, fonte, nomeOutraFonte, nomeInstanciaOrgao, nomeOrgaoFederal, nomeOrgaoEstadual, nomeOrgaoMunicipal);

			Variavel variavel =  variavelService.listarById(NumeroUtil.toLong(id));
			if(!variavel.getNome().equals(nome)) {
				throw new Exception("Variável não identificada");
			}
			
			if(usuario == null) {
				throw new Exception("Usuário não encontrado");
			}
			
			
			subdivisaoVP= variavelPreenchidaService.buscaSubdivisaoVariavelPreenchidaPorAnoVariavelSubdivisao(NumeroUtil.toLong(ano), variavel.getId(), subdivisao.getId(), idCidade);
			if(subdivisaoVP == null) {
				subdivisaoVP = new SubdivisaoVariavelPreenchida();
				subdivisaoVP.setVariavel(variavel);
				subdivisaoVP.setAno(NumeroUtil.toShort(ano));
				subdivisaoVP.setPrefeitura(usuario.getPrefeitura());
				subdivisaoVP.setSubdivisao(subdivisao);
			}
			subdivisaoVP.setDataPreenchimento(new Date());
			
			// Inicia o objeto fonte
			InstituicaoFonte instituicaofonte = null;
			
			// Cria um novo Orgao
			Orgao orgao = null;
			
			if ( fonte.equals("Outra fonte")) {
				instituicaofonte = serviceFonte.getByNomeAndCidadeId(nomeOutraFonte, idCidade);
				if(null == instituicaofonte) {
					instituicaofonte = new InstituicaoFonte();
					instituicaofonte.setNome(nomeOutraFonte);
					instituicaofonte.setCidade(usuario.getPrefeitura().getCidade());
					
					
					switch (nomeInstanciaOrgao) {
						case "Outro Órgão Federal":
							orgao = serviceOrgao.buscarPorNome(nomeOrgaoFederal, nomeInstanciaOrgao);
							if(orgao == null) {
								orgao = serviceOrgao.buscarPorId(68l);
							}						
							break;
						case "Outro Órgão Estadual":
							orgao = serviceOrgao.buscarPorNome(nomeOrgaoEstadual, nomeInstanciaOrgao);
							if(orgao == null) {
								orgao = serviceOrgao.buscarPorId(69l);
							}
							break;
						case "Outro Órgão Municipal":
							orgao = serviceOrgao.buscarPorNome(nomeOrgaoMunicipal, nomeInstanciaOrgao);						
							if(orgao == null) {
								orgao = serviceOrgao.buscarPorId(70l); 
							}
							break;
						case "Ministério Federal":
							orgao = serviceOrgao.buscarPorId(Long.parseLong("68"));						
							break;
						case "Secretaria Estadual":
							orgao = serviceOrgao.buscarPorId(Long.parseLong("69"));						
							break;
						case "Secretaria Municipal":
							orgao = serviceOrgao.buscarPorId(Long.parseLong("70"));						
							break;
	
						default:
							throw new Exception("Nome da Instancia do orgão não foi informado");
					}
					
					instituicaofonte.setOrgao(orgao);
					serviceFonte.inserirInstituicaoFonte(instituicaofonte);
				}
				subdivisaoVP.setInstituicaoFonte(instituicaofonte);
			} else {
				instituicaofonte = serviceFonte.getByNomeAndCidadeId(fonte, idCidade);
				subdivisaoVP.setInstituicaoFonte(instituicaofonte);
			}
			
			subdivisaoVP.setObservacao(observacao);
			atribuirValor(subdivisaoVP,valor);	
		} catch (Exception e) {
			stringBuilder.append("Aba: "+subdivisao.getTipoSubdivisao().getNome()+" - "+subdivisao.getNome()
				+", Linha: "+(row.getRowNum()+1)+": "+e.getMessage()+"\n");
			return null;
		}

		return subdivisaoVP;
	}

	private void validarLinha(String id, String nome, String ano, String valor, String fonte, String nomeOutraFonte, String nomeInstanciaOrgao, String nomeOrgaoFederal, String nomeOrgaoEstadual, String nomeOrgaoMunicipal ) throws Exception {
		String erro = "";
		if( id == null || id.isEmpty() ) {
			erro = erro+"ID da variável não informado, ";
		}

		if( nome== null || nome.isEmpty() ) {
			erro =  erro+"Nome da variável não informada, ";
		}

		if( ano== null || ano.isEmpty() ) {
			erro =  erro+"Ano de preenchimento não informado, ";
		}

		if( valor== null || valor.isEmpty() ) {
			erro =  erro+"Valor não informado, ";
		}

		if( fonte== null || fonte.isEmpty() ) {
			erro =  erro+"Fonte não encontrada";
		} else {
			if ( fonte.equals("Outra fonte")) {
				if (nomeOutraFonte == null || nomeOutraFonte.isEmpty()) {
					erro =  erro+"Você informou 'Outra Fonte' é necessário informá-la na coluna 'Nome da fonte de dados'";
				}
				if (nomeInstanciaOrgao == null || nomeInstanciaOrgao.isEmpty()) {
					erro =  erro+"Você informou 'Outra Fonte' é necessário selecionar o Nome da Instancia do Orgão na respectiva coluna";
				} else {
				
					if (nomeInstanciaOrgao.equals("Outro Orgão Federal")) {
						if (nomeOrgaoFederal == null || nomeOrgaoFederal.isEmpty()) {
							erro =  erro+"Você informou 'Outro Orgão Federal' é necessário selecionar o Nome do Orgão Federal na respectiva coluna";
						}
					} else if (nomeInstanciaOrgao.equals("Outro Orgão Estadual")) {
						if (nomeOrgaoEstadual == null || nomeOrgaoEstadual.isEmpty()) {
							erro =  erro+"Você informou 'Outro Orgão Estadual' é necessário selecionar o Nome do Orgão Estadual na respectiva coluna";
						}
					} else {
						if (nomeOrgaoMunicipal == null || nomeOrgaoMunicipal.isEmpty()) {
							erro =  erro+"Você informou 'Outro Orgão Municipal' é necessário selecionar o Nome do Orgão Municipal na respectiva coluna";
						}
					}
				
				}
			}
		}
		
		if(!erro.isEmpty()) {
			throw new Exception(erro);
		}
	}

	private void atribuirValor(VariavelPreenchida variavelPreenchida,String valor) throws Exception {
		if(valor.isEmpty()) {
			throw new Exception("Variável "+variavelPreenchida.getVariavel().getId()+" não Preenchida!");
		}
		switch (variavelPreenchida.getVariavel().getTipo()) {
		case "Numérico inteiro":
			variavelPreenchida.setValor(NumeroUtil.toDouble(valor));
			break;

		case "Numérico decimal":
			variavelPreenchida.setValor(NumeroUtil.toDouble(valor));
			break;

		case "Tipo sim/não":
			setRespostaSimNao(variavelPreenchida, valor);
			break;

		case "Tipo sim/não com lista de opções":
			setRespostaSimNaoOpcao(variavelPreenchida, valor);
			break;

		case "Tipo lista de opções":
			setRespostaOpcao(variavelPreenchida, valor);
			break;

		case "Texto livre":
			variavelPreenchida.setValorTexto(valor);
			variavelPreenchida.setStatus("Aguardando Avaliação");
			break;

		default:
			throw new Exception("Tipo de variável não encontrado");
		}
	}
	

	private void atribuirValor(SubdivisaoVariavelPreenchida variavelPreenchida,String valor) throws Exception {
		if(valor.isEmpty()) {
			throw new Exception("Variável "+variavelPreenchida.getVariavel().getId()+" não Preenchida!");
		}
		switch (variavelPreenchida.getVariavel().getTipo()) {
		case "Numérico inteiro":
			variavelPreenchida.setValor(NumeroUtil.toDouble(valor));
			break;

		case "Numérico decimal":
			variavelPreenchida.setValor(NumeroUtil.toDouble(valor));
			break;

		case "Tipo sim/não":
			setRespostaSimNao(variavelPreenchida, valor);
			break;

		case "Tipo sim/não com lista de opções":
			setRespostaSimNaoOpcao(variavelPreenchida, valor);
			break;

		case "Tipo lista de opções":
			setRespostaOpcao(variavelPreenchida, valor);
			break;

		case "Texto livre":
			variavelPreenchida.setValorTexto(valor);
			variavelPreenchida.setStatus("Aguardando Avaliação");
			break;

		default:
			throw new Exception("Tipo de variável não encontrado");
		}
	}

	private void setRespostaSimNao(VariavelPreenchida variavelPreenchida, String valor) throws Exception {
		switch (valor) {
			case "Sim":
				variavelPreenchida.setRespostaSimples(true);
				break;
			case "Não":
				variavelPreenchida.setRespostaSimples(false);
				break;
	
			default:
				throw new Exception("Valor da variável "+variavelPreenchida.getVariavel().getId()+" está inválido!");
		}		
	}
	
	private void setRespostaSimNao(SubdivisaoVariavelPreenchida variavelPreenchida, String valor) throws Exception {
		switch (valor) {
			case "Sim":
				variavelPreenchida.setRespostaSimples(true);
				break;
			case "Não":
				variavelPreenchida.setRespostaSimples(false);
				break;
	
			default:
				throw new Exception("Valor da variável "+variavelPreenchida.getVariavel().getId()+" está inválido!");
		}		
	}


	private void setRespostaSimNaoOpcao(VariavelPreenchida variavelPreenchida, String valor) throws Exception {
		String simNaoValor = valor.substring(0, 3);
		Boolean simNao = null;
		switch (simNaoValor) {
			case "Sim":
				simNao = true;
				break;
			case "Não":
				simNao = false;
				break;
			default:
				throw new Exception("Valor da variável "+variavelPreenchida.getVariavel().getId()+" está inválido!");
		}
		
		String descricao = valor.substring(3, valor.length());
		if(simNao) {
			variavelPreenchida.setRespostaSimples(true);
		}else{
			variavelPreenchida.setRespostaSimples(false);
		}
		
		if(!descricao.isEmpty()) {
			descricao = descricao.substring(2, descricao.length());
			VariaveisOpcoes opcaoEscolhida = null;
			for(VariaveisOpcoes opcao : variavelPreenchida.getVariavel().getVariavelResposta().getListaOpcoes() ) {
				if(simNao) {
					if(opcao.getTipo().equals("Sim") && opcao.getDescricao().equals(descricao)) {
						opcaoEscolhida = opcao;
						break;
					}				
				}else {
					if(opcao.getTipo().equals("Nao") && opcao.getDescricao().equals(descricao)) {
						opcaoEscolhida = opcao;
						break;
					}					
				}
			}
			if(opcaoEscolhida == null) {
				throw new Exception("Opção: "+valor+", da variável "+variavelPreenchida.getVariavel().getId()+" não encontrada!");
			}
			if(variavelPreenchida.getVariavel().isMultiplaSelecao()) {
				variavelPreenchida.setOpcoes(new ArrayList<>());
				variavelPreenchida.getOpcoes().add(opcaoEscolhida);
			}else {
				variavelPreenchida.setOpcao(opcaoEscolhida);
			}
		}		
		
	}
	private void setRespostaSimNaoOpcao(SubdivisaoVariavelPreenchida variavelPreenchida, String valor) throws Exception {
		String simNaoValor = valor.substring(0, 3);
		Boolean simNao = null;
		switch (simNaoValor) {
			case "Sim":
				simNao = true;
				break;
			case "Não":
				simNao = false;
				break;
			default:
				throw new Exception("Valor da variável "+variavelPreenchida.getVariavel().getId()+" está inválido!");
		}
		
		String descricao = valor.substring(3, valor.length());
		if(simNao) {
			variavelPreenchida.setRespostaSimples(true);
		}else{
			variavelPreenchida.setRespostaSimples(false);
		}
		
		if(!descricao.isEmpty()) {
			descricao = descricao.substring(2, descricao.length());
			VariaveisOpcoes opcaoEscolhida = null;
			for(VariaveisOpcoes opcao : variavelPreenchida.getVariavel().getVariavelResposta().getListaOpcoes() ) {
				if(simNao) {
					if(opcao.getTipo().equals("Sim") && opcao.getDescricao().equals(descricao)) {
						opcaoEscolhida = opcao;
						break;
					}				
				}else {
					if(opcao.getTipo().equals("Nao") && opcao.getDescricao().equals(descricao)) {
						opcaoEscolhida = opcao;
						break;
					}					
				}
			}
			if(opcaoEscolhida == null) {
				throw new Exception("Opção: "+valor+", da variável "+variavelPreenchida.getVariavel().getId()+" não encontrada!");
			}
			if(variavelPreenchida.getVariavel().isMultiplaSelecao()) {
				variavelPreenchida.setOpcoes(new ArrayList<>());
				variavelPreenchida.getOpcoes().add(opcaoEscolhida);
			}else {
				variavelPreenchida.setOpcao(opcaoEscolhida);
			}
		}		
		
	}

	private void setRespostaOpcao(VariavelPreenchida variavelPreenchida, String valor) throws Exception {
		VariaveisOpcoes opcaoEscolhida = null;
		for(VariaveisOpcoes opcao : variavelPreenchida.getVariavel().getVariavelResposta().getListaOpcoes() ) {
			if(opcao.getTipo().equals("Opcao") && opcao.getDescricao().equals(valor)) {
				opcaoEscolhida = opcao;
				break;
			}				
		}
		if(opcaoEscolhida == null) {
			throw new Exception("Opção: "+valor+", da variável "+variavelPreenchida.getVariavel().getId()+" não encontrada!");
		}
		if(variavelPreenchida.getVariavel().isMultiplaSelecao()) {
			variavelPreenchida.setOpcoes(new ArrayList<>());
			variavelPreenchida.getOpcoes().add(opcaoEscolhida);
		}else {
			variavelPreenchida.setOpcao(opcaoEscolhida);
		}
	}
	
	private void setRespostaOpcao(SubdivisaoVariavelPreenchida variavelPreenchida, String valor) throws Exception {
		VariaveisOpcoes opcaoEscolhida = null;
		for(VariaveisOpcoes opcao : variavelPreenchida.getVariavel().getVariavelResposta().getListaOpcoes() ) {
			if(opcao.getTipo().equals("Opcao") && opcao.getDescricao().equals(valor)) {
				opcaoEscolhida = opcao;
				break;
			}				
		}
		if(opcaoEscolhida == null) {
			throw new Exception("Opção: "+valor+", da variável "+variavelPreenchida.getVariavel().getId()+" não encontrada!");
		}
		if(variavelPreenchida.getVariavel().isMultiplaSelecao()) {
			variavelPreenchida.setOpcoes(new ArrayList<>());
			variavelPreenchida.getOpcoes().add(opcaoEscolhida);
		}else {
			variavelPreenchida.setOpcao(opcaoEscolhida);
		}
	}

	private String getCellValue(Cell celula) {
		String valor = "";
		if(celula == null) {
			return valor;
		}
		switch (celula.getCellType()) {
		case STRING:
			valor =celula.getStringCellValue();    
			break;
		case NUMERIC:
			valor =celula.getNumericCellValue()+"";    
			break;
		case BOOLEAN:
			valor =celula.getBooleanCellValue()+"";    
			break;
		default:
			break;
		}
		return valor;		
	}

	public File gerarArquivo() throws IOException, AuthenticationException {
		Usuario usuario = null;
		try {
			usuario = usuarioContextUtil.getUsuario() ;
		} catch (Exception e) {
			throw new AuthenticationException("Você precisa estar Logado");
		}
		
		Long idPrefeitura = null;
		if(usuario != null) {
			idPrefeitura = usuario.getPrefeitura().getId();
		}
		
		List<Variavel> listVar = new ArrayList<Variavel>();
		listVar = variavelService.listarApenasPCSEPrefeituraPorId(idPrefeitura);

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet abaVariaveis = workbook.createSheet("Variáveis");
		XSSFSheet abaOpcoes = workbook.createSheet("Opções");
		abaVariaveis = gerarCabecalho(abaVariaveis);
		int rownum = 0;				

		DataValidationHelper dvHelper = abaVariaveis.getDataValidationHelper();
		DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("Categories");
		CellRangeAddressList instanciafonteList = new CellRangeAddressList(1, listVar.size(), 4, 4);
		
		
		int i = 0;
		
		Long idCidade = null;
		if(null != usuario && null != usuario.getPrefeitura()) {
			idCidade = usuario.getPrefeitura().getCidade().getId();
		}else {
			idCidade = Long.parseLong("0");
		}
		List<InstituicaoFonte> listaInstituicaoFonte = serviceFonte.buscarComboBoxInstituicaoFonte(idCidade);
		
		String[] comboInstituicaoFonte = new String[listaInstituicaoFonte.size()];
		
		for(InstituicaoFonte instituicaofonte: listaInstituicaoFonte) {
			comboInstituicaoFonte[i] = instituicaofonte.getNome();
			i++;
		}
		
		
		// Instancia Orgao
		CellRangeAddressList instanciaOrgaoList = new CellRangeAddressList(1, 20, 6, 6);
		
		List<InstanciaOrgao> listainstanciaOrgao = instanciaOrgaoService.buscarComboBoxInstanciaOrgao();

		String[] comboInstanciaOrgao = new String[listainstanciaOrgao.size()];
		
		
		i=0;
		for(InstanciaOrgao instanciaOrgao: listainstanciaOrgao) {
		
			comboInstanciaOrgao[i] = instanciaOrgao.getNome();
			i++;
		} 
		
		// Outro Orgao Federal
		CellRangeAddressList orgaoFederalList = new CellRangeAddressList(1, listVar.size(), 7, 7);
		
		
		List<Orgao> listaOrgaoFederal = serviceOrgao.buscarComboBoxOrgao(4L);

		String[] comboOrgaoFederal = new String[listaOrgaoFederal.size()];
				
		i=0;
		for(Orgao nomeOrgaoFederal: listaOrgaoFederal) {
			comboOrgaoFederal[i] = nomeOrgaoFederal.getNome();
			i++;
		}
		
		// Outro Orgao Estadual
		CellRangeAddressList orgaoEstadualList = new CellRangeAddressList(1, listVar.size(), 8, 8);
		
		
		List<Orgao> listaOrgaoEstadual = serviceOrgao.buscarComboBoxOrgao(5L);

		String[] comboOrgaoEstadual = new String[listaOrgaoEstadual.size()];
		
		i=0;
		for(Orgao nomeOrgaoEstadual: listaOrgaoEstadual) {
			comboOrgaoEstadual[i] = nomeOrgaoEstadual.getNome();
			i++;
		}
		
		
		// Outro Orgao Municipal
		CellRangeAddressList orgaoMunicipalList = new CellRangeAddressList(1, listVar.size(), 9, 9);
		
		List<Orgao> listaOrgaoMunicipal = serviceOrgao.buscarComboBoxOrgao(6L);

		String[] comboOrgaoMunicipal = new String[listaOrgaoMunicipal.size()];
		
		i=0;
		for(Orgao nomeOrgaoMunicipal: listaOrgaoMunicipal) {
			comboOrgaoMunicipal[i] = nomeOrgaoMunicipal.getNome();
			i++;
		}
		

		
		for (Variavel variavel : listVar) {

			Row row = abaVariaveis.createRow(++rownum);
			//CellStyle cellStyleLocked = workbook.createCellStyle();
			//cellStyleLocked.setLocked(true);

			CellStyle cellStyleFree = workbook.createCellStyle();
			cellStyleFree.setLocked(false);

			Cell cellID = row.createCell(0);
			cellID.setCellValue(variavel.getId());
			cellID.setCellStyle(cellStyleFree);

			Cell cellNome = row.createCell(1);
			cellNome.setCellValue(variavel.getNome());
			cellNome.setCellStyle(cellStyleFree);


			Cell cellAno = row.createCell(2);
			cellAno.setCellStyle(cellStyleFree);

			Cell cellValor = row.createCell(3);
			cellValor.setCellStyle(cellStyleFree);

			switch (variavel.getTipo()) {
			case "Tipo sim/não":
				celulaSimNao(abaVariaveis, rownum);
				break;
			case "Tipo sim/não com lista de opções":
				String[] respostas =  respostasSimNaoComOpcao(variavel);
				celulaSimNaoComOpcao(abaVariaveis, rownum, respostas, abaOpcoes);		
				break;
			case "Tipo lista de opções":
				String[] opcoes = listaOpcoes(variavel);
				if (opcoes.length >= 1) {
					celulaListaOpcao(abaVariaveis, rownum, opcoes,abaOpcoes, 1);
				}
				
				break;
			case "Numérico inteiro":
				break;
			case "Numérico decimal":
				break;
			}         

			 
			Cell cellFonte = row.createCell(4);
			cellFonte.setCellStyle(cellStyleFree);
			
			Cell cellNomeOutraFonte = row.createCell(5);
			cellNomeOutraFonte.setCellStyle(cellStyleFree);
			
			Cell cellNomeInstanciaOrgao = row.createCell(6);
			cellNomeInstanciaOrgao.setCellStyle(cellStyleFree);
			
			Cell cellNomeOrgaoFederal = row.createCell(7);
			cellNomeOrgaoFederal.setCellStyle(cellStyleFree);

			Cell cellNomeOrgaoEstadual = row.createCell(8);
			cellNomeOrgaoEstadual.setCellStyle(cellStyleFree);

			Cell cellNomeOrgaoMunicipal = row.createCell(9);
			cellNomeOrgaoMunicipal.setCellStyle(cellStyleFree);

			Cell cellObservacao = row.createCell(10);
			cellObservacao.setCellStyle(cellStyleFree);

			Cell cellEixo = row.createCell(11);            
			cellEixo.setCellStyle(cellStyleFree);
			cellEixo.setCellValue(getEixos(variavel));
		}
		
		celulaListaOpcao(abaVariaveis, rownum, comboInstituicaoFonte,abaOpcoes, 4);
		
		celulaListaOpcao(abaVariaveis, rownum, comboInstanciaOrgao,abaOpcoes, 6);

		celulaListaOpcao(abaVariaveis, rownum, comboOrgaoFederal,abaOpcoes, 7);
		
		celulaListaOpcao(abaVariaveis, rownum, comboOrgaoEstadual,abaOpcoes, 8);

		celulaListaOpcao(abaVariaveis, rownum, comboOrgaoMunicipal,abaOpcoes, 9);
		
		workbook.setSheetHidden(1, true);;
		//abaOpcoes.protectSheet(SenhaUtil.criptografarSHA2(SenhaUtil.gerarSenha(20)));
		//abaVariaveis.protectSheet(SenhaUtil.criptografarSHA2(SenhaUtil.gerarSenha(20)));
		abaVariaveis.autoSizeColumn(0);
		abaVariaveis.autoSizeColumn(1);
		abaVariaveis.autoSizeColumn(2);
		abaVariaveis.autoSizeColumn(3);
		abaVariaveis.setColumnWidth(3,10000);
		abaVariaveis.autoSizeColumn(4);
		abaVariaveis.setColumnWidth(4,15000);
		abaVariaveis.autoSizeColumn(5);
		abaVariaveis.setColumnWidth(5,15000);
		abaVariaveis.autoSizeColumn(6);
		abaVariaveis.setColumnWidth(6,15000);
		abaVariaveis.autoSizeColumn(7);
		abaVariaveis.setColumnWidth(7,15000);
		abaVariaveis.autoSizeColumn(8);
		abaVariaveis.setColumnWidth(8,15000);
		abaVariaveis.autoSizeColumn(9);
		abaVariaveis.setColumnWidth(9,15000);
		abaVariaveis.autoSizeColumn(10);
		abaVariaveis.setColumnWidth(10,15000);
		abaVariaveis.autoSizeColumn(11);
		abaVariaveis.setColumnWidth(11,15000);

		

		this.gerarAbasParaSubdivisoes(workbook,usuario);
		File arquivo = new File("arquivo_"+LocalDateTime.now().getNano()+".xlsx");
		FileOutputStream out = 
				new FileOutputStream(arquivo);
		workbook.write(out);
		workbook.close();
		out.close();

		return arquivo;
	}


	private void gerarAbasParaSubdivisoes(XSSFWorkbook workbook, Usuario usuario) {
		// TODO Auto-generated method stub
		try {
			List<SubdivisaoDTO> listaSubdivisao = this.subdivisaoService.buscarTodosPorCidadeId(usuario.getPrefeitura().getCidade().getId());
			if( listaSubdivisao != null) {
				listaSubdivisao.forEach(subdivisao ->{
					workbook.cloneSheet(0, subdivisao.getTipoSubdivisao().getNome()+" - "+subdivisao.getNome());
				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private XSSFSheet gerarCabecalho(XSSFSheet aba) {
		Row row = aba.createRow(0);
		XSSFCellStyle style = aba.getWorkbook().createCellStyle();
		XSSFFont font = aba.getWorkbook().createFont();
		font.setBold(true);
		style.setFont(font);
		style.setLocked(true);

		Cell cellId = row.createCell(0);
		cellId.setCellStyle(style);
		cellId.setCellValue("ID");

		Cell cellNome = row.createCell(1);
		cellNome.setCellStyle(style);
		cellNome.setCellValue("Nome");

		Cell cellData = row.createCell(2);
		cellData.setCellStyle(style);
		cellData.setCellValue("Ano de Referência");

		Cell cellValor = row.createCell(3);
		cellValor.setCellStyle(style);
		cellValor.setCellValue("Valor");

		Cell cellFonte = row.createCell(4);
		cellFonte.setCellStyle(style);
		cellFonte.setCellValue("Fonte");
		
		Cell cellNomeOutraFonte = row.createCell(5);
		cellNomeOutraFonte.setCellStyle(style);
		cellNomeOutraFonte.setCellValue("Nome da Fonte de dados");
		
		Cell cellNomeInstanciaOrgao = row.createCell(6);
		cellNomeInstanciaOrgao.setCellStyle(style);
		cellNomeInstanciaOrgao.setCellValue("Nome da Instancia do orgão");	
		
		Cell cellNomeOrgaoFederal = row.createCell(7);
		cellNomeOrgaoFederal.setCellStyle(style);
		cellNomeOrgaoFederal.setCellValue("Nome do Orgão Federal");	

		Cell cellNomeOrgaoEstadual = row.createCell(8);
		cellNomeOrgaoEstadual.setCellStyle(style);
		cellNomeOrgaoEstadual.setCellValue("Nome do Orgão Estadual");	

		Cell cellNomeOrgaoMunicipal = row.createCell(9);
		cellNomeOrgaoMunicipal.setCellStyle(style);
		cellNomeOrgaoMunicipal.setCellValue("Nome do Orgão Municipal");	

		Cell cellObs = row.createCell(10);
		cellObs.setCellStyle(style);
		cellObs.setCellValue("Observação");

		Cell cellEixos = row.createCell(11);
		cellEixos.setCellStyle(style);
		cellEixos.setCellValue("Eixos");
		return aba;
	}

	private void celulaSimNao(XSSFSheet abaVariaveis, int rownum) {
		DataValidationHelper validationHelper = new XSSFDataValidationHelper(abaVariaveis);
		CellRangeAddressList addressList = new  CellRangeAddressList(rownum,rownum,3,3);
		DataValidationConstraint constraint =validationHelper.createExplicitListConstraint(new String[]{"","Sim", "Não"});
		DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);
		dataValidation.setSuppressDropDownArrow(true);   
		dataValidation.setShowErrorBox(true);
		abaVariaveis.addValidationData(dataValidation);
	}

	private String[] respostasSimNaoComOpcao(Variavel var) {
		List<String> respostas = new ArrayList<String>();

		if(var.getVariavelResposta().isExibirOpcaoSim()) {
			for(VariaveisOpcoes opcao : var.getVariavelResposta().getListaOpcoes()) {
				if(opcao.getTipo().equals("Sim")) {
					respostas.add("Sim- "+opcao.getDescricao());
				}
			}
		}else {
			respostas.add("Sim");
		}

		if(var.getVariavelResposta().isExibirOpcaoNao()) {
			for(VariaveisOpcoes opcao : var.getVariavelResposta().getListaOpcoes()) {
				if(opcao.getTipo().equals("Nao")) {
					respostas.add("Não- "+opcao.getDescricao());
				}
			}
		}else {
			respostas.add("Não");
		}
		return respostas.toArray(new String[respostas.size()]);
	}	

	private void celulaSimNaoComOpcao(XSSFSheet abaVariaveis, int rownum, String[] respostas, XSSFSheet abaOpcoes) {

		Integer initRow = abaOpcoes.getLastRowNum()+2;
		Integer lastRow = abaOpcoes.getLastRowNum()+2;
		lastRow = criarOpcoes(respostas,abaOpcoes,lastRow);
		
		DataValidationHelper validationHelper = new XSSFDataValidationHelper(abaVariaveis);
		CellRangeAddressList addressList = new  CellRangeAddressList(rownum,rownum,3,3);
		DataValidationConstraint constraint =validationHelper.createFormulaListConstraint("=Opções!$A$"+initRow+":$A$"+lastRow);
		DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);
		dataValidation.setShowErrorBox(true);
		dataValidation.setSuppressDropDownArrow(true);   
		abaVariaveis.addValidationData(dataValidation);
	}

	private String[] listaOpcoes(Variavel var) {
		List<String> respostas = new ArrayList<String>();
		for(VariaveisOpcoes opcao : var.getVariavelResposta().getListaOpcoes()) {
			respostas.add(opcao.getDescricao());
		}
		return respostas.toArray(new String[respostas.size()]);
	}

	private void celulaListaOpcao(XSSFSheet abaVariaveis, int rownum, String[] opcoes, XSSFSheet abaOpcoes, int coluna) {
		Integer initRow = abaOpcoes.getLastRowNum()+2;
		Integer lastRow = abaOpcoes.getLastRowNum()+2;
		lastRow = criarOpcoes(opcoes,abaOpcoes,lastRow);
		
		DataValidationHelper validationHelper = new XSSFDataValidationHelper(abaVariaveis);
		
		CellRangeAddressList addressList = new CellRangeAddressList(1, rownum, coluna, coluna);
		
		DataValidationConstraint constraint =validationHelper.createFormulaListConstraint("=Opções!$A$"+initRow+":$A$"+lastRow);
		DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);
		dataValidation.setShowErrorBox(true);
		dataValidation.setSuppressDropDownArrow(true);   
		abaVariaveis.addValidationData(dataValidation);
	}

	private Integer criarOpcoes(String[] opcoes, XSSFSheet abaOpcoes, Integer lastRow) {
		
		for(String opcao :opcoes) {			
			Row row = abaOpcoes.createRow(lastRow);
			Cell celulaOpcao = row.createCell(0);
			celulaOpcao.setCellValue(opcao);
			lastRow++;
		}
		return lastRow;
		
	}

	private String getEixos(Variavel variavel) {
		List<Eixo> eixos = variavelService.eixosPorVariavel(variavel);
		String eixosString = "";
		if(eixos != null && !eixos.isEmpty()) {
			for(Eixo eixo : eixos) {
				if(eixosString.isEmpty()) {
					eixosString = eixo.getNome();
				} else {
					eixosString = eixosString+", "+eixo.getNome();
				}
			}
		}
		return eixosString;
	}


}
