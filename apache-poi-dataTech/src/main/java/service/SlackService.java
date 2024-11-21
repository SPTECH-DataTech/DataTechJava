package service;

import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.Slack;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SlackService {
    private static List<String> errorList = new ArrayList<>();
    private static final String TOKEN = System.getenv("TOKEN_SLACK");
    private static final String CHANNEL_ID = System.getenv("CHANNEL_ID");
 ;

    public void sendMessage() {
        try {
            Slack slack = Slack.getInstance();
            MethodsClient methods = slack.methods(TOKEN);

            StringBuilder allErrors = new StringBuilder("Lista de erros registrados:\n");
            for (String error : errorList) {
                allErrors.append("```\n").append(error).append("\n```\n");
            }

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(CHANNEL_ID)
                    .text(allErrors.toString())
                    .build();

            ChatPostMessageResponse response = methods.chatPostMessage(request);

            if (response.isOk()) {
                System.out.println("Mensagem enviada com sucesso!");
            } else {
                System.err.println("Erro ao enviar mensagem: " + response.getError());
            }
        } catch (SlackApiException | IOException e) {
            System.err.println("Erro durante a comunicação com a API do Slack: " + e.getMessage());
        }



    }

    public static void errorSlack(Throwable e) {
        StackTraceElement errorLocation = e.getStackTrace()[0];

            String application = "DataTech";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            String description = e.getMessage();
            String tipoExecao = e.getClass().getName();
            String filePath = errorLocation.getFileName();

                String location = String.format(
                    "Método: %s\nClasse: %s\nLinha: %d\nPath: %s\n",
                    errorLocation.getMethodName(),
                    errorLocation.getClassName(),
                    errorLocation.getLineNumber(),
                    filePath
            );

            String errorMessage = String.format(
                    "Aplicação: %s\nData e Hora: %s\nDescrição do Erro: %s\nTipo Exceção: %s\nLocalização:\n%s",
                    application, timestamp, description, tipoExecao, location
            );

            errorList.add(errorMessage);
    }

    public void listarErros(){
        System.out.println(errorList);
    }

    public void limparListaErros(){
        errorList.clear();
    }
}
