package com.facturacion.dao;

import com.facturacion.entity.Articulo;
import com.facturacion.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ArticuloDAO {

    public void guardar(Articulo articulo) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.saveOrUpdate(articulo);

            tx.commit();

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();

        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    public List<Articulo> listarTodos() {
        Session session = null;
        List<Articulo> lista = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            lista = session.createQuery("FROM Articulo", Articulo.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }

        return lista;
    }

    public Articulo buscarPorId(int id) {
        Session session = null;
        Articulo articulo = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            articulo = session.get(Articulo.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }

        return articulo;
    }

    public List<Articulo> buscarPorNombre(String nombre) {
        Session session = null;
        List<Articulo> lista = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            lista = session.createQuery(
                            "FROM Articulo WHERE nom LIKE :nom", Articulo.class)
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

            Articulo articulo = session.get(Articulo.class, id);
            if (articulo != null) {
                session.delete(articulo);
            }

            tx.commit();

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();

        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }
}
