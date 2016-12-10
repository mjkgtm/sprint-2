package es.uc3.tiw.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import es.uc3.tiw.dominio.Producto;
import es.uc3.tiw.dominio.Usuario;

@Controller
@RequestMapping("/user")
public class GatewayUserController{
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@RequestMapping(value = (""), method = RequestMethod.GET)
	public String rootUser(){
		return "redirect:user/";
	}
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String menuPrincipal(Model model){
		return "menu_principal";
	}
	
	
	@RequestMapping(value = "/perfil", method = RequestMethod.GET)
	public String verPerfil(Model model){
		Usuario usuarioLogueado = GatewayHomeController.getUsuarioLogueado();
		model.addAttribute("usuario", usuarioLogueado);
		
		return "perfil";
	}
	
	@RequestMapping(value = "/perfil", method = RequestMethod.POST)
	public String modificarPerfil(Model model, Usuario newUsuario){
		
		Usuario currentUsuario = restTemplate.getForObject(URL.USUARIO_ID, Usuario.class, newUsuario.getId());
		
		if(currentUsuario == null){
			model.addAttribute("Mensaje", new InfoMessage("No existe el usuario con [Id]: " + newUsuario.getId(), TipoInfoMessage.ERROR, "alert-danger"));
		}
		else{
			
			currentUsuario.setNombre(newUsuario.getNombre());
			currentUsuario.setApellidos(newUsuario.getApellidos());
			currentUsuario.setEmail(newUsuario.getEmail());
			currentUsuario.setClave(newUsuario.getClave());
			currentUsuario.setCiudad(newUsuario.getCiudad());
			
			restTemplate.postForObject(URL.USUARIO, currentUsuario, Usuario.class);
			model.addAttribute("Mensaje", new InfoMessage("Usuario modificado correctamente", TipoInfoMessage.EXITO, "alert-success"));
		}
		
		
		return "perfil";
	}
	
	// De momento devuelve todos los productos, no los del usuario
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/productos", method = RequestMethod.GET)
	public String listarProductos(Model model){
		List<Producto> productos = restTemplate.getForObject(URL.TODOS_PRODUCTOS, List.class);
		model.addAttribute("productos", productos);
		
		return "listado_productos";
	}
	
	@RequestMapping(value="/producto/{id}", method = RequestMethod.GET)
	public String mostrarProducto(Model model, @PathVariable Long id){
		Producto producto = restTemplate.getForObject(URL.PRODUCTO_ID, Producto.class, id);
		model.addAttribute("producto", producto);
		
		return "modificar_producto";
	}
	
	
	@RequestMapping(value="/producto/{id}", method = RequestMethod.POST)
	public String editarProducto(Model model, @PathVariable Long id, Producto newProducto, @RequestParam CommonsMultipartFile foto){
		Producto producto = restTemplate.getForObject(URL.PRODUCTO_ID, Producto.class, id);
		
		if(producto == null){
			model.addAttribute("Mensaje", new InfoMessage("No existe el producto con [Id]: " + id, TipoInfoMessage.ERROR, "alert-danger"));
		}
		else{
			producto.setTitulo(newProducto.getTitulo());
			producto.setPrecio(newProducto.getPrecio());
			producto.setCategoria(newProducto.getCategoria());
			producto.setEstado(newProducto.getEstado());
			producto.setDescripcion(newProducto.getDescripcion());
			if(foto.getSize() > 0){
				producto.setImagen(foto.getBytes());
			}
			
			producto = restTemplate.postForObject(URL.PRODUCTO, producto, Producto.class);
			model.addAttribute("Mensaje", new InfoMessage("Producto modificado correctamente", TipoInfoMessage.EXITO, "alert-success"));
		}
		
		
		
		return "redirect:/user/productos";
	}
	
	
	@RequestMapping(value="/producto", method = RequestMethod.GET)
	public String altaProducto(Model model){
		return "alta_producto";
	}
	
	@RequestMapping(value="/producto", method = RequestMethod.POST)
	public String doAltaProducto(Model model, Producto producto, @RequestParam CommonsMultipartFile foto){
		
		Producto newProducto = new Producto();
		newProducto.setTitulo(producto.getTitulo());
		newProducto.setPrecio(producto.getPrecio());
		newProducto.setCategoria(producto.getCategoria());
		newProducto.setEstado(producto.getEstado());
		newProducto.setDescripcion(producto.getDescripcion());
		newProducto.setImagen(foto.getBytes());
		
		newProducto = restTemplate.postForObject(URL.PRODUCTO, newProducto, Producto.class);
		
		return "redirect:productos";
	}
	
	
}
