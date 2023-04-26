import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class Consumer_SOMA {
	
    public static void main(String[] args) throws Exception {
        new Consumer_SOMA().run();
    }
	

    public void run() throws MqttException, InterruptedException {
        String broker = "ssl://b-8f208ab8-d1ce-4b37-b63e-288c9e2b067b-1.mq.sa-east-1.amazonaws.com:8883";
        String clientId = "JavaConsumer_SOMA";
        String topic_in = "functionInput";
        String topic_out = "functionOutput";
        String username = "usuario";
        String password = "Usuario12345@";

        try {
            // Configura o cliente MQTT
            MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            connOpts.setCleanSession(true);
            
            // Conecta ao broker
            mqttClient.connect(connOpts);
            
            // Inscreve-se no tópico
            mqttClient.subscribe(topic_in);
          
            // Aviso de conexão com sucesso
            System.out.println("Consumer_SOMA: conectado e inscrito no tópico " + topic_in + ".");
            System.out.println("Consumer_SOMA: irá enviar o retorno da função ao tópico " + topic_out + ".\n");

            // Configura o callback do Consumer
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {              
                    //Tratamento da string
                    String message = new String(mqttMessage.getPayload());
                    System.out.println("Consumer_SOMA: Mensagem recebida: " + message);
                    
                    String[] parts = message.split(" ");
                    
                    //Definindo o tamanho da String
                    int k = parts.length;
                    //System.out.println("Debug: valor de k" + k);
                    
                    
                    //Fazendo a soma
                    int soma = 0;
                    for (int i = 0; i < k; i++)
                    {
                    	try {
                    		Integer.parseInt(parts[i]);
                    		soma += Integer.parseInt(parts[i]);
                    	}catch (NumberFormatException e) {
                    		try {
                    		Double.parseDouble(parts[i]);
                    		soma += Double.parseDouble(parts[i]);
                    	//String não é numero	
                    	}catch (NumberFormatException e2)
                    	{
                    		//System.out.println("Debug: " + parts[i] + "não é um número");
                    	}	
                      }
                    }
                    
                    String soma_return = Integer.toString(soma);
   
                    //Retorno ao cliente
                    String message_aux = "O retorno da soma para a mensagem: " + new String(mqttMessage.getPayload())  + " é: " + soma_return;
                    MqttMessage message_output = new MqttMessage(message_aux.getBytes());
                    mqttClient.publish(topic_out, message_output);
                    
                }
                
                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                	System.out.println("Consumer_SOMA: A mensagem foi enviada ao tópico " + topic_out + "\n");
                }
                
                @Override
                public void connectionLost(Throwable throwable) {
                    System.out.println("Consumer_SOMA: Conexão perdida com o broker: " + throwable.getMessage());
                }
            });
        } catch (MqttException me) {
            System.out.println("Consumer_SOMA: Erro ao conectar ao broker: " + me.getMessage());
        }
    }
    
}