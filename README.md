# Java Video Generator

## Descrição do Projeto
Este é um projeto desenvolvido em Java que permite a criação automática de vídeos interativos a partir de uma palavra-chave fornecida por meio de uma API. O sistema combina tecnologias de IA generativa, processamento de linguagem natural (NLP) e manipulação de multimídia para entregar vídeos dinâmicos e legendados.

## Funcionalidades
- **Geração de Roteiro**: Utiliza uma API de IA generativa (como OpenAI) para criar um roteiro com base na palavra-chave enviada.
- **Divisão de Texto em Sentenças**: Emprega o Apache OpenNLP para segmentar o roteiro em sentenças.
- **Extração de Palavras-Chave**: Integra um microserviço em Python que utiliza o spaCy e Named Entity Recognition (NER) para extrair palavras-chave das sentenças geradas.
- **Busca de Imagens**: Faz uso da API Custom Search do Google para buscar imagens relacionadas às palavras-chave extraídas.
- **Geração de Vídeo**: Utiliza a plataforma JavaCV para compor um vídeo interativo com:
  - Imagens como plano de fundo.
  - Legendas sincronizadas com as sentenças do roteiro.
  - Efeitos visuais para tornar o vídeo mais atrativo.

## Tecnologias Utilizadas
### Backend
- **Java**: Linguagem principal para orquestração do sistema.
- **Spring Framework**: Para criação das APIs REST.
- **Apache OpenNLP**: Processamento de linguagem natural para segmentação de sentenças.
- **JavaCV**: Construção e manipulação dos vídeos gerados.

### Microserviço Python
- **spaCy**: Para extração de palavras-chave usando Named Entity Recognition (NER).

### Integrações
- **OpenAI API**: Geração automática de texto criativo.
- **Google Custom Search API**: Busca de imagens baseadas nas palavras-chave extraídas.

## Como Funciona
1. **Entrada**:
   - O usuário envia uma palavra-chave por meio da API REST.
2. **Geração de Roteiro**:
   - A palavra-chave é enviada para uma IA generativa que devolve um texto criativo.
3. **Segmentação de Sentenças**:
   - O texto é processado pelo OpenNLP para dividir o roteiro em sentenças.
4. **Extração de Keywords**:
   - Um microserviço em Python utiliza o spaCy para identificar palavras-chave relevantes em cada sentença.
5. **Busca de Imagens**:
   - As palavras-chave extraídas são enviadas para a API do Google Custom Search, que retorna imagens correspondentes.
6. **Composição do Vídeo**:
   - As imagens são usadas como plano de fundo.
   - As sentenças do roteiro aparecem como legendas sincronizadas.
   - Efeitos visuais tornam o vídeo mais atrativo.
7. **Saída**:
   - O vídeo final é gerado e disponibilizado para download ou compartilhamento.

## Como Executar
### Pré-requisitos
- JDK 17 ou superior.
- Python 3.8 ou superior.
- Bibliotecas Python: `spacy`, `requests`.
- Dependências Maven para Java: `spring-boot-starter`, `opennlp-tools`, `javacv`.

### Passos
1. Clone o repositório:
   ```bash
   https://github.com/mateeooss/make-video.git
   ```
2. Configure as chaves de API:
   - **OpenAI API**: Insira sua chave no arquivo de propriedades.
   - **Google Custom Search API**: Adicione a chave e o CX no arquivo de propriedades.
3. Instale as dependências:
   ```bash
   mvn install
   ```
4. Execute o projeto:
   ```bash
   mvn spring-boot:run
   ```
5. Inicie o microserviço Python:
   ```bash
   python microservice.py
   ```
6. Envie uma requisição para a API com a palavra-chave:
   ```bash
   curl -X POST -H "Content-Type: application/json" -d '{"keyword": "exemplo"}' http://localhost:8080/generate-video
   ```

## Contribuição
Contribuições são bem-vindas! Fique à vontade para abrir um pull request ou relatar problemas na aba de issues.

## Licença
Este projeto está licenciado sob a [MIT License](LICENSE).

