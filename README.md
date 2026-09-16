# CampusRide API

API REST de caronas solidárias entre alunos de uma universidade. Um motorista publica uma carona; outros alunos reservam vaga nela.

Projeto Diamante — Java Advanced (FIAP).

## Integrantes

- Matheus Carneiro Maciel — RM 567753
- Murilo Marques — RM 568224
- Paulo Henrique da Silva Kian — RM 563343

## Tecnologias

- Java 17
- Spring Boot (Spring Web, Spring Data JPA, Bean Validation)
- Banco H2 em memória

## Como executar

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

O banco H2 é em memória, ou seja, os dados são recriados a cada inicialização. O console do H2 fica disponível em `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:campusride`).

## Estrutura do projeto

```
controller/   camada que expõe a API (sem regra de negócio)
service/      regras de negócio
repository/   acesso a dados (Spring Data JPA)
entity/       entidades e enums do domínio
dto/          objetos de entrada e saída da API
validation/   anotação de validação personalizada
exception/    tratamento centralizado de erros
```

## Domínio

**Carona** — motorista, origem, destino, horário de partida, tipo de veículo, vagas totais e situação.
Situações: `ABERTA`, `LOTADA`, `EM_ANDAMENTO`, `CONCLUIDA`, `CANCELADA`.

**Reserva** — carona à qual pertence, passageiro, momento da reserva (automático) e situação.
Situações: `CONFIRMADA`, `CANCELADA`.

Tipos de veículo: `MOTO`, `CARRO`, `SUV`, `VAN`.

## Endpoints

### Publicar uma carona

`POST /caronas` → **201 Created**

```bash
curl -X POST http://localhost:8080/caronas \
  -H "Content-Type: application/json" \
  -d '{
    "motorista": "Matheus",
    "origem": "Av. Paulista",
    "destino": "FIAP Paulista",
    "horarioPartida": "2026-12-01T08:00:00",
    "tipoVeiculo": "CARRO",
    "vagasTotais": 3
  }'
```

Resposta:

```json
{
  "id": 1,
  "motorista": "Matheus",
  "origem": "Av. Paulista",
  "destino": "FIAP Paulista",
  "horarioPartida": "2026-12-01T08:00:00",
  "tipoVeiculo": "CARRO",
  "vagasTotais": 3,
  "vagasDisponiveis": 3,
  "situacao": "ABERTA",
  "reservas": []
}
```

### Listar caronas disponíveis

`GET /caronas` → **200 OK**

```bash
curl http://localhost:8080/caronas
```

Retorna apenas as caronas com situação `ABERTA`.

### Consultar o detalhe de uma carona

`GET /caronas/{id}` → **200 OK** (ou **404** se não existir)

```bash
curl http://localhost:8080/caronas/1
```

Inclui a lista de reservas feitas na carona.

### Reservar uma vaga

`POST /caronas/{caronaId}/reservas` → **201 Created**

```bash
curl -X POST http://localhost:8080/caronas/1/reservas \
  -H "Content-Type: application/json" \
  -d '{"passageiro": "Ana"}'
```

Resposta:

```json
{
  "id": 1,
  "caronaId": 1,
  "passageiro": "Ana",
  "momentoReserva": "2026-09-16T09:08:39.207751",
  "situacao": "CONFIRMADA"
}
```

### Cancelar uma reserva

`DELETE /reservas/{id}` → **204 No Content**

```bash
curl -X DELETE http://localhost:8080/reservas/1
```

Se a carona estava `LOTADA`, ela volta para `ABERTA`.

### Cancelar uma carona

`DELETE /caronas/{id}` → **204 No Content**

```bash
curl -X DELETE http://localhost:8080/caronas/1
```

O cancelamento reflete nas reservas associadas: todas as reservas `CONFIRMADA` passam a `CANCELADA`.

## Validação de entrada

| Campo | Regra |
|---|---|
| motorista | obrigatório |
| origem | obrigatória |
| destino | obrigatório |
| horarioPartida | obrigatório e no futuro |
| tipoVeiculo | obrigatório, um dos valores do enum |
| vagasTotais | obrigatório, mínimo 1 |
| passageiro | obrigatório |

### Validação personalizada

`@VagasCompativeisComVeiculo` é uma anotação criada para o projeto. Ela é aplicada **na classe** `CaronaRequest` (e não em um campo) porque a regra cruza duas informações: o tipo de veículo e o número de vagas oferecidas.

| Tipo de veículo | Vagas máximas |
|---|---|
| MOTO | 1 |
| CARRO | 4 |
| SUV | 6 |
| VAN | 14 |

Exemplo de violação:

```bash
curl -X POST http://localhost:8080/caronas \
  -H "Content-Type: application/json" \
  -d '{"motorista":"Matheus","origem":"Paulista","destino":"FIAP","horarioPartida":"2026-12-01T08:00:00","tipoVeiculo":"MOTO","vagasTotais":3}'
```

```json
{
  "timestamp": "2026-09-16T09:01:44.76",
  "status": 400,
  "erro": "Erro de validação",
  "mensagem": "Um veículo do tipo MOTO comporta no máximo 1 vaga(s)",
  "campo": "vagasTotais"
}
```

## Regras de negócio

A API impede:

- reservar vaga em carona sem vagas disponíveis;
- reservar vaga em carona cancelada, concluída ou em andamento;
- cancelar uma reserva ou carona já concluída;
- cancelar algo que já está cancelado.

Além disso, a carona passa automaticamente para `LOTADA` quando a última vaga é ocupada, e volta para `ABERTA` se uma reserva for cancelada.

## Tratamento de erros

Todas as respostas de erro seguem o mesmo formato e nunca expõem stack trace:

```json
{
  "timestamp": "2026-09-16T09:08:54.43",
  "status": 409,
  "erro": "Operação não permitida",
  "mensagem": "Esta carona não possui vagas disponíveis",
  "campo": null
}
```

| Situação | Status |
|---|---|
| Campo de entrada inválido | 400 Bad Request |
| JSON malformado ou valor de enum inexistente | 400 Bad Request |
| Carona ou reserva inexistente | 404 Not Found |
| Operação proibida pelo estado atual do recurso | 409 Conflict |

A escolha de **409** para violação de regra de negócio (em vez de 400) é proposital: o dado enviado está bem formado — o que impede a operação é o estado atual do recurso.