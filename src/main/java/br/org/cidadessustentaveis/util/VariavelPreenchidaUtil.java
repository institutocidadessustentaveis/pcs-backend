package br.org.cidadessustentaveis.util;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.org.cidadessustentaveis.dto.VariavelPreenchidaDTO;
import br.org.cidadessustentaveis.model.enums.TipoVariavel;
import br.org.cidadessustentaveis.model.indicadores.SubdivisaoVariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.ValorReferencia;
import br.org.cidadessustentaveis.model.indicadores.VariaveisOpcoes;
import br.org.cidadessustentaveis.model.indicadores.Variavel;
import br.org.cidadessustentaveis.model.indicadores.VariavelPreenchida;
import br.org.cidadessustentaveis.model.indicadores.VariavelResposta;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class VariavelPreenchidaUtil {

	public static final ValorReferencia AGUARDANDO_AVALIACAO = ValorReferencia.builder().cor("#3333FF")
			.Label("Em avaliação por um administrador").build();

	public static final ValorReferencia VALOR_REF_NAO_SE_APLICA_INDICADOR = ValorReferencia.builder().cor("#D3D3D3")
			.Label("Não se aplica valor de referência para este indicador").build();

	
	public static final ValorReferencia VALOR_REF_NAO_SE_APLICA_VARIAVEL = ValorReferencia.builder().cor("#D3D3D3")
			.Label("Não se aplica valor de referência para esta variável").build();

	public void validarValorPreenchido(Boolean valoresObrigatorio, Variavel variavelEncontrada, TipoVariavel tipo,
			VariavelPreenchidaDTO variavel) {
		switch (tipo) {
		case SIM_NAO: {
			if (variavel.getRespostaSimples() == null && valoresObrigatorio)
				throw new ObjectNotFoundException(
						"O campo valor não está preenchido para a variável " + variavelEncontrada.getNome());
			break;
		}
		case SIM_NAO_COM_LISTA_OPCOES: {

			if(variavelEncontrada.isMultiplaSelecao()) {
				if ((variavel.getRespostaSimples() == null || (variavel.getIdOpcoes() == null || variavel.getIdOpcoes().isEmpty()) ) && valoresObrigatorio)
					throw new ObjectNotFoundException("O campo valor e/ou opção não estão preenchidos para a variável "
							+ variavelEncontrada.getNome());
			} else {
				if ((variavel.getRespostaSimples() == null || variavel.getIdOpcao() == null) && valoresObrigatorio)
					throw new ObjectNotFoundException("O campo valor e/ou opção não estão preenchidos para a variável "
							+ variavelEncontrada.getNome());
			}
			
			break;
		}
		case LISTA_OPCOES: {
			if(variavelEncontrada.isMultiplaSelecao()) {
				if ( (variavel.getIdOpcoes() == null || variavel.getIdOpcoes().isEmpty()) && valoresObrigatorio)
					throw new ObjectNotFoundException(
						"O campo opção não está preenchido para a variável " + variavelEncontrada.getNome());
			}else {
				if (variavel.getIdOpcao() == null && valoresObrigatorio)
					throw new ObjectNotFoundException(
						"O campo opção não está preenchido para a variável " + variavelEncontrada.getNome());
			}
			break;
		}
		case INTEIRO: {
			if (variavel.getValor() != null) {
				if (variavel.getValor() % 1 != 0) {
					throw new RuntimeException(
							"O campo valor deve ser preenchido para a variável " + variavelEncontrada.getNome());
				}
			} else {
				if (valoresObrigatorio) {
					throw new ObjectNotFoundException(
							"O campo valor não está preenchido para a variável " + variavelEncontrada.getNome());
				}
			}
			break;
		}
		case DECIMAL: {
			if (valoresObrigatorio && variavel.getValor() == null) {
				throw new ObjectNotFoundException(
						"O campo valor não está preenchido para a variável " + variavelEncontrada.getNome());
			}

			break;
		}
		case TEXTO_LIVRE: {
			if ((variavel.getValorTexto() == null) && valoresObrigatorio) {
				throw new RuntimeException(
						"O campo valor deve ser preenchido para a variável " + variavelEncontrada.getNome());
			}
			break;
		} 
		default:
			break;
		}
	}

	public Double getValorVariavelPreenchida(VariavelPreenchida preenchida) {
		Double valor = null;

		TipoVariavel tipo = TipoVariavel.fromString(preenchida.getVariavel().getTipo());

		switch (tipo) {
		case DECIMAL:
		case INTEIRO: {
			valor = preenchida.getValor();
			break;
		}
		case SIM_NAO: {
			VariavelResposta resposta = preenchida.getVariavel().getVariavelResposta();
			valor = preenchida.getRespostaSimples() ? resposta.getRespostaSim() : resposta.getRespostaNao();
			break;
		}
		case LISTA_OPCOES:
			if(preenchida.getVariavel().isMultiplaSelecao()) {
				if(preenchida.getOpcoes() != null) {
					if(valor == null) {
						valor = 0.0;
					}
					for(VariaveisOpcoes opcao : preenchida.getOpcoes()) {
						valor = valor + opcao.getValor();
					}
				}
			}else {
				valor = preenchida.getOpcao().getValor();
			}
			break;
		case SIM_NAO_COM_LISTA_OPCOES: {
			if(preenchida.getRespostaSimples()) {
				valor = preenchida.getVariavel().getVariavelResposta().getRespostaSim();				
			}else {
				valor = preenchida.getVariavel().getVariavelResposta().getRespostaNao();
			}
			if(preenchida.getVariavel().isMultiplaSelecao()) {
				if(preenchida.getOpcoes() != null) {
					for(VariaveisOpcoes opcao : preenchida.getOpcoes()) {
						valor = valor + opcao.getValor();
					}
				}
			}else {
				if(preenchida.getOpcao() != null) {
					valor = preenchida.getOpcao().getValor();
				}
			}
			break;
		}
		case TEXTO_LIVRE: {
			if(preenchida.getValor() != null) {
				valor = preenchida.getValor();
			} else {
				valor = null;
			}
		}
		default:
			break; 
		}

		return valor;
	}
	
	public Double getValorVariavelPreenchida(SubdivisaoVariavelPreenchida preenchida) {
		Double valor = null;

		TipoVariavel tipo = TipoVariavel.fromString(preenchida.getVariavel().getTipo());

		switch (tipo) {
		case DECIMAL:
		case INTEIRO: {
			valor = preenchida.getValor();
			break;
		}
		case SIM_NAO: {
			VariavelResposta resposta = preenchida.getVariavel().getVariavelResposta();
			valor = preenchida.getRespostaSimples() ? resposta.getRespostaSim() : resposta.getRespostaNao();
			break;
		}
		case LISTA_OPCOES:
			if(preenchida.getVariavel().isMultiplaSelecao()) {
				if(preenchida.getOpcoes() != null) {
					if(valor == null) {
						valor = 0.0;
					}
					for(VariaveisOpcoes opcao : preenchida.getOpcoes()) {
						valor = valor + opcao.getValor();
					}
				}
			}else {
				valor = preenchida.getOpcao().getValor();
			}
			break;
		case SIM_NAO_COM_LISTA_OPCOES: {
			if(preenchida.getRespostaSimples()) {
				valor = preenchida.getVariavel().getVariavelResposta().getRespostaSim();				
			}else {
				valor = preenchida.getVariavel().getVariavelResposta().getRespostaNao();
			}
			if(preenchida.getVariavel().isMultiplaSelecao()) {
				if(preenchida.getOpcoes() != null) {
					for(VariaveisOpcoes opcao : preenchida.getOpcoes()) {
						valor = valor + opcao.getValor();
					}
				}
			}else {
				if(preenchida.getOpcao() != null) {
					valor = preenchida.getOpcao().getValor();
				}
			}
			break;
		}
		case TEXTO_LIVRE: {
			if(preenchida.getValor() != null) {
				valor = preenchida.getValor();
			} else {
				valor = null;
			}
		}
		default:
			break; 
		}

		return valor;
	}
	
	public static String valorApresentacao(VariavelPreenchida vp) {
		String valor = "-";
		if(vp != null && vp.getVariavel() != null) {
			
			switch (vp.getVariavel().getTipo()) {
			case "Numérico inteiro":
				valor = NumeroUtil.integerToString(vp.getValor());
				break;
				
			case "Numérico decimal":
				if(vp.getVariavel().getId().equals(30l)) {
					valor = NumeroUtil.decimal3ToString(NumeroUtil.arredondarTresCasasDecimais(vp.getValor()));
				} else {
					valor = NumeroUtil.decimalToString(NumeroUtil.arredondarDuasCasasDecimais(vp.getValor()));
				}
				break;
				
			case "Tipo sim/não":
				valor = vp.getRespostaSimples() ? "Sim" : "Não";
				break;
				
			case "Tipo sim/não com lista de opções":
				valor = vp.getRespostaSimples() ? "Sim" : "Não";
				if(vp.getVariavel().isMultiplaSelecao() && vp.getOpcoes() != null) {
					
					for (int i = 0; vp.getOpcoes().size() < i ; i++){
						valor = valor+". "+(i+1)+" - "+vp.getOpcoes().get(i).getDescricao();
					}
				} else {
					valor = (vp.getOpcao() != null ) ? valor+". "+vp.getOpcao().getDescricao() : valor ;				
				}
				break;
				
			case "Tipo lista de opções":
				if(vp.getVariavel().isMultiplaSelecao() && vp.getOpcoes() != null) {
					valor = "";
					for (int i = 0; vp.getOpcoes().size() > i ; i++){
						valor = valor+""+(i+1)+" - "+vp.getOpcoes().get(i).getDescricao()+". ";
					}
				} else {
					valor = (vp.getOpcao() != null ) ? valor+""+vp.getOpcao().getDescricao()+". " : valor ;				
				}
				break;
				
			case "Texto livre":
				valor = vp.getValorTexto();
				break;
				
			default:
				valor = "-";
			}
		}
		return valor;
	}

	public static String valorApresentacao(SubdivisaoVariavelPreenchida vp) {
		String valor = "-";
		if(vp != null && vp.getVariavel() != null) {
			
			switch (vp.getVariavel().getTipo()) {
			case "Numérico inteiro":
				valor = NumeroUtil.integerToString(vp.getValor());
				break;
				
			case "Numérico decimal":
				if(vp.getVariavel().getId().equals(30l)) {
					valor = NumeroUtil.decimal3ToString(NumeroUtil.arredondarTresCasasDecimais(vp.getValor()));
				} else {
					valor = NumeroUtil.decimalToString(NumeroUtil.arredondarDuasCasasDecimais(vp.getValor()));
				}
				break;
				
			case "Tipo sim/não":
				valor = vp.getRespostaSimples() ? "Sim" : "Não";
				break;
				
			case "Tipo sim/não com lista de opções":
				valor = vp.getRespostaSimples() ? "Sim" : "Não";
				if(vp.getVariavel().isMultiplaSelecao() && vp.getOpcoes() != null) {
					
					for (int i = 0; vp.getOpcoes().size() < i ; i++){
						valor = valor+". "+(i+1)+" - "+vp.getOpcoes().get(i).getDescricao();
					}
				} else {
					valor = (vp.getOpcao() != null ) ? valor+". "+vp.getOpcao().getDescricao() : valor ;				
				}
				break;
				
			case "Tipo lista de opções":
				if(vp.getVariavel().isMultiplaSelecao() && vp.getOpcoes() != null) {
					valor = "";
					for (int i = 0; vp.getOpcoes().size() > i ; i++){
						valor = valor+""+(i+1)+" - "+vp.getOpcoes().get(i).getDescricao()+". ";
					}
				} else {
					valor = (vp.getOpcao() != null ) ? valor+""+vp.getOpcao().getDescricao()+". " : valor ;				
				}
				break;
				
			case "Texto livre":
				valor = vp.getValorTexto();
				break;
				
			default:
				valor = "-";
			}
		}
		return valor;
	}

}
