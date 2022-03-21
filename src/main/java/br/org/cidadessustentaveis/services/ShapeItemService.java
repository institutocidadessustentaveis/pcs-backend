package br.org.cidadessustentaveis.services;

import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.wololo.geojson.Feature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vividsolutions.jts.geom.Geometry;

import br.org.cidadessustentaveis.dto.AtributoDTO;
import br.org.cidadessustentaveis.dto.ConfirmacaoCriacaoShapeFileDTO;
import br.org.cidadessustentaveis.dto.ShapeFileMergedDTO;
import br.org.cidadessustentaveis.dto.ShapeItemDTO;
import br.org.cidadessustentaveis.dto.SubdivisaoDTO;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.SubdivisaoCidade;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeItem;
import br.org.cidadessustentaveis.repository.ShapeFileRepository;
import br.org.cidadessustentaveis.repository.ShapeItemRepository;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.GeoJSONReaderVivid;
import br.org.cidadessustentaveis.util.GeoJSONWriterVivid;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;	

@Service
@CacheConfig(cacheNames={"cache"})
public class ShapeItemService {


	@Autowired
	private ShapeItemRepository daoShapeItem;
	
	@Autowired
	private ShapeFileRepository daoShapeFile;

	@Autowired
	private AreaInteresseService areaInteresseService;
	
	@Autowired
	private TemaGeoespacialService temaGeoService;
	
	@Autowired
	private UsuarioContextUtil usuarioContextUtil;

	@Autowired
	private ShapeFileService shapeFileService;
	
	@Autowired
	private SubdivisaoService subdivisaoService;

	public List<Feature> findAll() {
		List<ShapeItem>lista = daoShapeItem.findAll();
		
		List<Feature> features = new ArrayList<>();
		for(ShapeItem s : lista) {
			Feature feature = convertEntityToFeature(s);
			features.add(feature);
		}
		return features;
	}

	public List<Feature> findFeatureByShapeId(Long id) {
		List<ShapeItem> lista = daoShapeItem.findByShapeFileId(id);

		List<Feature> features = new ArrayList<>();

		for(ShapeItem s : lista) {
			Feature feature = convertEntityToFeature(s);
			features.add(feature);
		}

		return features;
	}
	
	public ShapeItem buscarPorId(Long id) {
		Optional<ShapeItem> obj = daoShapeItem.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Shape não encontrado!"));
	}
	
	public org.wololo.geojson.Geometry convertJtsGeometryToGeoJson(Geometry geometry) {
        return new GeoJSONWriterVivid().write(geometry);
    }
	
