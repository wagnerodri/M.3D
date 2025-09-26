# 🏗️ M.3D - Marcenaria Artesanal

Uma aplicação web moderna para marcenaria artesanal com design 3D personalizado, desenvolvida em **Java 21** e **Spring Boot 3.5.6**.

## ✨ Funcionalidades

- 🎨 **Design Moderno**: Interface premium com efeitos glassmorphism
- 📱 **Responsivo**: Compatível com desktop, tablet e mobile  
- 🛠️ **Nosso Processo**: 4 etapas profissionais (Consultoria → Design 3D → Produção → Entrega)
- 💰 **Calculadora de Orçamento**: Sistema interativo de precificação
- 🖼️ **Galeria Premium**: Portfólio com imagens reais e animações
- 🌙 **Fundo Dinâmico**: Partículas animadas e elementos flutuantes
- 📋 **Formulários Inteligentes**: Validação e integração WhatsApp

## 🚀 Deploy Gratuito

### Railway (Recomendado)
1. Acesse [Railway.app](https://railway.app)
2. Conecte sua conta GitHub
3. Selecione este repositório `wagnerodri/M.3D`
4. Deploy automático com Dockerfile

### Render
1. Acesse [Render.com](https://render.com)
2. Conecte GitHub e selecione o repositório
3. Configure as variáveis:
   - Build Command: `./mvnw clean package -DskipTests`
   - Start Command: `java -jar target/marcenaria-0.0.1-SNAPSHOT.jar`

### Heroku
1. Instale Heroku CLI
2. Execute: `heroku create m3d-marcenaria`
3. `git push heroku main`

## 🛠️ Desenvolvimento Local

### Pré-requisitos
- Java 21 (LTS)
- Maven 3.9+

### Como executar
```bash
# Clone o repositório
git clone https://github.com/wagnerodri/M.3D.git
cd M.3D

# Execute a aplicação
./mvnw spring-boot:run

# Acesse: http://localhost:8080
```

### Build para produção
```bash
./mvnw clean package -DskipTests
java -jar target/marcenaria-0.0.1-SNAPSHOT.jar
```

## 🎨 Tecnologias

- **Backend**: Java 21, Spring Boot 3.5.6, Spring Security
- **Frontend**: HTML5, CSS3 (Glassmorphism), JavaScript ES6
- **Frameworks**: Bootstrap 5.3, Font Awesome 6.4
- **Build**: Maven, Docker
- **Deploy**: Railway, Render, Heroku

## 📱 Recursos Visuais

- **Design System**: Cores premium com gradientes dinâmicos
- **Animações**: Hover effects, particle system, floating shapes
- **Tipografia**: Inter + Playfair Display (Google Fonts)
- **Imagens**: Unsplash API para fotos profissionais
- **Responsividade**: Mobile-first design

## 🔧 Configuração

### Variáveis de Ambiente
- `PORT`: Porta da aplicação (default: 8080)
- `SPRING_PROFILES_ACTIVE`: Perfil ativo (prod para produção)
- `JAVA_OPTS`: Opções da JVM (-Xmx512m recomendado)

### Arquivos de Configuração
- `application.properties`: Configuração desenvolvimento
- `application-prod.properties`: Configuração produção
- `Dockerfile`: Build containerizado
- `railway.toml`: Deploy Railway

## 📞 Contato

**Wagner Rodrigues**
- GitHub: [@wagnerodri](https://github.com/wagnerodri)
- Repositório: [M.3D](https://github.com/wagnerodri/M.3D)

---

⚡ **Powered by Java 21 + Spring Boot 3.5.6** ⚡