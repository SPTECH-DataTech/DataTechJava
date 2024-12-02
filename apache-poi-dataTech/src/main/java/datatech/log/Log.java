package datatech.log;

import writer.ConexaoBanco;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Log {

    private String nomeUsuario;
    private String aplicacao;
    private LocalDateTime dataHora;
    private String descricao;
    private String classificacao;
    private Integer fkFazenda;
    private Integer fkEmpresa;
    private Integer fkEstadoMunicipio;
    private ConexaoBanco conexao = new ConexaoBanco();

    public Log(String classificacao, String aplicacao, LocalDateTime dataHora, String descricao, Integer fkFazenda, Integer fkEmpresa, Integer fkEstadoMunicipio) {
        this.aplicacao = aplicacao;
        this.dataHora = dataHora;
        this.descricao = descricao;
        this.classificacao = classificacao;
        this.fkFazenda = fkFazenda;
        this.fkEmpresa = fkEmpresa;
        this.fkEstadoMunicipio = fkEstadoMunicipio;
    }

    public Log(String classificacao, String aplicacao, LocalDateTime dataHora, String descricao) {
        this.aplicacao = aplicacao;
        this.dataHora = dataHora;
        this.descricao = descricao;
        this.classificacao = classificacao;
    }

    public void inserirLogEmArquivo(Log log) throws IOException {
        String caminho = String.format("./log%s.txt", LocalDate.now());
        String conteudo = "";
        conteudo += String.format("(%s) %s : %s %s", classificacao, aplicacao, descricao, LocalDateTime.now());

        FileWriter escritor = new FileWriter(caminho);
        escritor.write(conteudo);
        escritor.close();
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

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public Integer getFkFazenda() {
        return fkFazenda;
    }

    public void setFkFazenda(Integer fkFazenda) {
        this.fkFazenda = fkFazenda;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    public Integer getFkEstadoMunicipio() {
        return fkEstadoMunicipio;
    }

    public void setFkEstadoMunicipio(Integer fkEstadoMunicipio) {
        this.fkEstadoMunicipio = fkEstadoMunicipio;
    }
}
