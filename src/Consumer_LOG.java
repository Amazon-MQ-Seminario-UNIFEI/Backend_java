import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class Consumer_LOG {
	
    public static void main(String[] args) throws Exception {
        new Consumer_LOG().run();
    }
	

    public void run() throws MqttException, InterruptedException {
        String broker = "ssl://b-8f208ab8-d1ce-4b37-b63e-288c9e2b067b-1.mq.sa-east-1.amazonaws.com:8883";
        String clientId = "JavaConsumer_LOG";
        String topic_in = "fileInput";
        String topic_out = "fileOutput";
        String username = "usuario";
        String password = "Usuario12345@";

        try {
            // Configura o Servidor MQTT
            MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            connOpts.setCleanSession(true);
            
            // Conecta ao broker
            mqttClient.connect(connOpts);
            
            // Inscrever no tópico
            mqttClient.subscribe(topic_in);
            
            // Aviso de conexão com sucesso
            System.out.println("Consumer_LOG conectado e inscrito no tópico " + topic_in + ".");
            System.out.println("Consumer_LOG irá enviar a resposta de modificação de arquivo ao tópico " + topic_out + ".\n");
            
            // Configura o callback do Consumer
            mqttClient.setCallback(new MqttCallback() 
            {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println("Consumer_LOG: Mensagem escrita no LOG: " + new String(mqttMessage.getPayload()));
                    
                    //Modificação de arquivo
                    String arquivo_input = new String(mqttMessage.getPayload());
                    AdicionarPalavraNoArquivo.main(arquivo_input);
                    
                    //Retorno ao cliente
                    String message_aux = "Mensagem: " + new String(mqttMessage.getPayload())  + " | Registrada no arquivo do backend";
                    MqttMessage message = new MqttMessage(message_aux.getBytes());
                    mqttClient.publish(topic_out, message);
                    
                }
                
                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                	System.out.println("Consumer_LOG: A mensagem foi enviada ao tópico " + topic_out + "\n");
                }
                
                @Override
                public void connectionLost(Throwable throwable) {
                    System.out.println("Consumer_LOG: Conexão perdida com o broker: " + throwable.getMessage());
                }

            });
        } catch (MqttException me) {
            System.out.println("Consumer_LOG: Erro ao conectar ao broker: " + me.getMessage());
        }
    }
}