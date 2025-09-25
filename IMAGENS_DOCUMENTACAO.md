# 📸 Documentação das Imagens do Website de Marcenaria

Este arquivo contém todas as informações sobre as imagens utilizadas no website, facilitando futuras edições e personalizações.

## 🖼️ Imagens de Background

### 1. Background Principal do Site (Body)
- **Local**: `src/main/resources/static/css/style.css` - linha ~15
- **URL Atual**: `https://images.unsplash.com/photo-1441974231531-c6227db76b6e`
- **Descrição**: Textura de madeira suave para todo o site
- **Parâmetros**: `?ixlib=rb-4.0.3&auto=format&fit=crop&w=2000&q=60`
- **Transparência**: 75% de overlay branco

**Alternativas Sugeridas:**
- Madeira clara: `photo-1574323840792-d0b87e6e1b1c`
- Oficina carpintaria: `photo-1586023492125-27b2c045efd7`
- Textura madeira escura: `photo-1513475382585-d06e58bcb0e0`

### 2. Background da Hero Section
- **Local**: `src/main/resources/static/css/style.css` - linha ~60
- **URL Atual**: `https://images.unsplash.com/photo-1503387837-b154d5074bd2`
- **Descrição**: Carpinteiro profissional trabalhando com madeira
- **Parâmetros**: `?ixlib=rb-4.0.3&auto=format&fit=crop&w=2000&q=80`
- **Transparência**: 70% de overlay branco

**Alternativas Sugeridas:**
- Carpinteiro com serra: `photo-1621905251189-08b45d6a269e`
- Oficina de marcenaria: `photo-1581833971358-2c8b550f87b3`
- Artesão trabalhando: `photo-1607400201889-565b1ee75f8e`
- Mesa de trabalho: `photo-1556761175-4b46a572b786`

## 🏠 Imagens dos Projetos (Galeria)

### Projeto 1 - Guarda-roupa Planejado
- **Local**: `src/main/resources/static/js/galeria.js` - linha ~19
- **URL Atual**: `photo-1586023492125-27b2c045efd7`
- **Descrição**: Oficina de carpintaria com ferramentas
- **Alternativas**:
  - Guarda-roupa branco: `photo-1555041469-a586c61ea9bc`
  - Closet organizado: `photo-1631452180539-96aca7d48617`

### Projeto 2 - Cozinha Planejada Completa
- **Local**: `src/main/resources/static/js/galeria.js` - linha ~39
- **URL Atual**: `photo-1556912167-f556f1f39fdf`
- **Descrição**: Cozinha moderna com armários de madeira
- **Alternativas**:
  - Cozinha amadeirada: `photo-1565538810643-b5bdb714032a`
  - Armários cozinha: `photo-1560448204-e02f11c3d0e2`

### Projeto 3 - Home Theater Moderno
- **Local**: `src/main/resources/static/js/galeria.js` - linha ~59
- **URL Atual**: `photo-1560448204-e02f11c3d0e2`
- **Descrição**: Sala moderna com móveis de madeira
- **Alternativas**:
  - Rack para TV: `photo-1556909114-6f8ffc9dcd3b`
  - Home theater: `photo-1586023492125-27b2c045efd7`

### Projeto 4 - Mesa de Jantar Rústica
- **Local**: `src/main/resources/static/js/galeria.js` - linha ~79
- **URL Atual**: `photo-1549497538-303791108f95`
- **Descrição**: Mesa rústica de madeira em ambiente acolhedor
- **Alternativas**:
  - Mesa madeira maciça: `photo-1506439773649-6e0eb8cfb237`
  - Mesa jantar rústica: `photo-1555041469-a586c61ea9bc`

### Projeto 5 - Estante para Livros
- **Local**: `src/main/resources/static/js/galeria.js` - linha ~99
- **URL Atual**: `photo-1586023492125-27b2c045efd7`
- **Descrição**: Oficina carpintaria (reutilizada - pode personalizar)
- **Alternativas**:
  - Estante de livros: `photo-1507003211169-0a1dd7228f2d`
  - Biblioteca moderna: `photo-1481277542470-605612bd2d61`

### Projeto 6 - Cômoda com Espelho
- **Local**: `src/main/resources/static/js/galeria.js` - linha ~119
- **URL Atual**: `photo-1586023492125-27b2c045efd7`
- **Descrição**: Oficina carpintaria (reutilizada - pode personalizar)
- **Alternativas**:
  - Cômoda branca: `photo-1555041469-a586c61ea9bc`
  - Móvel com espelho: `photo-1631452180539-96aca7d48617`

### Projeto 7 - Bancada de Trabalho
- **Local**: `src/main/resources/static/js/galeria.js` - linha ~139
- **URL Atual**: `photo-1586023492125-27b2c045efd7`
- **Descrição**: Oficina carpintaria (reutilizada - pode personalizar)
- **Alternativas**:
  - Mesa escritório: `photo-1541558869434-2840d308329a`
  - Bancada trabalho: `photo-1556761175-4b46a572b786`

### Projeto 8 - Armário de Banheiro
- **Local**: `src/main/resources/static/js/galeria.js` - linha ~159
- **URL Atual**: `photo-1586023492125-27b2c045efd7`
- **Descrição**: Oficina carpintaria (reutilizada - pode personalizar)
- **Alternativas**:
  - Banheiro moderno: `photo-1620626011761-996317b8d101`
  - Armário banheiro: `photo-1584622650111-993a426fbf0a`

## 🔧 Como Editar as Imagens

### Para Backgrounds (CSS):
1. Abra o arquivo `src/main/resources/static/css/style.css`
2. Localize a linha com a URL da imagem
3. Substitua o ID da foto (ex: `photo-1441974231531-c6227db76b6e`)
4. Salve o arquivo - as mudanças aparecerão automaticamente

### Para Projetos da Galeria (JavaScript):
1. Abra o arquivo `src/main/resources/static/js/galeria.js`
2. Localize o projeto desejado no array `projetosGaleria`
3. Substitua a URL no campo `imagem`
4. Salve o arquivo - as mudanças aparecerão na próxima atualização da página

### Parâmetros das URLs do Unsplash:
- `ixlib=rb-4.0.3`: Versão da biblioteca Unsplash
- `auto=format`: Formato automático otimizado
- `fit=crop`: Corta a imagem para se ajustar
- `w=2000`: Largura em pixels
- `h=300`: Altura em pixels (para projetos)
- `q=80`: Qualidade (60-100, onde 100 é máxima)

## 📝 Dicas para Escolher Imagens:

1. **Consistência**: Mantenha um estilo visual similar
2. **Qualidade**: Use imagens de alta resolução
3. **Relevância**: Escolha imagens que representem bem o projeto
4. **Contraste**: Garanta que o texto seja legível sobre a imagem
5. **Performance**: Use qualidade adequada (q=60-80) para manter velocidade

## 🔍 Busca de Imagens no Unsplash:
- Site: https://unsplash.com
- Termos de busca sugeridos: "woodworking", "carpentry", "furniture", "wooden furniture", "kitchen cabinets", "wardrobe"
- Licença: Gratuita para uso comercial e pessoal