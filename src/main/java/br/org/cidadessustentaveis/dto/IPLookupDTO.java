package br.org.cidadessustentaveis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class IPLookupDTO {

    private String query;

    private String status;

    private String country;

    private String countryCode;

    private String region;

    private String regionName;

    private String city;

    private String zip;

    private String lat;

    private String lon;

    private String timezone;

    private String isp;

    private String org;

    private String as;

}
