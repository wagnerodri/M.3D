package com.example.marcenaria.controller;

import com.example.marcenaria.model.OrcamentoRequest;
import com.example.marcenaria.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orcamento")
@CrossOrigin(origins = {"http://localhost:*", "https://localhost:*"})
public class OrcamentoController {

    private static final Logger logger = LoggerFactory.getLogger(OrcamentoController.class);

    @Autowired
    private OrcamentoService orcamentoService;

    @PostMapping
    public ResponseEntity<String> calcularOrcamento(
            @Valid @RequestBody OrcamentoRequest req, 
            BindingResult bindingResult,
            HttpServletRequest request) {
        
        // Log da tentativa de orçamento
        String clientIP = getClientIP(request);
        logger.info("Tentativa de orçamento de IP: {} - Cliente: {}", clientIP, req.getNome());
        
        try {
            // Verificar erros de validação
            if (bindingResult.hasErrors()) {
                String erros = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
                
                logger.warn("Dados inválidos no orçamento de IP: {} - Erros: {}", clientIP, erros);
                return ResponseEntity.badRequest()
                        .body("{\"error\":\"Dados inválidos: " + erros + "\"}");
            }

            // Sanitizar dados de entrada
            sanitizarDados(req);
            
            // Calcular orçamento
            String resposta = orcamentoService.calcularOrcamento(req);
            
            logger.info("Orçamento calculado com sucesso para IP: {} - Cliente: {}", clientIP, req.getNome());
            return ResponseEntity.ok(resposta);
            
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação no orçamento de IP: {} - {}", clientIP, e.getMessage());
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            logger.error("Erro interno no orçamento de IP: {} - {}", clientIP, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"Erro interno do servidor. Tente novamente mais tarde.\"}");
        }
    }
    
    private void sanitizarDados(OrcamentoRequest req) {
        // Sanitizar strings - remover caracteres perigosos
        if (req.getNome() != null) {
            req.setNome(sanitizarString(req.getNome()));
        }
        if (req.getTelefone() != null) {
            req.setTelefone(sanitizarTelefone(req.getTelefone()));
        }
        if (req.getObservacoes() != null) {
            req.setObservacoes(sanitizarString(req.getObservacoes()));
        }
        
        // Validar dimensões
        if (req.getLargura() != null && (req.getLargura() <= 0 || req.getLargura() > 10)) {
            throw new IllegalArgumentException("Largura deve estar entre 0.1 e 10 metros");
        }
        if (req.getAltura() != null && (req.getAltura() <= 0 || req.getAltura() > 5)) {
            throw new IllegalArgumentException("Altura deve estar entre 0.1 e 5 metros");
        }
        if (req.getProfundidade() != null && (req.getProfundidade() <= 0 || req.getProfundidade() > 2)) {
            throw new IllegalArgumentException("Profundidade deve estar entre 0.1 e 2 metros");
        }
    }
    
    private String sanitizarString(String input) {
        if (input == null) return null;
        
        // Remover caracteres perigosos
        return input.replaceAll("[<>\"'&]", "")
                   .trim()
                   .substring(0, Math.min(input.length(), 500)); // Limitar tamanho
    }
    
    private String sanitizarTelefone(String telefone) {
        if (telefone == null) return null;
        
        // Manter apenas números, espaços, parênteses e hífens
        return telefone.replaceAll("[^0-9\\s\\(\\)\\-]", "")
                      .trim()
                      .substring(0, Math.min(telefone.length(), 20));
    }
    
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
