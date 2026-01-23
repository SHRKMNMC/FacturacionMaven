package com.facturacion.dao;

import com.facturacion.entity.Cliente;
import com.facturacion.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ClienteDAO {

    public void guardar(Cliente cliente) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.saveOrUpdate(cliente);

            tx.commit();

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();

        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // ====================== NUEVO MÉTODO ======================
    public List<Cliente> listarTodos() {
        Session session = null;
        List<Cliente> clientes = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            clientes = session.createQuery("FROM Cliente", Cliente.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        return clientes;
    }
}
