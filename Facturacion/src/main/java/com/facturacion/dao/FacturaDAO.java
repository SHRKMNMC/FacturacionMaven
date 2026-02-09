package com.facturacion.dao;

import com.facturacion.entity.*;
import com.facturacion.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class FacturaDAO {

    public void guardar(Factura factura) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.saveOrUpdate(factura);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    // ============================
    // CARGAR FACTURA COMPLETA (JOIN FETCH)
    // ============================
    public Factura buscarPorId(int id) {
        Session session = null;
        Factura factura = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            factura = session.createQuery(
                            "SELECT DISTINCT f FROM Factura f " +
                                    "LEFT JOIN FETCH f.lineas l " +
                                    "LEFT JOIN FETCH l.articulo " +
                                    "LEFT JOIN FETCH f.cliente " +
                                    "WHERE f.id = :id", Factura.class)
                    .setParameter("id", id)
                    .uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }

        return factura;
    }

    // ============================
    // LISTAR TODAS LAS FACTURAS (JOIN FETCH CLIENTE)
    // ============================
    public List<Factura> listarTodos() {
        Session session = null;
        List<Factura> lista = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            lista = session.createQuery(
                    "SELECT DISTINCT f FROM Factura f " +
                            "LEFT JOIN FETCH f.cliente",
                    Factura.class
            ).list();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }

        return lista;
    }

    // ============================
    // BUSCAR POR NOMBRE DE CLIENTE
    // ============================
    public List<Factura> buscarPorNombreCliente(String nombre) {
        Session session = null;
        List<Factura> lista = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            lista = session.createQuery(
                            "SELECT DISTINCT f FROM Factura f " +
                                    "LEFT JOIN FETCH f.cliente c " +
                                    "WHERE c.nombre LIKE :nom",
                            Factura.class)
                    .setParameter("nom", "%" + nombre + "%")
                    .list();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }

        return lista;
    }

    // ============================
    // ELIMINAR FACTURA
    // ============================
    public void eliminar(int id) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Factura factura = session.get(Factura.class, id);
            if (factura != null) session.delete(factura);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    // ============================
    // CREAR FACTURA DESDE ALBARÁN
    // ============================
    public Factura crearDesdeAlbaran(Albaran albaran) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            albaran = session.get(Albaran.class, albaran.getId());

            Factura factura = new Factura();
            factura.setCliente(albaran.getCliente());

            BigDecimal totalBase = BigDecimal.ZERO;
            BigDecimal totalIva = BigDecimal.ZERO;

            for (AlbaranLinea l : albaran.getLineas()) {
                FacturaLinea fl = new FacturaLinea();
                fl.setFactura(factura);
                fl.setArticulo(l.getArticulo());
                fl.setCantidad(l.getCantidad());
                fl.setPrecioUnitario(l.getPrecioUnitario());
                fl.setIvaPercent(l.getIvaPercent());

                factura.getLineas().add(fl);

                BigDecimal base = l.getPrecioUnitario()
                        .multiply(new BigDecimal(l.getCantidad()));

                BigDecimal iva = base
                        .multiply(new BigDecimal(l.getIvaPercent()))
                        .divide(new BigDecimal("100"));

                totalBase = totalBase.add(base);
                totalIva = totalIva.add(iva);
            }

            factura.setTotalBase(totalBase);
            factura.setTotalIva(totalIva);
            factura.setTotalFactura(totalBase.add(totalIva));

            albaran.setConvertidoFactura(true);

            session.save(factura);
            session.update(albaran);

            tx.commit();

            return factura;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }
}
