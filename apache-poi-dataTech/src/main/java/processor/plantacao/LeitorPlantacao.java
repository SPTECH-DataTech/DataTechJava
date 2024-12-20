package processor.plantacao;

import datatech.log.Log;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import processor.LeitorArquivos;
import writer.ConexaoBanco;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeitorPlantacao extends LeitorArquivos {
    private static final String aplicacao = "Leitor Plantacao" ;
    private static final ConexaoBanco conexaoBanco = new ConexaoBanco();
    private static final List logs = new ArrayList<>();


    public LeitorPlantacao() {
        super(aplicacao, conexaoBanco, logs);
    }

    @Override
    public List extrairDados(String nomeArquivo, InputStream arquivo) {
        try {
            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));
            JdbcTemplate conexao = conexaoBanco.getConnection();

            // divide o nome do arquivo em 2 pelo traço (exemplo: plantacao-5.xlsx
            // vira [plantacao, 5.xlsx]
            String[] splittedName = nomeArquivo.toString().split("-");

            // faz a mesma coisa e divide o 5.xlsx pelo ponto e vira [5, xlsx]
            Integer idFazenda = Integer.parseInt(splittedName[1].split("\\.")[0]);

            // query da fkEmpresa
            Integer fkEmpresa = conexao.queryForObject("SELECT fkEmpresa FROM fazenda where id = " + idFazenda, Integer.class);

            // query do fkEstadoMunicipio
            Integer fkEstadoMunicipio = conexao.queryForObject("SELECT fkEstadoMunicipio FROM fazenda where id = " + idFazenda, Integer.class);

            // query do fkTipoCafe
            Integer fkTipoCafe = conexao.queryForObject("SELECT fkTipoCafe FROM fazenda where id = " + idFazenda, Integer.class);

            // Criando um objeto Workbook a partir do arquivo recebido
            Workbook workbook;
            if (nomeArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new HSSFWorkbook(arquivo);
            }

            Sheet sheet = workbook.getSheetAt(0);

            List<Plantacao> plantacoes = new ArrayList<>();

            // Iterando sobre as linhas da planilha
            for (Row row : sheet) {

                //for (int b = 5000; b < 6000; b++) {

                //Row row = sheet.getRow(b);

                if (row.getRowNum() == 0) {
                    System.out.println("\nLendo cabeçalho");

                    for (int i = 0; i < 8; i++) {
                        String coluna = row.getCell(i).getStringCellValue();
                        System.out.println("Coluna " + i + ": " + coluna);
                    }

                    System.out.println("--------------------");
                    continue;
                }

                Integer ano = tratarNullInt(row, 0);
                Integer municipio = tratarNullInt(row, 2);
                Double qtdColhida = tratarNullDouble(row, 3);
                Double areaPlantada = tratarNullDouble(row, 4);
                Double valorReais = tratarNullDouble(row, 6);
                Integer idTipoCafe = 2;

                String tipoCafe = row.getCell(7).getStringCellValue();
                if (tipoCafe.equals("arábica")) {
                    idTipoCafe = 1;
                }

                if (municipio.equals(fkEstadoMunicipio) && idTipoCafe.equals(fkTipoCafe) &&
                        qtdColhida != 0 &&
                        areaPlantada != 0) {

                    // Extraindo valor das células e criando objeto plantação

                    Plantacao plantacao = new Plantacao();
                    plantacao.setAno(ano);
                    plantacao.setQuantidadeColhida(qtdColhida);
                    plantacao.setAreaPlantada(areaPlantada);
                    plantacao.setValorReais(valorReais);
                    plantacao.setFkFazenda(idFazenda);
                    plantacao.setFazenda_fkEmpresa(fkEmpresa);
                    plantacao.setFazenda_fkEstadoMunicipio(fkEstadoMunicipio);

                    System.out.println(plantacao.toString());

                    plantacoes.add(plantacao);

                }
            }

            // Fechando o workbook após a leitura
            workbook.close();

            Log log = new Log("OK", this.aplicacao + " ", LocalDateTime.now(), " Leitura do arquivo finalizada", idFazenda, fkEmpresa, fkEstadoMunicipio);
            System.out.println("\nLeitura do arquivo finalizada\n");
            logs.add(log);
            conexaoBanco.inserirLogNoBanco(log);
            return plantacoes;

        } catch (IOException e) {
            // Caso ocorra algum erro durante a leitura do arquivo uma exceção será lançada
            Log log = new Log("ERRO", this.aplicacao + " ", LocalDateTime.now(), "Erro ao ler o arquivo");
            System.out.println("Erro ao ler o arquivo" + e.getMessage());
            logs.add(log);
            conexaoBanco.inserirLogNoBanco(log);
            throw new RuntimeException(e);
        }
    }

    public Integer tratarNullInt(Row row, int num) {
        if (row.getCell(num) == null) {
            return 0;
        }

        return (int) row.getCell(num).getNumericCellValue();
    }

    public Double tratarNullDouble(Row row, int num) {
        if (row.getCell(num) == null) {
            return 0.0;
        }
        return row.getCell(num).getNumericCellValue();
    }

    private LocalDate converterDate(Date data) {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
