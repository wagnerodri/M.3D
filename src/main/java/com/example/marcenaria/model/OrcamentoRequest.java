package com.example.marcenaria.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

public class OrcamentoRequest {
    @NotBlank
    private String nome;

    @NotBlank
    private String telefone;

    @NotBlank
    private String tipoMovel;

    @NotBlank
    private String tipoMadeira;

    @NotBlank
    private String acabamento;

    @NotBlank
    private String complexidade;

    private String observacoes;

    @NotNull
    @DecimalMin(value = "0.1", message = "Largura deve ser pelo menos 0.1m")
    @DecimalMax(value = "10.0", message = "Largura não pode exceder 10m")
    private Double largura;  // em metros
    
    @NotNull
    @DecimalMin(value = "0.1", message = "Altura deve ser pelo menos 0.1m") 
    @DecimalMax(value = "5.0", message = "Altura não pode exceder 5m")
    private Double altura;
    
    @NotNull
    @DecimalMin(value = "0.1", message = "Profundidade deve ser pelo menos 0.1m")
    @DecimalMax(value = "2.0", message = "Profundidade não pode exceder 2m")
    private Double profundidade;

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getTipoMovel() { return tipoMovel; }
    public void setTipoMovel(String tipoMovel) { this.tipoMovel = tipoMovel; }

    public String getTipoMadeira() { return tipoMadeira; }
    public void setTipoMadeira(String tipoMadeira) { this.tipoMadeira = tipoMadeira; }

    public String getAcabamento() { return acabamento; }
    public void setAcabamento(String acabamento) { this.acabamento = acabamento; }

    public String getComplexidade() { return complexidade; }
    public void setComplexidade(String complexidade) { this.complexidade = complexidade; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Double getLargura() { return largura; }
    public void setLargura(Double largura) { this.largura = largura; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public Double getProfundidade() { return profundidade; }
    public void setProfundidade(Double profundidade) { this.profundidade = profundidade; }
}
