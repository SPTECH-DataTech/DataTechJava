package writer;

import datatech.log.Log;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import processor.Plantacao;

import javax.sql.DataSource;
import java.util.List;

public class ConexaoBanco {

    private final DataSource dataSource;

    public ConexaoBanco() {
        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setUrl("jdbc:mysql://54.221.139.10:3306/datatech");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("datatech123");

        this.dataSource = basicDataSource;
    }

    public JdbcTemplate gerarNovaConeexao() {
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        JdbcTemplate connection = conexaoBanco.getConnection();

        return connection;
    }

    public void inserirLogNoBanco(Log log){
        System.out.println(log.getData().toString() + log.getAplicacao() + log.getDescricao());
        gerarNovaConeexao().update("INSERT INTO logsjava (descricao) VALUES (?)",log.getData().toString() + " " + log.getAplicacao() + " " + log.getDescricao());
    }

    public void inserirPlantacoesNoBanco(List<Plantacao> plantacoes) {
        System.out.println("Inserindo dados lidos no Banco de dados...");
        for (Plantacao plantacao : plantacoes) {
            gerarNovaConeexao().update("INSERT INTO plantacaoMunicipioDash (fkMunicipio, ano, areaPlantada, quantidadeColhida, valorTotalReais) VALUES (?,?,?,?,?)",
                    plantacao.getMunicipio(), plantacao.getAno(), plantacao.getAreaPlantada(), plantacao.getQuantidadeColhida(), plantacao.getValorReais());
        }
        System.out.println("Inserções encerradas");
    }

    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }

}
