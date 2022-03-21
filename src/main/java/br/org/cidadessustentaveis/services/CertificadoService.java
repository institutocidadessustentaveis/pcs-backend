package br.org.cidadessustentaveis.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;
import java.util.Optional;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.org.cidadessustentaveis.dto.CertificadoDTO;
import br.org.cidadessustentaveis.model.capacitacao.Certificado;
import br.org.cidadessustentaveis.repository.CertificadoRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.EmailUtil;

@Service
public class CertificadoService {

	@Autowired
	private CertificadoRepository repository;
	
	@Autowired
	private EmailUtil emailUtil;
	
	public Certificado inserir(CertificadoDTO certificadoDTO) {

		Certificado certificado = certificadoDTO.toEntityInsert(certificadoDTO);
		
		repository.save(certificado);
		return certificado;
	}
	
	public void enviarPorEmail( String certificadoDataUrl, String destinatario, String nomeUsuario) throws IOException, EmailException {
		File arquivoParaEmail = salvarArquivo(certificadoDataUrl, nomeUsuario);
		List<String> destinatarios = new ArrayList<>();
		destinatarios.add(destinatario);
		emailUtil.enviarEmailHTMLPersonalizadoComAnexo(destinatarios, arquivoParaEmail);
		
	}
	
	public File salvarArquivo(String certificadoDataUrl, String nomeUsuario) throws IOException {
        Decoder decoder = Base64.getDecoder();		
        byte[] decodedByte = decoder.decode(certificadoDataUrl);
        Path path = FileSystems.getDefault().getPath(nomeUsuario + ".pdf");
        try {
            Files.deleteIfExists(path);
        } catch (IOException x) {
            System.err.println(x);
        }
        FileOutputStream fos = new FileOutputStream(nomeUsuario + ".pdf");
        fos.write(decodedByte);
        fos.close();
        File arquivoParaEmail = new File(nomeUsuario + ".pdf");
        return arquivoParaEmail;
	}
	
	public Certificado alterar(CertificadoDTO certificadoDTO) throws Exception {
		if (certificadoDTO.getId() == null) {
			throw new Exception("Campo id divergente.");
		}
		Certificado certificado = certificadoDTO.toEntityUpdate(buscarPorId(certificadoDTO.getId()));
		repository.save(certificado);
		return certificado;
	}
	
	public Certificado buscarPorId(Long id) {
		Optional<Certificado> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Certificado não encontrado!"));
	}
	
	public  CertificadoDTO buscarCertificadoPorId(Long id) {
		Certificado certificadoRef = buscarPorId(id);
		return new CertificadoDTO(certificadoRef);
	}
	
	public List<CertificadoDTO> buscarCertificadoToList() {
		return repository.buscarCertificadoToList();
	}
	
	public List<CertificadoDTO> buscarCertificadoToListResumido() {
		return repository.buscarCertificadoToListResumido();
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}

}
