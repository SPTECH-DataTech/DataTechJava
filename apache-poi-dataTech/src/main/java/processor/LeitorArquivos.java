package processor;

import datatech.log.Log;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.jdbc.core.JdbcTemplate;
import writer.ConexaoBanco;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public abstract class LeitorArquivos<T> {
    private String aplicacao;
    private ConexaoBanco conexaoBanco;
    private List<Log> logs;

    public LeitorArquivos(String aplicacao, ConexaoBanco conexaoBanco, List logs) {
        this.aplicacao = aplicacao;
        this.conexaoBanco = conexaoBanco;
        this.logs = logs;
    }

    public abstract List extrairDados(String nomeArquivo, InputStream arquivo);
}
