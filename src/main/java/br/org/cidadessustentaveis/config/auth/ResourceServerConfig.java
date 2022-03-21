package br.org.cidadessustentaveis.config.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import br.org.cidadessustentaveis.util.ProfileUtil;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	@Autowired
	private ProfileUtil profileUtil;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/stomp/**").permitAll()
		.antMatchers("/seo/**").permitAll()
		.antMatchers("/iacit/monitoringinfo/**", "/iacit/apimonitor/**").permitAll()
		.antMatchers("/dados-abertos/**").permitAll()
		.antMatchers(HttpMethod.GET, "/linksRodape/cache").permitAll()
		.antMatchers(HttpMethod.GET, "/contatoPcs/cache").permitAll()
		.antMatchers(HttpMethod.GET, "/redeSocialRodape/cache").permitAll()
		.antMatchers(HttpMethod.GET, "/institucional/criarPaginasMenu").permitAll()
		.antMatchers(HttpMethod.GET, "/planejamentoIntegrado/buscarConsultasIndicador").permitAll()
		.antMatchers(HttpMethod.GET, "/planejamentoIntegrado/buscarConsultasVariavel").permitAll()
		.antMatchers(HttpMethod.GET, "/shapefile/buscarShapesListagemMapa",
				"/shapefile/downloadAtributos/**",
				"/shapefile/downloadShapeFile/**",
				"/shapefile/filtrar").permitAll()
		.antMatchers(HttpMethod.POST, "/shapefile/exportarShapeFileCidades",
										"/shapefile/exportarShapeFile").permitAll()
		.antMatchers(HttpMethod.GET, "/variavel/buscarVariaveisPcsParaCombo").permitAll()
		.antMatchers(HttpMethod.GET, "/variavelPreenchida/carregarComboAnosPreenchidos").permitAll()
		.antMatchers(HttpMethod.GET, "/variavelPreenchida/carregarComboAnosPreenchidosPorVariavel").permitAll()
		.antMatchers(HttpMethod.GET, "/shapefile/buscarComPaginacao").permitAll()
		.antMatchers(HttpMethod.GET, "/shapefile/buscarShapesListagemMapa").permitAll()
		.antMatchers(HttpMethod.GET, "/shapeitens/buscarFeaturesPorShapeId/**").permitAll()
		.antMatchers(HttpMethod.GET, "/shapefile/buscarFileNameShapeFilePorId/**").permitAll()
		.antMatchers(HttpMethod.GET, "/shapefile/buscarOdsDoShapeFileId/**").permitAll()
		.antMatchers(HttpMethod.GET, "/shapefile/buscarShapeFileVisualizarDetalheDTOPorIdShapeFile/**").permitAll()
		.antMatchers(HttpMethod.GET, "/shapefile/buscarShapeFilePorId/**").permitAll()
		.antMatchers(HttpMethod.GET, "/arquivo/imagem/**").permitAll()
		.antMatchers(HttpMethod.GET, "/integracao/**").permitAll()
		.antMatchers(HttpMethod.GET, "/mapaTematico/**").permitAll()
		.antMatchers(HttpMethod.GET, "/planodemetas/buscarPorCidade/**").permitAll()
		.antMatchers(HttpMethod.GET, "/planodemetas/download/**").permitAll()
		.antMatchers(HttpMethod.GET, "/planoDeMetasDetalhadoHistorico/buscarHistoricoAnos/**").permitAll()
		.antMatchers("/actuator/**",
				"/apimonitor**",
				"/configuration/ui",
				"/swagger-ui.html",
				"/webjars/**", 
				"/v2/**", 
				"/swagger-resources/**", 
				"/configuration/security",
				"/swagger",
				"/swagger-ui/**").permitAll()
		.antMatchers("/monitor**").permitAll()
		.antMatchers(HttpMethod.POST, "/formulario-atendimento/salvar").permitAll()
		.antMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
		.antMatchers(HttpMethod.GET, "/boapratica/**", 
				"/institucional/**", 
				"/indicador/preenchidos/**", 
				"/painel/**",
				"/noticia/**",
				"/ods/**", 
				"/indicador/**",
				"/indicador/cidade/**",
				"/cidade/**",
				"/updateUsuario/atualizarUsuarios",
				"/provinciaEstado/**",
				"/buscar/**",
				"/menuitem/**").permitAll()
		.antMatchers("/ods/buscarOdsParaPaginaInstitucional" ).permitAll()
		.antMatchers(HttpMethod.POST, "/newsletter").permitAll()
		.antMatchers(HttpMethod.POST, "/newsletter/imagem/**").permitAll()
		.antMatchers(HttpMethod.GET, "/newsletter/imagem/**").permitAll()
		.antMatchers(HttpMethod.PUT, "/variavel/alterarId").permitAll()
		.antMatchers("/downloadsexportacoes/registraLogDownloadsExportacoes").permitAll()
		.antMatchers(HttpMethod.GET, "/shapefile/**.png").permitAll()
		.antMatchers(HttpMethod.GET, "/shapefile/**.geojson").permitAll()
		.antMatchers(HttpMethod.GET, "/shapefile/**.geotiff").permitAll()
		.antMatchers(HttpMethod.GET, "/shapefile/**.shp").permitAll()
		.antMatchers(HttpMethod.GET, "/materialapoio/imagem/**",
				"/materialapoio/buscarMaterialDeApoioPorId/**" ,
				"/materialapoio/download/**" ).permitAll()
		.antMatchers(HttpMethod.GET, "/biblioteca/imagem/**").permitAll()
		.antMatchers(HttpMethod.GET, "/biblioteca/imagemHome").permitAll()
		.antMatchers(HttpMethod.GET, "/biblioteca/download/**").permitAll()
		.antMatchers(HttpMethod.GET, "/biblioteca/idBibliotecasOrdenadas").permitAll()
		.antMatchers(HttpMethod.GET, "/biblioteca/buscarBibliotecasFiltrado").permitAll()
		.antMatchers(HttpMethod.GET, "/biblioteca/carregarCombosBiblioteca").permitAll()
		.antMatchers(HttpMethod.GET, "/biblioteca/buscarBibliotecaPorId/**").permitAll()
		.antMatchers(HttpMethod.GET, "/biblioteca/buscarBibliotecaSimples/**").permitAll()
		.antMatchers(HttpMethod.GET, "/biblioteca/buscarBibliotecaDetalhesPorId/**").permitAll()
		.antMatchers(HttpMethod.GET, "/biblioteca/buscarBibliotecaPlanoLeisRegulamentacaoFiltrida").permitAll()
		.antMatchers(HttpMethod.GET, "/evento/buscarEventosToListByIdCidade/**",
				"/evento/buscarEventoPorId/**",
				"/evento/buscarEventosFiltrados", 
				"/evento/buscarEventosNaoPrefeitura").permitAll()
		.antMatchers("/comentario/buscarComentariosToListPublica").permitAll()
		.antMatchers(HttpMethod.GET, "/materialinstitucional/buscarPorPublicacao/**",
				"/materialinstitucional/download/**").permitAll()
		.antMatchers(HttpMethod.GET, "/publicacao**").permitAll()
		.antMatchers(HttpMethod.GET, "/publicacao/imagem/**").permitAll()
		.antMatchers(HttpMethod.GET, "/materialapoio/buscarMaterialDeApoioPorId/**" ).permitAll()
		.antMatchers("/boapratica/buscarBoasPraticasFiltradasPaginaInicial").permitAll()
		.antMatchers("/partidoPolitico",
				"/usuarios/buscar", 
				"/build/backend",
				"/build/frontend",
				"/dbchangelog/versaoAtual", 
				"/areaInteresse/" ,
				"/usuarios/inserircidadao",
				"/usuarios/inserirResponsavel",
				"/instituicoes/" ,
				"/areaAtuacao/" ,
				"/areaInteresse",
				"/cidade/cidadesSignatarias",
				"/cidade/porEstado/**",
				"/cidade/porEstadoItemCombo/**",
				"/cidade/porEstadoPCSCombo/**",
				"/pais/buscarPaisesPorContinente/**",
				"/pais/buscarPaisesCombo",
				"/eixo/buscarEixosCombo",				
				"/provinciaEstado/buscarTodos",
				"/provinciaEstado/buscarTodosBrasil",
				"/partidoPolitico", 
				"/prefeitura/",
				"/perfis/buscarPerfilGestaoPublica",
				"/cidade/editarCordenadas",
				"/emailToken/buscarPorHash",
				"/usuarios/esquecisenha",
				"/usuarios/criarNovaSenha",
				"/usuarios/cadastrarSenha",
				"/cidade/buscarCidadesSignatarias",
				"/indicadordacidade**",
				"/indicador/simples/**",
				"/indicador/corrigirFormula",
				"/indicador/buscarIndicadoresPcs",
				"/indicador/filtro/pcs**",
				"/indicador/preenchidos/cidades",
				"/indicador/preenchidos/tabela/indicadores",
				"/indicador/preenchidos/mapa/indicadores",
				"/indicador/preenchidos/grafico/indicadores",
				"/indicador/preenchidos/recalcular",
				"/indicador/preenchidos/calcularResultadoApresentacao",
				"/recalculo/recalcular",
				"/variavel/pcsSimples/",
				"/variavel/alterarId",
				"/visualizarindicador/indicador/**",
				"/configuration/ui",
				"/painel/estados",
				"/prefeitura/inativarPrefeituras",
				"/prefeitura/buscarPrefeituraEdicao",
				"/prefeitura/atualizarCartaCompromisso",
				"/prefeitura/downloadCartaCompromisso/**",//
				"/prefeitura/buscarPrefeiturasSignatariasVigentes",
				"/prefeitura/buscarPrefeiturasSignatariasVigentesPorEstado",
				"/prefeitura/buscarPrefeiturasSignatariasVigentesPorEstadoCidadePartido",
				"/prefeitura/buscarPrefeiturasSignatariasVigentesPorEstadoCidadePartidoPopulacao",
				"/cidade/signatarias/porcentagensPorEstado",
				"/cidade/signatarias/quantidadePorEstado",
				"/cidade/buscarPorSiglaCidade",
				"/shape/estado/buscarListaShapesPorEstados",				
				"/boapratica/removerEstilo",			
				"/boapratica/buscarCidadesComBoasPraticas",
				"/boapratica/buscarCombosCidadesComBoasPraticas",
				"/boapratica/buscarBoasPraticasFiltradas",
				"/boapratica/buscarOdsDaBoaPratica",
				"/boapratica/buscarCidadesComBoasPraticas",
				"/boapratica/buscarCidadesComBoasPraticasFiltradas",
				"/noticia/titulo/**",
				"/noticia/id/**",
				"/noticia/idPublicada/**",
				"/noticia/countNoticias",
				"/noticia/ultimasNoticias/**",
				"/noticia/ultimasNoticiasAgenda/**",
				"/noticia/buscarNoticiasFiltradas",
				"/noticia/buscarNoticiaUsandoDataInicioFimPalavraChave",
				"/noticia/imagem",
				"/noticia/url",
				"/noticia/urlPublicada",
				"/variavelPreenchida/serieHistorica/**",
				"/variavelPreenchida/variavel**",
				"/variavelPreenchida/excluirDuplicadas",
				"/variavelPreenchida/buscarObservacaoVariavel",
				"/variavelPreenchida/buscarCidadesComVariavelPreenchida",
				"/variavel/pcsSimples",
                "/ods/imagem/**",
				"/institucional/imagem/**",
				"/home/imagem/**",
				"/imagens/**",
				"/boapratica/imagem/**",
				"/checkOnline/**",
				"/pagina-inicial/**",
				"/cidade/signatarias/buscarParaCombo",
				"/partidoPolitico/buscaritemcombo",
				"/cidade/atualizarPlanoDeMetas",
				"/eixo/imagem/**",
				"/eixo/buscarEixosDto",
				"/home/imagem/**",
				"/home/buscarIdsPaginaHomePorLink/**",
				"/home/buscarHomeBarraPorId/**",
				"/home/buscarPrimeiraSecaoPorId/**",
				"/home/buscarSegundaSecaoPorId/**",
				"/home/buscarTerceiraSecaoPorId/**",
				"/home/buscarQuartaSecaoPorId/**",
				"/home/buscarQuintaSecaoPorId/**",
				"/home/buscarSecaoLateralPorId/**",
				"/home/buscarSextaSecaoPorId/**",
				"/home/buscarSetimaSecaoPorId/**",
				"/home/buscarPaginaHomePorId/**",
				"/home/buscarListaImagensGaleriaPorId/**",
				"/home/buscarTodasSemConteudoPorIdHome/**",
				"/home/buscarListaPrimeiraSecaoResumidaPorId/**",
				"/home/buscarPrimeiraSecaoDetalhe/**",
				"/home/buscarListaSegundaSecaoResumidaPorId/**",
				"/home/buscarSegundaSecaoDetalhe/**",
				"/home/buscarListaTerceiraSecaoResumidaPorId/**",
				"/home/buscarTerceiraSecaoDetalhe/**",
				"/home/buscarListaQuartaSecaoResumidaPorId/**",
				"/home/buscarQuartaSecaoDetalhe/**",
				"/home/buscarListaQuintaSecaoResumidaPorId/**",
				"/home/buscarQuintaSecaoDetalhe/**",
				"/home/buscarListaSecaoLateralResumidaPorId/**",
				"/home/buscarSecaoLateralDetalhe/**",
				"/home/buscarListaSextaSecaoResumidaPorId/**",
				"/home/buscarSextaSecaoDetalhe/**",
				"/home/buscarListaSetimaSecaoResumidaPorId/**",
				"/home/buscarSetimaSecaoDetalhe/**",
				"/institucionalDinamico/imagem/**",
				"/institucionalDinamico/publicacao/**",
				"/institucionalDinamico/existeInstitucionalDinamicoComLink/**",
				"/temaforum/buscarListaTemaForum",
				"/forum-discussao/buscarDiscussoesFiltradas/**",
				"/forum-discussao/buscarDiscussaoPorId/**",
				"/forum-discussao/atualizarVisualizacao/**",
				"/visualizacaoCartografica/**",
				"/cidadesSignatarias/**",
				"/comentarioDiscussao/buscarComentariosDiscussaoPorIdDiscussao/**",
				"/ajuste-geral/buscarAjustePorLocalApp/**").permitAll()
		.antMatchers(HttpMethod.GET, "/formulario/link").permitAll()
		.antMatchers(HttpMethod.GET, "/formulario/buscarFormulariosResumido/**").permitAll()
		.antMatchers(HttpMethod.POST, "/formulario-preenchido").permitAll()
		.antMatchers(HttpMethod.POST, "/dadosDownload/cadastrar").permitAll()
		.antMatchers(HttpMethod.POST, "/dadosDownload/buscarFiltro").permitAll()
		.antMatchers(HttpMethod.POST, "/formulario-atendimento/salvar").permitAll()
		.antMatchers(HttpMethod.GET, "/dadosDownload/buscarComboBoxAcao").permitAll()
		.antMatchers(HttpMethod.GET, "/dadosDownload/buscarComboBoxPagina").permitAll()
		.antMatchers(HttpMethod.GET, "/dadosDownload/buscarComboBoxCidade").permitAll()
		.antMatchers(HttpMethod.GET, "/dadosDownload/buscarComPaginacao").permitAll()
		.antMatchers(HttpMethod.GET, "/dadosDownload/buscarComPaginacao").permitAll()
		.antMatchers(HttpMethod.GET, "/tema-geoespacial/listar").permitAll()
		.antMatchers(HttpMethod.GET, "/institucionalDinamico/buscarInstitucionalDinamicoPorLink/**").permitAll()
		.antMatchers(HttpMethod.GET, "/institucionalDinamico/buscarIdsInstitucionalDinamicoPorLink/**").permitAll()
		.antMatchers(HttpMethod.GET, "/institucionalDinamico/buscarInstitucionalDinamicoSecao01PorId/**").permitAll()
		.antMatchers(HttpMethod.GET, "/institucionalDinamico/buscarInstitucionalDinamicoSecao01Detalhe/**").permitAll()
		.antMatchers(HttpMethod.GET, "/institucionalDinamico/buscarInstitucionalDinamicoSecao02PorId/**").permitAll()
		.antMatchers(HttpMethod.GET, "/institucionalDinamico/buscarInstitucionalDinamicoSecao02Detalhe/**").permitAll()
		.antMatchers(HttpMethod.GET, "/institucionalDinamico/buscarInstitucionalDinamicoSecao03PorId/**").permitAll()
		.antMatchers(HttpMethod.GET, "/institucionalDinamico/buscarInstitucionalDinamicoSecao03Detalhe/**").permitAll()
		.antMatchers(HttpMethod.GET, "/institucionalDinamico/buscarInstitucionalDinamicoSecao04PorId/**").permitAll()
		.antMatchers(HttpMethod.GET, "/institucionalDinamico/buscarInstitucionalDinamicoSecao04Detalhe/**").permitAll()
		.antMatchers(HttpMethod.GET, "/prefeitura/buscarCidadesSignatariasDataMandatos").permitAll()
		.antMatchers(HttpMethod.GET, "/prefeitura/buscarCidadesSignatariasDataMandatosPorIdCidade").permitAll()
		.antMatchers(HttpMethod.GET, "/prefeitura/buscarPrefeiturasSignatariasComBoasPraticas").permitAll()
		.antMatchers("/subdivisao/arvore/**",
					"/tipoSubdivisao/cidade/**",
					"/subdivisao/feature/**",
					"/subdivisao/**/**/**").permitAll()
		.anyRequest().authenticated()
		.and().cors().configurationSource(corsConfigurationSource());
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList(/*profileUtil.getProperty("profile.frontend"),*/ "*"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;		
	}
	
}
