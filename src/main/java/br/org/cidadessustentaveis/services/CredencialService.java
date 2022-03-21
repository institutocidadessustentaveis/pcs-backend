package br.org.cidadessustentaveis.services;

import br.org.cidadessustentaveis.model.administracao.Credencial;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.repository.CredencialRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredencialService {

    @Autowired
    private CredencialRepository credencialRepository;

    public void setUserOnline(String username, boolean online) {
        Credencial credencial = credencialRepository.findByLoginIgnoreCaseAndSnExcluido(username, false);

        if(credencial != null) {
            credencial.setSnOnline(online);
        }

        credencialRepository.save(credencial);
    }
    
	public Credencial buscarPorId(Long id) {
		Optional<Credencial> obj = credencialRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Credencial não encontrado!"));
	}

}
