package br.org.cidadessustentaveis.services;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.GeoJSONUtil;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.locationtech.geomesa.utils.text.WKTUtils$;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wololo.geojson.Feature;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import br.org.cidadessustentaveis.config.SpringContext;
import br.org.cidadessustentaveis.dto.ConfirmacaoCriacaoShapeFileDTO;
import br.org.cidadessustentaveis.dto.EixoDTO;
import br.org.cidadessustentaveis.dto.FiltroShapeQueryDTO;
import br.org.cidadessustentaveis.dto.MesclagemAtributoDTO;
import br.org.cidadessustentaveis.dto.RasterItemDTO;
import br.org.cidadessustentaveis.dto.RelatorioShapesCriadosDTO;
import br.org.cidadessustentaveis.dto.RelatorioShapesExportadosDTO;
import br.org.cidadessustentaveis.dto.ShapeFileDTO;
import br.org.cidadessustentaveis.dto.ShapeFileDetalheDTO;
import br.org.cidadessustentaveis.dto.ShapeFileOpenEndPointDTO;
import br.org.cidadessustentaveis.dto.ShapeFileVisualizarDetalheDTO;
import br.org.cidadessustentaveis.dto.ShapeItemDTO;
import br.org.cidadessustentaveis.dto.ShapeListagemMapaDTO;
import br.org.cidadessustentaveis.dto.ShapesFiltroPalavraChaveDTO;
import br.org.cidadessustentaveis.dto.ShapesPaginacaoDTO;
import br.org.cidadessustentaveis.dto.SubdivisaoDTO;
import br.org.cidadessustentaveis.dto.TemaGeoespacialDTO;
import br.org.cidadessustentaveis.dto.TemaGeoespacialExibicaoDTO;
import br.org.cidadessustentaveis.model.administracao.Alerta;
import br.org.cidadessustentaveis.model.administracao.AreaInteresse;
import br.org.cidadessustentaveis.model.administracao.Cidade;
import br.org.cidadessustentaveis.model.administracao.Credencial;
import br.org.cidadessustentaveis.model.administracao.Eixo;
import br.org.cidadessustentaveis.model.administracao.MetaObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.ObjetivoDesenvolvimentoSustentavel;
import br.org.cidadessustentaveis.model.administracao.Pais;
import br.org.cidadessustentaveis.model.administracao.Perfil;
import br.org.cidadessustentaveis.model.administracao.Prefeitura;
import br.org.cidadessustentaveis.model.administracao.ProvinciaEstado;
import br.org.cidadessustentaveis.model.administracao.SubdivisaoCidade;
import br.org.cidadessustentaveis.model.administracao.Usuario;
import br.org.cidadessustentaveis.model.enums.TipoAlerta;
import br.org.cidadessustentaveis.model.enums.TipoExportacaoHitoricoExportacaoCatalogoShape;
import br.org.cidadessustentaveis.model.indicadores.Indicador;
import br.org.cidadessustentaveis.model.planjementoIntegrado.HistoricoUsoShape;
import br.org.cidadessustentaveis.model.planjementoIntegrado.RasterItem;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeFile;
import br.org.cidadessustentaveis.model.planjementoIntegrado.ShapeItem;
import br.org.cidadessustentaveis.model.planjementoIntegrado.TemaGeoespacial;
import br.org.cidadessustentaveis.model.planjementoIntegrado.TipoUsoShape;
import br.org.cidadessustentaveis.repository.RasterItemRepository;
import br.org.cidadessustentaveis.repository.ShapeFileRepository;
import br.org.cidadessustentaveis.services.exceptions.DataIntegrityException;
import br.org.cidadessustentaveis.services.exceptions.ObjectNotFoundException;
import br.org.cidadessustentaveis.util.GeoJSONWriterVivid;
import br.org.cidadessustentaveis.util.NumeroUtil;
import br.org.cidadessustentaveis.util.ProfileUtil;
import br.org.cidadessustentaveis.util.ShapeUtil;
import br.org.cidadessustentaveis.util.UsuarioContextUtil;
import br.org.cidadessustentaveis.util.WriteShapefile;
import br.org.cidadessustentaveis.util.ZipFileWriter;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder.ProjectionPolicy;
import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.SchemaData;
import de.micromata.opengis.kml.v_2_2_0.SimpleData;


@Service
public class ShapeFileService {

	@Autowired
	private ShapeFileRepository repository;

	@Autowired
	private AreaInteresseService areaInteresseService;

	@Autowired
	private TemaGeoespacialService temaGeoespacialService;

	@Autowired
	private GeoServerService geoServerService;

	@Autowired
	private ShapeItemService shapeItemService;
	
	@Autowired
	private PaisService paisService;

	@Autowired
	private RasterItemRepository rasterItemRepository;

	@Autowired
	private UsuarioContextUtil usuarioContextUtil;

	@Autowired
	private EntityManager em;
	
	@Autowired
	private ProfileUtil profileUtil;

	@Autowired
	private UsuarioService usuarioService;	

	@Autowired
	private HistoricoShapeService historicoShapeService;

	@Autowired
	private HistoricoUsoShapeService historicoUsoShapeService;

	@Autowired
	private AlertaService alertaService;
	
	@Autowired
	private PrefeituraService prefeituraService;
	
	@Autowired
	private EixoService eixoService;
	
	@Autowired
	private ObjetivoDesenvolvimentoSustentavelService odsService;
	
	@Autowired
	private MetaObjetivoDesenvolvimentoSustentavelService metaService;
	
	@Autowired
	private IndicadorService indicadorService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private ProvinciaEstadoService estadoService;
	
	@Autowired
	private SubdivisaoService subdivisaoService;
	
	@Autowired
	private TipoSubdivisaoService tipoSubdivisaoService;


	public List<ShapeFile> buscarTodos(){
		return repository.findAll();
	}

	public ShapeFile buscarPorId(Long id) {
		Optional<ShapeFile> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Shapefile não encontrado!"));
	}
	
