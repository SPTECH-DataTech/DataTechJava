package writer;

import datatech.log.Log;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import processor.Plantacao;
import processor.clima.Clima;
import processor.estadoMunicipio.EstadoMunicipio;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

public class ConexaoBanco {

    private final DataSource dataSource;

    private String aplicacao = "Conexao-banco";

    public ConexaoBanco() {
        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setUrl("jdbc:mysql://34.198.235.194:3306/datatech");
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
        for (Plantacao plantacao : plantacoes) {
            gerarNovaConeexao().update("INSERT INTO plantacaoMunicipioDash (fkMunicipio, ano, fkTipoCafe, areaPlantada, quantidadeColhida, valorTotalReais) VALUES (?,?,?,?,?,?)",
                    plantacao.getMunicipio(), plantacao.getAno(), plantacao.getTipoCafe(), plantacao.getAreaPlantada(), plantacao.getQuantidadeColhida(), plantacao.getValorReais());
        }
    }

    public void inserirClimasNoBanco(List<Clima> climas) {
        for (Clima clima : climas) {
            System.out.println(clima.toString());
            gerarNovaConeexao().update("INSERT INTO climaMunicipioDash (data, temperaturaMax, temperaturaMin, umidadeMedia) VALUES (?,?,?,?)",
                    clima.getDataMedicao(), clima.getMediaTemperaturaMaxima(), clima.getMediaTemperaturaMinima(), clima.getUmidadeAr());
        }
    }

    public void inserirEstadoMunicipioNoBanco(List<EstadoMunicipio> estadoMunicipios) {
        for (EstadoMunicipio estadoMunicipio : estadoMunicipios) {
            System.out.println(estadoMunicipio.toString());
            gerarNovaConeexao().update("INSERT IGNORE INTO estadoMunicipio (idUf, estado, idMunicipio, municipio) VALUES (?,?,?,?)",
                    estadoMunicipio.getIdUf(), estadoMunicipio.getEstado(), estadoMunicipio.getIdMunicipio(), estadoMunicipio.getMunicipio());
        }
    }

    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }

}
