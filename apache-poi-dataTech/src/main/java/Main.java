import org.springframework.jdbc.core.JdbcTemplate;
import writer.ConexaoBanco;

public class Main {

    public static void main(String[] args) {

        Log logs = new Log();
        logs.mostrarLogs();

        ConexaoBanco conexaoBanco = new ConexaoBanco();
        JdbcTemplate connection = conexaoBanco.getConnection();

        connection.update("""
                INSERT INTO dataTech.usuario (cpf, nome, email, senha, fk_empresa) values
                ("12345678901", "teste-conexao", "teste@gmail", "minhasenha123", 1);
                """);
    }

}
