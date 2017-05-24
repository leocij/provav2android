package com.lemelo.controle_v5_android_v2;

import java.math.BigDecimal;

/**
 * Created by leoci on 21/05/2017.
 */

class Parcelamento {
    private Long identifier;
    private int dia;
    private String descricao;
    private String mes;
    private int ano;
    private int quantParcelas;
    private int valorParcela;

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getQuantParcelas() {
        return quantParcelas;
    }

    public void setQuantParcelas(int quantParcelas) {
        this.quantParcelas = quantParcelas;
    }

    public int getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(int valorParcela) {
        this.valorParcela = valorParcela;
    }

    @Override
    public String toString() {
        return "Dia: " + dia +
                "\nDescrição: " + descricao +
                "\nMes: " + mes +
                "\nAno: " + ano +
                "\nQuantidade de Parcelas: " + quantParcelas +
                "\nValor da Parcela: " + valorParcela;
    }
}
