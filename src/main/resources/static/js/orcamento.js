document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('orcamentoForm');
    const resultado = document.getElementById('resultado');
    const detalhes = document.getElementById('detalhes');
    const whatsappBtn = document.getElementById('whatsappBtn');
    
    let ultimoOrcamento = null;
    
    // Máscara para telefone
    const telefoneInput = document.getElementById('telefone');
    telefoneInput.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '');
        valor = valor.replace(/^(\d{2})(\d)/g, '($1) $2');
        valor = valor.replace(/(\d)(\d{4})$/, '$1-$2');
        e.target.value = valor;
    });
    
    // Validação em tempo real com sanitização
    const inputs = form.querySelectorAll('input[required], select[required]');
    inputs.forEach(input => {
        input.addEventListener('blur', validarCampo);
        input.addEventListener('input', function(e) {
            limparErro(e);
            sanitizarInput(e.target);
        });
    });
    
    function sanitizarInput(input) {
        if (input.type === 'text' || input.tagName === 'TEXTAREA') {
            // Remover caracteres perigosos em tempo real
            input.value = input.value.replace(/[<>\"'&]/g, '');
            
            // Limitar tamanho
            if (input.value.length > 500) {
                input.value = input.value.substring(0, 500);
                mostrarAlerta('Texto muito longo. Máximo de 500 caracteres.', 'warning');
            }
        }
        
        if (input.type === 'tel') {
            // Manter apenas números e caracteres de formatação
            input.value = input.value.replace(/[^0-9\s\(\)\-]/g, '');
        }
    }
    
    function validarCampo(e) {
        const campo = e.target;
        const valor = campo.value.trim();
        
        // Remove classes anteriores
        campo.classList.remove('is-valid', 'is-invalid');
        
        if (!valor) {
            campo.classList.add('is-invalid');
            return false;
        }
        
        // Validações específicas de segurança
        if (campo.type === 'text' || campo.tagName === 'TEXTAREA') {
            // Verificar caracteres perigosos
            if (/<script|javascript:|data:|vbscript:|onload|onerror/i.test(valor)) {
                campo.classList.add('is-invalid');
                mostrarAlerta('Caracteres não permitidos detectados.', 'danger');
                return false;
            }
            
            // Verificar tamanho
            if (valor.length > 500) {
                campo.classList.add('is-invalid');
                mostrarAlerta('Texto muito longo. Máximo de 500 caracteres.', 'danger');
                return false;
            }
        }
        
        if (campo.type === 'tel' && !validarTelefone(valor)) {
            campo.classList.add('is-invalid');
            return false;
        }
        
        if (campo.type === 'number') {
            const num = parseFloat(valor);
            const min = parseFloat(campo.min) || 0;
            const max = parseFloat(campo.max) || Infinity;
            
            if (num < min || num > max || isNaN(num)) {
                campo.classList.add('is-invalid');
                return false;
            }
        }
        
        campo.classList.add('is-valid');
        return true;
    }
    
    function limparErro(e) {
        e.target.classList.remove('is-invalid');
    }
    
    function validarTelefone(telefone) {
        const numero = telefone.replace(/\D/g, '');
        return numero.length >= 10 && numero.length <= 11;
    }
    
    // Submissão do formulário
    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        // Validar todos os campos
        let formularioValido = true;
        inputs.forEach(input => {
            if (!validarCampo({target: input})) {
                formularioValido = false;
            }
        });
        
        if (!formularioValido) {
            mostrarAlerta('Por favor, preencha todos os campos corretamente.', 'danger');
            return;
        }
        
        const btn = form.querySelector('button[type="submit"]');
        const textoBotaoOriginal = btn.innerHTML;
        
        try {
            // Loading state
            btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Calculando...';
            btn.classList.add('loading');
            btn.disabled = true;
            
            // Preparar dados
            const dadosForm = new FormData(form);
            const dados = Object.fromEntries(dadosForm);
            
            // Converter para números
            dados.largura = parseFloat(dados.largura);
            dados.altura = parseFloat(dados.altura);
            dados.profundidade = parseFloat(dados.profundidade);
            
            // Fazer a requisição com headers de segurança
            const response = await fetch('/api/orcamento', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest', // Header AJAX padrão
                },
                body: JSON.stringify(dados)
            });
            
            if (!response.ok) {
                const errorText = await response.text();
                let errorMessage = 'Erro no servidor';
                
                // Tentar parsear erro JSON
                try {
                    const errorJson = JSON.parse(errorText);
                    errorMessage = errorJson.error || errorMessage;
                } catch {
                    // Se não for JSON, usar texto padrão
                    errorMessage = `Erro ${response.status}: ${response.statusText}`;
                }
                
                throw new Error(errorMessage);
            }
            
            const resposta = await response.text();
            
            // Calcular detalhes localmente para exibição rica
            const detalhesCalculados = calcularDetalhes(dados);
            ultimoOrcamento = { dados, detalhes: detalhesCalculados, resposta };
            
            // Exibir resultado
            exibirResultado(detalhesCalculados, resposta);
            
        } catch (error) {
            console.error('Erro ao calcular orçamento:', error);
            mostrarAlerta('Erro ao calcular orçamento. Tente novamente.', 'danger');
        } finally {
            // Restaurar botão
            btn.innerHTML = textoBotaoOriginal;
            btn.classList.remove('loading');
            btn.disabled = false;
        }
    });
    
    function calcularDetalhes(dados) {
        // Obter preço por m² da madeira
        const tipoMadeiraSelect = document.querySelector('#tipoMadeira');
        const opcaoSelecionada = tipoMadeiraSelect.querySelector(`option[value="${dados.tipoMadeira}"]`);
        const precoM2 = parseFloat(opcaoSelecionada?.dataset.preco || 85);
        
        // Obter adicional do acabamento
        const acabamentoSelect = document.querySelector('#acabamento');
        const opcaoAcabamento = acabamentoSelect.querySelector(`option[value="${dados.acabamento}"]`);
        const adicionalAcabamento = parseFloat(opcaoAcabamento?.dataset.adicional || 0);
        
        // Obter multiplicador de complexidade
        const complexidadeSelect = document.querySelector('#complexidade');
        const opcaoComplexidade = complexidadeSelect.querySelector(`option[value="${dados.complexidade}"]`);
        const multiplicadorComplexidade = parseFloat(opcaoComplexidade?.dataset.multiplicador || 1);
        
        // Cálculos
        const volume = dados.largura * dados.altura * dados.profundidade;
        
        // Área total (considerando 6 faces de um móvel básico)
        const areaTotal = 2 * (dados.largura * dados.altura + 
                              dados.altura * dados.profundidade + 
                              dados.largura * dados.profundidade);
        
        // Custo base da madeira
        const custoMadeira = areaTotal * precoM2;
        
        // Custo com acabamento
        const custoComAcabamento = custoMadeira * (1 + adicionalAcabamento / 100);
        
        // Custo final com complexidade
        const custoFinal = custoComAcabamento * multiplicadorComplexidade;
        
        // Mão de obra (50% do custo do material)
        const maoDeObra = custoFinal * 0.5;
        
        // Total
        const total = custoFinal + maoDeObra;
        
        return {
            volume: volume.toFixed(3),
            areaTotal: areaTotal.toFixed(2),
            precoM2,
            custoMadeira,
            adicionalAcabamento,
            custoComAcabamento,
            multiplicadorComplexidade,
            custoFinal,
            maoDeObra,
            total
        };
    }
    
    function exibirResultado(detalhesCalculados, resposta) {
        detalhes.innerHTML = `
            <div class="resultado-detalhado">
                <div class="resultado-item">
                    <strong>Volume do móvel:</strong> ${detalhesCalculados.volume} m³
                </div>
                <div class="resultado-item">
                    <strong>Área total de madeira:</strong> ${detalhesCalculados.areaTotal} m²
                </div>
                <div class="resultado-item">
                    <strong>Custo da madeira:</strong> R$ ${detalhesCalculados.custoMadeira.toFixed(2)}
                </div>
                <div class="resultado-item">
                    <strong>Custo com acabamento:</strong> R$ ${detalhesCalculados.custoComAcabamento.toFixed(2)}
                </div>
                <div class="resultado-item">
                    <strong>Custo final material:</strong> R$ ${detalhesCalculados.custoFinal.toFixed(2)}
                </div>
                <div class="resultado-item">
                    <strong>Mão de obra:</strong> R$ ${detalhesCalculados.maoDeObra.toFixed(2)}
                </div>
                <div class="resultado-item">
                    <strong>TOTAL ESTIMADO:</strong> R$ ${detalhesCalculados.total.toFixed(2)}
                </div>
                <div class="mt-3 p-3 bg-light rounded">
                    <small class="text-muted">
                        <i class="fas fa-info-circle"></i>
                        <strong>Resposta personalizada:</strong><br>
                        ${resposta.replace(/\n/g, '<br>')}
                    </small>
                </div>
            </div>
        `;
        
        resultado.classList.remove('d-none');
        whatsappBtn.classList.remove('d-none');
        
        // Scroll para o resultado
        resultado.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
    
    // Botão WhatsApp
    whatsappBtn.addEventListener('click', function() {
        if (!ultimoOrcamento) return;
        
        const { dados, detalhes } = ultimoOrcamento;
        
        const mensagem = `🛠️ *ORÇAMENTO DE MARCENARIA*

👤 *Cliente:* ${dados.nome}
📞 *Telefone:* ${dados.telefone}

📏 *Detalhes do Projeto:*
• Tipo de móvel: ${dados.tipoMovel}
• Madeira: ${dados.tipoMadeira}
• Dimensões: ${dados.largura}m x ${dados.altura}m x ${dados.profundidade}m
• Volume: ${detalhes.volume} m³
• Acabamento: ${dados.acabamento}
• Complexidade: ${dados.complexidade}

💰 *Orçamento:*
• Área total: ${detalhes.areaTotal} m²
• Custo material: R$ ${detalhes.custoFinal.toFixed(2)}
• Mão de obra: R$ ${detalhes.maoDeObra.toFixed(2)}
• *TOTAL: R$ ${detalhes.total.toFixed(2)}*

${dados.observacoes ? `📝 *Observações:*\n${dados.observacoes}\n\n` : ''}
Gostaria de mais informações sobre este orçamento!`;
        
        // Número do WhatsApp (ajuste conforme necessário)
        const numeroWhatsApp = '5511999999999'; // Formato: país + DDD + número
        const url = `https://wa.me/${numeroWhatsApp}?text=${encodeURIComponent(mensagem)}`;
        
        window.open(url, '_blank');
    });
    
    function mostrarAlerta(mensagem, tipo = 'info') {
        // Remove alertas anteriores
        const alertasAnteriores = document.querySelectorAll('.alert-temp');
        alertasAnteriores.forEach(alerta => alerta.remove());
        
        const alerta = document.createElement('div');
        alerta.className = `alert alert-${tipo} alert-dismissible fade show alert-temp`;
        alerta.innerHTML = `
            ${mensagem}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        form.insertBefore(alerta, form.firstChild);
        
        // Remove automaticamente após 5 segundos
        setTimeout(() => {
            if (alerta.parentNode) {
                alerta.remove();
            }
        }, 5000);
    }
    
    // Smooth scroll para links da navegação
    document.querySelectorAll('a[href^="#"]').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
});