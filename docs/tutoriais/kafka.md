TUTORIAL

Fontes: 
- https://kafka.apache.org/intro 
- https://kafka.apache.org/documentation/ 
- https://kafka.apache.org/documentation/#design 
- https://kafka.apache.org/documentation.html#adminapi 
- https://docs.confluent.io/kafka/introduction.html 
- https://avro.apache.org/docs/1.12.0/getting-started-java/ 
- https://aws.amazon.com/pt/what-is/apache-kafka/ (Kafka x RabbitMQ) 
- https://www.redhat.com/pt-br/topics/integration/what-is-apache-kafka 
- https://www.baeldung.com/apache-kafka 
- https://www.datacamp.com/pt/tutorial/apache-kafka-for-beginners-a-comprehensive-guide 
- https://medium.com/@fabiosalomao/uma-breve-introdu%C3%A7%C3%A3o-ao-kafka-8bb32b2059ac


Teoria:  
```
Apache Kafka é uma plataforma open source distribuída para transmissão de dados capaz de 
publicar, subscrever, armazenar e processar fluxos de registro em tempo real. 

Kafka é um sistema distribuído composto por servidores e clientes que comunicam através de 
um protocolo de rede TCP de alto desempenho. Em sua essência, o Kafka é um sistema de 
mensagens de inscrição publicado distribuído. Os dados são gravados em tópicos do Kafka 
pelos produtores e consumidos pelos consumidores. Os tópicos do Kafka podem ser 
particionados, habilitando o processamento paralelo de dados, e os tópicos podem ser 
replicados em vários corretores para tolerância a falhas.

Principais Conceitos e Terminologia

Um evento registra o fato de que "algo aconteceu" no mundo ou no seu negócio. Também é 
chamado de registro ou mensagem na documentação. Quando você lê ou grava dados para o 
Kafka, você faz isso na forma de eventos. Conceitualmente, um evento tem uma chave, valor, 
carimbo de data/hora e cabeçalhos de metadados opcionais.

Os produtores são os aplicativos do cliente que publicam eventos (gravam) para o Kafka, e 
os consumidores são aqueles que se inscrevem (leêm e processam) esses eventos. Em Kafka, 
os produtores e consumidores são totalmente dissociados e agnósticos uns dos outros, o que 
é um elemento-chave de design para alcançar a alta escalabilidade pela qual o Kafka é 
conhecido. Por exemplo, os produtores nunca precisam esperar pelos consumidores.

Os eventos são organizados e armazenados de forma durável em tópicos. Muito simplificado, 
um tópico é semelhante a uma pasta em um sistema de arquivos, e os eventos são os arquivos 
nessa pasta. Um exemplo de nome de tópico pode ser "pagamentos". Os tópicos no Kafka são 
sempre multi-produtor e multi-assinante: um tópico pode ter zero, um ou muitos produtores 
que escrevem eventos para ele, bem como zero, um ou muitos consumidores que assinam esses 
eventos. Eventos em um tópico podem ser lidos com a frequência necessária – ao contrário 
dos sistemas de mensagens tradicionais, os eventos não são excluídos após o consumo. Em 
vez disso, você define por quanto tempo o Kafka deve reter seus eventos por meio de uma 
configuração por tópico, após o qual os eventos antigos serão descartados. 

Os tópicos são particionados, o que significa que um tópico é distribuído por uma série de 
"baldes" localizados em diferentes corretores Kafka. Esse posicionamento distribuído de 
seus dados é muito importante para a escalabilidade, pois permite que os aplicativos do 
cliente leiam e escrevam os dados de/para muitos corretores ao mesmo tempo. Quando um novo 
evento é publicado em um tópico, ele é realmente anexado a uma das partições do tópico. 
Eventos com a mesma chave de evento (por exemplo, um cliente ou ID do veículo) são 
gravados na mesma partição, e a Kafka garante que qualquer consumidor de um determinado 
tópico-partição sempre lerá os eventos dessa partição exatamente na mesma ordem em que 
foram escritos. 
```
Conceitos: 
```
- Broker: um nó/servidor Kafka em execução. Recebe, armazena e entrega mensagens; 

- Cluster: conjunto de brokers trabalhando juntos (cluster = vários brokers). Fornece 
escalabilidade, alta disponibilidade e tolerância a falhas. Os brokers se comunicam 
para coordenar líderes de partições, replication, etc;

- Produtores: enviam mensagens para a Kafka. Pode escolher para qual partição do tópico;

- Consumidores: lêem mensagens do Kafka. Faz parte de um consumer-group; 

- Tópicos: categoria/nome lógico para agrupar mensagens. É particionado e replicado. São 
imutáveis (não podem ser renomeados);

- Partição: cada tópico é dividido em partições para permitir paralelismo e escalabilidade. 
É como dividir uma estrada em várias faixas para aumentar o tráfego. A ordem das mensagens 
é garantida por partição, não globalmente;

- Offset: dentro de uma partição, as mensagens recebem um número de ordem (offset). Isso 
pode garantir que as mensagens sejam entregues ao consumidor na mesma ordem em que foram 
armazenadas na partição. O offset é um ID sequencial de cada mensagem na partição. O 
consumer armazena o offset já processado (commit) para não reprocessar;

- Replication Factor: número de cópias de cada partição no cluster. Garante 
disponibilidade: se um broker falhar, outra cópia assume como leader. Ideal ≥3 em 
produção.

- Consumer group (group-id): conjunto de consumidores que dividem o processamento de um 
tópico. Cada partição é consumida por apenas 1 consumer por grupo. Permite escalabilidade 
horizontal (mais consumers = mais throughput);

- bootstrap-servers: lista de endereços de brokers usados para conectar no cluster (ex: 
kafka1:9092,kafka2:9092).

- auto-offset-reset (earliest, latest e none): define onde começa a ler as mensagens. 
earliest começa do início da partição; latest somente novas mensagens (padrão); none lança 
exceção se não houver offset; 

- key-serializer: como serializar a chave da mensagem;

- value-serializer: como serializar o valor da mensagem;

- Schema Registry: controle de versão de mensagens (Avro, JSON Schema); Garante 
compatibilidade entre producer/consumer.

- Dead Letter Topic (DLT): mensagens com falha de processamento são redirecionadas para 
um tópico de erro.

- producer.properties.enable.idempotence (true ou false): garante que mensagens não 
sejam duplicadas mesmo com retries. Requer: acks=all, 
max.in.flight.requests.per.connection ≤ 5, retries > 0;

- max.in.flight.requests.per.connection: controla quantas requisições não confirmadas o 
produtor pode enviar ao mesmo tempo. 5 é seguro com idempotência. >5 pode bagunçar ordem.

- consumer.properties.spring.json.trusted.packages ("*"): define os pacotes com permissão 
onde se pode desserializar. Evita ataques de desserialização. O uso do "*" permite 
desserializar em qualquer pacote.

- auto.register.schemas (true ou false): true → Schema Registry registra schema 
automaticamente. false -> só pode usar schemas já registrados → mais seguro.

- specific.avro.reader (true ou false): true -> o consumidor recebe classes Avro geradas.
false -> recebe GenericRecord;

- Acks: configura como o produtor confirma envio: acks=0 não espera confirmação; acks=1 
confirma no líder (rápido); acks=all confirma em todos os replicas (seguro);

- Zookeeper: foi usado para coordenar e gerenciar o cluster Kafka, mantendo informações 
sobre os brokers, tópicos e partições. No entanto, com o desenvolvimento do KRaft (Kafka 
Raft), o Zookeeper está sendo gradualmente substituído, permitindo que o Kafka se torne 
uma plataforma ainda mais autônoma e eficiente. 

- Kafka Raft (KRaft): novo modo (sem Zookeeper). Usa Raft para consenso. Mais simples e 
rápido.

Objetivo: 1 tópico, com 2 partições e 1 cluster com 2 brokers (1 broker por partição); 
```

![Anatomia da Mensagem Kafka](/imagens/AnatomiaMensagemKafka.png)

PASSO-A-PASSO 




