package br.org.cidadessustentaveis.services;

import java.io.*;
import java.net.MalformedURLException;

import br.org.cidadessustentaveis.dto.BoundingBoxDTO;
import br.org.cidadessustentaveis.util.RestClient;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder.ProjectionPolicy;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@Service
public class GeoServerService {
	
	public final String workspaceRasterName = "wsnRaster";
	public final String rasterDefaultStyle = "raster";
	
	public void conectToGeoServer() {

		GeoServerRESTReader reader = getReader();
		GeoServerRESTPublisher publisher = getPublisher();
		
		createWorkspace(publisher, "myWorkspaceJava");
		
		createStyle(publisher, new File(""), "myStyle");
		
		createLayerFromShapeFile("myWorkspaceJava", "myStore", "cities", new File(""), "EPSG:4326", "default_point");
		
		removeLayer(publisher, "myWorkspaceJava", "myStore", "cities");
		
		createLayerFromDbTable(publisher, "keyword", "tableName", "EPSG:4326", "default_polygon", "myWorkspaceJava", "dataStoreConfigured");
	}
	
	public boolean createWorkspace(GeoServerRESTPublisher publisher, String workspaceName) {
		return publisher.createWorkspace(workspaceName);	
	}
	
	public boolean createStyle(GeoServerRESTPublisher publisher, File sldFile, String styleName) {
		return publisher.publishStyle(sldFile, styleName);
//		return publisher.publishStyle(sldFile); // Will take the name from SLD contents
	}
	
	/* The layername (“cities”) should be the same as the shapefile (.shp) in the .zip file. In this example, we have “cities.shp” in the zipfile. */
	public boolean createLayerFromShapeFile(String workspaceName, String storename, String layerName, File file, String srs, String deafultStyle) {
		try {
			GeoServerRESTPublisher publisher = getPublisher();
			//return publisher.publishShp(workspaceName, storename, layerName, file, srs, deafultStyle);
			return publisher.publishShp(workspaceName, storename, layerName, file);
		} catch (FileNotFoundException | IllegalArgumentException e) {
			//			e.printStackTrace();
			return false;
		}		
	}
	
	public void removeLayer(GeoServerRESTPublisher publisher, String workspaceName, String storename, String layerName) {
		publisher.unpublishFeatureType(workspaceName, storename, layerName);
		publisher.removeCoverageStore(workspaceName, storename, true);
	}
	
	/* 'dataStoreConfigured' is a PostGIS datastore already configured in GeoServer 
	 * 'tableName' is a table that has not yet been published in GeoServer*/
	public boolean createLayerFromDbTable(GeoServerRESTPublisher publisher, String keyword, String tableName, String srs, String deafultStyle, String workspaceName, String dataStoreConfigured) {
		GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
		fte.setProjectionPolicy(GSResourceEncoder.ProjectionPolicy.REPROJECT_TO_DECLARED);
		fte.addKeyword(keyword);
		fte.setTitle(tableName);
		fte.setName(tableName);
		fte.setSRS(srs);

		final GSLayerEncoder layerEncoder = new GSLayerEncoder();
		layerEncoder.setDefaultStyle(deafultStyle);

		return publisher.publishDBLayer(workspaceName, dataStoreConfigured, fte, layerEncoder);
	}
	

	
	public GeoServerRESTReader getReader() {
		String RESTURL  = "http://localhost:8088/geoserver";
		String RESTUSER = "admin";
		String RESTPW   = "geoserver";
		try {
			return new GeoServerRESTReader(RESTURL, RESTUSER, RESTPW);
		} catch (MalformedURLException e) {
			return null;
		}		
	}
	
