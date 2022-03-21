package br.org.cidadessustentaveis.model.sistema;

import br.org.cidadessustentaveis.dto.IPLookupDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ip_lookup")
@NoArgsConstructor @AllArgsConstructor
public class IPLookup {

    @Id @GeneratedValue(generator = "ip_lookup_id_seq")
    @SequenceGenerator(name = "ip_lookup_id_seq", sequenceName = "ip_lookup_id_seq", allocationSize = 1)
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name = "ip")
    private String ip;

    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "isp")
    private String isp;

    @Column(name = "org")
    private String org;

    @Column(name = "org_as")
    private String as;

    @Column(name="lookup_date")
    private LocalDateTime lookupDate;

    public IPLookup(IPLookupDTO ipLookupDTO) {
        this.ip = ipLookupDTO.getQuery();
        this.country = ipLookupDTO.getCountry();
        this.region = ipLookupDTO.getRegionName();
        this.city = ipLookupDTO.getCity();
        this.latitude = ipLookupDTO.getLat() != null ? Double.parseDouble(ipLookupDTO.getLat()) : null;
        this.longitude = ipLookupDTO.getLon() != null ? Double.parseDouble(ipLookupDTO.getLon()) : null;
        this.isp = ipLookupDTO.getIsp();
        this.org = ipLookupDTO.getOrg();
        this.as = ipLookupDTO.getAs();
        this.lookupDate = LocalDateTime.now();
    }

}
