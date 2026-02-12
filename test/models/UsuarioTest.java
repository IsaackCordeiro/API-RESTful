package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.*;

public class UsuarioTest {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void deveGerarHashAoSetarSenha() {
        Usuario usuario = new Usuario();
        String senhaPura = "minhaSenhaForte123";

        usuario.setSenha(senhaPura);


        assertNotNull(usuario.getSenha());
        assertNotEquals(senhaPura, usuario.getSenha());
        assertTrue(usuario.getSenha().startsWith("$2"));
    }

    @Test
    public void deveValidarSenhaCorreta() {
        Usuario usuario = new Usuario();
        usuario.setSenha("123456");

        assertTrue("A senha deve ser válida", usuario.checkSenha("123456"));

        assertFalse("Senha errada deve retornar false", usuario.checkSenha("654321"));
    }

    @Test
    public void naoDeveAceitarUsuarioSemEmail() {
        Usuario usuario = new Usuario();
        usuario.setSenha("1234");

        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(usuario);

        assertFalse("Deveria ter erros de validação", violacoes.isEmpty());

        boolean erroNoEmail = violacoes.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertTrue(erroNoEmail);
    }

    @Test
    public void naoDeveAceitarEmailInvalido() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email_sem_arroba.com");
        usuario.setSenha("1234");

        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(usuario);
        assertFalse(violacoes.isEmpty());
    }


    @Test
    public void naoDeveExporSenhaNoJson() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenha("123456");

        JsonNode json = Json.toJson(usuario);

        assertFalse("A senha não deve aparecer no JSON de resposta", json.has("senha"));
        assertFalse("A senha não deve aparecer no JSON de resposta", json.has("password"));
        assertTrue("O email deve aparecer", json.has("email"));
    }

    @Test
    public void deveLerSenhaDoJson() throws Exception {
        String jsonInput = "{\"email\": \"teste@email.com\", \"senha\": \"123456\"}";

        ObjectMapper mapper = new ObjectMapper();
        Usuario usuario = mapper.readValue(jsonInput, Usuario.class);

        assertNotNull("A senha deve ser lida do JSON", usuario.getSenha());
    }
}