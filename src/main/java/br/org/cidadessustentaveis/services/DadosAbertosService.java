package br.org.cidadessustentaveis.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

import br.org.cidadessustentaveis.dto.IndicadorDadosAbertosDTO;
import br.org.cidadessustentaveis.dto.VariaveisDadosAbertosDTO;
import br.org.cidadessustentaveis.model.indicadores.IndicadorPreenchido;
import br.org.cidadessustentaveis.util.NumeroUtil;

@Service
public class DadosAbertosService {

    @Autowired
    private IndicadorPreenchidoService indicadorPreenchidoService;

    @Autowired
    private VariavelPreenchidaService variavelPreenchidaService;

    private List<Object> headerIndicador = Arrays.asList(
                                                "Código IBGE", "Nome da cidade", "UF", "Estado Nome", "Eixo",
                                                "ID Indicador", "Nome do indicador", "Formula do indicador",
                                                "Meta ODS", "Número ODS", "Nome do ODS", "Descrição do indicador",
                                                "Ano de Preenchimento", "Valor", "Justificativa");

    private List<Object> headerIndicadorXML = Arrays.asList(
                                                "codigo_ibge", "nome_cidade", "uf", "nome_estado", "eixo",
                                                "id_indicador", "nome_indicador", "formula_indicador",
                                                "meta_ods", "numero_ods", "nome_ods", "descricao_indicador",
                                                "ano_preenchimento", "valor", "justificativa");

    private List<Object> headerVariavel = Arrays.asList("Código IBGE", "Nome da cidade", "ID", "UF", "Estado", "Tipo",
                                                        "Unidade de medida", "Nome", "Ano de Preenchimento", "Valor",
                                                        "Observações", "Fonte preenchida");

    private List<Object> headerVariavelXML = Arrays.asList("codigo_ibge", "nome_cidade", "id_variavel", "uf", "estado",
                                                            "tipo", "unidade_medida", "nome_variavel",
                                                            "ano_preenchimento", "valor", "observacoes",
                                                            "fonte_preenchida");

    public byte[] getIndicadoresFileContent(Long idCidade, Long idIndicador, String format)
                                                                                throws IOException,
                                                                                        ParserConfigurationException,
                                                                                        TransformerException {
        if(format.equalsIgnoreCase(".xls")) {
            return this.getIndicadoresAsXLS(idCidade, idIndicador);
        }

        if(format.equalsIgnoreCase(".csv")) {
            return this.getIndicadoresAsCSV(idCidade, idIndicador);
        }

        if(format.equalsIgnoreCase(".xml")) {
            return this.getIndicadoresAsXML(idCidade, idIndicador);
        }

        if(format.equalsIgnoreCase(".json")) {
            return this.getIndicadoresAsJSON(idCidade, idIndicador);
        }

        throw new IllegalStateException("Formato de arquivo não suportado");
    }

    public byte[] getVariaveisFileContent(Long idCidade, Long idIndicador, String format)
                                                                                throws IOException,
                                                                                        ParserConfigurationException,
                                                                                        TransformerException {
        if(format.equalsIgnoreCase(".xls")) {
            return this.getVariaveisAsXLS(idCidade, idIndicador);
        }

        if(format.equalsIgnoreCase(".csv")) {
            return this.getVariveisAsCSV(idCidade, idIndicador);
        }

        if(format.equalsIgnoreCase(".xml")) {
            return this.getVariaveisAsXML(idCidade, idIndicador);
        }

        if(format.equalsIgnoreCase(".json")) {
            return this.getVariaveisAsJSON(idCidade, idIndicador);
        }

        throw new IllegalStateException("Formato de arquivo não suportado");
    }

