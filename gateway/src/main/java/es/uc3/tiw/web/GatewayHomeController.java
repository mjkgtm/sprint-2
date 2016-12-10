package es.uc3.tiw.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import es.uc3.tiw.dominio.Usuario;

@Controller
public class GatewayHomeController {	
	
	@Autowired
	protected RestTemplate restTemplate;
	
	private static Usuario usuarioLogueado;
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login(){
		return "login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String doLogin(Model model, Usuario usuario){
		
		Usuario u = restTemplate.postForObject(URL.VALIDAR_USUARIO, usuario, Usuario.class);
		
		if(u != null){
			
			setUsuarioLogueado(u);
			
			if(u.isAdmin()){
				return "redirect:admin/";
			}
			
			return "redirect:user/";
		}
		else{
			model.addAttribute("Mensaje", new InfoMessage("Usuario o contraseña no son correctos", TipoInfoMessage.ERROR, "alert-danger") );
			return "login";
		}
		
	}
	
	
	@RequestMapping(value = "/registro", method = RequestMethod.GET)
	public String registro(){
		return "registro";
	}
	
	
	@RequestMapping(value = "/registro", method = RequestMethod.POST)
	public String doRegistro(Model model, Usuario usuario){
		
		
		if(restTemplate.getForObject(URL.USUARIO_EMAIL, Usuario.class, usuario.getEmail()) != null){
			model.addAttribute("Mensaje", new InfoMessage("Ya existe un usuario con ese email", TipoInfoMessage.ERROR, "alert-danger"));
			
			return "registro";
		}
		else{
			
			usuario.setAdmin(false);
			restTemplate.postForObject(URL.USUARIO, usuario, Usuario.class);
			
			return "redirect:login";
		}
		
	}
	
	@RequestMapping(value = "/acerca", method = RequestMethod.GET)
	public String acerca(){
		return "acerca";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String doLogout(){
		return "redirect:/login";
	}
	
	
	
	public static Usuario getUsuarioLogueado(){
		return usuarioLogueado;
	}
	
	public static void setUsuarioLogueado(Usuario usuario){
		usuarioLogueado = usuario;
	}
	
	
}
