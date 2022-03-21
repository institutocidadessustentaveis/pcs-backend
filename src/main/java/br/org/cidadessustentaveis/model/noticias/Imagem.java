package br.org.cidadessustentaveis.model.noticias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.xmlbeans.impl.util.Base64;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@Entity
@Table(name = "imagens")
@NoArgsConstructor @AllArgsConstructor
public class Imagem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="bytes")
    private byte[] bytes;

    public Imagem(String base64) {
        this.bytes = Base64.decode(base64.getBytes());
    }

    public Imagem(byte[] bytes) {
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

}
