# Desafio técnico da NTConsult

### Configurações do projeto
O diretório de dados contendo a pasta "in" e "out" é lido da variável de ambiente ```HOMEPATH```. Se não for definida esta variável, a pasta padrão é a raiz do projeto.

### Para executar
O comando para executar o projeto é o seguinte:

```
./mvnw spring-boot:run
```

### Obsevações sobre o funcionamento

Serão lidos todos os arquivos .dat da pasta ```$HOMEPATH/data/in``` processados e o resultado será salvo no arquivo ```$HOMEPATH/data/out/report.done.dat```

O sistema não deve parar se ocorrer algum erro lendo os arquivos, mas serão impressas mensagens de erro no console.
