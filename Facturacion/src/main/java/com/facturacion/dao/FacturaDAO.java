package com.facturacion.dao;

import com.facturacion.entity.*;
import com.facturacion.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class FacturaDAO {

    // ============================
    // GUARDAR FACTURA
    // ============================
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

            // 🔥 PONEMOS UN VALOR TEMPORAL PARA EVITAR EL ERROR
            factura.setNumeroFactura("TEMP");

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

            // 1️⃣ Guardar para obtener ID
            session.save(factura);
            session.flush();

            // 2️⃣ Ahora sí: numeroFactura = ID real
            factura.setNumeroFactura(factura.getId().toString());
            session.update(factura);

            // 3️⃣ Marcar albarán como convertido
            albaran.setConvertidoFactura(true);
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


    // ============================
    // CREAR FACTURA RECTIFICATIVA
    // ============================
    public Factura crearRectificativa(Factura original) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            original = session.get(Factura.class, original.getId());

            Factura rect = new Factura();
            rect.setCliente(original.getCliente());

            // Número rectificativa: 152R
            rect.setNumeroFactura(original.getNumeroFactura() + "R");

            rect.setEsRectificativa(true);
            rect.setFacturaRectificada(original);

            BigDecimal totalBase = BigDecimal.ZERO;
            BigDecimal totalIva = BigDecimal.ZERO;

            for (FacturaLinea l : original.getLineas()) {

                FacturaLinea rl = new FacturaLinea();
                rl.setFactura(rect);
                rl.setArticulo(l.getArticulo());
                rl.setCantidad(-l.getCantidad());
                rl.setPrecioUnitario(l.getPrecioUnitario());
                rl.setIvaPercent(l.getIvaPercent());

                rect.getLineas().add(rl);

                BigDecimal base = l.getPrecioUnitario()
                        .multiply(new BigDecimal(l.getCantidad()))
                        .negate();

                BigDecimal iva = base
                        .multiply(new BigDecimal(l.getIvaPercent()))
                        .divide(new BigDecimal("100"));

                totalBase = totalBase.add(base);
                totalIva = totalIva.add(iva);
            }

            rect.setTotalBase(totalBase);
            rect.setTotalIva(totalIva);
            rect.setTotalFactura(totalBase.add(totalIva));

            session.save(rect);

            tx.commit();
            return rect;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
            return null;

        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    // ============================
    // CARGAR FACTURA COMPLETA
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
                                    "LEFT JOIN FETCH f.facturaRectificada " +
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
    // LISTAR TODAS LAS FACTURAS
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
    // BUSCAR POR NOMBRE CLIENTE
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
    // ELIMINAR FACTURA (NO USAR)
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
}