    public Geometry convertGeoJsonToJtsGeometry(org.wololo.geojson.Geometry geoJson) {
        return new GeoJSONReaderVivid().read(geoJson);
    }

    
    public Feature convertEntityToFeature(ShapeItem entity) {
    	
        org.wololo.geojson.Geometry geometry = convertJtsGeometryToGeoJson(entity.getShape());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        
        Map<String,Object> properties =  null;

            try {
            	String res = entity.getAtributos();
            	if(res != null && !res.isEmpty() ) {
                	properties = objectMapper.readValue(entity.getAtributos(), HashMap.class);
            	}

    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        

        return new Feature(entity.getId(), geometry, properties);
    }
    
    public ConfirmacaoCriacaoShapeFileDTO salvarShape(ShapeFileMergedDTO shapeFileMergedDTO)throws Exception {
    	if(shapeFileMergedDTO.getFeatures().isEmpty()) {
    		throw new Exception("A Camada precisa ter pelo menos um objeto desenhado.");
    	}
    	if(shapeFileMergedDTO.getTitulo() == null ) {
    		throw new Exception("A Camada precisa ter um título.");
    	}
    	if(this.shapeFileService.camadaExiste(shapeFileMergedDTO.getTitulo(),shapeFileMergedDTO.getId())) {
    		throw new Exception("Já existe uma camada com esse título");
    	}
    	
		Usuario usuarioLogado = usuarioContextUtil.getUsuario();
		boolean shapePertenceAPrefeitura = usuarioLogado.getPrefeitura() != null;
        boolean temInterseccao = false;
        
    	List<ShapeItem> listaParaSalvar = new ArrayList<>();
    	ShapeFile sf = new ShapeFile();
    	sf.setAno(shapeFileMergedDTO.getAno());
    	sf.setTitulo(shapeFileMergedDTO.getTitulo());
    	
    	List<AreaInteresse> listaAreaInteresse = new ArrayList<>();
		for (Long id_area : shapeFileMergedDTO.getAreasInteresse()) {
			AreaInteresse areaRef = areaInteresseService.buscarPorId(id_area);
			listaAreaInteresse.add(areaRef);
		}
		sf.setAreasInteresse(listaAreaInteresse);

    	if(shapeFileMergedDTO.getTemaGeoespacial() != null) {
        	sf.setTemaGeoespacial(temaGeoService.buscar(shapeFileMergedDTO.getTemaGeoespacial()));
    	}
    	sf.setInstituicao(shapeFileMergedDTO.getInstituicao());
    	sf.setNivelTerritorial(shapeFileMergedDTO.getNivelTerritorial());
    	sf.setPublicar(shapeFileMergedDTO.isPublicar());
    	sf.setTipoArquivo(shapeFileMergedDTO.getTipoArquivo());
    	sf.setFonte(shapeFileMergedDTO.getFonte());
    	sf.setSistemaDeReferencia(shapeFileMergedDTO.getSistemaDeReferencia());
    	sf.setExibirAuto(shapeFileMergedDTO.isExibirAuto());
    	
    	sf.setDataHoraCadastro(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		if(usuarioContextUtil != null && usuarioContextUtil.getUsuario() != null ) {
			sf.setUsuario(usuarioContextUtil.getUsuario());
			if(usuarioContextUtil.getUsuario().getPrefeitura() != null) {
				sf.setPrefeitura(usuarioContextUtil.getUsuario().getPrefeitura());	
				sf.setCidade(usuarioContextUtil.getUsuario().getPrefeitura().getCidade());
			}
		}
		
    	
    	for (Feature shape : shapeFileMergedDTO.getFeatures()) {
    		ShapeItem shapeItem = new ShapeItem();
    		shapeItem.setAtributos("");
    		Geometry geo = this.convertGeoJsonToJtsGeometry(shape.getGeometry());
    		shapeItem.setShape(geo);
    		shapeItem.setShapeFile(sf);
    		
    		GsonBuilder gsonBuilder = new GsonBuilder();  
    		gsonBuilder.serializeNulls();  
    		Gson gson = gsonBuilder.create();

			String jsonString = gson.toJson(shape.getProperties());
    		shapeItem.setAtributos(jsonString);
    		listaParaSalvar.add(shapeItem);
		}
    	
    	sf.setShapes(listaParaSalvar);
    	daoShapeFile.save(sf);
    	
    	
    	if(usuarioLogado != null && shapePertenceAPrefeitura) {
            temInterseccao = this.shapeFileService.checarShapeEstaDentroDaCidadePrefeitura(sf, usuarioLogado);

            if(!temInterseccao && sf.getPublicar()) {
            	this.shapeFileService.enviarAlertaShapeForaDaPrefeitura(sf, usuarioLogado);
			}
        }

		return new ConfirmacaoCriacaoShapeFileDTO(sf, shapePertenceAPrefeitura, temInterseccao);
    }
    
    public List<Feature> findPorShapeFile(Long idShapefile) {
    	List<ShapeItem> lista = daoShapeItem.findByShapeFileId(idShapefile);

    	List<Feature> features = new ArrayList<>();
    	for(ShapeItem s : lista) {
    		Feature feature = convertEntityToFeature(s);
    		features.add(feature);
    	}
    	return features;
    }
    
    public List<Feature> findPorAtributosPorShapeFile(String atributo, Long referencia,Long idShapefile ) {
    	List<ShapeItem> lista = daoShapeItem.buscarPorAtributoPorShapeFileId(atributo, referencia.toString(), idShapefile);

    	List<Feature> features = new ArrayList<>();
    	for(ShapeItem s : lista) {
    		Feature feature = convertEntityToFeature(s);
    		features.add(feature);
    	}
    	return features;
    }
    
    public List<ShapeItemDTO> findByShapeFileIdAndAtributo(Long idShapeFile, String atributo, String referencia){
    	return daoShapeItem.findByShapeFileIdAndAtributo(idShapeFile, atributo, referencia); 
    }
    
    public void mesclarAtributos(Long idShapeFile, String atributo) {
    	
    }
    

	public List<ShapeItem> buscarPorAtributo(Pair atributo) {
		if(atributo == null) throw new IllegalArgumentException("Atributo para a busca do shape não pode ser nulo");
		return daoShapeItem.buscarPorAtributo(atributo.getFirst().toString(), atributo.getSecond().toString());
	}

	public boolean shapesTemInterseccao(Long idShapeEnvolvente, Long idShapeInterno) {
		return daoShapeItem.shapesTemInterseccao(idShapeEnvolvente, idShapeInterno);
	}

	public void editarPorIdShapeFile(List<Feature> shapes, Long idShapeFile) {

		ShapeFile shapefile = shapeFileService.buscarPorId(idShapeFile);
    	List<ShapeItem> listaParaSalvar = new ArrayList<>();
    	ShapeFile sf = new ShapeFile();
    	

   
    	if (shapefile != null) {
        	for (Feature shape : shapes) {
        		ShapeItem shapeItem = new ShapeItem();
        		if(shape.getProperties() != null) {
        			shapeItem.setAtributos(shape.getProperties().toString());
        		}
        		Geometry geo = convertGeoJsonToJtsGeometry(shape.getGeometry());
        		shapeItem.setShape(geo);
        		shapeItem.setShapeFile(sf);
        		
        		if(shapefile != null) {
        			shapeItem.setShapeFile(shapefile);
        		}

        		GsonBuilder gsonBuilder = new GsonBuilder();  
        		gsonBuilder.serializeNulls();  
        		Gson gson = gsonBuilder.create();

    			String jsonString = gson.toJson(shape.getProperties());
        		shapeItem.setAtributos(jsonString);
        		listaParaSalvar.add(shapeItem);
    		}
        	
        	this.clearShapes(shapefile);
        	
    		shapefile.getShapes().addAll(listaParaSalvar);
    		shapefile.setDataHoraAlteracao(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    		shapefile.setAno(Year.now().getValue());
    		daoShapeFile.save(shapefile);
    	}
    }
	
	public void clearShapes(ShapeFile shapefile) {
		List<ShapeItem> listaParaDeletar = shapefile.getShapes();
		for (ShapeItem shapeItem : listaParaDeletar) {
			if(shapeItem.getId() != null) {
				try {
					daoShapeItem.deleteByIdShapeItem(shapeItem.getId());
				}catch (Exception e){}
			}
			
		}
	}
	
	public void editarAtributos(Long idShape, List<AtributoDTO> atributos) {
		ShapeItem shape = buscarPorId(idShape);
		
		Map<String,String> properties =  new HashMap<>();
		for (AtributoDTO atributoDTO : atributos) {
			properties.put(atributoDTO.getAtributo(), atributoDTO.getValor());
		}
		
		GsonBuilder gsonBuilder = new GsonBuilder();  
		gsonBuilder.serializeNulls();  
		Gson gson = gsonBuilder.create();
		String jsonString = gson.toJson(properties);
		
		shape.setAtributos(jsonString);
		daoShapeItem.saveAndFlush(shape);
	}

	public List<ShapeItemDTO> findDtoByShapeFileId(Long idShapeFile) {
		return daoShapeItem.findDtoByShapeFileId(idShapeFile);
	}
	
    public ConfirmacaoCriacaoShapeFileDTO salvarShapeSubdivisao(SubdivisaoDTO subdivisaoDTO)throws Exception {
    	
		SubdivisaoCidade  subdivisaoCidadeRef = null;
		
    	if(subdivisaoDTO.getFeatures() == null) {
    		throw new Exception("A subdivisao precisa ter pelo menos um objeto desenhado.");
    	}
    	if(subdivisaoDTO.getNome() == null ) {
    		throw new Exception("A subdivisao precisa ter um nome.");
    	}
    	
		Usuario usuarioLogado = usuarioContextUtil.getUsuario();
		boolean shapePertenceAPrefeitura = usuarioLogado.getPrefeitura() != null;
        boolean temInterseccao = false;
        
    	List<ShapeItem> listaParaSalvar = new ArrayList<>();
    	ShapeFile sf = new ShapeFile();
    	sf.setAno(Year.now().getValue());
    	sf.setTitulo(subdivisaoDTO.getNome());
    	
    	sf.setPublicar(false);
    	sf.setTipoArquivo("SHP");

    	
    	sf.setDataHoraCadastro(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		if(usuarioContextUtil != null && usuarioContextUtil.getUsuario() != null ) {
			sf.setUsuario(usuarioContextUtil.getUsuario());
			if(usuarioContextUtil.getUsuario().getPrefeitura() != null) {
				sf.setPrefeitura(usuarioContextUtil.getUsuario().getPrefeitura());	
				sf.setCidade(usuarioContextUtil.getUsuario().getPrefeitura().getCidade());
			}
		}
		
    	
    	for (Feature shape : subdivisaoDTO.getFeatures()) {
    		ShapeItem shapeItem = new ShapeItem();
    		shapeItem.setAtributos("");
    		Geometry geo = this.convertGeoJsonToJtsGeometry(shape.getGeometry());
    		shapeItem.setShape(geo);
    		shapeItem.setShapeFile(sf);
    		
    		GsonBuilder gsonBuilder = new GsonBuilder();  
    		gsonBuilder.serializeNulls();  
    		Gson gson = gsonBuilder.create();

			String jsonString = gson.toJson(shape.getProperties());
    		shapeItem.setAtributos(jsonString);
    		listaParaSalvar.add(shapeItem);
		}
    	
    	sf.setShapes(listaParaSalvar);
    	daoShapeFile.save(sf);
    	
    	temInterseccao = this.shapeFileService.checarShapeEstaDentroDaCidadePrefeituraSubDivisao(sf, usuarioLogado);
    	if(usuarioLogado != null && shapePertenceAPrefeitura) {
            
            if(!temInterseccao && sf.getPublicar()) {
            	this.shapeFileService.enviarAlertaShapeForaDaPrefeitura(sf, usuarioLogado);
			}
        }
    	
    	if(temInterseccao) {
    		if(subdivisaoDTO.getId() == null) {
    			subdivisaoCidadeRef = subdivisaoService.inserir(subdivisaoDTO);
    		} else {
    			subdivisaoCidadeRef = subdivisaoService.update(subdivisaoDTO);
    			
    			Long idShapeFile = shapeFileService.buscarShapeFileIdPorSubdivisaoId(subdivisaoCidadeRef.getId());
    			if(idShapeFile != null) {
    				shapeFileService.excluirShapeFilePorId(idShapeFile);
    			}
    		}
    	}
    	
    	sf.setSubdivisaoCidade(subdivisaoCidadeRef);
    	daoShapeFile.save(sf);
    	

		return new ConfirmacaoCriacaoShapeFileDTO(sf, shapePertenceAPrefeitura, temInterseccao);
    }
    
}
