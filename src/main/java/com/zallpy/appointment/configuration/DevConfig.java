package com.zallpy.appointment.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zallpy.appointment.application.domain.entity.Alocacao;
import com.zallpy.appointment.application.domain.entity.Colaborador;
import com.zallpy.appointment.application.domain.entity.Projeto;
import com.zallpy.appointment.application.service.AlocacaoService;
import com.zallpy.appointment.application.service.ColaboradorService;
import com.zallpy.appointment.application.service.ProjetoService;
import com.zallpy.appointment.security.domain.entity.Acao;
import com.zallpy.appointment.security.domain.entity.Modulo;
import com.zallpy.appointment.security.domain.entity.ModuloAcao;
import com.zallpy.appointment.security.domain.entity.Perfil;
import com.zallpy.appointment.security.domain.entity.Usuario;
import com.zallpy.appointment.security.service.AcaoService;
import com.zallpy.appointment.security.service.ModuloAcaoService;
import com.zallpy.appointment.security.service.ModuloService;
import com.zallpy.appointment.security.service.PerfilService;
import com.zallpy.appointment.security.service.UsuarioService;

@Configuration
@Profile("dev")
public class DevConfig implements WebMvcConfigurer {

	@Autowired
	private ColaboradorService colaboradorService;

	@Autowired
	private ProjetoService projetoService;

	@Autowired
	private AlocacaoService alocacaoService;

	@Autowired
	private ModuloService moduloService;

	@Autowired
	private AcaoService acaoService;

	@Autowired
	private ModuloAcaoService moduloAcaoService;

	@Autowired
	private PerfilService perfilService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private BCryptPasswordEncoder pe;

	private List<Acao> todasAcoes = new ArrayList<>();
	private List<Modulo> todosModulos = new ArrayList<>();

	@Transactional
	@Bean
	public boolean instantiateDatabase() throws Exception {
		cadastrarAlocacoes();

		cadastrarAcoes();
		cadastrarModulos();
		cadastrarModulosAcoes();
		cadastrarPerfis();
		cadastrarUsuariosAtualizarColaboradores();

		return true;
	}

	@Transactional
	private void cadastrarAlocacoes() throws Exception {

		Colaborador administrador = new Colaborador("Administrador");
		Colaborador programador1 = new Colaborador("Programador 1");
		Colaborador programador2 = new Colaborador("Programador 2");

		Projeto projetoA = new Projeto("Projeto Cliente A");
		Projeto projetoB = new Projeto("Projeto Cliente B");

		Alocacao a1 = new Alocacao(programador1, projetoA);
		Alocacao a2 = new Alocacao(programador2, projetoA);
		Alocacao a3 = new Alocacao(programador2, projetoB);

		colaboradorService.salvarTodos(Arrays.asList(administrador, programador1, programador2));
		projetoService.salvarTodos(Arrays.asList(projetoA, projetoB));
		alocacaoService.salvarTodos(Arrays.asList(a1, a2, a3));
	}

	@Transactional
	private void cadastrarAcoes() throws Exception {

		Acao exibir = new Acao("EXIBIR", "Exibir", "Mostrar item no menu");
		Acao listar = new Acao("LISTAR", "Listar", "Lista registros");
		Acao cadastrar = new Acao("CADASTRAR", "Cadastrar", "Cadastra registros");
		Acao editar = new Acao("EDITAR", "Editar", "Edita registros");
		Acao remover = new Acao("REMOVER", "Remover", "Remove registros");
		Acao listarPorProjeto = new Acao("LISTAR_POR_PROJETO", "Listar por projeto", "Lista registros");
		Acao listarPorColaborador = new Acao("LISTAR_POR_COLABORADOR", "Listar por colaborador", "Lista registros");

		todasAcoes = Arrays.asList(exibir, listar, cadastrar, editar, remover, listarPorProjeto, listarPorColaborador);

		acaoService.salvarTodos(todasAcoes);
	}

	@Transactional
	private void cadastrarModulos() throws Exception {

		Modulo projeto = new Modulo("PROJETO", "Projeto", "Módulo para gerenciar projetos");
		Modulo alocacao = new Modulo("ALOCACAO", "Alocacao", "Módulo para alocacao de colaboradores em projetos");
		Modulo apontamento = new Modulo("APONTAMENTO", "Apontamento", "Módulo para apontamento em projetos");

		todosModulos = Arrays.asList(projeto, alocacao, apontamento);

		moduloService.salvarTodos(todosModulos);
	}

