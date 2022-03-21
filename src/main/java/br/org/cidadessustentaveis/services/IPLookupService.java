package br.org.cidadessustentaveis.services;

import br.org.cidadessustentaveis.model.sistema.IPLookup;
import br.org.cidadessustentaveis.repository.IPLookupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IPLookupService {

    @Autowired
    private IPLookupRepository repository;

    public IPLookup save(IPLookup ipLookup) {
        return repository.save(ipLookup);
    }

    public IPLookup findByIp(String ip) {
        List<IPLookup> lookups = repository.findByIp(ip);
        if(lookups.isEmpty()) return null;
        return lookups.get(0);
    }

}
