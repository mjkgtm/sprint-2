package es.uc3m.tiw.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uc3m.tiw.dominio.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{

}
