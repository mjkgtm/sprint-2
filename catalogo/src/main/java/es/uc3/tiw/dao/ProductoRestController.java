package es.uc3.tiw.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import es.uc3.tiw.dao.ProductoRepository;
import es.uc3.tiw.dominio.Producto;

@RestController
@RequestMapping("/rest")
public class ProductoRestController {

	@Autowired
	private ProductoRepository productoRepository;
	
	@RequestMapping(value = "/producto/{id}",  method = RequestMethod.GET)
	public Producto mostrarProducto(@PathVariable Long id){
		return productoRepository.findOne(id);	
	}
	
	
	@RequestMapping(value = "/productos",  method = RequestMethod.GET)
	public List<Producto> mostrarProductos(){
		return productoRepository.findAll();
		
	}
	
	@RequestMapping(value = "/producto",  method = RequestMethod.POST)
	public Producto crearProducto(@RequestBody Producto producto){
		return productoRepository.save(producto);			
	}
	
	
	@RequestMapping(value = "/producto",  method = RequestMethod.PUT)
	public void editarProductos(@RequestBody Producto producto){
		productoRepository.save(producto);
	}
	
	
	@RequestMapping(value = "/producto/{id}",  method = RequestMethod.DELETE)
	public void eliminarProducto(@PathVariable long id){
		productoRepository.delete(id);	
	}
	
	
	
}
