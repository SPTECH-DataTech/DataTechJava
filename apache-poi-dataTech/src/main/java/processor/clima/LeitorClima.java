package processor.clima;

import com.mysql.cj.jdbc.JdbcConnection;
import datatech.log.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcOperations;
import processor.LeitorArquivos;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;
import writer.ConexaoBanco;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static service.SlackService.errorSlack;
import static service.SlackService.sendMessage;

public class LeitorClima extends LeitorArquivos {
    private static final String aplicacao = "Leitor Estado Municipio" ;
    private static final ConexaoBanco conexaoBanco = new ConexaoBanco();
    private static final List logs = new ArrayList<>();

    String municipio = "";
    String campoMunicipio = "";


    public LeitorClima() {
        super(aplicacao, conexaoBanco, logs);
    }

    @Override
    public List extrairDados(String nomeArquivo, InputStream arquivo) {
        try {
            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));

            String nomeArquivoString = nomeArquivo.toString();

            // Criando um objeto Workbook a partir do arquivo recebido
            Workbook workbook;
            if (nomeArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new XSSFWorkbook(arquivo);
            }

            Sheet sheet = workbook.getSheetAt(0);
            List<Clima> climasExtraidos = new ArrayList<>();

            // Iterando sobre as linhas da planilha
            for (Row row : sheet) {
                Clima clima = new Clima();

                if (row.getRowNum() == 0){
                    Cell cell = row.getCell(0);
                    campoMunicipio = cell.getStringCellValue();
                    String[] partes = campoMunicipio.split(": ");
                    municipio = partes[1];

                }
                if (row.getRowNum() < 11) {

                    System.out.println("\nLendo cabeçalho");

                    for (int i = 0; i < 4; i++) {
                        Cell cell = row.getCell(i);

                        if (row.getRowNum() == 0) {
                            clima.setMunicipio(row.getCell(0).getStringCellValue());
                        }



                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            String coluna = cell.getStringCellValue();
                            System.out.println("Coluna " + i + ": " + coluna);
                        } else {
                            System.out.println("Coluna " + i + ": não é string.");
                        }

                    }

                    System.out.println("--------------------");
                    continue;

                }


                Cell cellDataMedicao = row.getCell(0);
                if (cellDataMedicao != null) {
                    clima.setDataMedicao(cellDataMedicao.toString());
                } else {
                    clima.setDataMedicao("Data não disponível");
                }
                Double mediaTemperaturaMaxima = lerValor(row.getCell(1));
                Double mediaTemperaturaMinima = lerValor(row.getCell(2));
                Double umidadeAr = lerValor(row.getCell(3));


                if (mediaTemperaturaMaxima == 0.0 || mediaTemperaturaMinima == 0.0 || umidadeAr == 0.0) {
                    continue;
                }

                clima.setMediaTemperaturaMaxima(lerValor(row.getCell(1)));
                clima.setMediaTemperaturaMinima(lerValor(row.getCell(2)));
                clima.setUmidadeAr(lerValor(row.getCell(3)));
                clima.setMunicipio(municipio);
                climasExtraidos.add(clima);
            }

            // Fechando o workbook após a leitura
            workbook.close();

            System.out.println("\nLeitura do arquivo finalizada\n");
            Log log = new Log("OK", this.aplicacao, LocalDateTime.now(), "Leitura do arquivo finalizada");
            logs.add(log);

            System.out.println("Municipio: " + municipio);
//            Log log = new Log("OK", this.aplicacao, LocalDateTime.now(), "Leitura do arquivo finalizada");

//            conexaoBanco.inserirLogNoBanco(log);
            return climasExtraidos;
        } catch (IOException e) {
            System.out.println("Falha ao ler arquivo de clima");
            Log log = new Log("Erro", this.aplicacao, LocalDateTime.now(), "Falha ao ler arquivo de clima");
            logs.add(log);
            throw new RuntimeException(e);
        }
    }

    public Double lerValor(Cell cell) {
        if (cell == null) {
            return 0.0;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                String stringValue = cell.getStringCellValue().trim(); // Remove espaços em branco
                if (stringValue.equalsIgnoreCase("null") || stringValue.isEmpty()) {
                    return 0.0; // Retorna 0.0 se a string for "null" ou vazia
                }
                try {
                    return Double.parseDouble(stringValue.replace(",", "."));
                } catch (NumberFormatException e) {
                    errorSlack(e);
                    System.err.println("Erro ao converter para Double: " + e.getMessage());
                    throw new Error("Erro ao converter para Double (Leitor Clima): " + e.getMessage(), e);
                }
            default:
                return 0.0;
        }

    }
}
