package com.facturacion.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaLinea> lineas = new ArrayList<>();

    @Column(name = "total_base", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalBase = BigDecimal.ZERO;

    @Column(name = "total_iva", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalIva = BigDecimal.ZERO;

    @Column(name = "total_factura", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalFactura = BigDecimal.ZERO;

    @PrePersist
    public void prePersist() {
        if (fecha == null) fecha = LocalDateTime.now();
    }

    // GETTERS & SETTERS

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public List<FacturaLinea> getLineas() { return lineas; }
    public void setLineas(List<FacturaLinea> lineas) { this.lineas = lineas; }

    public BigDecimal getTotalBase() { return totalBase; }
    public void setTotalBase(BigDecimal totalBase) { this.totalBase = totalBase; }

    public BigDecimal getTotalIva() { return totalIva; }
    public void setTotalIva(BigDecimal totalIva) { this.totalIva = totalIva; }

    public BigDecimal getTotalFactura() { return totalFactura; }
    public void setTotalFactura(BigDecimal totalFactura) { this.totalFactura = totalFactura; }
}
