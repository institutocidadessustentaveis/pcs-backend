package br.org.cidadessustentaveis.repository.dev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.cidadessustentaveis.model.dev.DBChangeLog;
@Repository

public interface DBChangeLogRepository extends JpaRepository<DBChangeLog, String>{
	public DBChangeLog findTopByOrderByOrderexecutedDesc();
}
