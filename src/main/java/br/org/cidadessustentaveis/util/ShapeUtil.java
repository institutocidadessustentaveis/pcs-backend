package br.org.cidadessustentaveis.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.IOUtils;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.geomesa.utils.text.WKTUtils$;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.wololo.geojson.Feature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;

public class ShapeUtil {
	
	
	public static byte[] exportShapefileFromFeatures(List<Feature> listaFeature, String nomeDoShape) throws Exception {
		Set<String> tipos = new HashSet<>();

		List<Feature> poligonos = new ArrayList<>();
		List<Feature> linhas = new ArrayList<>();
		List<Feature> pontos = new ArrayList<>();
		int random = new Random().nextInt(100000);
		File pasta = new File("shapes/"+random);
		pasta.mkdirs();		

		listaFeature.forEach(f -> {
			tipos.add(f.getGeometry().getType());
			switch (f.getGeometry().getType()) {
		     case "Polygon":
		     case "MultiPolygon":
		    	 poligonos.add(f);
		         break;

		     case "LineString":
		     case "MultiLineString":
		         linhas.add(f);
		         break;

		     case "Point":
		     case "MultiPoint":
		    	 pontos.add(f);
		         break;

		     default:
		    	 System.out.println("Nenhum desses tipos");
		         // e.g. unspecified Geometry, GeometryCollection
		         break;
			}
		});

		listaFeature = null;
		System.out.println(tipos);
		for(String tipo : tipos) {
			String nomeTipo = "";
			Boolean isMultiPolygon = false;
			List<Feature> features = new ArrayList<>();
			SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
			b.setName( "Flag" );
			b.setCRS( DefaultGeographicCRS.WGS84 );
			switch (tipo) {
		     case "Polygon":
		    	 nomeTipo = "poligonos";
		    	 b.add( "location", Polygon.class );
		         features = poligonos;
		         break;
		     case "MultiPolygon":
		    	 nomeTipo = "poligonos";
		    	 isMultiPolygon = true;
		    	 b.add( "location", Polygon.class );
		         features = poligonos;
		         break;

		     case "LineString":
		     case "MultiLineString":
		    	 nomeTipo = "linhas";
		    	 b.add( "location", LineString.class );
		         features = linhas;
		         break;

		     case "Point":
		     case "MultiPoint":
		    	 nomeTipo = "pontos";
		    	 b.add( "location", Point.class );
		         features = pontos;
		         break;

		     default:
		    	 System.out.println("Nenhum desses tipos");
		         break;
			}

			if(features != null && !features.isEmpty()) {
				for (Map.Entry<String, Object> entry : features.get(0).getProperties().entrySet()) {
					String key = entry.getKey();
					b.add(key, String.class);
				}
			}
			
			SimpleFeatureType schema = b.buildFeatureType();
			SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
			GeometryFactory geometryFactory = new GeometryFactory();

			List<SimpleFeature> geoms =  new ArrayList<>();
			
			
			
			for(Feature feature :features) {

				try {
					Geometry geo = convertGeoJsonToJtsGeometry(feature.getGeometry());
					
				
					switch (nomeTipo) {
					case "pontos":

						featureBuilder.add( WKTUtils$.MODULE$.read(geometryFactory.createPoint( geo.getCoordinate() ).toString()));
						break;
					case "poligonos":
						if(isMultiPolygon) {
							MultiPolygon aux = (MultiPolygon) geo;
							featureBuilder.add(WKTUtils$.MODULE$.read(aux.toString()));
						}else {
							featureBuilder.add( WKTUtils$.MODULE$.read(geometryFactory.createPolygon( geo.getCoordinates() ).toString()));
						}
						break;
					case "linhas":
						featureBuilder.add( WKTUtils$.MODULE$.read(geometryFactory.createLineString( geo.getCoordinates() ).toString()));
						
						break;

					default:
						break;
					}
					for (Map.Entry<String, Object> entry : feature.getProperties().entrySet()) {
						Object value = entry.getValue();
						if(value != null) {
							featureBuilder.add(value);
						}else {
							featureBuilder.add("");						
						}
					}
					SimpleFeature featureShape = featureBuilder.buildFeature(null);
					geoms.add(featureShape);
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			

			if(!geoms.isEmpty()) {
				SimpleFeatureCollection collection = new ListFeatureCollection(schema, geoms);
				new WriteShapefile(new File(pasta, nomeDoShape+ "-"+ nomeTipo + ".shp")).writeFeatures(collection);
			}

		}
		
		
		byte[] bytes = null;

		if(!tipos.isEmpty()) {

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
	

	public static org.wololo.geojson.Geometry convertJtsGeometryToGeoJson(Geometry geometry) {
        return new GeoJSONWriterVivid().write(geometry);
    }
	
    public static Geometry convertGeoJsonToJtsGeometry(org.wololo.geojson.Geometry geoJson) {
        return new GeoJSONReaderVivid().read(geoJson);
    }
}
