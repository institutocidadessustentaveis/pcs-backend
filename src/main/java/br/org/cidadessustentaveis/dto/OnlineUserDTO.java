package br.org.cidadessustentaveis.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class OnlineUserDTO {

    private String login;

    private boolean online;

    private Date date;

}
