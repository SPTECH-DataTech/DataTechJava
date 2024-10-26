package datatech.log;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Log {

    private String nomeUsuario;
    private String aplicacao;
    private LocalDate dataHora;
    private String descricao;

    public Log(String aplicacao, LocalDate dataHora, String descricao) {
        this.aplicacao = aplicacao;
        this.dataHora = dataHora;
        this.descricao = descricao;
    }

    public String getAplicacao() {
        return aplicacao;
    }

    public LocalDate getData() {
        return dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }

    public void setData(LocalDate dataHora) {
        this.dataHora = dataHora;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
