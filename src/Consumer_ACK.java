import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Consumer_ACK {

    public static void main(String[] args) throws Exception {
        new Consumer_ACK().run();
    }
    
    public void run() throws MqttException, InterruptedException {
        String broker = "ssl://b-8f208ab8-d1ce-4b37-b63e-288c9e2b067b-1.mq.sa-east-1.amazonaws.com:8883";
        String clientId = "JavaConsumer_ACK";
        String topic_in = "messageInput";
        String topic_out = "messageOutput";
        String username = "usuario";
        String password = "Usuario12345@";

        try {
            // Configura o cliente MQTT
            MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            connOpts.setCleanSession(true);
            
            //Criação da mensagem que será enviada de volta ao cliente
            String message_aux = "Recebido a mensagem na aplicação backend";
            MqttMessage message = new MqttMessage(message_aux.getBytes());
            
            // Conecta ao broker
            mqttClient.connect(connOpts);
            
            // Inscreve-se no tópico
            mqttClient.subscribe(topic_in);
           
            // Aviso de conexão com sucesso
            System.out.println("Consumer_ACK conectado e inscrito no tópico " + topic_in + ".");
            System.out.println("Consumer_ACK irá enviar a resposta ao tópico " + topic_out + ".\n");
            
            // Configura o callback do Consumer
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println("Consumer_ACK: Mensagem recebida: " + new String(mqttMessage.getPayload()));
                    mqttClient.publish(topic_out, message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                	System.out.println("Consumer_ACK: A mensagem foi enviada ao tópico " + topic_out + "\n");
                }
                
                @Override
                public void connectionLost(Throwable throwable) {
                    System.out.println("Consumer_ACK: Conexão perdida com o broker: " + throwable.getMessage());
                }

            });

        } catch (MqttException me) {
            System.out.println("Consumer_ACK: Erro ao conectar ao broker: " + me.getMessage());
        }
    }
}