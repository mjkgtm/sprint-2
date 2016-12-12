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
@RequestMapping("/admin")
public class GatewayAdminController{

	
	@Autowired
	protected RestTemplate restTemplate;
	
	
	@RequestMapping(value = (""), method = RequestMethod.GET)
	public String rootAdmin(){
		return "redirect:admin/";
	}
	
	////////GESTIÓN DE USUARIOS ////////
	
	@RequestMapping(value = ("/"), method = RequestMethod.GET)
	public String mostrarUsuarios(Model model){
		@SuppressWarnings("unchecked")
		List<Usuario> usuarios = restTemplate.getForObject(URL.TODOS_USUARIOS, List.class);
		model.addAttribute("usuarios", usuarios);
		
		return "listado_usuarios";
	}
	
	
	@RequestMapping(value = "/usuario/{id}", method = RequestMethod.GET)
	public String mostrarUsuario(Model model, @PathVariable long id){
		Usuario usuario = restTemplate.getForObject(URL.USUARIO_ID, Usuario.class, id);
		
		if(usuario == null){
			model.addAttribute("Mensaje", new InfoMessage("No existe el usuario con [Id]: " + id, TipoInfoMessage.ERROR, "alert-danger"));
		}
		
		model.addAttribute("usuario", usuario);
		
		return "modificar_usuario";
	}
	
	
	@RequestMapping(value = "/usuario/{id}", method = RequestMethod.POST)
	public String editarUsuario(Model model, @PathVariable long id, Usuario newUsuario){
		
		Usuario currentUsuario = restTemplate.getForObject(URL.USUARIO_ID, Usuario.class, id);
		
		if(currentUsuario == null){
			model.addAttribute("Mensaje", new InfoMessage("No existe el usuario con [Id]: " + id, TipoInfoMessage.ERROR, "alert-danger"));
		}
		else{
			
			currentUsuario.setNombre(newUsuario.getNombre());
			currentUsuario.setApellidos(newUsuario.getApellidos());
			currentUsuario.setEmail(newUsuario.getEmail());
			currentUsuario.setClave(newUsuario.getClave());
			currentUsuario.setCiudad(newUsuario.getCiudad());
			currentUsuario.setAdmin(newUsuario.isAdmin());
			
			restTemplate.postForObject(URL.USUARIO, currentUsuario, Usuario.class);
			model.addAttribute("Mensaje", new InfoMessage("Usuario modificado correctamente", TipoInfoMessage.EXITO, "alert-success"));
		}

		return "modificar_usuario";	
	}
	
	
	@RequestMapping(value = "/usuario", method = RequestMethod.POST)
	public String eliminarUsuario(Model model, @RequestParam long id){
		restTemplate.delete(URL.USUARIO_ID, id);
		
		return "redirect:admin/";
	}
	
	//////// GESTIÓN DE PRODUCTOS ////////
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/productos", method = RequestMethod.GET)
	public String listarProductos(Model model){
		List<Producto> productos = restTemplate.getForObject(URL.TODOS_PRODUCTOS, List.class);
		model.addAttribute("productos", productos);
		
		return "listado_productos_admin";
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
		
		
		
		return "modificar_producto";
	}
	
	
	
	@RequestMapping(value="/producto", method = RequestMethod.POST)
	public String eliminarProducto(Model model, @RequestParam long id){
		restTemplate.delete(URL.PRODUCTO_ID, id);
		
		return "redirect:productos";
	}
	
	
	
}
