TUTORIAL

Fontes: 
- https://kafka.apache.org/intro 
- https://kafka.apache.org/documentation/ 
- https://kafka.apache.org/documentation/#design 
- https://kafka.apache.org/documentation.html#adminapi 
- https://docs.confluent.io/kafka/introduction.html 
- https://avro.apache.org/docs/1.12.0/getting-started-java/ 


Teoria:  
```
Kafka é um sistema distribuído composto por servidores e clientes que comunicam através de 
um protocolo de rede TCP de alto desempenho. Em sua essência, o Kafka é um sistema de 
mensagens de inscrição publicado distribuído. Os dados são gravados em tópicos do Kafka 
pelos produtores e consumidos a partir desses temas pelos consumidores. Os tópicos do 
Kafka podem ser particionados, habilitando o processamento paralelo de dados, e os tópicos 
podem ser replicados em vários corretores para tolerância a falhas.

Servidores : Kafka é executado como um cluster de um ou mais servidores que podem abranger 
vários datacenters ou regiões de nuvem. Alguns desses servidores formam a camada de 
armazenamento, chamada de corretores. Outros servidores são executados Kafka Connect para 
importar e exportar continuamente dados como fluxos de eventos para integrar o Kafka com 
seus sistemas existentes, como bancos de dados relacionais, bem como outros aglomerados de 
Kafka. Para permitir que você implemente casos de uso de missão crítica, um cluster Kafka 
é altamente escalável e tolerante a falhas: se algum de seus servidores falhar, os outros 
servidores assumirão seu trabalho para garantir operações contínuas sem qualquer perda de 
dados. 

Clientes : permitem escrever aplicações distribuídas e microsserviços que lêem, escrevem e 
processam fluxos de eventos em paralelo, em escala e de maneira tolerante a falhas, mesmo 
no caso da problemas de rede ou falhas da máquina.

Principais Conceitos e Terminologia

Um evento registra o fato de que "algo aconteceu" no mundo ou no seu negócio. Também é 
chamado de registro ou mensagem na documentação. Quando você lê ou grava dados para o 
Kafka, você faz isso na forma de eventos. Conceitualmente, um evento tem uma chave, valor, 
carimbo de data/hora e cabeçalhos de metadados opcionais.

    "Os produtores são clientes que escrevem eventos para a Kafka."

Os produtores são os aplicativos do cliente que publicam eventos (gravam) para o Kafka, e 
os consumidores são aqueles que se inscrevem (leêm e processam) esses eventos. Em Kafka, 
os produtores e consumidores são totalmente dissociados e agnósticos uns dos outros, o que 
é um elemento-chave de design para alcançar a alta escalabilidade pela qual o Kafka é 
conhecido. Por exemplo, os produtores nunca precisam esperar pelos consumidores.

    "Consumidores são clientes que lêem eventos do Kafka."

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


PASSO-A-PASSO 




