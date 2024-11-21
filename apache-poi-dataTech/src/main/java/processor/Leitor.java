package processor;

import datatech.log.Log;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.SlackService;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;
import writer.ConexaoBanco;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static service.SlackService.errorSlack;

public class Leitor {
    String aplicacao = "Leitor";
    ConexaoBanco conexao = new ConexaoBanco();

    public Leitor() {}

    public List<Plantacao> extrairPlantacao(String nomeArquivo, InputStream arquivo) {
        try {
            Log logInicioLeitura = new Log(this.aplicacao + " ", LocalDateTime.now(), " Iniciando leitura do arquivo %s\n".formatted(nomeArquivo));
            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));
            conexao.inserirLogNoBanco(logInicioLeitura);

            // Criando um objeto Workbook a partir do arquivo recebido
            Workbook workbook;
            if (nomeArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new HSSFWorkbook(arquivo);
            }

            Sheet sheet = workbook.getSheetAt(0);

            List<Plantacao> plantacoes = new ArrayList<Plantacao>();

            // Iterando sobre as linhas da planilha
            for (Row row : sheet) {

                if (row.getRowNum() == 0) {
                    System.out.println("\nLendo cabeçalho");

                    for (int i = 0; i < 5; i++) {
                        String coluna = row.getCell(i).getStringCellValue();
                        System.out.println("Coluna " + i + ": " + coluna);
                    }

                    System.out.println("--------------------");
                    continue;
                }

                // Extraindo valor das células e criando objeto plantação

                Plantacao plantacao = new Plantacao();
                plantacao.setAno((int) row.getCell(0).getNumericCellValue());
                plantacao.setMunicipio((int) row.getCell(2).getNumericCellValue());
                plantacao.setQuantidadeColhida(row.getCell(3).getNumericCellValue());
                plantacao.setAreaPlantada((int) row.getCell(4).getNumericCellValue());
                plantacao.setValorReais((int) row.getCell(6).getNumericCellValue());

                String tipoCafe = row.getCell(7).getStringCellValue();
                if (tipoCafe.equals("arábica")) {
                    plantacao.setTipoCafe(1);
                } else {
                    plantacao.setTipoCafe(2);
                }

                plantacoes.add(plantacao);
            }

            // Fechando o workbook após a leitura
            workbook.close();

            Log logFimLeitura = new Log(this.aplicacao + " ", LocalDateTime.now(), " Leitura do arquivo finalizada");
            System.out.println("\nLeitura do arquivo finalizada\n");
            conexao.inserirLogNoBanco(logFimLeitura);

            return plantacoes;

        } catch (IOException e) {
            // Caso ocorra algum erro durante a leitura do arquivo uma exceção será lançada
            Log log = new Log(this.aplicacao + " ", LocalDateTime.now(), "Erro ao ler o arquivo" + e.getMessage());
            System.out.println("Erro ao ler o arquivo" + e.getMessage());
            conexao.inserirLogNoBanco(log);
            errorSlack(e);
            throw new RuntimeException(e);

        }
    }

    private LocalDate converterDate(Date data) {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
