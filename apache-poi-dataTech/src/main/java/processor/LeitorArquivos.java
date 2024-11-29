package processor;

import processor.plantacao.Plantacao;
import writer.ConexaoBanco;

import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public abstract class LeitorArquivos<T> {
    String aplicacao = "Leitor";
    private ConexaoBanco conexao = new ConexaoBanco();

    public LeitorArquivos() {}

    public LeitorArquivos(ConexaoBanco conexao) {
        this.conexao = conexao;
    }

    public ConexaoBanco getConexao() {
        return conexao;
    }

    public void setConexao(ConexaoBanco conexao) {
        this.conexao = conexao;
    }

    public abstract List<T> extrairDados(Path nomeArquivo, InputStream arquivo);

    public LocalDate converterDate(Date data) {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
