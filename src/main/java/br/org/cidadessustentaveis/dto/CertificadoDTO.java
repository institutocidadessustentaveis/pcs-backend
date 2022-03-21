package br.org.cidadessustentaveis.dto;

import br.org.cidadessustentaveis.model.capacitacao.Certificado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class CertificadoDTO {

	private Long id;
	
	private String titulo;
	
	private String texto1;
	
	private String texto2;
	
	private String texto3;
	
	private ArquivoDTO imagem;

	private Boolean orientacaoPaisagem;
	
	public Certificado toEntityInsert(CertificadoDTO certificadoDTO) {
		return new Certificado(
				null, titulo, texto1, texto2, texto3, imagem != null ? imagem.toEntityInsert() : null,
				orientacaoPaisagem);
	}
	
	public Certificado toEntityUpdate(Certificado certificado) {
		certificado.setTitulo(this.titulo);
		certificado.setTexto1(this.texto1);
		certificado.setTexto2(this.texto2);
		certificado.setTexto3(this.texto3);
		certificado.setImagem(this.imagem != null ?  this.imagem.toEntityInsert() : null);
		certificado.setOrientacaoPaisagem(this.orientacaoPaisagem);
		return certificado;
	}
	
	public CertificadoDTO(Certificado certificado) {
		this.id = certificado.getId();
		this.titulo = certificado.getTitulo();
		this.texto1 = certificado.getTexto1();
		this.texto2 = certificado.getTexto2();
		this.texto3 = certificado.getTexto3();
		this.imagem = certificado.getImagem() != null ? new ArquivoDTO(certificado.getImagem()): null;
		this.orientacaoPaisagem = certificado.getOrientacaoPaisagem();
	}
	
	public CertificadoDTO(Long id, String titulo) {
		this.id = id;
		this.titulo = titulo;
	}
	
	public CertificadoDTO(Long id, String titulo, String texto1, String texto2, String texto3, Boolean orientacaoPaisagem) {
		this.id = id;
		this.titulo = titulo;
		this.texto1 = texto1;
		this.texto2 = texto2;
		this.texto3 = texto3;
		this.orientacaoPaisagem = orientacaoPaisagem;
	}
}
