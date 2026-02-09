package com.facturacion.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "albaranes")
public class Albaran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "convertido_factura", nullable = false)
    private boolean convertidoFactura = false;

    @OneToMany(mappedBy = "albaran", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlbaranLinea> lineas = new ArrayList<>();

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

    public boolean isConvertidoFactura() { return convertidoFactura; }
    public void setConvertidoFactura(boolean convertidoFactura) { this.convertidoFactura = convertidoFactura; }

    public List<AlbaranLinea> getLineas() { return lineas; }
    public void setLineas(List<AlbaranLinea> lineas) { this.lineas = lineas; }

    public void addLinea(AlbaranLinea linea) {
        lineas.add(linea);
        linea.setAlbaran(this);
    }

    public void removeLinea(AlbaranLinea linea) {
        lineas.remove(linea);
        linea.setAlbaran(null);
    }

    // ============================
    // MÉTODOS DE UTILIDAD
    // ============================


    public boolean estaBloqueado() {
        return convertidoFactura;
    }


    public boolean isEditable() {
        return !convertidoFactura;
    }
}
