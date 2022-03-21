package br.org.cidadessustentaveis.model.planjementoIntegrado;

public enum TipoUsoShape {
    CONSULTA("CONSULTA"), DOWNLOAD("DOWNLOAD");


    private String name;

    private TipoUsoShape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