	@Transactional
	private void cadastrarModulosAcoes() throws Exception {

		Optional<Acao> exibir = todasAcoes.stream().filter(acao -> acao.getNome().equals("EXIBIR")).findFirst();
		Optional<Acao> listar = todasAcoes.stream().filter(acao -> acao.getNome().equals("LISTAR")).findFirst();
		Optional<Acao> listarPorProjeto = todasAcoes.stream()
				.filter(acao -> acao.getNome().equals("LISTAR_POR_PROJETO")).findFirst();
		Optional<Acao> listarPorColaborador = todasAcoes.stream()
				.filter(acao -> acao.getNome().equals("LISTAR_POR_COLABORADOR")).findFirst();
		Optional<Acao> cadastrar = todasAcoes.stream().filter(acao -> acao.getNome().equals("CADASTRAR")).findFirst();

		Optional<Modulo> projeto = todosModulos.stream().filter(modulo -> modulo.getNome().equals("PROJETO"))
				.findFirst();
		moduloAcaoService.salvar(new ModuloAcao(projeto.get(), exibir.get()));
		moduloAcaoService.salvar(new ModuloAcao(projeto.get(), listar.get()));
		moduloAcaoService.salvar(new ModuloAcao(projeto.get(), listarPorColaborador.get()));

		Optional<Modulo> alocacao = todosModulos.stream().filter(modulo -> modulo.getNome().equals("ALOCACAO"))
				.findFirst();
		moduloAcaoService.salvar(new ModuloAcao(alocacao.get(), listar.get()));
		moduloAcaoService.salvar(new ModuloAcao(alocacao.get(), listarPorProjeto.get()));
		moduloAcaoService.salvar(new ModuloAcao(alocacao.get(), listarPorColaborador.get()));

		Optional<Modulo> apontamento = todosModulos.stream().filter(modulo -> modulo.getNome().equals("APONTAMENTO"))
				.findFirst();
		moduloAcaoService.salvar(new ModuloAcao(apontamento.get(), cadastrar.get()));
	}

	@Transactional
	private void cadastrarPerfis() throws Exception {

		Optional<Modulo> projeto = todosModulos.stream().filter(modulo -> modulo.getNome().equals("PROJETO"))
				.findFirst();
		Optional<Modulo> alocacao = todosModulos.stream().filter(modulo -> modulo.getNome().equals("ALOCACAO"))
				.findFirst();
		Optional<Modulo> apontamento = todosModulos.stream().filter(modulo -> modulo.getNome().equals("APONTAMENTO"))
				.findFirst();

		List<ModuloAcao> todosModulosAcoesProjeto = moduloAcaoService.buscarPorModulo(projeto.get().getId());
		List<ModuloAcao> todosModulosAcoesAlocacao = moduloAcaoService.buscarPorModulo(alocacao.get().getId());
		List<ModuloAcao> todosModulosAcoesApontamento = moduloAcaoService.buscarPorModulo(apontamento.get().getId());
		Optional<ModuloAcao> exibirProjeto = todosModulosAcoesProjeto.stream()
				.filter(moduloAcao -> moduloAcao.getAuthority().equals("EXIBIR_PROJETO")).findFirst();
		Optional<ModuloAcao> listarProjeto = todosModulosAcoesProjeto.stream()
				.filter(moduloAcao -> moduloAcao.getAuthority().equals("LISTAR_PROJETO")).findFirst();
		Optional<ModuloAcao> listarProjetoPorColaborador = todosModulosAcoesProjeto.stream()
				.filter(moduloAcao -> moduloAcao.getAuthority().equals("LISTAR_POR_COLABORADOR_PROJETO")).findFirst();
		Optional<ModuloAcao> listarAlocacaoPorColaborador = todosModulosAcoesAlocacao.stream()
				.filter(moduloAcao -> moduloAcao.getAuthority().equals("LISTAR_POR_COLABORADOR_ALOCACAO")).findFirst();
		Optional<ModuloAcao> cadastrarApontamento = todosModulosAcoesApontamento.stream()
				.filter(moduloAcao -> moduloAcao.getAuthority().equals("CADASTRAR_APONTAMENTO")).findFirst();

		Perfil adm = new Perfil("ADM");
		adm.addModuloAcao(exibirProjeto.get());
		adm.addModuloAcao(listarProjeto.get());
		adm.addModulosAcao(todosModulosAcoesAlocacao);
		perfilService.salvar(adm);

		Perfil colaborador = new Perfil("COLABORADOR");
		colaborador.addModuloAcao(exibirProjeto.get());
		colaborador.addModuloAcao(listarProjetoPorColaborador.get());
		colaborador.addModuloAcao(listarAlocacaoPorColaborador.get());
		colaborador.addModuloAcao(cadastrarApontamento.get());
		perfilService.salvar(colaborador);
	}

	@Transactional
	private void cadastrarUsuariosAtualizarColaboradores() throws Exception {

		Perfil adm = perfilService.buscarPorNome("ADM");
		Perfil colaborador = perfilService.buscarPorNome("COLABORADOR");

		Usuario usuarioAdm = new Usuario("adm@teste.com.br", pe.encode("123"), adm);
		Usuario usuarioProg1 = new Usuario("prog1@teste.com.br", pe.encode("123"), colaborador);
		Usuario usuarioProg2 = new Usuario("prog2@teste.com.br", pe.encode("123"), colaborador);

		usuarioService.salvarTodos(Arrays.asList(usuarioAdm, usuarioProg1, usuarioProg2));

		Colaborador administrador = colaboradorService.buscarPorNome("Administrador");
		administrador.setIdUsuario(usuarioAdm.getId());
		Colaborador programador1 = colaboradorService.buscarPorNome("Programador 1");
		programador1.setIdUsuario(usuarioProg1.getId());
		Colaborador programador2 = colaboradorService.buscarPorNome("Programador 2");
		programador2.setIdUsuario(usuarioProg2.getId());

		colaboradorService.salvarTodos(Arrays.asList(administrador, programador1, programador2));
	}
}
