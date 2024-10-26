package commons;

import datatech.log.Log;
import org.springframework.jdbc.core.JdbcTemplate;
import writer.ConexaoBanco;

public class BancoConnection {

    public JdbcTemplate gerarNovaConeexao() {
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        JdbcTemplate connection = conexaoBanco.getConnection();

        return connection;
    }

    public void inserirLogNoBanco(Log log){
        System.out.println(log.getData().toString() + log.getAplicacao() + log.getDescricao());
        gerarNovaConeexao().update("INSERT INTO logsJava (descricao) VALUES (?)",log.getData().toString() + log.getAplicacao() + log.getDescricao());
    }
}
