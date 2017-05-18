package com.lemelo.controle_v5_android_v2;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by leoci on 12/05/2017.
 */

class Barganha {
    private Long identifier;
    private Date data;
    private String descricao;
    private BigDecimal valor;

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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Data: " + data +
                "\nDescricao: " + descricao +
                "\nValor: " + valor;
    }
}
