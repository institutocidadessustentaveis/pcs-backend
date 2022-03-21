package br.org.cidadessustentaveis.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.contribuicoesAcademicas.GrupoAcademico;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class ImportacaoAcademicosService {


	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;
	@Autowired 
	private CidadeService cidadeService;	
	@Autowired 
	private GrupoAcademicoService grupoAcademicoService;
	@Autowired 
	private PaisService paisService;	

	public List<String> importar(MultipartFile arquivoOriginal) throws Exception {
		List<GrupoAcademico> listPreenchidas = new ArrayList<GrupoAcademico>();
		ArrayList<String> listaDados = new ArrayList<String>();
		StringBuilder errosBuilder = new StringBuilder();
		if(arquivoOriginal.getOriginalFilename().toLowerCase().endsWith("xlsx")) {
			listPreenchidas = importarXLSX(arquivoOriginal,errosBuilder);
		} else if( arquivoOriginal.getOriginalFilename().toLowerCase().endsWith("xls")){
			listPreenchidas = importarXLS(arquivoOriginal,errosBuilder);
		} else {
			throw new IOException("Arquivo inválido!");
		}

		Integer qtdPreenchida = listPreenchidas.toArray().length;
		listaDados.add(qtdPreenchida.toString());
		listaDados.add(errosBuilder.toString());
		
		return listaDados;
	}
	


	private List<GrupoAcademico> importarXLSX(MultipartFile arquivoOriginal, StringBuilder stringBuilder) throws Exception {
		List<GrupoAcademico> listPreenchidas = new ArrayList<GrupoAcademico>();
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
				if( rownum > 1) {
					GrupoAcademico grupoAcademico = geraGrupoAcademico(row,stringBuilder,idCidade);
					if(grupoAcademico != null) {
						listPreenchidas.add(grupoAcademico);
					}
				}
				rownum++;
			}
		}
		
		arquivoOriginal.getInputStream().close();	
		workbook.close();
		return listPreenchidas;
	}
	
	private List<GrupoAcademico> importarXLS(MultipartFile arquivoOriginal, StringBuilder stringBuilder) throws Exception {
		List<GrupoAcademico> listPreenchidas = new ArrayList<GrupoAcademico>();
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
				GrupoAcademico grupoAcademico = geraGrupoAcademico(row,stringBuilder,idCidade);
				if(grupoAcademico != null) {
					listPreenchidas.add(grupoAcademico);
				}
			}
			rownum++;
		}
		arquivoOriginal.getInputStream().close();	
		workbook.close();
		return listPreenchidas;
	}
	
	//Aqui recebe excel
	private GrupoAcademico geraGrupoAcademico(Row row, StringBuilder stringBuilder, Long idCidade) throws Exception {
		GrupoAcademico grupoAcademico = new GrupoAcademico();
		try {
			
			String tipoCadastro = getCellValue(row.getCell(0));
			String nomeGrupo = getCellValue(row.getCell(1)) +  " - " + getCellValue(row.getCell(2));
			String descricaoInstituicao = getCellValue(row.getCell(3));
			String experienciasDesenvolvidas = "<p><a href=\\\"" + getCellValue(row.getCell(4)) + "\\\" target=\\\"_blank\\\">" + getCellValue(row.getCell(5)) 
			+  "</a></p><p><a href=\\\"" + getCellValue(row.getCell(6))  + "\\\" target=\\\"_blank\\\">" + getCellValue(row.getCell(7)) +  "</a></p>"  ;
			String observacoes = getCellValue(row.getCell(8));
			String paginaOnline = getCellValue(row.getCell(9));
			String telefoneInstitucional = getCellValue(row.getCell(10));
			String emailInstitucional = getCellValue(row.getCell(11));
			String linkBaseDados = getCellValue(row.getCell(12));
			String continente = getCellValue(row.getCell(13));
			String pais = getCellValue(row.getCell(14));
			String estado = getCellValue(row.getCell(15));
			String codCidade = getCellValue(row.getCell(16));
			String cidade = getCellValue(row.getCell(17));
			String logradouro = getCellValue(row.getCell(18));
			String numero = getCellValue(row.getCell(19));
			String complemento = getCellValue(row.getCell(20));
			String latitude = getCellValue(row.getCell(21));
			String longitude = getCellValue(row.getCell(22));
			String tipoInstituicao = getCellValue(row.getCell(23));
		
			validarLinha(tipoCadastro, nomeGrupo);

			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
			if(usuario == null) {
				throw new Exception("Usuário não encontrado");
			}else {
				grupoAcademico.setUsuario(usuario);
				grupoAcademico.setDataCadastro(LocalDate.now());
			}
			
			// Inicia o objeto fonte
			try {
			    if(pais != null) {
					Optional<Pais> paisRef = paisService.buscarPorNomePais(pais.toString());
					if (paisRef == null || paisRef.isPresent()){
						grupoAcademico.setPais(paisRef.get());
					}
				}

				if(codCidade != null) {
					Long codigoLong = Long.valueOf(codCidade);
					Cidade cidadeRef = cidadeService.findByCodigoIbge(codigoLong);
					if(cidadeRef != null) {
						grupoAcademico.setEstado(cidadeRef.getProvinciaEstado());
						grupoAcademico.setPais(cidadeRef.getProvinciaEstado().getPais());
						grupoAcademico.setCidade(cidadeRef);
					}
				}
			}catch (Exception ex) {}

			

			
			grupoAcademico.setTipoCadastro(tipoCadastro);
			grupoAcademico.setNomeGrupo(nomeGrupo);
			grupoAcademico.setDescricaoInstituicao(descricaoInstituicao);
			grupoAcademico.setExperienciasDesenvolvidas(experienciasDesenvolvidas);
			grupoAcademico.setObservacoes(observacoes);
			grupoAcademico.setPaginaOnline(paginaOnline);
			grupoAcademico.setTelefoneInstitucional(telefoneInstitucional);
			grupoAcademico.setEmailInstitucional(emailInstitucional);
			grupoAcademico.setLinkBaseDados(linkBaseDados);
			grupoAcademico.setContinente(continente);
			grupoAcademico.setLogradouro(logradouro);
			grupoAcademico.setNumero(numero);
			grupoAcademico.setComplemento(complemento);
			
			if(latitude != null && longitude != null)
			try {
				Double latitudeParse = Double.parseDouble(latitude.replace(',', '.'));
				grupoAcademico.setLatitude(latitudeParse);
				Double longitudeParse = Double.parseDouble(longitude.replace(',', '.'));
				grupoAcademico.setLongitude(longitudeParse);
			}catch (Exception ex) {}
		
			grupoAcademico.setTipo(tipoInstituicao);
			
			grupoAcademicoService.inserirLinhaPlanilhaExcell(grupoAcademico);
			

		} catch (Exception e) {
			stringBuilder.append("Linha: "
					+(row.getRowNum()+1)+": "+e.getMessage()+"\n");
			return null;
		}


		return grupoAcademico;
	}
	
	private void validarLinha(String tipoCadastro, String nomeGrupo) throws Exception {
		String erro = "";
		if( tipoCadastro == null || tipoCadastro.isEmpty() ) {
			erro = erro+"Tipo do cadastro não informado, ";
		}

		if( nomeGrupo== null || nomeGrupo.isEmpty() ) {
			erro =  erro+"Nome do grupo não informado, ";
		}
		
		if(!erro.isEmpty()) {
			throw new Exception(erro);
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
			DataFormatter formatter = new DataFormatter();
			valor = formatter.formatCellValue(celula);     
			break;
		case BOOLEAN:
			valor =celula.getBooleanCellValue()+"";    
			break;
		default:
			break;
		}
		return valor;		
	}

	









	

}
