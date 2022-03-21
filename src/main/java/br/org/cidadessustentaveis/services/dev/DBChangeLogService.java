															package br.org.cidadessustentaveis.services.dev;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.model.dev.DBChangeLog;
import br.org.cidadessustentaveis.repository.dev.DBChangeLogRepository;

@Service
public class DBChangeLogService {
	
	@Autowired
	private DBChangeLogRepository repository;
	
	public List<DBChangeLog> listar(){
		return repository.findAll();	
	}

	@Cacheable("versao")
	public DBChangeLog versaoAtual(){
		return repository.findTopByOrderByOrderexecutedDesc();	
	}
	
}
