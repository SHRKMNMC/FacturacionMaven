package com.facturacion.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "articulos")
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false, unique = true)
    private String codi;

    @Column(length = 80, nullable = false)
    private String nom;

    @Column(length = 500)
    private String descripcio;

    @Column(length = 50, nullable = false)
    private String familia;

    @Column(length = 50, nullable = false)
    private String categoria;

    @Column(length = 30, nullable = false)
    private String unitat;

    @Column(length = 80, nullable = false)
    private String proveedor;

    @Column(name = "preu_cost", precision = 12, scale = 2, nullable = false)
    private BigDecimal preuCost;

    @Column(name = "preu_venda", precision = 12, scale = 2, nullable = false)
    private BigDecimal preuVenda;

    @Column(name = "iva_percent", nullable = false)
    private Integer ivaPercent;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    @Column(name = "stock_minim", nullable = false)
    private Integer stockMinim;

    @Column(name = "codi_barres", length = 13)
    private String codiBarres;

    @Column(nullable = false)
    private boolean actiu;

    @Column(name = "imatge_path", length = 255)
    private String imatgePath;

    @Column(name = "data_alta", nullable = false)
    private LocalDateTime dataAlta;

    @Column(length = 500)
    private String observacions;

    @PrePersist
    public void prePersist() {
        if (dataAlta == null) {
            dataAlta = LocalDateTime.now();
        }
    }

    // ============ GETTERS & SETTERS ============

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodi() {
        return codi;
    }

    public void setCodi(String codi) {
        this.codi = codi;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnitat() {
        return unitat;
    }

    public void setUnitat(String unitat) {
        this.unitat = unitat;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public BigDecimal getPreuCost() {
        return preuCost;
    }

    public void setPreuCost(BigDecimal preuCost) {
        this.preuCost = preuCost;
    }

    public BigDecimal getPreuVenda() {
        return preuVenda;
    }

    public void setPreuVenda(BigDecimal preuVenda) {
        this.preuVenda = preuVenda;
    }

    public Integer getIvaPercent() {
        return ivaPercent;
    }

    public void setIvaPercent(Integer ivaPercent) {
        this.ivaPercent = ivaPercent;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Integer getStockMinim() {
        return stockMinim;
    }

    public void setStockMinim(Integer stockMinim) {
        this.stockMinim = stockMinim;
    }

    public String getCodiBarres() {
        return codiBarres;
    }

    public void setCodiBarres(String codiBarres) {
        this.codiBarres = codiBarres;
    }

    public boolean isActiu() {
        return actiu;
    }

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }

    public String getImatgePath() {
        return imatgePath;
    }

    public void setImatgePath(String imatgePath) {
        this.imatgePath = imatgePath;
    }

    public LocalDateTime getDataAlta() {
        return dataAlta;
    }

    public void setDataAlta(LocalDateTime dataAlta) {
        this.dataAlta = dataAlta;
    }

    public String getObservacions() {
        return observacions;
    }

    public void setObservacions(String observacions) {
        this.observacions = observacions;
    }

    @Override
    public String toString() {
        return codi + " - " + nom;
    }

}
