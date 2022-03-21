package br.org.cidadessustentaveis.services;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.BoletimTemplate01ToListDTO;
import br.org.cidadessustentaveis.model.noticias.BoletimInformativo;
import br.org.cidadessustentaveis.model.noticias.BoletimTemplate01;
import br.org.cidadessustentaveis.model.noticias.InformacaoLivre;
import br.org.cidadessustentaveis.repository.BoletimInformativoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;

@Service
public class BoletimInformativoService {

	@Autowired
	private BoletimInformativoRepository repository;
	
	
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;
	
	public void gravarDadosDoBoletimInformacoesLivres(List<InformacaoLivre> informacoesLivres) throws Exception {
		BoletimInformativo boletim = new BoletimInformativo();	
		boletim.setInformacaoLivre(informacoesLivres);
		saveDadosBoletim(boletim);
		
	}
	
	public Long gravarDadosDoBoletimTemplate01(BoletimTemplate01 template01) throws Exception{
		BoletimInformativo boletim = new BoletimInformativo();
		boletim.setBoletimTemplate01(template01);
		return saveDadosBoletim(boletim);
	}
	
	public Long saveDadosBoletim(BoletimInformativo boletim) throws Exception {
		boletim.setUsuario(usuarioContextUtil.getUsuario());
		repository.save(boletim);
		return boletim.getId();
	}
	
	public BoletimInformativo buscarPorId(Long id) {
		Optional<BoletimInformativo> boletim = repository.findById(id);
		return boletim.orElseThrow(() -> new ObjectNotFoundException("Boletim não encontrado!"));
	}
	
	public BoletimInformativo buscarPorIdBoletimTemplate01(Long id) {
		Optional<BoletimInformativo> boletim = repository.buscarPorIdBoletimTemplate01(id);
		return boletim.orElseThrow(() -> new ObjectNotFoundException("Boletim não encontrado!"));
	}
	
	public List<BoletimTemplate01ToListDTO> buscarBoletinsTemplate01() {
		return repository.buscarBoletinsTemplate01();
	}
	
	public BoletimTemplate01 buscarBoletimTemplate01PorId(Long id) {
		return repository.buscarBoletimTemplate01PorId(id);
	}
	
	public void deletarBoletim(Long id) {
		BoletimInformativo boletimRef = buscarPorId(id);
		repository.delete(boletimRef);
	}
}
