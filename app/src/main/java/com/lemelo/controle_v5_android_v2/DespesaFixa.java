package com.lemelo.controle_v5_android_v2;

/**
 * Created by leoci on 24/05/2017.
 */

class DespesaFixa {
    private Long identifier;
    private int dia;
    private String descricao;
    private String mes;
    private int ano;

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

    @Override
    public String toString() {
        return "Dia: " + dia +
                "\nDescrição: " + descricao +
                "\nMês: " + mes +
                "\nAno: " + ano;
    }
}
