package br.org.cidadessustentaveis.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.util.ProfileUtil;
import br.org.cidadessustentaveis.util.RestClient;

@Service
public class SeoService {
	@Autowired
	private ProfileUtil profileUtil;
	HashMap<String, LocalDateTime> paginas = new HashMap<>();

	public void gerarPagina(String pagina) throws IOException{
		String env = profileUtil.getProperty("spring.profiles.active");	
		boolean ativarFuncao = true;
		if(ativarFuncao) { //travando essa funcao
			boolean ehProducao = (env.equals("producao"));
			if(ehProducao) {
				String paginaDiretorio = pagina;
				if(pagina.contains("?")) {
					int index = pagina.indexOf("?");
					paginaDiretorio = pagina.substring(0, index);
				}
				boolean podeGerar = false;
				if(this.paginas.containsKey(paginaDiretorio)) {
					if(this.paginas.get(paginaDiretorio).isBefore(LocalDateTime.now().minusMinutes(30))) {
						podeGerar = true;
					}
				}else {
					podeGerar = true;
				}
				if(podeGerar) {
					File pasta = new File("pcs_static_files/");
					if(!pasta.exists()) {
						pasta.mkdirs();
					}

					if(paginaDiretorio != null && !paginaDiretorio.isEmpty()) {
						File arquivo = new File(pasta, paginaDiretorio+".html");
						if(!arquivo.exists()) {
							arquivo.getParentFile().mkdirs();
							arquivo.createNewFile();
						} else {
							long milis = arquivo.lastModified();
							LocalDateTime dataModificacao = new Timestamp(milis).toLocalDateTime();
							if(!dataModificacao.isBefore(LocalDateTime.now().minusMinutes(1))) {
								return;
							}
						}
						RestClient rest = new RestClient();
						String resposta = rest.get("http://34.73.202.122:3000/render/https://www.cidadessustentaveis.org.br"+pagina);
						
						int qtd = 0;
						while(resposta.contains("<style>") && resposta.contains("</style>") && qtd < 100) {
							try {
								int indiceInicio = resposta.indexOf("<style>");
								int indiceFim = resposta.indexOf("</style>")+8;
								if(indiceFim < indiceInicio) {
									int aux = indiceInicio;
									indiceInicio = indiceFim;
									indiceFim = aux;
								}
								StringBuilder builder = new StringBuilder(resposta);

								resposta = builder.delete(indiceInicio, indiceFim).toString();
								builder = null;
							}catch (Exception e) {
								e.printStackTrace();
								break;
							}
							qtd++;
						}
						qtd = 0;
						while(resposta.contains("<script") && resposta.contains("</script>")  && qtd < 100) {
							try {
								int indiceInicio = resposta.indexOf("<script");
								int indiceFim = resposta.indexOf("</script>")+9;

								if(indiceFim < indiceInicio) {
									int aux = indiceInicio;
									indiceInicio = indiceFim;
									indiceFim = aux;
								}
								StringBuilder builder = new StringBuilder(resposta);

								resposta = builder.delete(indiceInicio, indiceFim).toString();
								builder = null;
							} catch (Exception e) {
								e.printStackTrace();
								break;
							}
							qtd++;
						}
						qtd = 0;
						while(resposta.contains("data:image")  && qtd < 100) {
							try {
								int indiceInicio = resposta.indexOf("data:image");

								int indiceFim = resposta.substring(indiceInicio, resposta.length()).indexOf("\"") + 1 + indiceInicio;

								if(indiceFim < indiceInicio) {
									int aux = indiceInicio;
									indiceInicio = indiceFim;
									indiceFim = aux;
								}
								StringBuilder builder = new StringBuilder(resposta);

								resposta = builder.delete(indiceInicio, indiceFim).toString();
								builder = null;
							} catch (Exception e) {
								e.printStackTrace();
								break;
							}
							qtd++;
						}

						Path path = Paths.get(arquivo.getPath());
						Files.write(path,resposta.getBytes());
						
						resposta = null;

					}
					this.paginas.put(paginaDiretorio, LocalDateTime.now());
				}
			}
		}
	}

	public void limpar() {
		this.paginas = new HashMap<>();
	}
}
