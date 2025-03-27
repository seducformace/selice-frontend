import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashValidator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Senha original
        String rawPassword = "123456";

        // Hash armazenado no banco
        String encodedPassword = "$2a$10$eBmn0m.CWro0Ga6G6FTRle3GbV1RO7NeiLl27FNS0LlRI/viPqoG2";

        // Testando se a senha original corresponde ao hash
        boolean matches = encoder.matches(rawPassword, encodedPassword);

        if (matches) {
            System.out.println("Senha válida: O hash corresponde à senha.");
        } else {
            System.out.println("Senha inválida: O hash não corresponde à senha.");
        }
    }
}
