import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TesteSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senha = "123";
        String hash = encoder.encode(senha);
        System.out.println("Hash gerado: " + hash);
    }
}
