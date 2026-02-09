package com.facturacion.dao;

import com.facturacion.entity.Albaran;
import com.facturacion.entity.Cliente;
import com.facturacion.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AlbaranDAO {

    public void guardar(Albaran albaran) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.saveOrUpdate(albaran);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    public Albaran buscarPorId(int id) {
        Session session = null;
        Albaran albaran = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            albaran = session.createQuery(
                            "SELECT DISTINCT a FROM Albaran a " +
                                    "LEFT JOIN FETCH a.lineas " +
                                    "WHERE a.id = :id", Albaran.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }

        return albaran;
    }

    public List<Albaran> listarTodos() {
        Session session = null;
        List<Albaran> lista = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            lista = session.createQuery(
                    "SELECT DISTINCT a FROM Albaran a " +
                            "LEFT JOIN FETCH a.lineas", Albaran.class
            ).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }

        return lista;
    }

    public List<Albaran> buscarPorCliente(Cliente cliente) {
        Session session = null;
        List<Albaran> lista = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            lista = session.createQuery(
                            "SELECT DISTINCT a FROM Albaran a " +
                                    "LEFT JOIN FETCH a.lineas " +
                                    "WHERE a.cliente = :cli", Albaran.class)
                    .setParameter("cli", cliente)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }

        return lista;
    }

    public List<Albaran> buscarPorNombreCliente(String nombre) {
        Session session = null;
        List<Albaran> lista = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            lista = session.createQuery(
                            "SELECT DISTINCT a FROM Albaran a " +
                                    "LEFT JOIN FETCH a.lineas " +
                                    "WHERE a.cliente.nombre LIKE :nom", Albaran.class)
                    .setParameter("nom", "%" + nombre + "%")
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }

        return lista;
    }

    public void eliminar(int id) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Albaran albaran = session.get(Albaran.class, id);
            if (albaran != null) session.delete(albaran);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }
}