	public ShapeFile buscarPorIdPublicarIsTrue(Long id) {
		Optional<ShapeFile> obj = repository.buscarPorIdPublicarIsTrue(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Shapefile não encontrado!"));
	}

	public List<ShapeFileDTO> buscarTodosDto(){
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		if (usuario != null && usuario.getPrefeitura() != null) {
			List<ShapeFile> lista = repository.findAllByOrderByDataHoraCadastroDescPrefeitura(usuario.getPrefeitura().getId());
			return lista.stream().map(obj -> new ShapeFileDTO(obj)).collect(Collectors.toList());
		}else {
			List<ShapeFile> lista = repository.findAllByOrderByDataHoraCadastroDesc();
			return lista.stream().map(obj -> new ShapeFileDTO(obj)).collect(Collectors.toList());
		}
	}

	public void excluirShapeFilePorId(Long idShapeFile) {

		ShapeFile shapeFile = buscarPorId(idShapeFile);

		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		if (usuario != null && usuario.getPrefeitura() != null && (shapeFile.getPrefeitura().getId() != usuario.getPrefeitura().getId())) {
			throw new DataIntegrityException("Usuário não possui permissão para excluir shapefile selecionado");
		}

		if(shapeFile != null && shapeFile.getRasterItem() != null) {
			RasterItem rasterItem = shapeFile.getRasterItem();
			geoServerService.removeLayer(geoServerService.getPublisher(), geoServerService.workspaceRasterName, rasterItem.getStoreName(), rasterItem.getCoverageName());
		}

		try {
			repository.deleteById(idShapeFile);
		}catch (ObjectNotFoundException e) {
			throw new ObjectNotFoundException("Shapefile não encontrado!");
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O registro está relacionado com outra entidade");
		}
	}
	
	
	public ConfirmacaoCriacaoShapeFileDTO editarArquivosShape(MultipartFile arquivoShp,
			 MultipartFile arquivoDbf, MultipartFile arquivoShx, String cidade) throws Exception {
		Cidade cidadeDoShape = cidadeService.buscarPorId(Long.parseLong(cidade));
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuarioLogado = usuarioService.buscarPorEmailCredencial(user);
		
		int random = new Random().nextInt(100000);
		File pasta = new File("shapesimportados/"+random);
		pasta.mkdirs();
		
		//String filename = UUID.randomUUID().toString();
		String filename = FilenameUtils.removeExtension(arquivoShp.getOriginalFilename());

		byte[] shpBytes = arquivoShp.getBytes();
		byte[] dbfBytes = arquivoDbf.getBytes();
		byte[] dshxBytes = arquivoShx.getBytes();
		
		File fileShpAux = new File(pasta,filename + ".shp");
		FileUtils.writeByteArrayToFile(fileShpAux, shpBytes);

		File fileDbfAux = new File(pasta,filename + ".dbf");
		FileUtils.writeByteArrayToFile(fileDbfAux, dbfBytes);

		File fileShxAux = new File(pasta,filename + ".shx");
		FileUtils.writeByteArrayToFile(fileShxAux, dshxBytes);
		
		List<File> lista = new ArrayList<>();
		lista.add(fileShpAux);
		lista.add(fileDbfAux);
		//lista.add(fileShxAux); Fazer teste adcionando este arquivo


		File zipFile = new File(pasta,filename + ".zip");
		this.packZip(filename, zipFile, lista);
		
		ShapeFile shapefile = repository.buscarShapeZoneamento(cidadeDoShape.getId());
		ShapeFile newShapeFile = new ShapeFile();
		
		newShapeFile.setFileName(filename);

		newShapeFile.setDataHoraCadastro(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		if(usuarioContextUtil != null && usuarioContextUtil.getUsuario() != null ) {
			newShapeFile.setUsuario(usuarioContextUtil.getUsuario());
			newShapeFile.setPrefeitura(usuarioContextUtil.getUsuario().getPrefeitura());			
		}

		List<ShapeItem> listShapeItem = new ArrayList<>();
		
		ShapefileDataStore shapefileDataStore = new ShapefileDataStore(fileShpAux.toURI().toURL());
		
		shapefileDataStore.setCharset(Charset.forName("UTF-8"));
		
		SimpleFeatureIterator features = shapefileDataStore.getFeatureSource().getFeatures().features();
		SimpleFeature shp;
		
		while (features.hasNext()) {
			shp = features.next();

			HashMap<String, Object> elements = new HashMap<String, Object>();
			Geometry geometry = null;

			Collection<Property> att = shp.getProperties();
			for(Property pro : att) {
				if(!(pro.getValue() instanceof MultiPolygon || pro.getValue() instanceof MultiLineString || pro.getValue() instanceof MultiPoint 
						|| pro.getValue() instanceof Point || pro.getValue() instanceof LineString || pro.getValue() instanceof Polygon)) {
					elements.put(pro.getName().toString(), pro.getValue());
				}else {
					//MultiPolygon mp = (MultiPolygon) pro.getValue();
					WKTReader wktr = new WKTReader();
					try {
						geometry = wktr.read(pro.getValue().toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}

			Gson gson = new Gson();
			Type gsonType = new TypeToken<HashMap>(){}.getType();
			String gsonString = gson.toJson(elements,gsonType);
			if(gsonString.contains("�") && !gsonString.contains("�\"")) {
				features.close();
				shapefileDataStore.dispose();
				shapefileDataStore = new ShapefileDataStore(fileShpAux.toURI().toURL());
				features = shapefileDataStore.getFeatureSource().getFeatures().features();
				listShapeItem = new ArrayList<>();
			} else {

				ShapeItem shapeItem = new ShapeItem();
				shapeItem.setShape(geometry);
				if(shapefile != null) {
					shapeItem.setShapeFile(shapefile);
				}else {
					shapeItem.setShapeFile(newShapeFile);
				}
				shapeItem.setAtributos(gsonString);
				listShapeItem.add(shapeItem);
			}
			
		}
		
		features.close();
		shapefileDataStore.dispose();
		
		this.deleteFiles(fileShpAux, fileDbfAux, fileShxAux);
		zipFile.delete();
		
		Cidade shapeCidade  = cidadeService.buscarPorId(cidadeDoShape.getId());

		if (shapefile != null) {
			shapeItemService.clearShapes(shapefile);
			shapefile.getShapes().addAll(listShapeItem);
			shapefile.setDataHoraAlteracao(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
			shapefile.setAno(Year.now().getValue());
			repository.save(shapefile);
			historicoShapeService.save(shapefile);
		} else {
			newShapeFile.setTitulo("$shape_zoneamento$");
			newShapeFile.setShapes(listShapeItem);
			newShapeFile.setCidade(shapeCidade);
			newShapeFile.setPrefeitura(usuarioLogado.getPrefeitura());
			newShapeFile.setUsuario(usuarioLogado);
			newShapeFile.setTipoArquivo("SHP");
			newShapeFile.setPublicar(true);
			newShapeFile.setDataHoraCadastro(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
			newShapeFile.setAno(Year.now().getValue());
			repository.save(newShapeFile);
		}
		
		
		boolean shapePertenceAPrefeitura = usuarioLogado.getPrefeitura() != null;
        boolean temInterseccao = false;

        if(usuarioLogado != null && shapePertenceAPrefeitura) {
            temInterseccao = this.checarShapeEstaDentroDaCidadePrefeitura(newShapeFile, usuarioLogado);
        }

		return new ConfirmacaoCriacaoShapeFileDTO(newShapeFile, shapePertenceAPrefeitura, temInterseccao);
	}
	
	public List<Map<String, Object>> readKmlKmz(File file, Boolean isKmz){
	    List<Map<String, Object>> wktObjrow = new ArrayList<Map<String, Object>>();
	 
	  	try {
	  		
	    	if(isKmz) {
	    		Kml[] unmarshalKMZ = Kml.unmarshalFromKmz(file);
	    		for (Kml kml : unmarshalKMZ) {
	    			 List<Map<String, Object>> lista = this.parseKml(kml);
	    			 wktObjrow.addAll(lista);
	    		}
	    	} else {
	    		 Kml kml = Kml.unmarshal(file);
	    		 List<Map<String, Object>> lista = this.parseKml(kml);
	    		 wktObjrow.addAll(lista);
	    	}

		} catch (IOException e) {

		}
	  	
	    return wktObjrow;
	}
	
	public List<Map<String, Object>> parseKml(Kml kml){
	    Map<String, Object> response = new HashMap<String, Object>();
	    List<Map<String, Object>> wktObjrow = new ArrayList<Map<String, Object>>();
	    try {
	        de.micromata.opengis.kml.v_2_2_0.Feature feature = kml.getFeature();
	        Map<String, Object> geodata = new HashMap<String, Object>();

	        if(feature != null) {
	            if(feature instanceof Document) {
	                Document document = (Document) feature;
	                List<de.micromata.opengis.kml.v_2_2_0.Feature> featureList = document.getFeature();
	                for(de.micromata.opengis.kml.v_2_2_0.Feature documentFeature : featureList) {
	                	
	                	HashMap<String, Object> elements = new HashMap<String, Object>();
                        ExtendedData extdat = documentFeature.getExtendedData();
                        if(extdat != null && extdat.getData() != null && !extdat.getData().isEmpty() ) {
                            List<Data> lista = extdat.getData();
                            for (Data dat : lista) {
    							if(NumeroUtil.isANumber(dat.getValue())) {
    								double valorNumerico = Double.valueOf(dat.getValue());
    								elements.put(dat.getName().toString(), valorNumerico);
    							} else {
    								elements.put(dat.getName().toString(), dat.getValue().toString());
    							}  
                            }
                        }

                        
            			Gson gson = new Gson();
            			Type gsonType = new TypeToken<HashMap>(){}.getType();
            			String gsonString = gson.toJson(elements,gsonType);
            		     
	                    if(documentFeature instanceof Placemark) {
	                        geodata = new HashMap<String, Object>();
	                        Placemark placemark = (Placemark) documentFeature;
	                        de.micromata.opengis.kml.v_2_2_0.Geometry geometry = placemark.getGeometry();
	                        geodata = parseGeometry(geometry, documentFeature.getName().toString());   
	                        
	                   	 	geodata.put("gsonString",gsonString );
	                   	 	
	                        if(!geodata.isEmpty())
	                        {
	                            wktObjrow.add(geodata);
	                        }
	                    }
	                    else if(documentFeature instanceof Folder) 
	                    {
	                        Folder folder = (Folder) documentFeature;
	                        List<de.micromata.opengis.kml.v_2_2_0.Feature> folderfeaturList = folder.getFeature();
	                        for(de.micromata.opengis.kml.v_2_2_0.Feature folderfeature : folderfeaturList) 
	                        {
	                        	
	                            geodata = new HashMap<String, Object>();
	                            if(folderfeature instanceof Placemark) {
	                                Placemark placemark = (Placemark) folderfeature;
	                                de.micromata.opengis.kml.v_2_2_0.Geometry geometry = placemark.getGeometry();
	                                //push each of return store in list
	                                geodata = parseGeometry(geometry, placemark.getName().toString());
	                                if(!geodata.isEmpty())
	                                {
	                                    wktObjrow.add(geodata);
	                                }
	                            }
	                            else
	                            {
	                                System.err.println("folderfeatures was not of type Placemark"); 
	                            }
	                        }
	                    }
	                    else
	                    {
	                        System.err.println("Was not instance of Placemark or Folder");
	                    }
	                }
	                System.out.println("wktObjrow : "+wktObjrow);
	            }
	            else
	            {
	                System.err.println("instance of feature was Not Document");
	                if(feature instanceof Folder) {
	                    Folder folder = (Folder) feature;
	                    List<de.micromata.opengis.kml.v_2_2_0.Feature> featureList = folder.getFeature();

	                    geodata = new HashMap<String, Object>();

	                    for(de.micromata.opengis.kml.v_2_2_0.Feature documentFeature : featureList) {

	                        if(documentFeature instanceof Placemark) {
	                            Placemark placemark = (Placemark) documentFeature;
	                            de.micromata.opengis.kml.v_2_2_0.Geometry geometry = placemark.getGeometry();
	                            if(documentFeature.getName().toString().length() > 0)
	                            {
	                                geodata =  parseGeometry(geometry, documentFeature.getName().toString());
	                                if(!geodata.isEmpty())
	                                {
	                                    wktObjrow.add(geodata);
	                                }
	                            }
	                            else 
	                            {
	                                geodata = parseGeometry(geometry, placemark.getName().toString());
	                                if(!geodata.isEmpty())
	                                {
	                                    wktObjrow.add(geodata);
	                                }
	                            }

	                        }else
	                        {
	                            System.err.println("Was not instance of Placemark");
	                        }
	                    }
	                    System.out.println("wktObjrow : "+wktObjrow);

	                }
	            }
	        }
	        else
	        {
	            System.err.println("Feature was null");
	            response.put("Null", "Feature was null");
	        }
	    }
	    catch (Exception e) {
	        // TODO: handle exception
	        System.err.println("Exception @ : "+ e);
	    }

	    if(!wktObjrow.isEmpty() && wktObjrow != null)
	    {
	        response.put("data", wktObjrow);
	    }

	    return wktObjrow;
	}

	public Map<String, Object> parseGeometry(de.micromata.opengis.kml.v_2_2_0.Geometry geometry,String name) {
	    // <Point> <LinearRing> <Geometry> <Model> <LineString> <Polygon> <MultiGeometry>

	    Map<String, Object> response = new HashMap<String, Object>();

	    if(geometry != null) {
	        if(geometry instanceof de.micromata.opengis.kml.v_2_2_0.Polygon) {
	        	de.micromata.opengis.kml.v_2_2_0.Polygon polygon = (de.micromata.opengis.kml.v_2_2_0.Polygon) geometry;
	            Boundary outerBoundaryIs = polygon.getOuterBoundaryIs();
	            if(outerBoundaryIs != null) {
	                LinearRing linearRing = outerBoundaryIs.getLinearRing();
	                if(linearRing != null) {
	                    List<Coordinate> coordinates = linearRing.getCoordinates();
	                    if(coordinates != null) {
	                        Map<String, Object> map = new HashMap<String, Object>();
	                        ArrayList<String> wkt_lonlat = new ArrayList<String>();
	                        for(Coordinate coordinate : coordinates) {
	                            wkt_lonlat.add(coordinate.getLongitude()+" "+coordinate.getLatitude());
	                        }
	                        response.put("name",name);
	                        response.put("category","POLYGON");
	                        response.put("row","POLYGON(("+String.join(",", wkt_lonlat)+"))");
	                    }
	                    else
	                    {
	                        System.err.println("coordinate was null");
	                    }
	                }
	            }
	        }
	        else if(geometry instanceof de.micromata.opengis.kml.v_2_2_0.Point)
	        {
	        	de.micromata.opengis.kml.v_2_2_0.Point point = (de.micromata.opengis.kml.v_2_2_0.Point) geometry;
	            List<Coordinate> coordinates = point.getCoordinates();
	            if(coordinates != null  && !coordinates.isEmpty())
	            {
	                if(coordinates != null) {

	                    for(Coordinate coordinate : coordinates) {
	                        Map<String, Object> map = new HashMap<String, Object>();
	                        response.put("lon",coordinate.getLongitude());
	                        response.put("lat",coordinate.getLatitude());
	                        response.put("name",name);
	                        response.put("category","POINT");
	                        response.put("row","POINT("+String.join(",", coordinate.getLongitude()+" "+coordinate.getLatitude())+")");
	                    }
	                }
	            }
	        }
	        else if(geometry instanceof de.micromata.opengis.kml.v_2_2_0.LineString)
	        {
	        	de.micromata.opengis.kml.v_2_2_0.LineString line = (de.micromata.opengis.kml.v_2_2_0.LineString) geometry;
	            List<Coordinate> coordinates = line.getCoordinates();
	            if(coordinates != null  && !coordinates.isEmpty())
	            {
	                if(coordinates != null) {
	                    Map<String, Object> map = new HashMap<String, Object>();
	                    ArrayList<String> wkt_lonlat = new ArrayList<String>();
	                    for(Coordinate coordinate : coordinates) {
	                        wkt_lonlat.add(coordinate.getLongitude()+" "+coordinate.getLatitude());
	                    }
	                    response.put("name",name);
	                    response.put("category","LINESTRING");
	                    response.put("row","LINESTRING("+String.join(",", wkt_lonlat)+")");
	                }
	            }
	       }
	       else if(geometry instanceof de.micromata.opengis.kml.v_2_2_0.MultiGeometry)
	       {

	    	   de.micromata.opengis.kml.v_2_2_0.MultiGeometry multigeometry = (de.micromata.opengis.kml.v_2_2_0.MultiGeometry) geometry;
	               for (int j = 0; j < multigeometry.getGeometry().size(); j++) {
	                    if(multigeometry.getGeometry().get(j) instanceof de.micromata.opengis.kml.v_2_2_0.LineString)
	                    {
	                    	de.micromata.opengis.kml.v_2_2_0.LineString line = (de.micromata.opengis.kml.v_2_2_0.LineString) multigeometry.getGeometry().get(j);
	                        List<Coordinate> coordinates = line.getCoordinates();
	                        if(coordinates != null  && !coordinates.isEmpty())
	                        {
	                            if(coordinates != null) {
	                                Map<String, Object> map = new HashMap<String, Object>();

	                                ArrayList<String> wkt_lonlat = new ArrayList<String>();

	                                for(Coordinate coordinate : coordinates) {
	                                    wkt_lonlat.add(coordinate.getLongitude()+" "+coordinate.getLatitude());
	                                }
	                                response.put("name",name);
	                                response.put("category","LINESTRING");
	                                response.put("row","LINESTRING("+String.join(",", wkt_lonlat)+")");
	                            }
	                        }
	                    }
	                    else if(multigeometry.getGeometry().get(j) instanceof de.micromata.opengis.kml.v_2_2_0.Point)
	                    {
	                    	de.micromata.opengis.kml.v_2_2_0.Point point = (de.micromata.opengis.kml.v_2_2_0.Point) multigeometry.getGeometry().get(j);
	                        List<Coordinate> coordinates = point.getCoordinates();
	                           if(coordinates != null  && !coordinates.isEmpty())
	                           {
	                               if(coordinates != null) {
	                                   for(Coordinate coordinate : coordinates) {
	                                    Map<String, Object> map = new HashMap<String, Object>();
	                                    response.put("lon",coordinate.getLongitude());
	                                    response.put("lat",coordinate.getLatitude());
	                                    response.put("name",name);
	                                    response.put("category","POINT");
	                                    response.put("row","POINT("+String.join(",", coordinate.getLongitude()+" "+coordinate.getLatitude())+")");
	                                   }
	                               }
	                           }
	                    }
	                    else if(multigeometry.getGeometry().get(j) instanceof de.micromata.opengis.kml.v_2_2_0.Polygon)
	                    {
	                    	de.micromata.opengis.kml.v_2_2_0.Polygon polygon = (de.micromata.opengis.kml.v_2_2_0.Polygon) multigeometry.getGeometry().get(j);
	                        Boundary outerBoundaryIs = polygon.getOuterBoundaryIs();
	                        if(outerBoundaryIs != null) {
	                            LinearRing linearRing = outerBoundaryIs.getLinearRing();
	                            if(linearRing != null) {
	                                List<Coordinate> coordinates = linearRing.getCoordinates();
	                                if(coordinates != null) {

	                                    Map<String, Object> map = new HashMap<String, Object>();

	                                    ArrayList<String> wkt_lonlat = new ArrayList<String>();

	                                    for(Coordinate coordinate : coordinates) {
	                                        wkt_lonlat.add(coordinate.getLongitude()+" "+coordinate.getLatitude());
	                                    }
	                                    response.put("name",name);
	                                    response.put("category","POLYGON");
	                                    response.put("row","POLYGON(("+String.join(",", wkt_lonlat)+"))");
	                                }
	                                else
	                                {
	                                    System.err.println("coordinate was null");
	                                }
	                            }
	                        }
	                    }
	               }

	       }
	    }
	    else
	    {
	        System.err.println("geometry was null");
	        response.put("Null", "geometry was null");
	    }
	    return response;
	}
	
	@Transactional
	public ConfirmacaoCriacaoShapeFileDTO salvarKml(String shapefile, MultipartFile arquivoKml) throws Exception {
		Boolean isKmz = false;
	
		ShapeFileDTO shapefileDto = jsonToObj(shapefile);
		
		if(shapefileDto.getExibirAuto() == true) {
			repository.setExibirAutoToFalseAll();		
		}
				
    	if(shapefileDto.getTitulo() == null ) {
    		throw new Exception("A Camada precisa ter um título.");
    	}
    	if(camadaExiste(shapefileDto.getTitulo(),shapefileDto.getId())) {
    		throw new Exception("Já existe uma camada com esse título");
    	}
    	
    	int random = new Random().nextInt(100000);
		File pasta = new File("shapesimportados/"+random);
		pasta.mkdirs();
		
		String extension = FilenameUtils.getExtension(arquivoKml.getOriginalFilename());
		String filename = FilenameUtils.removeExtension(arquivoKml.getOriginalFilename());

		byte[] kmlBytes = arquivoKml.getBytes();
		
		File fileKmlAux;
		if(extension.toLowerCase().contains("kmz")) {
			fileKmlAux = new File(pasta,filename + ".kmz");
			isKmz = true;
		} else {
			fileKmlAux = new File(pasta,filename + ".kml");
		}

		FileUtils.writeByteArrayToFile(fileKmlAux, kmlBytes);

		List<File> lista = new ArrayList<>();
		lista.add(fileKmlAux);
		
		File zipFile = new File(pasta,filename + ".zip");
		this.packZip(filename, zipFile, lista);

		List<ShapeItem> listShapeItem = new ArrayList<>();

		ShapeFile newShapeFile = shapefileDto.toEntityInsert();
		newShapeFile.setFileName(filename);
		newShapeFile.setPais(paisService.buscarPorId(shapefileDto.getPais()));

		newShapeFile.setDataHoraCadastro(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		if(usuarioContextUtil != null && usuarioContextUtil.getUsuario() != null ) {
			newShapeFile.setUsuario(usuarioContextUtil.getUsuario());
			newShapeFile.setPrefeitura(usuarioContextUtil.getUsuario().getPrefeitura());			
		}
		
		
		List<Map<String, Object>> list = readKmlKmz(fileKmlAux,isKmz);
		
		 for (Map<String, Object> map : list) { 
			 
			// elements.put(pro.getName().toString(), pro.getValue());
			 
			 Object mkt = map.get("row");
			 Object refGsonString = map.get("gsonString");
			 String gsonString = "";
			 if(refGsonString != null) {
				 gsonString  = (String) map.get("gsonString");
			 }
			 if(mkt != null) {
				 Geometry geometry = null;
				 
				WKTReader wktr = new WKTReader();
				try {
					geometry = wktr.read(mkt.toString());
					
					ShapeItem shapeItem = new ShapeItem();
					shapeItem.setShape(geometry);
					shapeItem.setShapeFile(newShapeFile);
					shapeItem.setAtributos(gsonString);

					listShapeItem.add(shapeItem);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			 }
		 }
		
		this.carregarListas(newShapeFile, shapefileDto);
		
		newShapeFile.setTemaGeoespacial(shapefileDto.getTemaGeoespacial() != null ? temaGeoespacialService.buscar(shapefileDto.getTemaGeoespacial()) : null);
		newShapeFile.setIndicador(shapefileDto.getIndicador() != null ? indicadorService.listarById(shapefileDto.getIndicador()) : null);
		if(shapefileDto.getIndicadores() != null && !shapefileDto.getIndicadores().isEmpty()){
			if(newShapeFile.getIndicadores() == null) {
				newShapeFile.setIndicadores(new ArrayList<>());
			}
			for(Long idIndicador : shapefileDto.getIndicadores()){
				Indicador indicador = indicadorService.listarById(idIndicador);
				newShapeFile.getIndicadores().add(indicador);
			}
		}

		newShapeFile.setShapes(listShapeItem);

		newShapeFile = repository.save(newShapeFile);
		historicoShapeService.save(newShapeFile);

		this.deleteFiles(fileKmlAux);
		zipFile.delete();

		Usuario usuarioLogado = this.getUsuario();

		boolean shapePertenceAPrefeitura = usuarioLogado.getPrefeitura() != null;
        boolean temInterseccao = false;

        if(usuarioLogado != null && shapePertenceAPrefeitura) {
            temInterseccao = this.checarShapeEstaDentroDaCidadePrefeitura(newShapeFile, usuarioLogado);

            if(temInterseccao == false && shapefileDto.getPublicar()) {
				this.enviarAlertaShapeForaDaPrefeitura(newShapeFile, usuarioLogado);
			}
        }

        return new ConfirmacaoCriacaoShapeFileDTO(newShapeFile, shapePertenceAPrefeitura, temInterseccao);
	}
	

	@Transactional
	public ConfirmacaoCriacaoShapeFileDTO salvarShapeFile(String shapefile, MultipartFile arquivoShp,
									 MultipartFile arquivoDbf, MultipartFile arquivoShx) throws Exception {
		System.out.println(shapefile);
		
		ShapeFileDTO shapefileDto = jsonToObj(shapefile);
		
		if(shapefileDto.getExibirAuto() == true) {
			repository.setExibirAutoToFalseAll();
		}	
		
    	if(shapefileDto.getTitulo() == null ) {
    		throw new Exception("A Camada precisa ter um título.");
    	}
    	if(camadaExiste(shapefileDto.getTitulo(),shapefileDto.getId())) {
    		throw new Exception("Já existe uma camada com esse título");
    	}
    	
    	int random = new Random().nextInt(100000);
		File pasta = new File("shapesimportados/"+random);
		pasta.mkdirs();
		
		//String filename = UUID.randomUUID().toString();
		String filename = FilenameUtils.removeExtension(arquivoShp.getOriginalFilename());

		byte[] shpBytes = arquivoShp.getBytes();
		byte[] dbfBytes = arquivoDbf.getBytes();
		byte[] dshxBytes = arquivoShx.getBytes();
		
		File fileShpAux = new File(pasta,filename + ".shp");
		FileUtils.writeByteArrayToFile(fileShpAux, shpBytes);

		File fileDbfAux = new File(pasta,filename + ".dbf");
		FileUtils.writeByteArrayToFile(fileDbfAux, dbfBytes);

		File fileShxAux = new File(pasta,filename + ".shx");
		FileUtils.writeByteArrayToFile(fileShxAux, dshxBytes);
		
		List<File> lista = new ArrayList<>();
		lista.add(fileShpAux);
		lista.add(fileDbfAux);
		lista.add(fileShxAux);


		File zipFile = new File(pasta,filename + ".zip");
		this.packZip(filename, zipFile, lista);

		ShapeFile newShapeFile = shapefileDto.toEntityInsert();
		newShapeFile.setFileName(filename);
		newShapeFile.setPais(paisService.buscarPorId(shapefileDto.getPais()));

		newShapeFile.setDataHoraCadastro(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		if(usuarioContextUtil != null && usuarioContextUtil.getUsuario() != null ) {
			newShapeFile.setUsuario(usuarioContextUtil.getUsuario());
			newShapeFile.setPrefeitura(usuarioContextUtil.getUsuario().getPrefeitura());			
		}

		List<ShapeItem> listShapeItem = new ArrayList<>();

		ShapefileDataStore shapefileDataStore = new ShapefileDataStore(fileShpAux.toURI().toURL());
		
		shapefileDataStore.setCharset(Charset.forName("UTF-8"));
		SimpleFeatureIterator features = shapefileDataStore.getFeatureSource().getFeatures().features();
		SimpleFeature shp;
		while (features.hasNext()) {
			shp = features.next();

			HashMap<String, Object> elements = new HashMap<String, Object>();
			Geometry geometry = null;

			Collection<Property> att = shp.getProperties();
			for(Property pro : att) {
				if(!(pro.getValue() instanceof MultiPolygon || pro.getValue() instanceof MultiLineString || pro.getValue() instanceof MultiPoint 
						|| pro.getValue() instanceof Point || pro.getValue() instanceof LineString || pro.getValue() instanceof Polygon)) {
					elements.put(pro.getName().toString(), pro.getValue());
				}else {
					//MultiPolygon mp = (MultiPolygon) pro.getValue();
					WKTReader wktr = new WKTReader();
					try {
						geometry = wktr.read(pro.getValue().toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}

			Gson gson = new Gson();
			Type gsonType = new TypeToken<HashMap>(){}.getType();
			String gsonString = gson.toJson(elements,gsonType);
			if(gsonString.contains("�") && !gsonString.contains("�\"")) {
				features.close();
				shapefileDataStore.dispose();
				shapefileDataStore = new ShapefileDataStore(fileShpAux.toURI().toURL());
				features = shapefileDataStore.getFeatureSource().getFeatures().features();
				listShapeItem = new ArrayList<>();
			} else {

				ShapeItem shapeItem = new ShapeItem();
				shapeItem.setShape(geometry);
				shapeItem.setShapeFile(newShapeFile);
				shapeItem.setAtributos(gsonString);

				listShapeItem.add(shapeItem);
			}
			
		}

		features.close();
		shapefileDataStore.dispose(); 
		
		this.carregarListas(newShapeFile, shapefileDto);
		
		newShapeFile.setTemaGeoespacial(shapefileDto.getTemaGeoespacial() != null ? temaGeoespacialService.buscar(shapefileDto.getTemaGeoespacial()) : null);
		newShapeFile.setIndicador(shapefileDto.getIndicador() != null ? indicadorService.listarById(shapefileDto.getIndicador()) : null);
		if(shapefileDto.getIndicadores() != null && !shapefileDto.getIndicadores().isEmpty()){
			if(newShapeFile.getIndicadores() == null) {
				newShapeFile.setIndicadores(new ArrayList<>());
			}
			for(Long idIndicador : shapefileDto.getIndicadores()){
				Indicador indicador = indicadorService.listarById(idIndicador);
				newShapeFile.getIndicadores().add(indicador);
			}
		}



		newShapeFile.setShapes(listShapeItem);

		newShapeFile = repository.save(newShapeFile);
		historicoShapeService.save(newShapeFile);

		this.deleteFiles(fileShpAux, fileDbfAux, fileShxAux);
		zipFile.delete();

		Usuario usuarioLogado = this.getUsuario();

		boolean shapePertenceAPrefeitura = usuarioLogado.getPrefeitura() != null;
        boolean temInterseccao = false;

        if(usuarioLogado != null && shapePertenceAPrefeitura) {
            temInterseccao = this.checarShapeEstaDentroDaCidadePrefeitura(newShapeFile, usuarioLogado);

            if(temInterseccao == false && shapefileDto.getPublicar()) {
				this.enviarAlertaShapeForaDaPrefeitura(newShapeFile, usuarioLogado);
			}
        }

		return new ConfirmacaoCriacaoShapeFileDTO(newShapeFile, shapePertenceAPrefeitura, temInterseccao);
	}
	
	private void carregarListas(ShapeFile newShapeFile, ShapeFileDTO  shapefileDto) {
		
		List<AreaInteresse> listaAreaInteresse = new ArrayList<>();
		if(shapefileDto.getAreasInteresse() != null) {
			shapefileDto.getAreasInteresse().forEach(item -> {
				listaAreaInteresse.add(areaInteresseService.buscarPorId(item));
			});
			newShapeFile.setAreasInteresse(listaAreaInteresse);
		}
		
		List<Eixo> eixos = new ArrayList<>();
		if(shapefileDto.getEixos() != null) {
			shapefileDto.getEixos().forEach(item -> {
				eixos.add(eixoService.listarById(item));
			});
			newShapeFile.setEixos(eixos);
		}
		List<ObjetivoDesenvolvimentoSustentavel> ods = new ArrayList<>();
		if(shapefileDto.getOds() != null) {
			shapefileDto.getOds().forEach(item -> {
				ods.add(odsService.listarPorId(item));
			});
			newShapeFile.setOds(ods);
		}
		List<MetaObjetivoDesenvolvimentoSustentavel> metas = new ArrayList<>();
		if(shapefileDto.getMetas() != null) {
			shapefileDto.getMetas().forEach(item -> {
				metas.add(metaService.find(item));
			});
			newShapeFile.setMetas(metas);
		}
		List<Cidade> cidades = new ArrayList<>();
		if(shapefileDto.getCidades() != null) {
			shapefileDto.getCidades().forEach(item -> {
				cidades.add(cidadeService.buscarPorId(item));
			});
			newShapeFile.setCidades(cidades);
		}
		List<ProvinciaEstado> estados = new ArrayList<>();
		if(shapefileDto.getEstados() != null) {
			shapefileDto.getEstados().forEach(item -> {
				estados.add(estadoService.buscarPorId(item));
			});
			newShapeFile.setEstados(estados);
		}
	}
	
	   
    public void packZip(String filename, File output, List<File> sources) throws IOException
    {
        //System.out.println("Packaging to " + output.getName());
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(output));
        zipOut.setLevel(Deflater.DEFAULT_COMPRESSION);

        for (File source : sources)
        {
            if (source.isDirectory())
            {
                zipDir(zipOut, "", source);
            } else
            {
                zipFile(zipOut, "", source);
            }
        }
        zipOut.flush();
        zipOut.close();
        //System.out.println("Done");
        
        //geoServerService.createLayerFromShapeFile(geoServerService.workspaceRasterName, filename, filename, output, "EPSG:4326", "default_point");
       
    }
    
    private static void zipDir(ZipOutputStream zos, String path, File dir) throws IOException
    {
        if (!dir.canRead())
        {
            //System.out.println("Cannot read " + dir.getCanonicalPath() + " (maybe because of permissions)");
            return;
        }

        File[] files = dir.listFiles();
        path = buildPath(path, dir.getName());
        //System.out.println("Adding Directory " + path);

        for (File source : files)
        {
            if (source.isDirectory())
            {
                zipDir(zos, path, source);
            } else
            {
                zipFile(zos, path, source);
            }
        }

        //System.out.println("Leaving Directory " + path);
    }

    private static void zipFile(ZipOutputStream zos, String path, File file) throws IOException
    {
        if (!file.canRead())
        {
            //System.out.println("Cannot read " + file.getCanonicalPath() + " (maybe because of permissions)");
            return;
        }

        //System.out.println("Compressing " + file.getName());
        zos.putNextEntry(new ZipEntry(buildPath(path, file.getName())));

        FileInputStream fis = new FileInputStream(file);

        byte[] buffer = new byte[4092];
        int byteCount = 0;
        while ((byteCount = fis.read(buffer)) != -1)
        {
            zos.write(buffer, 0, byteCount);
            System.out.print('.');
            System.out.flush();
        }
        System.out.println();

        fis.close();
        zos.closeEntry();
    }
    
    private static String buildPath(String path, String file)
    {
    	if (path == null || path.isEmpty())
    	{
    		return file;
    	} else
    	{
    		return path + "/" + file;
    	}
    }

	public boolean checarShapeEstaDentroDaCidadePrefeitura(ShapeFile shape, Usuario usuario) {
	    if(shape == null) throw new IllegalArgumentException("Shapefile não pode ser nulo");
        if(usuario == null) throw new IllegalArgumentException("Usuário não pode ser nulo");
        Prefeitura prefeitura = prefeituraService.buscarPrefeituraPorIdUsuario(usuario.getId());
        Cidade cidade = prefeitura.getCidade();
        List<ShapeItem> itens = shapeItemService.buscarPorAtributo(Pair.of("CD_GEOCMU", cidade.getCodigoIbge()));
        
        if(!itens.isEmpty()) {
        	Long idShapeFile = shape.getShapes().get(0).getId();
            Long idShapefileCidade = itens.get(0).getId();
            return shapeItemService.shapesTemInterseccao(idShapefileCidade, idShapeFile);
        }

        return false;
    }
	
	
	public void veririficaUsuarioEShapeNull(ShapeFile shape, Usuario usuario) {		
		if(shape == null) throw new IllegalArgumentException("Shapefile não pode ser nulo");
		if(usuario == null) throw new IllegalArgumentException("Usuário não pode ser nulo");
	}
	
	
	public boolean checarShapeEstaDentroDaCidadePrefeituraSubDivisao(ShapeFile shape, Usuario usuario) throws Exception {
        veririficaUsuarioEShapeNull(shape, usuario);
        List<ShapeItem> shapeItensCidadeUsuario = buscaShapeItensDaCidadeUsuarioLogado();
        if(!shapeItensCidadeUsuario.isEmpty()) {
        	return compararShapeComShapeCidade(shape, shapeItensCidadeUsuario);
        }
        return false;
    }
	
	public List<ShapeItem> buscaShapeItensDaCidadeUsuarioLogado() throws Exception {
		Cidade cidade = buscarCidadeIdUsuarioLogado();
		List<ShapeItem> itensDaCidade = shapeItemService.buscarPorAtributo(Pair.of("CD_MUN", cidade.getCodigoIbge()));
		return itensDaCidade;
	}
	
	public Cidade buscarCidadeIdUsuarioLogado() throws Exception {
		Usuario usuarioLogado = usuarioContextUtil.getUsuario();
		Cidade cidade = buscarCidadeIdPorUsuario(usuarioLogado);
		return cidade;
	}
	
	public Cidade buscarCidadeIdPorUsuario(Usuario usuario) {
        Prefeitura usuarioPrefeitura = prefeituraService.buscarPrefeituraPorIdUsuario(usuario.getId());
        return usuarioPrefeitura.getCidade();
	}
	
	public boolean compararShapeComShapeCidade(ShapeFile shape, List<ShapeItem> shapeItensCidadeUsuario) {
		Long idShapeFile = shape.getShapes().get(0).getId();
		Long idShapefileCidade = shapeItensCidadeUsuario.get(0).getId();
		return shapeItemService.shapesTemInterseccao(idShapefileCidade, idShapeFile);
		
	}
	
    public Alerta enviarAlertaShapeForaDaPrefeitura(ShapeFile shape, Usuario usuario) {
		if(shape == null) throw new IllegalArgumentException("Shapefile não pode ser nulo");
		if(usuario == null) throw new IllegalArgumentException("Usuário não pode ser nulo");

		StringBuilder builder = new StringBuilder();

		builder.append("O usuário ");
		builder.append(usuario.getNome());
		builder.append(" (e-mail: ");
		builder.append(usuario.getEmail());
		builder.append("), pertencente à prefeitura de ");
		builder.append(usuario.getPrefeitura().getCidade().getNome());
		builder.append(" - ");
		builder.append(usuario.getPrefeitura().getCidade().getProvinciaEstado().getSigla());
		builder.append(", cadastrou e publicou um shape fora dos limites do seu município.");

		String mensagem = builder.toString();

		//Alerta alerta = new Alerta(mensagem, TipoAlerta.SHAPE_FORA_DA_PREFEITURA);
		Alerta alerta = new Alerta(mensagem, TipoAlerta.SHAPE_FORA_DA_PREFEITURA,usuario.getPrefeitura().getCidade());

		return alertaService.salvar(alerta);
	}

    private Usuario getUsuario() {
        ApplicationContext context = SpringContext.getAppContext();
        UsuarioService usuarioService = (UsuarioService)context.getBean("usuarioService");
        String user = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            return usuarioService.buscarPorEmailCredencial(user);
        } catch(ObjectNotFoundException ex) {
            return null;
        }
    }

	
	private void deleteFiles(File fileShpAux, File fileDbfAux, File  fileShxAux) {
		if(fileShpAux != null) {
			fileShpAux.delete();
		}
		if(fileDbfAux != null) {
			fileDbfAux.delete();
		}
		if(fileShxAux != null) {
			fileShxAux.delete();
		}
	}
	
	private void deleteFiles(File fileKmlAux) {
		if(fileKmlAux != null) {
			fileKmlAux.delete();
		}
	}


	public ShapeFileDTO jsonToObj(String jsonObject) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonObject, ShapeFileDTO.class);
	}

	public void salvarRasterFile(String shapefile, MultipartFile arquivoTiff) throws Exception{

		ShapeFileDTO shapefileDto = jsonToObj(shapefile);
		ShapeFile newShapeFile = shapefileDto.toEntityInsert();
		
		if(newShapeFile.getExibirAuto() == true) {
			repository.setExibirAutoToFalseAll();
		}

		List<AreaInteresse> listaAreaInteresse = new ArrayList<>();
		for (Long id_area : shapefileDto.getAreasInteresse()) {
			AreaInteresse areaRef = areaInteresseService.buscarPorId(id_area);
			listaAreaInteresse.add(areaRef);
		}
		newShapeFile.setAreasInteresse(listaAreaInteresse);

		newShapeFile.setDataHoraCadastro(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		if(usuarioContextUtil != null && usuarioContextUtil.getUsuario() != null ) {
			newShapeFile.setUsuario(usuarioContextUtil.getUsuario());
			newShapeFile.setPrefeitura(usuarioContextUtil.getUsuario().getPrefeitura());			
		}

		ProjectionPolicy pp = null;
		if(shapefileDto.getPublicar()) {
			pp = GSResourceEncoder.ProjectionPolicy.FORCE_DECLARED;
		}else {
			pp = GSResourceEncoder.ProjectionPolicy.NONE;
		}

		String storeName;
		String coverageName;

		Long maxId = rasterItemRepository.findMaxId();
		if(maxId != null) {
			maxId ++;
			coverageName = "raster_" + maxId;
			storeName = "raster_" + maxId;
		}else {
			coverageName = "raster_0";
			storeName = "raster_0";
		}


		Boolean valid = geoServerService.saveRasterIntoGeoServer(geoServerService.workspaceRasterName, storeName, coverageName, arquivoTiff, 
				newShapeFile.getSistemaDeReferencia(), pp , geoServerService.rasterDefaultStyle);

		if(valid) {

			RasterItem rasterItem = new RasterItem();

			rasterItem.setCoverageName(coverageName);
			rasterItem.setStoreName(storeName);
			rasterItem.setDefaultStyle(geoServerService.rasterDefaultStyle);
			rasterItem.setWorkspaceName(geoServerService.workspaceRasterName);
			rasterItem.setPolicyName(pp.toString());

			newShapeFile.setRasterItem(rasterItem);

			newShapeFile = repository.save(newShapeFile);

			historicoShapeService.save(newShapeFile);
		}else {
			throw new DataIntegrityException("Erro ao salvar ShapeFile");
		}
	}

	public Long count() {
		return repository.count();
	}

	public Long countShapesPublicados() {
		return repository.countShapesPublicados();
	}

	public ShapesPaginacaoDTO buscarComPaginacao(Integer page, Integer itemsPerPage, String orderBy, String direction) throws Exception {
		ShapesPaginacaoDTO dto = new ShapesPaginacaoDTO();
		
		try {
			Usuario usuario = usuarioContextUtil.getUsuario();

			if (usuario.getCredencial().getListaPerfil().stream().anyMatch(perfil -> perfil.getNome().equals("Administrador"))) {
				Page<ShapeFile> shapeFiles = repository.buscarComPaginacaoPrefeituraPublicadoTrueETodosDeAdmin(PageRequest.of(page, itemsPerPage, Sort.by(orderBy).ascending()));

				List<ShapeFileDetalheDTO> dtos = shapeFiles.stream().map((s) -> new ShapeFileDetalheDTO(s)).collect(Collectors.toList());

				dto = new ShapesPaginacaoDTO(dtos, shapeFiles.getTotalElements());
				
			} else if (usuario.getPrefeitura() != null) {
				Page<ShapeFile> shapeFiles = repository
						.buscarComPaginacaoAdminEOutrasPrefeiturasPublicadoTrueETodosDaPrefeituraLogada(PageRequest.of(page, itemsPerPage, Sort.by(orderBy).ascending()),usuario.getPrefeitura().getId());

				List<ShapeFileDetalheDTO> dtos = shapeFiles.stream().map((s) -> new ShapeFileDetalheDTO(s)).collect(Collectors.toList());

				dto = new ShapesPaginacaoDTO(dtos, shapeFiles.getTotalElements());

			}
		} catch (Exception e) {
			Page<ShapeFile> shapeFiles = repository.buscarComPaginacao(PageRequest.of(page, itemsPerPage, Sort.by(orderBy).ascending()));
			List<ShapeFileDetalheDTO> dtos = shapeFiles.stream().map((s) -> new ShapeFileDetalheDTO(s)).collect(Collectors.toList());

			 dto = new ShapesPaginacaoDTO(dtos, shapeFiles.getTotalElements());

		}
		
		return dto;

	}

	public ShapesPaginacaoDTO filtrarShapes(String titulo, Integer ano, String sistemaDeReferencia, String tipo,
			String nivelTerritorial, Long temaGeoespacial, Integer page, Integer itemsPerPage,
			String orderBy, String direction) throws Exception {
		
		Usuario usuario = new Usuario();
		boolean isPrefeitura = false;
		boolean isAdmin  = false;
		boolean isUsuarioComum  = false;
		
		try {
			usuario = usuarioContextUtil.getUsuario();			
		}catch(Exception e) {
			usuario =  null;
		}
		
		if(usuario != null && usuario.getCredencial().getListaPerfil().stream().anyMatch(perfil -> perfil.getNome().equals("Administrador"))){
			isAdmin = true;
		}else if(usuario != null && usuario.getPrefeitura() != null) {
			isPrefeitura = true;
		}else {
			isUsuarioComum = true;
		}
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ShapeFile> query = cb.createQuery(ShapeFile.class);

		Root<ShapeFile> shape = query.from(ShapeFile.class);
		
		Join<ShapeFile, Prefeitura> joinPrefeitura = shape.join("prefeitura", JoinType.LEFT);
		Join<ShapeFile, Usuario> joinUsuario = shape.join("usuario", JoinType.LEFT);
		Join<ShapeFile, TemaGeoespacial> joinTemaGeoespacial = shape.join("temaGeoespacial", JoinType.LEFT);
		Join<Usuario, Credencial> joinCredencial = joinUsuario.join("credencial", JoinType.LEFT);
		Join<Credencial, Perfil> joinPerfil = joinCredencial.join("listaPerfil", JoinType.LEFT);
		

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		if(titulo != null && !titulo.isEmpty()) {
			Path<String> shapeNome = shape.get("titulo");
			predicateList.add(cb.like(cb.lower(shapeNome), "%" + titulo.toLowerCase() + "%"));
		}
		

		if(ano != null && ano > 0) {
			Path<Long> shapeAno = shape.get("ano");
			predicateList.add(cb.equal(shapeAno, ano));
		}

		if(sistemaDeReferencia != null && !sistemaDeReferencia.isEmpty() &&
				!sistemaDeReferencia.equalsIgnoreCase("TODOS")) {
			Path<String> shapeSistemaReferencia = shape.get("sistemaDeReferencia");
			predicateList.add(cb.equal(shapeSistemaReferencia, sistemaDeReferencia));
		}


		if(tipo != null && !tipo.isEmpty() && !tipo.equalsIgnoreCase("TODOS")) {
			Path<String> shapeTipo = shape.get("tipoArquivo");
			predicateList.add(cb.equal(shapeTipo, tipo));
		}

		if(nivelTerritorial != null && !nivelTerritorial.isEmpty() &&
				!nivelTerritorial.equalsIgnoreCase("TODOS")) {
			Path<String> shapeNivelTerritorial = shape.get("nivelTerritorial");
			predicateList.add(cb.equal(shapeNivelTerritorial, nivelTerritorial));	
			
		}
		
		if(temaGeoespacial != null) {
			Path<Long> shapeTemaGeospacial = joinTemaGeoespacial.get("id");
			predicateList.add(cb.equal(shapeTemaGeospacial, temaGeoespacial));	
			
		}
		
				
		if(isAdmin) {
			Predicate predicate;
			Predicate predicate2;
			System.out.println(joinPerfil.get("nome"));
			predicate = cb.equal(joinPerfil.get("nome"), "Administrador");
			predicate2 = cb.and(cb.or(cb.isNull(joinPrefeitura.get("id")),
					cb.isNotNull(joinPrefeitura.get("id"))),
			cb.equal(shape.get("publicar"), true));
			
			predicateList.add(cb.or(predicate, predicate2));	
		}
		
		if(isPrefeitura) {
			Long idPrefeitura;
			Predicate predicate;
			Predicate predicate2;
			idPrefeitura = usuario.getPrefeitura().getId();
			
			predicate = cb.equal(joinPrefeitura.get("id"), idPrefeitura);
			predicate2 = cb.and(cb.or(cb.isNull(joinPrefeitura.get("id")),
										cb.notEqual(joinPrefeitura.get("id"), idPrefeitura)),
								cb.equal(shape.get("publicar"), true));
			predicateList.add(cb.or(predicate, predicate2));
		}
		
		if(isUsuarioComum) {
			predicateList.add(cb.equal(shape.get("publicar"), true));
		}
		
		predicateList.add(cb.notLike(shape.get("titulo"), "$shape_zoneamento$"));

		javax.persistence.criteria.Predicate[] predicates =
				new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);

		if(direction.equals("ASC") ) {
			query.where(predicates).distinct(true).orderBy(cb.asc(shape.get(orderBy)));	
		}else {
			query.where(predicates).distinct(true).orderBy(cb.desc(shape.get(orderBy)));	
		}

		TypedQuery<ShapeFile> typedQuery = em.createQuery(query);
		Long totalElements = new Long(em.createQuery(query).getResultList().size());

		if(page != null && itemsPerPage != null) {
			int firstItem = page * itemsPerPage;
			int lastItem = firstItem + itemsPerPage;

			typedQuery = typedQuery.setFirstResult(firstItem).setMaxResults(itemsPerPage);
		}
		
		FiltroShapeQueryDTO dtoGeral = new FiltroShapeQueryDTO(typedQuery.getResultList(), totalElements);
		
		List<ShapeFileDetalheDTO> dtos = dtoGeral.getShapes().stream()
					.map((s) -> new ShapeFileDetalheDTO(s))
				.collect(Collectors.toList());
	
		ShapesPaginacaoDTO dto = new ShapesPaginacaoDTO(dtos, dtoGeral.getTotalCount());
		
		return dto;
	}
	
	public ShapesFiltroPalavraChaveDTO filtrarPorPalavraChave(String filtro, String origem, Long idPais, List<Long> idsEstados, List<Long> idsCidades, Long idTema, Integer page, Integer itemsPerPage,
			String orderBy, String direction) throws Exception {
		
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario  = usuarioService.buscarPorEmailCredencial(user);
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ShapeFile> query = cb.createQuery(ShapeFile.class);

		Root<ShapeFile> shape = query.from(ShapeFile.class);
		
		Join<ShapeFile, Prefeitura> joinPrefeitura = shape.join("prefeitura", JoinType.LEFT);
		Join<Prefeitura, Cidade> joinPrefeituraCidade = joinPrefeitura.join("cidade", JoinType.LEFT);
		Join<ShapeFile, Cidade> joinCidades = shape.join("cidades", JoinType.LEFT);
		Join<ShapeFile, ProvinciaEstado> joinEstados = shape.join("estados", JoinType.LEFT);
		Join<ShapeFile, Pais> joinPais = shape.join("pais", JoinType.LEFT);
		Join<ShapeFile, TemaGeoespacial> joinTemaGeoespacial = shape.join("temaGeoespacial", JoinType.LEFT);

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		if (idPais != null) {
			Path<Long> pais = joinPais.get("id");
			predicateList.add(cb.equal(pais, idPais));
		}
		
		if(idsEstados != null && idsEstados.size() > 0) {
			Path<Long> inClause = joinEstados.get("id");
			predicateList.add(inClause.in(idsEstados));	
		}   
		
		if(idsCidades != null && idsCidades.size() > 0) {
			Path<Long> inClause = joinCidades.get("id");
			predicateList.add(inClause.in(idsCidades));	
		} 
		
		if (idTema != null) {
			Path<Long> tema = joinTemaGeoespacial.get("id");
			predicateList.add(cb.equal(tema, idTema));
		}

		if(filtro != null && !filtro.isEmpty()) {			
			Path<String> shapeTitulo = shape.get("titulo");
			Path<String> palavra = shape.get("palavra");
			
			predicateList.add(cb.or(
					cb.like(cb.lower(shapeTitulo), "%" + filtro.toLowerCase() + "%"),
					cb.like(cb.lower(palavra), "%" + filtro.toLowerCase() + "%")
					));				
		}	
				
		if(origem != null && !origem.isEmpty()) {
			if(origem.equals("PCS")) {
				predicateList.add(cb.isNull(shape.get("prefeitura")));
			}
			if(origem.equals("Prefeitura")) {
				predicateList.add(cb.isNotNull(shape.get("prefeitura")));
			}
		}
		
		if(usuario != null && usuario.getPrefeitura() != null) {
			predicateList.add(cb.equal(joinPrefeitura.get("id"), usuario.getPrefeitura().getId()));
		} 
		
		predicateList.add(cb.notLike(shape.get("titulo"), "$shape_zoneamento$"));

		javax.persistence.criteria.Predicate[] predicates =
				new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);


		if(orderBy.equals("origemCadastro")) {
			if(direction.equals("ASC")) {
				query.where(predicates).orderBy(cb.asc(joinPrefeituraCidade.get("nome")));
			}else {
				query.where(predicates).orderBy(cb.desc(joinPrefeituraCidade.get("nome")));
			}
		} else {
			if(direction.equals("ASC")) {
				query.where(predicates).distinct(true).orderBy(cb.asc(shape.get(orderBy)));	
			}else {
				query.where(predicates).distinct(true).orderBy(cb.desc(shape.get(orderBy)));	
			}
		}	
		
		TypedQuery<ShapeFile> typedQuery = em.createQuery(query);
		Long totalElements = new Long(em.createQuery(query).getResultList().size());

		if(page != null && itemsPerPage != null) {
			int firstItem = page * itemsPerPage;
			int lastItem = firstItem + itemsPerPage;

			typedQuery = typedQuery.setFirstResult(firstItem).setMaxResults(itemsPerPage);
		}
		
		List <ShapeFileDTO> shapesLista = typedQuery.getResultList().stream()
				.map((s) -> new ShapeFileDTO(s))
			.collect(Collectors.toList());
		
		ShapesFiltroPalavraChaveDTO shapes = new ShapesFiltroPalavraChaveDTO();
		shapes.setShapes(shapesLista);
		shapes.setTotalCount(totalElements);
		
		return shapes;
	}

	public ShapeFileDetalheDTO buscarShapeFilePorId(Long id) {
		ShapeFile shapefileRef = buscarPorId(id);
		ShapeFileDetalheDTO dto = new ShapeFileDetalheDTO(shapefileRef);

//		if (dto.getTipoArquivo().equals("Vetorial")) {
			dto.setShapes(shapeItemService.findPorShapeFile(id));
//		}else {
//			dto.setRaster(new RasterItemDTO(shapefileRef.getRasterItem()));
//		}

		return dto;
	}

	public ShapeFileDetalheDTO buscarShapeFilePorIdValidacao(Long id) {
		Usuario usuario = new Usuario();
		boolean isPrefeitura = false;
		boolean isAdmin  = false;
		
		try {
			usuario = usuarioContextUtil.getUsuario();			
		}catch(Exception e) {
			usuario =  null;
		}
		
		if(usuario != null && usuario.getCredencial().getListaPerfil().stream().anyMatch(perfil -> perfil.getNome().equals("Administrador"))){
			isAdmin = true;
		}
		
		if(usuario != null && usuario.getPrefeitura() != null) {
			isPrefeitura = true;
		}
		
		ShapeFile shapefileRef = buscarPorId(id);
		ShapeFileDetalheDTO dto = new ShapeFileDetalheDTO(shapefileRef);

		
		dto.setShapes(shapeItemService.findPorShapeFile(id));
		
		if(isAdmin && dto.getOrigemCadastro() == "PCS" ) {
			return dto;
		} else if(isPrefeitura && dto.getOrigemCadastro() != "PCS") {
			return dto;
		} else {
			return null;
		}	
	}
	
	public List<RelatorioShapesCriadosDTO> gerarRelatorioShapesCadastrados(Long idUsuario, String dtCadastro, String dtEdicao, String tituloShape) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<RelatorioShapesCriadosDTO> query = cb.createQuery(RelatorioShapesCriadosDTO.class);

		Root<ShapeFile> shape = query.from(ShapeFile.class);

		Join<ShapeFile, Usuario> joinUsuario = shape.join("usuario", JoinType.LEFT);

		query.multiselect(shape.get("id"), shape.get("dataHoraCadastro"), shape.get("dataHoraAlteracao"), joinUsuario.get("nome"), shape.get("titulo"));

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		if (idUsuario != null) {
			Path<Long> usuario = joinUsuario.get("id");
			predicateList.add(cb.equal(usuario, idUsuario));
		}

		if (tituloShape != null && !tituloShape.equals("")) {
			Path<String> shapeNome = shape.get("titulo");
			predicateList.add(cb.like(cb.lower(shapeNome), "%" + tituloShape + "%"));
		}

		if (dtCadastro != null && !dtCadastro.equals("")) {
			LocalDate dt = LocalDate.parse(dtCadastro);
			Path<LocalDate> dtCad = shape.get("dataHoraCadastro");
			predicateList.add(cb.equal(dtCad, dt.atStartOfDay()));
		}

		if (dtEdicao != null && !dtEdicao.equals("")) {
			LocalDate dt = LocalDate.parse(dtEdicao);
			Path<LocalDate> dtEd = shape.get("dataHoraAlteracao");
			predicateList.add(cb.equal(dtEd, dt.atStartOfDay()));
		}
		
		predicateList.add(cb.isNull(joinUsuario.get("prefeitura")));

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<RelatorioShapesCriadosDTO> typedQuery = em.createQuery(query);
		List<RelatorioShapesCriadosDTO> lista = typedQuery.getResultList();

		return lista;
	}

	public String buscarNomeArquivoShape(Long id) {
		ShapeFile shapeFile = this.buscarPorId(id);
		String nome =  "";
		if(shapeFile.getAno() != null) {
			nome = shapeFile.getTitulo() + "-" + shapeFile.getAno();
		} else {
			nome = shapeFile.getTitulo();
		}
		
		return nome.toLowerCase();
	}

	public byte[] buscarPngShape(Long id) throws IOException, ParserConfigurationException, SAXException {
		ShapeFile shapeFile = this.buscarPorId(id);

		if(shapeFile == null) throw new IllegalArgumentException("Shape não encontrado");

		ShapeFileDetalheDTO shapeFileDto = new ShapeFileDetalheDTO(shapeFile);

		if(shapeFileDto.getTipoArquivo().equals("Vetorial")) {
			shapeFileDto.setShapes(shapeItemService.findPorShapeFile(id));
			String geoJson = new GeoJSONWriterVivid().convert(shapeFileDto.getShapes());

			FeatureJSON featureJSON = new FeatureJSON();
			FeatureCollection features = featureJSON.readFeatureCollection(
					GeoJSONUtil.toReader(geoJson));

			MapContent map = this.generateMapFeatures(features);
			historicoUsoShapeService.gerarHistoricoUsoShape(shapeFile, TipoUsoShape.DOWNLOAD, TipoExportacaoHitoricoExportacaoCatalogoShape.PNG);
			return this.generateMapPng(map, features.getBounds());
		}

		if(shapeFileDto.getTipoArquivo().equals("Raster")) {
			shapeFileDto.setRaster(new RasterItemDTO(shapeFile.getRasterItem()));
			historicoUsoShapeService.gerarHistoricoUsoShape(shapeFile, TipoUsoShape.DOWNLOAD, TipoExportacaoHitoricoExportacaoCatalogoShape.PNG);
			return geoServerService.getRasterPng(shapeFileDto.getRaster().getWorkspaceName(),
					"raster_" + shapeFileDto.getRaster().getId());
		}

		throw new IllegalStateException("Não possível determinar o tipo de dado georeferenciado (Vetorial ou Raster)");
	}


	public byte[] buscarGeoJsonShape(Long id) {
		ShapeFile shapeFile = this.buscarPorId(id);

		if(shapeFile == null) throw new IllegalArgumentException("Shape não encontrado");

		ShapeFileDetalheDTO shapeFileDto = new ShapeFileDetalheDTO(shapeFile);

		if(shapeFileDto == null) throw new IllegalArgumentException("Shape não encontrado");

//		if(shapeFileDto.getTipoArquivo().equals("Vetorial")) {
			shapeFileDto.setShapes(shapeItemService.findPorShapeFile(id));
			String geoJson = new GeoJSONWriterVivid().convert(shapeFileDto.getShapes());
			historicoUsoShapeService.gerarHistoricoUsoShape(shapeFile, TipoUsoShape.DOWNLOAD, TipoExportacaoHitoricoExportacaoCatalogoShape.GEO_JSON);
			try {
				return geoJson.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
//		}

		throw new IllegalStateException("Não possível gerar um GeoJSON a partir de um raster");
	}
	
	public byte[] buscarGeoJsonShapePublicarIsTrueNaoSalvaHistorico(Long id) {
		ShapeFile shapeFile = this.buscarPorIdPublicarIsTrue(id);

		if(shapeFile == null) throw new IllegalArgumentException("Shape não encontrado");

		ShapeFileDetalheDTO shapeFileDto = new ShapeFileDetalheDTO(shapeFile);

		if(shapeFileDto == null) throw new IllegalArgumentException("Shape não encontrado");

//		if(shapeFileDto.getTipoArquivo().equals("Vetorial")) {
			shapeFileDto.setShapes(shapeItemService.findPorShapeFile(id));
			String geoJson = new GeoJSONWriterVivid().convert(shapeFileDto.getShapes());
			try {
				return geoJson.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
//		}

		throw new IllegalStateException("Não possível gerar um GeoJSON a partir de um raster");
	}

	public byte[] buscarGeoTiffShape(Long id) throws IOException, SAXException, ParserConfigurationException {
		ShapeFile shapeFile = this.buscarPorId(id);

		if(shapeFile == null) throw new IllegalArgumentException("Shape não encontrado");

		ShapeFileDetalheDTO shapeFileDto = new ShapeFileDetalheDTO(shapeFile);

		if(shapeFileDto.getTipoArquivo().equals("Raster")) {
			shapeFileDto.setRaster(new RasterItemDTO(shapeFile.getRasterItem()));
			historicoUsoShapeService.gerarHistoricoUsoShape(shapeFile, TipoUsoShape.DOWNLOAD, TipoExportacaoHitoricoExportacaoCatalogoShape.GEO_TIFF);
			return geoServerService.getRasterGeoTiff(shapeFileDto.getRaster().getWorkspaceName(),
					"raster_" + shapeFileDto.getRaster().getId());
		}

		throw new IllegalStateException("Não possível gerar um TIFF a partir de dados vetoriais");
	}

	public byte[] buscarShapefileShape2(Long id) throws Exception {
		ShapeFile shapeFile = this.buscarPorId(id);

		if(shapeFile == null) throw new IllegalArgumentException("Shape não encontrado");

		ShapeFileDetalheDTO shapeFileDto = new ShapeFileDetalheDTO(shapeFile);

//		if(shapeFileDto.getTipoArquivo().equals("Vetorial")) {
			shapeFileDto.setShapes(shapeItemService.findPorShapeFile(id));
//		}
		historicoUsoShapeService.gerarHistoricoUsoShape(shapeFile, TipoUsoShape.DOWNLOAD, TipoExportacaoHitoricoExportacaoCatalogoShape.SHAPE_FILE);
		return ShapeUtil.exportShapefileFromFeatures(shapeFileDto.getShapes(),shapeFileDto.getTitulo());
	
	}
	public byte[] buscarShapefileShape(Long id) throws Exception {
		ShapeFile shapeFile = this.buscarPorId(id);

		if(shapeFile == null) throw new IllegalArgumentException("Shape não encontrado");

		ShapeFileDetalheDTO shapeFileDto = new ShapeFileDetalheDTO(shapeFile);

//		if(shapeFileDto.getTipoArquivo().equals("Vetorial")) {
			shapeFileDto.setShapes(shapeItemService.findPorShapeFile(id));
			String geoJson = new GeoJSONWriterVivid().convert(shapeFileDto.getShapes());

			FeatureJSON featureJSON = new FeatureJSON();
			FeatureCollection features = featureJSON.readFeatureCollection(
					GeoJSONUtil.toReader(geoJson));

			String filename = shapeFileDto.getTitulo() ;

			new WriteShapefile(new File(filename + ".shp")).writeFeatures(features);

			List<String> paths = Arrays.asList(new File(".").list());
			paths = paths.stream()
					.filter((p) -> p.contains(filename))
					.collect(Collectors.toList());

			String zipFilePath = ZipFileWriter.zip(paths);

			File zipFile = new File(zipFilePath);
			byte[] bytes = IOUtils.toByteArray(new FileInputStream(zipFile));

			paths.add(zipFilePath);

			for(String path : paths) {
				new File(path).delete();
			}
			if(zipFile.exists()) {
				zipFile.delete();
			}
			

			historicoUsoShapeService.gerarHistoricoUsoShape(shapeFile, TipoUsoShape.DOWNLOAD, TipoExportacaoHitoricoExportacaoCatalogoShape.SHAPE_FILE);

			return bytes;
//		}

//		throw new IllegalStateException("Não possível gerar um shapefile a partir de um raster");
	}

	private MapContent generateMapFeatures(FeatureCollection features) {
		MapContent mapContent = new MapContent();
		mapContent.setTitle("map");
		Style style = SLD.createPolygonStyle(Color.BLACK, new Color(0, 117, 0), 1.0f);

		Layer layer = new FeatureLayer(features, style);
		mapContent.addLayer(layer);

		return mapContent;
	}

	private byte[] generateMapPng(MapContent map, ReferencedEnvelope bounds) {
		byte[] bytes = new byte[1];

		String outputPath = UUID.randomUUID().toString();

		File outputFile = new File(outputPath + ".png");
		ImageOutputStream outputImageFile = null;
		FileOutputStream fileOutputStream = null;

		try {
			fileOutputStream = new FileOutputStream(outputFile);
			outputImageFile = ImageIO.createImageOutputStream(fileOutputStream);

			int w = 1000;
			int h = (int) (w * (bounds.getHeight() / bounds.getWidth()));

			BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = bufferedImage.createGraphics();

			map.getViewport().setMatchingAspectRatio(true);
			map.getViewport().setScreenArea(new Rectangle(Math.round(w), Math.round(h)));
			map.getViewport().setBounds(bounds);

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			Rectangle outputArea = new Rectangle(w, h);

			GTRenderer renderer = new StreamingRenderer();
			LabelCacheImpl labelCache = new LabelCacheImpl();
			Map<Object, Object> hints = renderer.getRendererHints();

			if (hints == null) {
				hints = new HashMap<>();
			}

			hints.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
			renderer.setRendererHints(hints);
			renderer.setMapContent(map);
			renderer.paint(g2d, outputArea, bounds);
			ImageIO.write(bufferedImage, "png", outputImageFile);

			bytes = IOUtils.toByteArray(new FileInputStream(outputFile));

			outputFile.delete();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (outputImageFile != null) {
					outputImageFile.flush();
					outputImageFile.close();
					fileOutputStream.flush();
					fileOutputStream.close();
				}
			} catch (IOException e) {

			}
		}

		map.dispose();

		return bytes;
	}

	public ShapeFile publicar(Long id) {
		ShapeFile shapefile = buscarPorId(id);
		shapefile.setPublicar(!shapefile.getPublicar());
		shapefile = repository.save(shapefile);
		return shapefile;
	}

	public void salvarShapeZoneamentoCidade(List<Feature> shapes, Cidade cidadeDoShape) {

		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuarioLogado = usuarioService.buscarPorEmailCredencial(user);

		ShapeFile shapefile = repository.buscarShapeZoneamento(cidadeDoShape.getId());
		List<ShapeItem> listaParaSalvar = new ArrayList<>();
		ShapeFile sf = new ShapeFile();

		for (Feature shape : shapes) {
			ShapeItem shapeItem = new ShapeItem();
			shapeItem.setAtributos("");
			Geometry geo = shapeItemService.convertGeoJsonToJtsGeometry(shape.getGeometry());
			shapeItem.setShape(geo);
			shapeItem.setShapeFile(sf);

			if(shapefile != null) {
				shapeItem.setShapeFile(shapefile);
			}

			Gson gson = new Gson();

			String jsonString = gson.toJson(shape.getProperties());
			shapeItem.setAtributos(jsonString);
			listaParaSalvar.add(shapeItem);
		}

		if (shapefile != null) {
			shapeItemService.clearShapes(shapefile);
			shapefile.getShapes().addAll(listaParaSalvar);
			shapefile.setDataHoraAlteracao(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
			shapefile.setAno(Year.now().getValue());
			repository.save(shapefile);
		} else {
			sf.setTitulo("$shape_zoneamento$");
			sf.setShapes(listaParaSalvar);
			sf.setCidade(cidadeDoShape);
			sf.setPrefeitura(usuarioLogado.getPrefeitura());
			sf.setUsuario(usuarioLogado);
			sf.setTipoArquivo("SHP");
			sf.setPublicar(true);
			sf.setDataHoraCadastro(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
			sf.setAno(Year.now().getValue());
			repository.save(sf);
		}
	}

//	public List<Feature> buscarShapeZoneamento(Long idCidade) {
//		ShapeFile shapefile = repository.buscarShapeZoneamento(idCidade);
//		if (shapefile != null) {
//			return shapeItemService.findPorShapeFile(shapefile.getId());
//		}else {
//			return null;
//		}
//	}
	
	public List<Feature> buscarShapeZoneamento(Long idCidade) {
		ShapeFile shapefileCidade = repository.buscarShapeZoneamento(idCidade);
		if(shapefileCidade != null) {
			return shapeItemService.findPorShapeFile(shapefileCidade.getId());
		}else { //retorna o poligono default
			ShapeFile shapefileMunicipios = repository.buscarShapeZoneamentoMunicipios("BR_Municipios_2019");
			Cidade cidadeRef = cidadeService.buscarPorId(idCidade);
			if (shapefileMunicipios != null && cidadeRef.getCodigoIbge() != null) {
				return shapeItemService.findPorAtributosPorShapeFile("CD_MUN",cidadeRef.getCodigoIbge(), shapefileMunicipios.getId());
			}else {
				return null;
			}
		}
	}

	public Boolean exists(Long id) {
		return repository.existsById(id);
	}

	public byte[] exportShapefileFromFeatures(List<Feature> listaFeature, String nomeDoShape) throws Exception {


		SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
		b.setName( "Flag" );
		b.setCRS( DefaultGeographicCRS.WGS84 );
		b.add( "location", Point.class );

		if(listaFeature != null && !listaFeature.isEmpty()) {
			for (Map.Entry<String, Object> entry : listaFeature.get(0).getProperties().entrySet()) {
				String key = entry.getKey();
				b.add(key, String.class);
			}
		}

		SimpleFeatureType schema = b.buildFeatureType();
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
		GeometryFactory geometryFactory = new GeometryFactory();

		List<SimpleFeature> geoms =  new ArrayList<>();

		for(Feature feature :listaFeature) {

			Geometry geo = shapeItemService.convertGeoJsonToJtsGeometry(feature.getGeometry());
			featureBuilder.add( WKTUtils$.MODULE$.read(geometryFactory.createPoint( geo.getCoordinate() ).toString()));
			for (Map.Entry<String, Object> entry : feature.getProperties().entrySet()) {
				Object value = entry.getValue();
				if(value != null ) {
					featureBuilder.add(value.toString());
				} else {
					featureBuilder.add("");
				}
				
			}
			SimpleFeature featureShape = featureBuilder.buildFeature(null);
			geoms.add(featureShape);
		}

		byte[] bytes = null;

		if(!geoms.isEmpty()) {
			int random = new Random().nextInt(100000);
			File pasta = new File("shapes/"+random);
			pasta.mkdirs();		
			
			SimpleFeatureCollection collection = new ListFeatureCollection(schema, geoms);
			new WriteShapefile(new File(pasta, nomeDoShape + ".shp")).writeFeatures(collection);

			List<String> paths = Arrays.asList(pasta.list());
			paths = paths.stream()
					.filter((p) -> p.contains(nomeDoShape))
					.collect(Collectors.toList());
			for(int i = 0 ; i < paths.size() ; i ++) {
				String path = paths.get(i);
				path = pasta.getPath()+"/" +path;
				paths.set(i, path);
				
			}
			String zipFilePath = ZipFileWriter.zip(paths);

			bytes = IOUtils.toByteArray(new FileInputStream(new File(zipFilePath)));

			paths.add(zipFilePath);

			for(String path : paths) {
				new File(path).delete();
			}		
		}


		return bytes;

	}


	public byte[] exportShapefileFromIdsCidades(List<Long> idsCidadesBoasPraticas) throws Exception {
		List<Feature> listaFeatureAll = new ArrayList<Feature>();

		for(Long idCidade : idsCidadesBoasPraticas) {
			List<Feature> listaFeature = this.buscarShapeZoneamento(idCidade);
			listaFeatureAll.addAll(listaFeature);
		}

		SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
		b.setName( "Flag" );
		b.setCRS( DefaultGeographicCRS.WGS84 );
		b.add( "location", MultiPolygon.class );

		if(listaFeatureAll != null && !listaFeatureAll.isEmpty()) {
			for (Map.Entry<String, Object> entry : listaFeatureAll.get(0).getProperties().entrySet()) {
				String key = entry.getKey();
				b.add(key, String.class);
			}
		}

		SimpleFeatureType schema = b.buildFeatureType();
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
		GeometryFactory geometryFactory = new GeometryFactory();

		List<SimpleFeature> geoms =  new ArrayList<>();

		for(Feature feature :listaFeatureAll) {

			com.vividsolutions.jts.geom.Geometry geo = shapeItemService.convertGeoJsonToJtsGeometry(feature.getGeometry());
			if(geo instanceof com.vividsolutions.jts.geom.MultiPolygon) {
				com.vividsolutions.jts.geom.MultiPolygon mp = (com.vividsolutions.jts.geom.MultiPolygon) geo;
				com.vividsolutions.jts.geom.Polygon[] polys = new com.vividsolutions.jts.geom.Polygon[mp.getNumGeometries()];
				for (int i = 0; i < mp.getNumGeometries(); i += 1) {
					polys[i] = (com.vividsolutions.jts.geom.Polygon) mp.getGeometryN(i);
				}
				featureBuilder.add( WKTUtils$.MODULE$.read(geometryFactory.createMultiPolygon(polys).toString()));
			}else if (geo instanceof com.vividsolutions.jts.geom.Polygon) {
				com.vividsolutions.jts.geom.Polygon pol = (com.vividsolutions.jts.geom.Polygon) geo;
				featureBuilder.add( WKTUtils$.MODULE$.read(geometryFactory.createPolygon(pol.getCoordinates()).toString()));
			}

			for (Map.Entry<String, Object> entry : feature.getProperties().entrySet()) {
				Object value = entry.getValue();
				featureBuilder.add(value);
			}
			SimpleFeature featureShape = featureBuilder.buildFeature(null);
			geoms.add(featureShape);
		}

		byte[] bytes = null;

		if(!geoms.isEmpty()) {
			SimpleFeatureCollection collection = new ListFeatureCollection(schema, geoms);
			new WriteShapefile(new File("shapefile" + ".shp")).writeFeatures(collection);

			List<String> paths = Arrays.asList(new File(".").list());
			paths = paths.stream()
					.filter((p) -> p.contains("shapefile"))
					.collect(Collectors.toList());

			String zipFilePath = ZipFileWriter.zip(paths);

			bytes = IOUtils.toByteArray(new FileInputStream(new File(zipFilePath)));

			paths.add(zipFilePath);

			for(String path : paths) {
				new File(path).delete();
			}
		}


		return bytes;

	}
	@Transactional
	public int mesclarAtributos(MesclagemAtributoDTO dto, Principal principal) {
		Gson gson = new Gson();
		Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
		if(usuario == null) {
			throw new SecurityException("Você deve estar logado para utilizar essa função");
		}
		JsonObject  jsonMesclagemDTO = new JsonParser().parse(gson.toJson(dto)).getAsJsonObject();
		JsonElement dadosMesclagem = jsonMesclagemDTO.get("dados");
		dadosMesclagem.getAsJsonArray().remove(0);

		int posicaoAtributoRelacionamento = dto.getColunas().indexOf(dto.getAtributo());
		int qtdDeObjetosAlterados = 0;
		for(JsonElement elemento : dadosMesclagem.getAsJsonArray()) {
			try {
				String idReferencia = elemento.getAsJsonArray().get(posicaoAtributoRelacionamento).getAsString();
				List<ShapeItemDTO> shapeItensDTO = shapeItemService.findByShapeFileIdAndAtributo(dto.getIdShape(), dto.getAtributo(), idReferencia);
				if(!shapeItensDTO.isEmpty()) {
					for(ShapeItemDTO siDTO : shapeItensDTO ) {
						Long idShapeItem = siDTO.getId();
						String atributos = siDTO.getAtributos();
						JsonObject atributosJSON = gson.fromJson(atributos, JsonObject.class);
						
						for(int i = 0 ;i < elemento.getAsJsonArray().size(); i++) {
							if(i != posicaoAtributoRelacionamento) {
								String nomeAtributo = dto.getColunas().get(i);
								String valor = !elemento.getAsJsonArray().get(i).isJsonNull() ? elemento.getAsJsonArray().get(i).getAsString() : null;
								
								if(NumeroUtil.isANumber(valor)) {
									double valorNumerico = Double.valueOf(valor);
									atributosJSON.addProperty(nomeAtributo, valorNumerico);
								} else {
									atributosJSON.addProperty(nomeAtributo, valor);
								}
								atualizarAtributo(idShapeItem, atributosJSON.toString());
								qtdDeObjetosAlterados++; 
							}
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return qtdDeObjetosAlterados;

	}
	
	@Transactional
	private void atualizarAtributo(Long idIndicador, String atributos) {
		Query q = em.createNativeQuery("update shape_itens set atributos = cast(:atributos as json) where id = :id");
		q.setParameter("id" , idIndicador);
		q.setParameter("atributos" , atributos);
		q.executeUpdate();
	}
	
	public List<ShapeListagemMapaDTO> buscarShapesListagemMapa() {
		return repository.buscarShapesListagemMapa();
	}

	public File gerarArquivoAtributos(Long idShapeFile) throws IOException {
		Gson gson =new Gson();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet aba = workbook.createSheet("Atributos");
 		List<ShapeItemDTO> shapes = shapeItemService.findDtoByShapeFileId(idShapeFile);
		List<String> cabecalho = new ArrayList<>();
		
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		Row headerRow = aba.createRow(0);
		int indiceLinha = 0;
		for(ShapeItemDTO shapeItem: shapes) {
			indiceLinha++;
			JsonObject  objJson = gson.fromJson(shapeItem.getAtributos(), JsonObject.class);
			Row linha = aba.createRow(indiceLinha);
			for(String key: objJson.keySet()) {
				gerarCabecalhoArquivoAtributos(cabecalho, headerRow, key, headerCellStyle);
				gerarLinha(cabecalho, linha, key, objJson.get(key).toString());
			}
		}
		
		
		File arquivo = new File("atributos_"+LocalDateTime.now().getNano()+".xlsx");
		FileOutputStream out = 
				new FileOutputStream(arquivo);
		workbook.write(out);
		workbook.close();
		out.close();
		
		
		return arquivo;
	}
	
	private void gerarCabecalhoArquivoAtributos(List<String> cabecalho, Row rowCabecalho, String atributo, CellStyle headerCellStyle) {
		if(!cabecalho.contains(atributo)) {
			cabecalho.add(atributo);
			int indiceAtributo = cabecalho.indexOf(atributo);
			Cell cell = rowCabecalho.createCell(indiceAtributo);
			cell.setCellValue(atributo);
			cell.setCellStyle(headerCellStyle);	
		}
		
	}
	
	private void gerarLinha(List<String> cabecalho, Row linha, String atributo, String valor) {
		int indiceAtributo = cabecalho.indexOf(atributo);
		Cell cell = linha.createCell(indiceAtributo);
		String texto = valor;
		if(!texto.isEmpty() && texto.startsWith("\"") && texto.endsWith("\"")) {
			texto = texto.substring(1,texto.length()-1);
		}
		cell.setCellValue(texto);
		
	}
	
	public ShapeFile editarShapeFile(ShapeFileDTO shapefileDTO) {
		ShapeFile shapefileRef = shapefileDTO.toEntityUpdate(buscarPorId(shapefileDTO.getId()));
		
		if(shapefileRef.getExibirAuto() == true) {
			repository.setExibirAutoToFalseAll();
		}	
			
		
		
		List<AreaInteresse> listaAreaInteresse = new ArrayList<>();
		for (Long id_area : shapefileDTO.getAreasInteresse()) {
			AreaInteresse areaRef = areaInteresseService.buscarPorId(id_area);
			listaAreaInteresse.add(areaRef);
		}

		List<Indicador> listaIndicadores = new ArrayList<>();
		for (Long idIndicador : shapefileDTO.getIndicadores()) {
			Indicador indicador = indicadorService.listarById(idIndicador);
			listaIndicadores.add(indicador);
		}
		
		List<Eixo> listaEixo = new ArrayList<>();
		if(shapefileDTO.getEixos() != null) {
			for (Long id_eixo : shapefileDTO.getEixos()) {
				Eixo areaRef = eixoService.listarById(id_eixo);
				listaEixo.add(areaRef);
			}
		}
		
		List<ObjetivoDesenvolvimentoSustentavel> listaODS = new ArrayList<>();
		if(shapefileDTO.getOds() != null) {
			for (Long id_ods : shapefileDTO.getOds()) {
				ObjetivoDesenvolvimentoSustentavel odsRef = odsService.listarPorId(id_ods);
				listaODS.add(odsRef);
			}
		}
		
		List<MetaObjetivoDesenvolvimentoSustentavel> listaMetas = new ArrayList<>();
		if (shapefileDTO.getMetas() != null) {
			for (Long id_meta : shapefileDTO.getMetas()) {
				MetaObjetivoDesenvolvimentoSustentavel metaRef = metaService.find(id_meta);
				listaMetas.add(metaRef);
			}
		}
		
		List<Cidade> listaCidades = new ArrayList<>();
		if(shapefileDTO.getCidades() != null) {
			for (Long id_cidade : shapefileDTO.getCidades()) {
			Cidade cidadeRef = cidadeService.buscarPorId(id_cidade);
			listaCidades.add(cidadeRef);
			}
		}
		
		List<ProvinciaEstado> listaEstados = new ArrayList<>();
		if(shapefileDTO.getEstados() != null) {
			for (Long id_estado : shapefileDTO.getEstados()) {
				ProvinciaEstado estadosRef = estadoService.buscarPorId(id_estado);
				listaEstados.add(estadosRef);
			}
		}
		
		shapefileRef.setDataHoraAlteracao(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		shapefileRef.setEstados(listaEstados);
		shapefileRef.setCidades(listaCidades);
		shapefileRef.setEixos(listaEixo);
		shapefileRef.setOds(listaODS);
		shapefileRef.setMetas(listaMetas);
		shapefileRef.setAreasInteresse(listaAreaInteresse);
		shapefileRef.setIndicadores(listaIndicadores);
		shapefileRef.setTemaGeoespacial(shapefileDTO.getTemaGeoespacial() != null ? temaGeoespacialService.buscar(shapefileDTO.getTemaGeoespacial()) : null);
		shapefileRef.setIndicador(shapefileDTO.getIndicador() != null ? indicadorService.listarById(shapefileDTO.getIndicador()) : null);
		shapefileRef.setPais(shapefileDTO.getPais() != null ? paisService.buscarPorId(shapefileDTO.getPais()) : null);
		shapefileRef = repository.save(shapefileRef);
		return shapefileRef;
	}
	
	public List<RelatorioShapesExportadosDTO> gerarRelatorioShapesExportados(Long idPerfil, Long idCidade) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<RelatorioShapesExportadosDTO> query = cb.createQuery(RelatorioShapesExportadosDTO.class);

		Root<HistoricoUsoShape> historicoUsoShape = query.from(HistoricoUsoShape.class);

		Join<HistoricoUsoShape, ShapeFile> joinShapeFile = historicoUsoShape.join("shape", JoinType.INNER);
		Join<HistoricoUsoShape, Usuario> joinUsuario = historicoUsoShape.join("usuario", JoinType.LEFT);
		Join<HistoricoUsoShape, Cidade> joinCidade = historicoUsoShape.join("cidade",JoinType.LEFT);
		Join<Usuario, Credencial> joinCredencial = joinUsuario.join("credencial", JoinType.INNER);
		Join<Credencial, Perfil> joinPerfil = joinCredencial.joinList("listaPerfil", JoinType.INNER);

		query.multiselect(joinUsuario.get("nome"), joinCidade.get("nome"), joinShapeFile.get("titulo"), historicoUsoShape.get("dataHoraAcesso"));
		query.distinct(true);

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();

		if (idPerfil != null) {
			Path<Long> perfil = joinPerfil.get("id");
			predicateList.add(cb.equal(perfil, idPerfil));
		}

		if (idCidade != null) {
			Path<Long> cidade = joinCidade.get("id");
			predicateList.add(cb.equal(cidade, idCidade));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<RelatorioShapesExportadosDTO> typedQuery = em.createQuery(query);
		List<RelatorioShapesExportadosDTO> lista = typedQuery.getResultList();

		return lista;
	}

	
	public List<RelatorioShapesCriadosDTO> gerarRelatorioShapesCadastradosPrefeitura(Long idUsuario, Long idCidade, String dtCadastro, String dtEdicao, String tituloShape) {

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<RelatorioShapesCriadosDTO> query = cb.createQuery(RelatorioShapesCriadosDTO.class);

		Root<ShapeFile> shape = query.from(ShapeFile.class);

		Join<ShapeFile, Usuario> joinUsuario = shape.join("usuario", JoinType.INNER);
		Join<Usuario, Prefeitura> joinPrefeitura = shape.join("prefeitura",JoinType.INNER);
		Join<Prefeitura, Cidade> joinCidade = joinPrefeitura.join("cidade",JoinType.INNER);
		
		query.multiselect(shape.get("id"), shape.get("dataHoraCadastro"), shape.get("dataHoraAlteracao"), joinUsuario.get("nome"), joinCidade.get("nome"), shape.get("titulo"));

		List<javax.persistence.criteria.Predicate> predicateList = new ArrayList<>();
		
		if(idUsuario != null) {
			Path<Long> usuario = joinUsuario.get("id");
			predicateList.add(cb.equal(usuario, idUsuario));
		}

		if (idCidade != null) {
			Path<Long> cidade = joinCidade.get("id");
			predicateList.add(cb.equal(cidade, idCidade));
		}

		if (tituloShape != null && !tituloShape.equals("")) {
			Path<String> shapeNome = shape.get("titulo");
			predicateList.add(cb.like(cb.lower(shapeNome), "%" + tituloShape + "%"));
		}

		if (dtCadastro != null && !dtCadastro.equals("")) {
			LocalDate dt = LocalDate.parse(dtCadastro);
			Path<LocalDate> dtCad = shape.get("dataHoraCadastro");
			predicateList.add(cb.equal(dtCad, dt.atStartOfDay()));
		}

		if (dtEdicao != null && !dtEdicao.equals("")) {
			LocalDate dt = LocalDate.parse(dtEdicao);
			Path<LocalDate> dtEd = shape.get("dataHoraAlteracao");
			predicateList.add(cb.equal(dtEd, dt.atStartOfDay()));
		}

		javax.persistence.criteria.Predicate[] predicates = new javax.persistence.criteria.Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		query.where(predicates);

		TypedQuery<RelatorioShapesCriadosDTO> typedQuery = em.createQuery(query);
		List<RelatorioShapesCriadosDTO> lista = typedQuery.getResultList();

		return lista;
	}
	
	public ShapeFileDTO buscarFileNameShapeFilePorId(Long idShapeFile) {
		String fileName = repository.buscarFileNameShapeFilePorId(idShapeFile);
		ShapeFileDTO shapeFileDTO = new ShapeFileDTO();
		shapeFileDTO.setId(idShapeFile);
		shapeFileDTO.setFileName(fileName);
		return shapeFileDTO;
	}

	public boolean camadaExiste(String titulo, Long id) {
		if(id != null) {
			boolean existe =  repository.existsByTituloAndIdNot(titulo.trim(),id);
			return existe;
		
		} else {
			boolean existe =  repository.existsByTitulo(titulo.trim());
			return existe;
		}
	}
	
	public List<ShapeFileOpenEndPointDTO> buscarShapeFileOpenEndPointDTO () {
		List<ShapeFileOpenEndPointDTO> list = repository.buscarShapeFileOpenEndPointDTO();
		String urlAPI = profileUtil.getProperty("profile.api");
		
		list.forEach(shape -> {
			shape.setUrl(urlAPI + "/integracao/v1/camadas/" + shape.getId());
		});
		
		return list;
	}
	
	@Transactional
	public ConfirmacaoCriacaoShapeFileDTO salvarShapeFileSubdivisao(String subdivisao, MultipartFile arquivoZip) throws Exception {
		SubdivisaoDTO subdivisaoDTO = jsonToObjSubdivisao(subdivisao);
    	int random = new Random().nextInt(100000);
		File pasta = new File("shapesimportados/"+random);
		pasta.mkdirs();
		
		List<File> listFiles = this.unzip(arquivoZip);
		File arquivoShp = null;
		File arquivoDbf = null;
		File arquivoShx = null;
		
		 for(File f : listFiles){
	           String fileName = f.getName();
	           String fileExtension = fileName.substring(fileName.indexOf(".") + 1, f.getName().length());
	           fileExtension = fileExtension.toLowerCase();
	           if(fileExtension.equals("shp")){
	        	   arquivoShp = f;
	           }
	           if(fileExtension.equals("dbf")){
	        	   arquivoDbf = f;
		       }
	           if(fileExtension.equals("shx")){
	        	   arquivoShx = f;
		       }
	     }
		
		
		//String filename = UUID.randomUUID().toString();
		String filename = FilenameUtils.removeExtension(arquivoZip.getOriginalFilename());
		
		
		byte[] shpBytes = Files.readAllBytes(arquivoShp.toPath());
		byte[] dbfBytes = Files.readAllBytes(arquivoDbf.toPath());
		byte[] dshxBytes = Files.readAllBytes(arquivoShx.toPath());
		
		File fileShpAux = new File(pasta,filename + ".shp");
		FileUtils.writeByteArrayToFile(fileShpAux, shpBytes);

		File fileDbfAux = new File(pasta,filename + ".dbf");
		FileUtils.writeByteArrayToFile(fileDbfAux, dbfBytes);

		File fileShxAux = new File(pasta,filename + ".shx");
		FileUtils.writeByteArrayToFile(fileShxAux, dshxBytes);
		
		List<File> lista = new ArrayList<>();
		lista.add(fileShpAux);
		lista.add(fileDbfAux);
		//lista.add(fileShxAux); Fazer teste adcionando este arquivo
		
		File zipFile = new File(pasta,filename + ".zip");
		this.packZip(filename, zipFile, lista);


		ShapeFile newShapeFile = new ShapeFile();
		newShapeFile.setFileName(filename);
		newShapeFile.setTitulo(subdivisaoDTO.getNome());
		newShapeFile.setTipoArquivo("SHP");
		newShapeFile.setPublicar(false);


		newShapeFile.setDataHoraCadastro(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		if(usuarioContextUtil != null && usuarioContextUtil.getUsuario() != null ) {
			newShapeFile.setUsuario(usuarioContextUtil.getUsuario());
			newShapeFile.setPrefeitura(usuarioContextUtil.getUsuario().getPrefeitura());			
		}

		List<ShapeItem> listShapeItem = new ArrayList<>();

		ShapefileDataStore shapefileDataStore = new ShapefileDataStore(fileShpAux.toURI().toURL());
		
		shapefileDataStore.setCharset(Charset.forName("UTF-8"));
		SimpleFeatureIterator features = shapefileDataStore.getFeatureSource().getFeatures().features();
		SimpleFeature shp;
		while (features.hasNext()) {
			shp = features.next();

			HashMap<String, Object> elements = new HashMap<String, Object>();
			Geometry geometry = null;

			Collection<Property> att = shp.getProperties();
			for(Property pro : att) {
				if(!(pro.getValue() instanceof MultiPolygon || pro.getValue() instanceof MultiLineString || pro.getValue() instanceof MultiPoint 
						|| pro.getValue() instanceof Point || pro.getValue() instanceof LineString || pro.getValue() instanceof Polygon)) {
					elements.put(pro.getName().toString(), pro.getValue());
				}else {
					//MultiPolygon mp = (MultiPolygon) pro.getValue();
					WKTReader wktr = new WKTReader();
					try {
						geometry = wktr.read(pro.getValue().toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}

			Gson gson = new Gson();
			Type gsonType = new TypeToken<HashMap>(){}.getType();
			String gsonString = gson.toJson(elements,gsonType);
			if(gsonString.contains("�") && !gsonString.contains("�\"")) {
				features.close();
				shapefileDataStore.dispose();
				shapefileDataStore = new ShapefileDataStore(fileShpAux.toURI().toURL());
				features = shapefileDataStore.getFeatureSource().getFeatures().features();
				listShapeItem = new ArrayList<>();
			} else {

				ShapeItem shapeItem = new ShapeItem();
				shapeItem.setShape(geometry);
				shapeItem.setShapeFile(newShapeFile);
				shapeItem.setAtributos(gsonString);

				listShapeItem.add(shapeItem);
			}
			
		}

		features.close();
		shapefileDataStore.dispose();

		newShapeFile.setShapes(listShapeItem);

		newShapeFile = repository.save(newShapeFile);
		historicoShapeService.save(newShapeFile);

		this.deleteFiles(fileShpAux, fileDbfAux, fileShxAux);
		zipFile.delete();

		Usuario usuarioLogado = this.getUsuario();

		boolean shapePertenceAPrefeitura = usuarioLogado.getPrefeitura() != null;
        boolean temInterseccao = false;

        if(usuarioLogado != null && shapePertenceAPrefeitura) {
            temInterseccao = this.checarShapeEstaDentroDaCidadePrefeitura(newShapeFile, usuarioLogado);

           // if(temInterseccao == false && shapefileDto.getPublicar()) {
            if(temInterseccao == false) {
				this.enviarAlertaShapeForaDaPrefeitura(newShapeFile, usuarioLogado);
			}
        }
        
        subdivisaoService.inserir(subdivisaoDTO);

		return new ConfirmacaoCriacaoShapeFileDTO(newShapeFile, shapePertenceAPrefeitura, temInterseccao);
	}
	
	public SubdivisaoDTO jsonToObjSubdivisao(String jsonObject) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonObject, SubdivisaoDTO.class);		
	}
	
	private List<File> unzip(MultipartFile arquivoZip) {
	    List<File> files = new ArrayList<>();
	    try {
	        ZipInputStream zin = new ZipInputStream(arquivoZip.getInputStream());
	        ZipEntry entry = null;
	        while((entry = zin.getNextEntry()) != null) {
	            File file = new File(entry.getName());
	            FileOutputStream  os = new FileOutputStream(file);
	            for (int c = zin.read(); c != -1; c = zin.read()) {
	                os.write(c);
	            }
	            files.add(file);
	            
	            os.close();
	            zin.closeEntry();
	            
	        }
	        zin.close();
	    } catch (IOException e) {
	        //log.error("Error while extract the zip: "+e);
	    }
	    return files;
	  
	}
	
	public ShapeFile buscarPorSubdivisaoCidade(Long idSubdivisaoCidade){
		return repository.findByidSubdivisaoCidade(idSubdivisaoCidade);
	}
	
	public Long buscarShapeFileIdPorSubdivisaoId(Long idSubdivisaoCidade){
		return repository.buscarShapeFileIdPorSubdivisaoId(idSubdivisaoCidade);
	}

	public ShapeFile buscarShapeFilePorSubdivisaoId(Long id) {
		return repository.findBySubdivisaoCidadeId(id);
	}
	
	
	
	public ShapeFileVisualizarDetalheDTO buscarShapeFileVisualizarDetalheDTOPorIdShapeFile(Long idShapeFile) {
		
		ShapeFileVisualizarDetalheDTO shape = repository.buscarShapeFileVisualizarDetalheDTOPorIdShapeFile(idShapeFile);
		shape.setShapes(shapeItemService.findPorShapeFile(idShapeFile));
		return shape;
	}
	
	public List<ObjetivoDesenvolvimentoSustentavel> buscarOdsDoShapeFileId(Long idShapeFile){
		return repository.buscarOdsDoShapeFileId(idShapeFile);
	}

	public byte[] buscarShapefileCGEE(String nomeCamada) throws Exception {
		ShapeFile shapeFile = repository.buscarShapeCGEE(nomeCamada);

		if(shapeFile == null) throw new IllegalArgumentException("Shape não encontrado");

		ShapeFileDetalheDTO shapeFileDto = new ShapeFileDetalheDTO(shapeFile);

//		if(shapeFileDto.getTipoArquivo().equals("Vetorial")) {
			shapeFileDto.setShapes(shapeItemService.findPorShapeFile(shapeFile.getId()));
//		}
		historicoUsoShapeService.gerarHistoricoUsoShape(shapeFile, TipoUsoShape.DOWNLOAD, TipoExportacaoHitoricoExportacaoCatalogoShape.SHAPE_FILE);
		return ShapeUtil.exportShapefileFromFeatures(shapeFileDto.getShapes(),shapeFileDto.getTitulo());
	
	}
	
	public String buscarNomeArquivoShapeCGEE(String nomeCamada) {
		ShapeFile shapeFile = repository.buscarShapeCGEE(nomeCamada);
		String nome =  "";
		if(shapeFile.getAno() != null) {
			nome = shapeFile.getTitulo() + "-" + shapeFile.getAno();
		} else {
			nome = shapeFile.getTitulo();
		}
		
		return nome.toLowerCase();
	}
	
	
}

