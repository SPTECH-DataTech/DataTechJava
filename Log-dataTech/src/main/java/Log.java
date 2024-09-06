import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    String nomeUsuario;
    String data;
    String hora;

    void mostrarLogs() {

        nomeUsuario = System.getProperty("user.name");

        Date dataHoraAtual = new Date();
        data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
        hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);

        System.out.println(nomeUsuario + " | " + data + " " + hora + " - Lendo os dados da base de dados \"Agropecuária\"...");
        System.out.println(nomeUsuario + " | " + data + " " + hora + " - Dados da base de dados \"Agropecuária\" registrados com sucesso no database.");
        System.out.println(nomeUsuario + " | " + data + " " + hora + " - Lendo os dados da base de dados \"Munícipios\"...");
        System.out.println(nomeUsuario + " | " + data + " " + hora + " - Não foi possível ler a base de dados \"Munícipios\", erro ao ler.");
        System.out.println(nomeUsuario + " | " + data + " " + hora + " - Lendo os dados da base de dados \"Munícipios-Refatorado\"...");
        System.out.println(nomeUsuario + " | " + data + " " + hora + " - Dados da base de dados \"Munícipios-Refatorado\" registrado com sucesso no database.");
        System.out.println(nomeUsuario + " | " + data + " " + hora + " - Lendo os dados da base de dados \"Base-de-Dados-Clima\"...");
        System.out.println(nomeUsuario + " | " + data + " " + hora + " - Dados da base de dados \"Base-de-Dados-Clima\" registrados com sucesso no database.");

    }

}
