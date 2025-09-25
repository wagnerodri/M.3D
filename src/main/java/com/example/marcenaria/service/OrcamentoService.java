package com.example.marcenaria.service;

import com.example.marcenaria.model.OrcamentoRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrcamentoService {
    
    // Preços por tipo de madeira (R$ por m²)
    private static final Map<String, Double> PRECOS_MADEIRA = new HashMap<>();
    static {
        PRECOS_MADEIRA.put("MDF Branco", 85.0);
        PRECOS_MADEIRA.put("MDF Amadeirado", 95.0);
        PRECOS_MADEIRA.put("MDP Branco", 75.0);
        PRECOS_MADEIRA.put("MDP Amadeirado", 85.0);
        PRECOS_MADEIRA.put("Compensado", 120.0);
        PRECOS_MADEIRA.put("Pinus", 150.0);
        PRECOS_MADEIRA.put("Eucalipto", 180.0);
    }
    
    // Adicional por tipo de acabamento (%)
    private static final Map<String, Double> ADICIONAL_ACABAMENTO = new HashMap<>();
    static {
        ADICIONAL_ACABAMENTO.put("Básico", 0.0);
        ADICIONAL_ACABAMENTO.put("Verniz", 15.0);
        ADICIONAL_ACABAMENTO.put("Laca", 25.0);
        ADICIONAL_ACABAMENTO.put("Pintura Especial", 35.0);
    }
    
    // Multiplicador por complexidade
    private static final Map<String, Double> MULTIPLICADOR_COMPLEXIDADE = new HashMap<>();
    static {
        MULTIPLICADOR_COMPLEXIDADE.put("Simples", 1.0);
        MULTIPLICADOR_COMPLEXIDADE.put("Média", 1.3);
        MULTIPLICADOR_COMPLEXIDADE.put("Complexa", 1.6);
    }
    
    public String calcularOrcamento(OrcamentoRequest req) {
        // Cálculos básicos
        double volume = req.getLargura() * req.getAltura() * req.getProfundidade();
        
        // Área total considerando estrutura do móvel
        double areaTotal = calcularAreaTotal(req);
        
        // Preço base da madeira
        double precoM2 = PRECOS_MADEIRA.getOrDefault(req.getTipoMadeira(), 85.0);
        double custoMadeira = areaTotal * precoM2;
        
        // Aplicar acabamento
        double adicionalAcabamento = ADICIONAL_ACABAMENTO.getOrDefault(req.getAcabamento(), 0.0);
        double custoComAcabamento = custoMadeira * (1 + adicionalAcabamento / 100);
        
        // Aplicar complexidade
        double multiplicador = MULTIPLICADOR_COMPLEXIDADE.getOrDefault(req.getComplexidade(), 1.0);
        double custoMaterial = custoComAcabamento * multiplicador;
        
        // Mão de obra (50% do custo do material)
        double maoDeObra = custoMaterial * 0.5;
        
        // Total
        double valorTotal = custoMaterial + maoDeObra;
        
        // Calcular quantidade de chapas estimada
        double chapasEstimadas = Math.ceil(areaTotal / 2.75);
        
        return String.format(
            "Olá %s! 🛠️\n\n" +
            "Para seu móvel do tipo '%s' com medidas %.2fm x %.2fm x %.2fm (%s), temos:\n\n" +
            "📏 Volume: %.3f m³\n" +
            "📋 Área de madeira: %.2f m²\n" +
            "🪵 Chapas estimadas: %.0f unidades\n" +
            "🎨 Acabamento: %s\n" +
            "⚙️ Complexidade: %s\n\n" +
            "💰 ORÇAMENTO ESTIMADO: R$ %.2f\n" +
            "(Material: R$ %.2f + Mão de obra: R$ %.2f)\n\n" +
            "⏱️ Prazo estimado: %s\n\n" +
            "Este é um orçamento preliminar. Entre em contato para mais detalhes!",
            
            req.getNome(),
            req.getTipoMovel(),
            req.getLargura(), req.getAltura(), req.getProfundidade(),
            req.getTipoMadeira(),
            volume,
            areaTotal,
            chapasEstimadas,
            req.getAcabamento(),
            req.getComplexidade(),
            valorTotal,
            custoMaterial,
            maoDeObra,
            calcularPrazo(req.getComplexidade(), volume)
        );
    }
    
    private double calcularAreaTotal(OrcamentoRequest req) {
        double largura = req.getLargura();
        double altura = req.getAltura(); 
        double profundidade = req.getProfundidade();
        
        // Área básica considerando as 6 faces principais
        double areaBasica = 2 * (largura * altura + altura * profundidade + largura * profundidade);
        
        // Adicionar área extra baseada no tipo de móvel
        double multiplicadorTipo = getMultiplicadorPorTipo(req.getTipoMovel());
        
        return areaBasica * multiplicadorTipo;
    }
    
    private double getMultiplicadorPorTipo(String tipoMovel) {
        switch (tipoMovel.toLowerCase()) {
            case "guarda-roupa":
            case "armário":
                return 1.4; // Mais divisórias e prateleiras
            case "estante":
                return 1.3; // Várias prateleiras
            case "cômoda":
                return 1.2; // Gavetas e divisórias
            case "mesa":
                return 0.8; // Estrutura mais simples
            case "aparador":
            case "bancada":
                return 1.0; // Estrutura padrão
            case "rack/home theater":
                return 1.1; // Estrutura com nichos
            default:
                return 1.0; // Padrão
        }
    }
    
    private String calcularPrazo(String complexidade, double volume) {
        int diasBase = 7; // Uma semana base
        
        // Ajustar por complexidade
        switch (complexidade.toLowerCase()) {
            case "média":
                diasBase = 10;
                break;
            case "complexa":
                diasBase = 15;
                break;
        }
        
        // Ajustar por volume (móveis grandes levam mais tempo)
        if (volume > 0.5) {
            diasBase += 3;
        }
        if (volume > 1.0) {
            diasBase += 5;
        }
        
        return diasBase + " a " + (diasBase + 5) + " dias úteis";
    }
}