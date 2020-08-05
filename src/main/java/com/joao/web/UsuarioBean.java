package com.joao.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Column;

import com.joao.controlleImpl.ControllerImpl;
import com.joao.daoImpl.UsuarioDAOImpl;
import com.joao.model.Usuario;

@ManagedBean(name = "usuarioBean")
@RequestScoped
public class UsuarioBean {

	private UsuarioDAOImpl usuarioDAO;

	private String nome;
	private String email;
	private String senha;
	
	private Usuario usuario;
	private List<Usuario> usuarios;
	private List<Usuario> listUser = new ArrayList<Usuario>();
	
	public UsuarioBean() {
		usuario = new Usuario();
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public List<Usuario> getUsuarios() {
		if (usuarios == null) {
			usuarios = usuarioDAO.listarUsuarios();
		}
		return usuarios;
	}
	public List<Usuario> getListUser() {
		return listUser;
	}
	public void setListUser(List<Usuario> listUser) {
		this.listUser = listUser;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	public String novo() {
		return "usuario?faces-redirect=true";
	}
	
	public void limparCampos() {
		this.usuario = null;
	}
	
	
	public String alterar(Integer id) {
		FacesContext context = FacesContext.getCurrentInstance();
		String retorno = "";
		usuarios = new ArrayList<Usuario>();

		if(id == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Id do usuário não pode  ser vazio!",""));
			retorno = "mostrarUsuario";
		}else {
			UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
			usuario = usuarioDAO.findUserById(id);
			retorno = "alterar";
		}
		
		return retorno;		
	}

	public String logar() {
		FacesContext contex = FacesContext.getCurrentInstance();
		UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

		if (nome.equals("") && senha.equals("")) {
			contex.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Login e Senha não podem ser vazios!",""));
		} else {
			Usuario user = usuarioDAO.findUserByName(nome);
			listUser = usuarioDAO.listarUsuarios();
			return "sucesso";
		}
		return "falha"; 
	}
	
	public String salvar() {
		FacesContext contex = FacesContext.getCurrentInstance();
		ControllerImpl controlador = new ControllerImpl();
			try {
				if(validaCPF(usuario.getCpf())) {
					Usuario user = usuarioDAO.findUserByCpf(usuario.getCpf());
					
					if(usuario != null && usuario.getCpf().equals(user.getCpf())) {
						controlador.salvarUsuario(usuario);
					}else {
						contex.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cpf já cadastrado!",""));
					}
				}else {
					contex.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cpf inválido!",""));
				}
			} catch (Exception e) {
				contex.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario não cadastrado!",""));
			}
		return "sucesso";
	}
	
	public String editar() {
		FacesContext contex = FacesContext.getCurrentInstance();
		ControllerImpl controlador = new ControllerImpl();
			try {
				
				UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
				Usuario usuariNew = usuarioDAO.findUserById(usuario.getId());
				
				usuariNew.setId(usuario.getId());
				usuariNew.setNome(usuario.getNome());
				usuariNew.setSexo(usuario.getSexo());
				usuariNew.setEmail(usuario.getEmail());
				usuariNew.setEmail(usuario.getNascimento());
				usuariNew.setEmail(usuario.getNascionalidade());
				usuariNew.setEmail(usuario.getNaturalidade());
					
				controlador.alterarUsuario(usuariNew);
			} catch (Exception e) {
				contex.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario não alterado!",""));
			}
		return "editar";
	}
	
	public String excluir(Integer id) {
		FacesContext context = FacesContext.getCurrentInstance();
		ControllerImpl controlador = new ControllerImpl();

		try {
			if(usuario.getId() == null) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Id do usuário não pode  ser vazio!",""));
			}
			controlador.excluirUsuario(id);
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario não excluido!",""));
		}
		return "mostrarUsuario";
	}
	
    public boolean validaCPF(String strCpf) {  
        if (strCpf.equals("")) {  
            return false;  
        }  
        int d1, d2;  
        int digito1, digito2, resto;  
        int digitoCPF;  
        String nDigResult;  
  
        d1 = d2 = 0;  
        digito1 = digito2 = resto = 0;  
  
        for (int nCount = 1; nCount < strCpf.length() - 1; nCount++) {  
            digitoCPF = Integer.valueOf(strCpf.substring(nCount - 1, nCount)).intValue();  
  
            //multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.  
            d1 = d1 + (11 - nCount) * digitoCPF;  
  
            //para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.  
            d2 = d2 + (12 - nCount) * digitoCPF;  
        }  
  
        //Primeiro resto da divisão por 11.  
        resto = (d1 % 11);  
  
        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.  
        if (resto < 2) {  
            digito1 = 0;  
        } else {  
            digito1 = 11 - resto;  
        }  
  
        d2 += 2 * digito1;  
  
        //Segundo resto da divisão por 11.  
        resto = (d2 % 11);  
  
        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.  
        if (resto < 2) {  
            digito2 = 0;  
        } else {  
            digito2 = 11 - resto;  
        }  
  
        //Digito verificador do CPF que está sendo validado.  
        String nDigVerific = strCpf.substring(strCpf.length() - 2, strCpf.length());  
  
        //Concatenando o primeiro resto com o segundo.  
        nDigResult = String.valueOf(digito1) + String.valueOf(digito2);  
  
        //comparar o digito verificador do cpf com o primeiro resto + o segundo resto.  
        return nDigVerific.equals(nDigResult);  
    } 

}
