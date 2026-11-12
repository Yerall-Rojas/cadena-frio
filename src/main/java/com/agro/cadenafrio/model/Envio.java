package com.agro.cadenafrio.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_envio")
@EntityListeners(AuditingEntityListener.class)
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String producto;

    @Column(name = "temperatura_minima", nullable = false)
    private Double temperaturaMinima;

    @Column(name = "temperatura_maxima", nullable = false)
    private Double temperaturaMaxima;

    @Column(nullable = false)
    private String estado = "EN_TRANSITO";

    @CreatedDate
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    // ===== Getters y Setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public Double getTemperaturaMinima() { return temperaturaMinima; }
    public void setTemperaturaMinima(Double temperaturaMinima) { this.temperaturaMinima = temperaturaMinima; }

    public Double getTemperaturaMaxima() { return temperaturaMaxima; }
    public void setTemperaturaMaxima(Double temperaturaMaxima) { this.temperaturaMaxima = temperaturaMaxima; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaModificacion() { return fechaModificacion; }
}