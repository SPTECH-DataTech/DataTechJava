package writer;

import datatech.log.Log;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import processor.plantacao.Plantacao;
import processor.clima.Clima;
import processor.estadoMunicipio.EstadoMunicipio;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class ConexaoBanco {

    private final DataSource dataSource;

    private String aplicacao = "Conexao-banco";

    private JdbcTemplate singletonJdbcTemplate;

    public ConexaoBanco() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:mysql://34.198.235.194:3306/datatech");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("datatech123");

        this.dataSource = basicDataSource;
    }

    public JdbcTemplate getConnection() {
        if (singletonJdbcTemplate == null) {
            this.singletonJdbcTemplate = new JdbcTemplate(dataSource);
        }
        return this.singletonJdbcTemplate;
    }

    public JdbcTemplate gerarNovaConexao() {
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        JdbcTemplate connection = conexaoBanco.getConnection();

        return connection;
    }

    public void inserirLogNoBanco(Log log) {
        System.out.println(log.getData().toString() + log.getAplicacao() + log.getDescricao());
        gerarNovaConexao().update("INSERT IGNORE INTO logJava (statusLog, descricao, dataLog, fkFazenda, fkEmpresa, fkEstadoMunicipio) VALUES (?, ?, ?, ?, ?, ?)", log.getClassificacao(), log.getAplicacao() + " " + log.getDescricao(), log.getData().toString(), log.getFkFazenda(), log.getFkEmpresa(), log.getFkEstadoMunicipio());
    }

    /*public void inserirPlantacoesNoBanco(List<Plantacao> plantacoes) {
        JdbcTemplate conexao = getConnection();

        for (Plantacao plantacao : plantacoes) {
            conexao.update("INSERT INTO plantacaoMunicipioDash (fkMunicipio, ano, fkTipoCafe, areaPlantada, quantidadeColhida, valorTotalReais) VALUES (?,?,?,?,?,?)",
                    plantacao.getMunicipio(), plantacao.getAno(), plantacao.getTipoCafe(), plantacao.getAreaPlantada(), plantacao.getQuantidadeColhida(), plantacao.getValorReais());
        }
    }*/

    public void inserirPlantacoesNoBanco(List<Plantacao> plantacoes) {
        JdbcTemplate conexao = getConnection();

        List<String> queries = new ArrayList<>();
        for (Plantacao plantacao : plantacoes) {
            // System.out.println(estadoMunicipio.toString());
            queries.add(String.format("INSERT IGNORE INTO plantacaoFazenda (fkFazenda, fazenda_fkEmpresa, fazenda_fkEstadoMunicipio, ano, areaPlantada, quantidadeColhida, valorTotalReais) VALUES (%d, %d, %d, %d, %s, %s, %s);",
                    plantacao.getFkFazenda(), plantacao.getFazenda_fkEmpresa(), plantacao.getFazenda_fkEstadoMunicipio(), plantacao.getAno(), plantacao.getAreaPlantada().toString().replace(",", "."), plantacao.getQuantidadeColhida().toString().replace(",", "."), plantacao.getValorReais().toString().replace(",", ".")));
        }

        conexao.batchUpdate(queries.toArray(new String[0]));
    }

    public Integer buscarFkMunicipio(String municipio) {
        String sql = "SELECT id FROM estadoMunicipio WHERE municipio = ?";
        try {
            return getConnection().queryForObject(sql, new Object[]{municipio}, Integer.class);
        } catch (Exception e) {
            System.err.println("Erro ao buscar fkMunicipio: " + e.getMessage());
            return null;
        }
    }

    public void inserirClimasNoBanco(List<Clima> climas) {
        for (Clima clima : climas) {
        Integer fkMunicipio = buscarFkMunicipio(clima.getMunicipio());

        if (fkMunicipio == null) {
            System.out.println("fkMunicipio não encontrado: " + clima.getMunicipio() + ". Ignorando inserção...");
            continue;
        }


            System.out.println(clima.toString());

            getConnection().update("INSERT INTO climaMunicipioDash (fkMunicipio, dataCaptura, temperaturaMax, temperaturaMin, umidadeMedia) VALUES (?, ?, ?, ?, ?)",
                    fkMunicipio,
                    clima.getDataMedicao(),
                    clima.getMediaTemperaturaMaxima(),
                    clima.getMediaTemperaturaMinima(),
                    clima.getUmidadeAr());
        }
    }
    public void inserirEstadoMunicipioNoBanco(List<EstadoMunicipio> estadoMunicipios) {
        JdbcTemplate conexao = getConnection();
        List<String> queries = new ArrayList<>();
        for (EstadoMunicipio estadoMunicipio : estadoMunicipios) {
            System.out.println(estadoMunicipio.toString());
            queries.add(String.format("INSERT IGNORE INTO estadoMunicipio(id, idUf, estado, municipio) VALUES (%d,%d,\"%s\",\"%s\")",
                    estadoMunicipio.getIdMunicipio(), estadoMunicipio.getIdUf(), estadoMunicipio.getEstado(), estadoMunicipio.getMunicipio()));
        }
        conexao.batchUpdate(queries.toArray(new String[0]));
    }
}
