package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.administracao.ContatoPCS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ContatoPcsDTO {

    private Long id;

    @NotNull
    @NotEmpty
    @Length(max = 200)
    private String endereco;

    @NotNull
    @NotEmpty
    @Length(max = 50)
    private String cidade;

    @NotNull
    @NotEmpty
    @Length(max = 50)
    private String telefone;

    @NotNull
    @NotEmpty
    @Length(max = 15)
    private String cep;

    @Length(max = 500)
    private String link;

    public ContatoPcsDTO(ContatoPCS contato) {
        this.id = contato.getId();
        this.endereco = contato.getEndereco();
        this.cidade = contato.getCidade();
        this.telefone = contato.getTelefone();
        this.cep = contato.getCep();
        this.link = contato.getLink();
    }

    public ContatoPCS toEntityInsert() {
        ContatoPCS contato = new ContatoPCS();
        contato.setEndereco(this.endereco);
        contato.setCidade(this.cidade);
        contato.setTelefone(this.telefone);
        contato.setCep(this.cep);
        contato.setLink(this.link);
        contato.setData(new Date());
        return contato;
    }

    public ContatoPCS toEntityUpdate(ContatoPCS contato) {
        contato.setId(this.id);
        contato.setEndereco(this.endereco);
        contato.setTelefone(this.telefone);
        contato.setCep(this.cep);
        contato.setCidade(this.cidade);
        contato.setLink(this.link);
        contato.setData(new Date());

        if(contato.getLink().equals("")) {
            contato.setLink(null);
        }

        return contato;
    }

}
