package com.veterinaria.vet.Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "deudas")
public class Deuda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @OneToMany(mappedBy = "deuda", cascade = CascadeType.ALL)
    private List<PagosDeuda> pagosDeuda;

    @ManyToOne
    @JoinColumn(name = "ProovedorID", nullable = false)
    private Proveedor proveedor;

    @Column(name = "Descripcion", nullable = false)
    private String descripcion;

    @Column(name = "Precio", nullable = false)
    private BigDecimal precio;

    @Column(name = "CreatedAt", nullable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false, insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Crear una lista de precios en formato JSON
        List<Map<String, Object>> deudaJson = new ArrayList<>();
        if (pagosDeuda != null) {
            for (PagosDeuda pago : pagosDeuda) {
                Map<String, Object> pagoMap = new HashMap<>();
                pagoMap.put("id", pago.getID());
                pagoMap.put("pago", pago.getPago());
                pagoMap.put("createdAt", pago.getCreatedAt());
                pagoMap.put("deletedAt", pago.getDeletedAt());
                // Agrega otros campos del objeto Precio que quieras incluir en el JSON
                deudaJson.add(pagoMap);
            }
        }

        // Crear un mapa para representar el objeto Practica en JSON
        Map<String, Object> deudaMap = new HashMap<>();
        deudaMap.put("ID", ID);
        deudaMap.put("proveedor", proveedor);
        deudaMap.put("descripcion", descripcion);
        deudaMap.put("precio", precio);
        deudaMap.put("createdAt", createdAt);
        deudaMap.put("updatedAt", updatedAt);
        deudaMap.put("deletedAt", deletedAt);

        if (!deudaJson.isEmpty()) {
            deudaMap.put("pagosDeuda", deudaJson);
        }

        return objectMapper.writeValueAsString(deudaMap);
    }

    public BigDecimal getTotalPagos() {
        BigDecimal total = BigDecimal.valueOf(0);
        for (PagosDeuda pago : this.pagosDeuda) {
            total = total.add(pago.getPago());
        }
        return total;
    }

    public ArrayList<Deuda> orderByCreatedAt(ArrayList<Deuda> deudas) {
        for (int i = 0; i < deudas.size(); i++) {
            for (int j = 0; j < deudas.size() - 1; j++) {
                if (deudas.get(j).getCreatedAt().isBefore(deudas.get(j + 1).getCreatedAt())) {
                    Deuda aux = deudas.get(j);
                    deudas.set(j, deudas.get(j + 1));
                    deudas.set(j + 1, aux);
                }
            }
        }
        return deudas;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
    
    public List<PagosDeuda> getPagosDeuda() {
        return pagosDeuda;
    }

    public void setPagosDeuda(List<PagosDeuda> pagosDeuda) {
        this.pagosDeuda = pagosDeuda;
    }
    public void addPagosDeuda(PagosDeuda pagosDeuda) {
        this.pagosDeuda.add(pagosDeuda);
    }
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }



}
