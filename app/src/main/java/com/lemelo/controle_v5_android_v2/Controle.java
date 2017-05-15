package com.lemelo.controle_v5_android_v2;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by leoci on 27/04/2017.
 */

class Controle {
    private Long identifier;
    private Date data;
    private String descricao;
    private BigDecimal entrada;
    private BigDecimal saida;


    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getEntrada() {
        return entrada;
    }

    public void setEntrada(BigDecimal entrada) {
        this.entrada = entrada;
    }

    public BigDecimal getSaida() {
        return saida;
    }

    public void setSaida(BigDecimal saida) {
        this.saida = saida;
    }

    @Override
    public String toString() {
        return "Data: " + data +
                "\nDescrição: " + descricao +
                "\nEntrada: " + entrada +
                "\nSaida: " + saida;
    }
}
