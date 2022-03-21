package br.org.cidadessustentaveis.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.PropostaMunicipioDTO;
import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import br.org.cidadessustentaveis.model.participacaoCidada.PropostaMunicipio;
import br.org.cidadessustentaveis.repository.PropostaMunicipioRepository;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class PropostaMunicipioService {

    @Autowired
    private UsuarioContextUtil usuarioContextUtil;
    @Autowired
    private PropostaMunicipioRepository repository;
    @Autowired
    private PrefeituraService prefeituraService;
    @Autowired
    private AlertaService alertaService;

	public void salvar(PropostaMunicipioDTO dto) throws Exception {
        Usuario usuario = usuarioContextUtil.getUsuario();
        if(usuario != null) {
            PropostaMunicipio proposta = new PropostaMunicipio();
            Prefeitura prefeitura =  prefeituraService.buscarAtualPorCidade(dto.getCidade());

            proposta.setDataEnvio(LocalDateTime.now());
            proposta.setDescricao(dto.getDescricao());
            proposta.setPrefeitura(prefeitura);
            proposta.setUsuario(usuario);

            repository.save(proposta);

            alertaService.salvar(Alerta.builder()
					.mensagem(proposta.getDescricao())
                        .tipoAlerta(TipoAlerta.PROPOSTA_MUNICIPIO)
                        .link(null)
						.data(LocalDateTime.now())
						.cidade(proposta.getPrefeitura().getCidade())
						.build());
            System.out.println("salvou a proposta" );
        }
	}
	
}
