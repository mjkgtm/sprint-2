package es.uc3.tiw.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import es.uc3.tiw.dominio.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{

}
