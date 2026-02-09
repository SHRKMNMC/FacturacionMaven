package com.facturacion.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "albaran_lineas")
public class AlbaranLinea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "albaran_id")
    private Albaran albaran;

    @ManyToOne(optional = false)
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 12, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "iva_percent", nullable = false)
    private Integer ivaPercent;

    // GETTERS & SETTERS

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Albaran getAlbaran() { return albaran; }
    public void setAlbaran(Albaran albaran) { this.albaran = albaran; }

    public Articulo getArticulo() { return articulo; }
    public void setArticulo(Articulo articulo) { this.articulo = articulo; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public Integer getIvaPercent() { return ivaPercent; }
    public void setIvaPercent(Integer ivaPercent) { this.ivaPercent = ivaPercent; }
}
