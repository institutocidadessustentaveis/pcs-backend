package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class PrefeituraEdicaoDTO {

    @NotNull(message = "ID da prefeitura não pode ser nulo")
    private Long id;

    @NotNull(message = "Nome do prefeito não pode ser nulo")
    @Size(min = 3, max = 150, message = "Nome do prefeito não pode ter menos de três ou mais de 150 caracteres")
    private String nomePrefeito;

    @NotNull(message = "E-mail não pode ser nulo")
    @Email(message = "E-mail inválido")
    @Size(min = 3, max = 150, message = "E-mail não pode ter menos de três ou mais de 150 caracteres")
    private String email;

    @NotNull(message = "Telefone não pode ser nulo")
    @Size(min = 10, max = 11, message = "Telefone inválido")
    private String telefone;

    @NotNull(message = "Data de início do mandato não pode ser nula")
    private LocalDate inicioMandato;

    @NotNull(message = "Data final do mandato não pode ser nula")
    private LocalDate fimMandato;

    @NotNull(message = "ID do partido não pode ser nulo")
    private Long idPartido;

    @NotNull(message = "ID do país não pode ser nulo")
    private Long idPais;

    @NotNull(message = "ID do estado não pode ser nulo")
    private Long idEstado;

    @NotNull(message = "ID da cidade não pode ser nulo")
    private Long idCidade;

    private List<CartaCompromissoDTO> cartasCompromisso;

    public PrefeituraEdicaoDTO(Prefeitura prefeitura) {
        this.id = prefeitura.getId();
        this.nomePrefeito = prefeitura.getNome();
        this.email = prefeitura.getEmail();
        this.telefone = prefeitura.getTelefone();
        this.inicioMandato = prefeitura.getInicioMandato();
        this.fimMandato = prefeitura.getFimMandato();

        if(prefeitura.getPartidoPolitico() != null) {
            this.idPartido = prefeitura.getPartidoPolitico().getId();
        }

        if(prefeitura.getCidade() != null) {
            this.idCidade = prefeitura.getCidade().getId();

            if(prefeitura.getCidade().getProvinciaEstado() != null) {
                this.idEstado = prefeitura.getCidade().getProvinciaEstado().getId();

                if(prefeitura.getCidade().getProvinciaEstado().getPais() != null) {
                    this.idPais = prefeitura.getCidade().getProvinciaEstado().getPais().getId();
                }
            }
        }
    }

}
