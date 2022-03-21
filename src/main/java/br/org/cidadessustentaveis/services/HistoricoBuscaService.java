package br.org.cidadessustentaveis.services;

import br.org.cidadessustentaveis.model.sistema.HistoricoBusca;
import br.org.cidadessustentaveis.repository.HistoricoBuscaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoricoBuscaService {

    @Autowired
    private HistoricoBuscaRepository repository;

    public HistoricoBusca save(HistoricoBusca historico) {
        return repository.save(historico);
    }

}