	public GeoServerRESTPublisher getPublisher() {
		String RESTURL  = "http://localhost:8088/geoserver";
		String RESTUSER = "admin";
		String RESTPW   = "geoserver";
		return new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);	
	}
	
	public File convertToFile(MultipartFile file) throws IOException {
	    File convFile = new File(file.getOriginalFilename());
	    convFile.createNewFile();
	    FileOutputStream fos = new FileOutputStream(convFile);
	    fos.write(file.getBytes());
	    fos.close();
	    return convFile;
	}
	
	public Boolean saveRasterIntoGeoServer(String workspaceName, String storeName, String coverageName,
			MultipartFile fileUploaded,String srs, ProjectionPolicy pp, String defaultStyle) throws IOException{
		
		File geotiff = convertToFile(fileUploaded);
		GeoServerRESTPublisher publisher = getPublisher();
		
		return createLayerFromGeotiff(publisher, workspaceName, storeName, coverageName, geotiff,srs, 
				pp, defaultStyle, null);
	}
	
	/* srs - the native CRS
	 * policy - projection policy. See ProjectionPolicy.
	 * defaultStyle - the default style to apply.
	 * bbox - An array of 4 doubles indicating envelope in EPSG:4326. Order is [Xmin, Ymin, Xmax, Ymax].*/
	public boolean createLayerFromGeotiff(GeoServerRESTPublisher publisher, String workspaceName, String storeName, String coverageName, 
			File geotiff, String srs, ProjectionPolicy policy, String defaultStyle, double[] bbox) {
		try {
			
			Boolean publish = publisher.publishGeoTIFF(workspaceName, storeName, coverageName, geotiff, srs, policy, defaultStyle,bbox);
			if(geotiff != null) {
				geotiff.delete();
			}
			
			return publish;
//			return publisher.publishGeoTIFF(workspaceName, storeName, geotiff);
		} catch (FileNotFoundException | IllegalArgumentException e) {
//			e.printStackTrace();
			return false;
		}

	}

	public byte[] getRasterGeoTiff(String workspace, String raster) throws ParserConfigurationException,
																			SAXException, IOException {
		BoundingBoxDTO boundingBox = this.getShapeBoundingBox(workspace, raster);

		String rasterUrl = this.generateWmsGeoServerURL(workspace, raster, boundingBox, "geotiff");

		RestClient restClient = new RestClient();

		return restClient.getBytes(rasterUrl);
	}

	public byte[] getRasterPng(String workspace, String raster) throws ParserConfigurationException,
																		SAXException, IOException {
		BoundingBoxDTO boundingBox = this.getShapeBoundingBox(workspace, raster);

		String rasterUrl = this.generateWmsGeoServerURL(workspace, raster, boundingBox, "png");

		RestClient restClient = new RestClient();

		return restClient.getBytes(rasterUrl);
	}

	public BoundingBoxDTO getShapeBoundingBox(String workspace, String name) throws IOException,
																					SAXException,
																					ParserConfigurationException {
		RestClient restClient = new RestClient();

		RESTLayer layer = this.getReader().getLayer(workspace, name);

		if(layer == null) throw new IllegalStateException("Não foi possível encontrar o raster nessa camada");

		// TODO: Parametrizar dados de acesso ao GeoServer nessa consulta
		restClient.addHeader("Authorization", "Basic YWRtaW46Z2Vvc2VydmVy");

		String xmlResponse = restClient.get(layer.getResourceUrl());

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		ByteArrayInputStream input = new ByteArrayInputStream(xmlResponse.getBytes("UTF-8"));

		Document doc = builder.parse(input);

		NodeList nodes = doc.getElementsByTagName("nativeBoundingBox");

		if(nodes.getLength() == 0)
						throw new IllegalStateException("Não foi possível recuperar a bounding box do raster");

		Element boundingBox = (Element) nodes.item(0);

		Double minX = Double.parseDouble(boundingBox.getElementsByTagName("minx").item(0).getTextContent());
		Double maxX = Double.parseDouble(boundingBox.getElementsByTagName("maxx").item(0).getTextContent());
		Double minY = Double.parseDouble(boundingBox.getElementsByTagName("miny").item(0).getTextContent());
		Double maxY = Double.parseDouble(boundingBox.getElementsByTagName("maxy").item(0).getTextContent());
		String crs = boundingBox.getElementsByTagName("crs").item(0).getTextContent();

		return new BoundingBoxDTO(minX, maxX, minY, maxY, crs);
	}

	private String generateWmsGeoServerURL(String workspace, String raster, BoundingBoxDTO boundingBox, String format) {
		if(workspace == null || workspace.isEmpty())
									throw new IllegalArgumentException("Nome do workspace não pode ser nulo ou vazio");
		if(raster == null || raster.isEmpty())
									throw new IllegalArgumentException("Nome do raster não pode ser nulo ou vazio");
		if(boundingBox == null)  throw new IllegalArgumentException("Bounding box não pode ser nulo ou vazio");

		StringBuilder builder = new StringBuilder();
		builder.append("http://localhost:8088/geoserver/wsnRaster/wms?service=WMS&version=1.1.0&request=GetMap");
		builder.append("&layers=");
		builder.append(workspace);
		builder.append(":");
		builder.append(raster);
		builder.append("&bbox=");
		builder.append(boundingBox.toString());
		builder.append("&srs=");
		builder.append(boundingBox.getCrs());
		builder.append("&width=768&height=596");
		builder.append("&format=image/");
		builder.append(format);

		return builder.toString();
	}

}
