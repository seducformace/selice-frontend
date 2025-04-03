import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TesteSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = "$2a$10$BjOsOelkFoZX/juDPJR28uUlki9MQCxJJZv4BppZEptMX9M8VjkyK";
        String senha = "123";

        boolean corresponde = encoder.matches(senha, hash);
        System.out.println("Senha confere? " + corresponde);
    }
}
