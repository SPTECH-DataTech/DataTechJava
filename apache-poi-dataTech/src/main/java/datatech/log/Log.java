package datatech.log;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Log {

    private String nomeUsuario;
    private String aplicacao;
    private LocalDateTime dataHora;
    private String descricao;

    public Log(String aplicacao, LocalDateTime dataHora, String descricao) {
        this.aplicacao = aplicacao;
        this.dataHora = dataHora;
        this.descricao = descricao;
    }

    public String getAplicacao() {
        return aplicacao;
    }

    public LocalDateTime getData() {
        return dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }

    public void setData(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