    public byte[] getIndicadoresAsXLS(Long idCidade, Long idIndicador) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        Sheet sheet = workbook.createSheet("Indicadores");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);

        int cellNumber = 0;
        for(Object column : headerIndicador) {
            Cell cell = headerRow.createCell(cellNumber);
            cell.setCellValue(column.toString());
            cell.setCellStyle(headerCellStyle);
            cellNumber++;
        }

        List<IndicadorDadosAbertosDTO> dados = indicadorPreenchidoService.buscarIndicadoresDadosAbertos(idCidade,
                                                                                                        idIndicador);
        int rowNum = 1;
        for(IndicadorDadosAbertosDTO indicador : dados) {
            if(!indicador.getValor().equalsIgnoreCase("nan")) {
                if(indicador.getValor().equals("Preenchido") && indicador.getResultado() == null && indicador.getValorTexto() != null){
                    if(indicador.getValorTexto().length() >= 32767) {
                    	indicador.setValor(indicador.getValorTexto().substring(0,32767));	
                    } else {
                    	indicador.setValor(indicador.getValorTexto());
                    }
                }
                
                if(!indicador.getValor().equals("Preenchido")){
                    Row row = sheet.createRow(rowNum++);
                    
                    Object[] properties = indicador.convertObjectToPropertiesArray();
                    
                    for(int i = 0; i < properties.length; i++) {
                        String value = this.convertValueToString(properties[i]);
                        
                        if(NumeroUtil.isANumber(value)) {
                            row.createCell(i).setCellValue(Double.parseDouble(value));
                        } else {
                            row.createCell(i).setCellValue(value);
                        }
                    }
                }
            }
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        workbook.write(output);

        byte[] content = output.toByteArray();

        workbook.close();
        output.close();

        return content;
    }

    public byte[] getIndicadoresAsCSV(Long idCidade, Long idIndicador) throws IOException {
        StringWriter output = new StringWriter();
        CSVWriter writer = new CSVWriter(output);

        String[] headerArray = new String[headerIndicador.size()];
        headerIndicador.toArray(headerArray);
        writer.writeNext(headerArray);

        List<IndicadorDadosAbertosDTO> dados = indicadorPreenchidoService.buscarIndicadoresDadosAbertos(idCidade,
                                                                                                        idIndicador);

        for(IndicadorDadosAbertosDTO row : dados) {
            if(!row.getValor().equalsIgnoreCase("nan")) {
                if(row.getValor().equals("Preenchido") && row.getResultado() == null && row.getValorTexto() != null){
                    if(row.getValorTexto().length() >= 32767) {
                    	row.setValor(row.getValorTexto().substring(0,32767));	
                    } else {
                    	row.setValor(row.getValorTexto());
                    }
                }
                writer.writeNext(row.convertObjectToPropertiesArray());
            }
        }

        writer.close();
        return output.toString().getBytes("UTF-16LE");
    }

    public byte[] getIndicadoresAsXML(Long idCidade, Long idIndicador) throws IOException,
                                                                                ParserConfigurationException,
                                                                                TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement("indicadores");
        doc.appendChild(rootElement);

        List<IndicadorDadosAbertosDTO> dados = indicadorPreenchidoService.buscarIndicadoresDadosAbertos(idCidade,
                                                                                                        idIndicador);
        for(IndicadorDadosAbertosDTO row : dados) {
            Element anon = doc.createElement("indicador");

            if(!row.getValor().equalsIgnoreCase("nan")) {
                if(row.getValor().equals("Preenchido") && row.getResultado() == null && row.getValorTexto() != null){
                    		row.setValor(row.getValorTexto());
                }
                int columnNumber = 0;
                for(Object property : row.convertObjectToPropertiesList()) {
                    Element anonProperty = doc.createElement(headerIndicadorXML.get(columnNumber).toString());
                    anonProperty.setTextContent(this.convertValueToString(property));

                    anon.appendChild(anonProperty);

                    columnNumber++;
                }

                rootElement.appendChild(anon);
            }
        }

        DOMSource source = new DOMSource(doc);

        StringWriter output = new StringWriter();
        StreamResult result = new StreamResult(output);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(source, result);

        byte[] content = output.toString().getBytes("UTF-8");

        output.close();

        return content;
    }

    public byte[] getIndicadoresAsJSON(Long idCidade, Long idIndicador) throws JsonProcessingException,
                                                                                UnsupportedEncodingException {
        List<IndicadorDadosAbertosDTO> dados = indicadorPreenchidoService.buscarIndicadoresDadosAbertos(idCidade,
                                                                                                        idIndicador);
        
        for(IndicadorDadosAbertosDTO indicador : dados) {
            if(!indicador.getValor().equalsIgnoreCase("nan")) {
                if(indicador.getValor().equals("Preenchido") && indicador.getResultado() == null && indicador.getValorTexto() != null){
                    	indicador.setValor(indicador.getValorTexto());
                }
            }
        }
        return new ObjectMapper().writeValueAsString(dados).getBytes("UTF-8");
    }

    public List<List<Object>> getIndicadoresAsList(Long idCidade, Long idIndicador) {
        List<IndicadorDadosAbertosDTO> dados = indicadorPreenchidoService.buscarIndicadoresDadosAbertos(idCidade,
                                                                                                        idIndicador);

        List<List<Object>> rows = new LinkedList<>();
        rows.add(headerIndicador);

        for(IndicadorDadosAbertosDTO dadosIndicador : dados) {
            rows.add(dadosIndicador.convertObjectToPropertiesList());
        }

        return rows;
    }

    public byte[] getVariaveisAsXLS(Long idCidade, Long idIndicador) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        Sheet sheet = workbook.createSheet("Variáveis");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);

        int cellNumber = 0;
        for(Object column : headerVariavel) {
            Cell cell = headerRow.createCell(cellNumber);
            cell.setCellValue(column.toString());
            cell.setCellStyle(headerCellStyle);
            cellNumber++;
        }

        List<VariaveisDadosAbertosDTO> dados = variavelPreenchidaService.buscarVariaveisDadosAbertos(idCidade,
                                                                                                        idIndicador);

        int rowNum = 1;
        for(VariaveisDadosAbertosDTO indicador : dados) {
            Row row = sheet.createRow(rowNum++);

            Object[] properties = indicador.convertObjectToPropertiesArray();

            for(int i = 0; i < properties.length; i++) {
                String value = this.convertValueToString(properties[i]);

                if(NumeroUtil.isANumber(value)) {
                    row.createCell(i).setCellValue(Double.parseDouble(value));
                } else {
                    row.createCell(i).setCellValue(value);
                }
            }
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        workbook.write(output);

        byte[] content = output.toByteArray();

        workbook.close();
        output.close();

        return content;
    }

    public byte[] getVariveisAsCSV(Long idCidade, Long idIndicador) throws IOException {
        StringWriter output = new StringWriter();
        CSVWriter writer = new CSVWriter(output);

        String[] headerArray = new String[headerVariavel.size()];
        headerVariavel.toArray(headerArray);
        writer.writeNext(headerArray);

        List<VariaveisDadosAbertosDTO> dados = variavelPreenchidaService.buscarVariaveisDadosAbertos(idCidade,
                                                                                                    idIndicador);

        for(VariaveisDadosAbertosDTO row : dados) {
            writer.writeNext(row.convertObjectToPropertiesArray());
        }

        writer.close();

        return output.toString().getBytes("UTF-16LE");
    }

    public byte[] getVariaveisAsXML(Long idCidade, Long idIndicador) throws IOException,
                                                                            ParserConfigurationException,
                                                                            TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement("variaveis");
        doc.appendChild(rootElement);

        List<VariaveisDadosAbertosDTO> dados = variavelPreenchidaService.buscarVariaveisDadosAbertos(idCidade,
                                                                                                    idIndicador);

        for(VariaveisDadosAbertosDTO row : dados) {
            Element variavel = doc.createElement("variavel");

            int columnNumber = 0;
            for(Object property : row.convertObjectToPropertiesList()) {
                Element propertyNode = doc.createElement(headerVariavelXML.get(columnNumber).toString());
                propertyNode.setTextContent(this.convertValueToString(property));

                variavel.appendChild(propertyNode);

                columnNumber++;
            }

            rootElement.appendChild(variavel);
        }

        DOMSource source = new DOMSource(doc);

        StringWriter output = new StringWriter();
        StreamResult result = new StreamResult(output);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(source, result);

        byte[] content = output.toString().getBytes("UTF-8");

        output.close();

        return content;
    }

    public byte[] getVariaveisAsJSON(Long idCidade, Long idIndicador) throws JsonProcessingException,
                                                                                UnsupportedEncodingException {
        List<VariaveisDadosAbertosDTO> dados = variavelPreenchidaService.buscarVariaveisDadosAbertos(idCidade,
                                                                                                    idIndicador);
        return new ObjectMapper().writeValueAsString(dados).getBytes("UTF-8");
    }

    public List<List<Object>> getVariaveisAsList(Long idCidade, Long idIndicador) {
        List<VariaveisDadosAbertosDTO> dados = variavelPreenchidaService.buscarVariaveisDadosAbertos(idCidade,
                                                                                                    idIndicador);

        List<List<Object>> rows = new LinkedList<>();
        rows.add(headerVariavel);

        for(VariaveisDadosAbertosDTO variavel : dados) {
            rows.add(variavel.convertObjectToPropertiesList());
        }

        return rows;
    }

    private String convertValueToString(Object obj) {
        if(obj == null) {
            return "";
        }

        if(obj.getClass().equals(Long.class)) {
            Long value = (Long) obj;
            return value.toString();
        }

        if(obj.getClass().equals(Short.class)) {
            Short value = (Short) obj;
            return value.toString();
        }

        if(obj.getClass().equals(LocalDateTime.class)) {
            LocalDateTime date = (LocalDateTime) obj;
            return date.format(DateTimeFormatter.ISO_DATE_TIME);
        }

        if(obj.getClass().equals(Date.class)) {
            Date date = (Date) obj;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyy-mm-ddThh:mm:ss");
            return sdf.format(date);
        }

        return obj.toString();
    }

}
