package br.org.cidadessustentaveis.model.institucional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.xmlbeans.impl.util.Base64;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.org.cidadessustentaveis.model.home.Home;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@Entity
@Table(name = "institucional_dinamico_imagens")
@NoArgsConstructor @AllArgsConstructor
public class InstitucionalDinamicoImagem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="bytes")
    private byte[] bytes;
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="institucional")
	@JsonBackReference
	private InstitucionalDinamico institucional;
	
	@Column(name="indice")
	private Long indice;
	
	@Column(name = "autor")
	private String nomeAutor;
	
	@Column(name = "titulo")
	private String titulo;
	
	@Column(name = "link")
	private String link;

    public InstitucionalDinamicoImagem(String base64) {
        this.bytes = Base64.decode(base64.getBytes());
    }

    public InstitucionalDinamicoImagem(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setImagePayload(String base64) {
        this.bytes = Base64.decode(base64.getBytes());
    }

    public String toBase64() {
        if(this.bytes == null) {
            return "";
        }

        return new String(Base64.encode(this.bytes));
    }

	@Column(name = "subtitulo")
	private String subtitulo;
}
