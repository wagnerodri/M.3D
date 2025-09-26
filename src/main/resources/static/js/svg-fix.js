// =========================================
// CORREÇÃO DE ERROS SVG EXTERNOS
// =========================================

// Interceptar e corrigir erros de SVG viewBox
(function() {
    'use strict';
    
    // Override para createElement para interceptar SVGs problemáticos
    const originalCreateElement = document.createElement;
    
    document.createElement = function(tagName) {
        const element = originalCreateElement.call(this, tagName);
        
        if (tagName.toLowerCase() === 'svg') {
            const originalSetAttribute = element.setAttribute;
            
            element.setAttribute = function(name, value) {
                if (name === 'viewBox' && typeof value === 'string') {
                    // Corrigir viewBox com percentuais inválidos
                    const correctedValue = value.replace(/(\d+)\s+(\d+)\s+100%\s+(\d+)/, '$1 $2 100 $3');
                    return originalSetAttribute.call(this, name, correctedValue);
                }
                return originalSetAttribute.call(this, name, value);
            };
        }
        
        return element;
    };
    
    // Interceptar erros globais relacionados a SVG
    window.addEventListener('error', function(event) {
        if (event.message && event.message.includes('viewBox') && event.message.includes('Expected number')) {
            console.warn('SVG viewBox error intercepted and handled:', event.message);
            event.preventDefault();
            return true;
        }
    });
    
    // Observer para corrigir SVGs existentes no DOM
    const observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            mutation.addedNodes.forEach(function(node) {
                if (node.nodeType === 1) { // Element node
                    const svgs = node.tagName === 'SVG' ? [node] : node.querySelectorAll && node.querySelectorAll('svg') || [];
                    
                    svgs.forEach(function(svg) {
                        const viewBox = svg.getAttribute('viewBox');
                        if (viewBox && viewBox.includes('100%')) {
                            const correctedViewBox = viewBox.replace(/(\d+)\s+(\d+)\s+100%\s+(\d+)/, '$1 $2 100 $3');
                            svg.setAttribute('viewBox', correctedViewBox);
                            console.log('SVG viewBox corrected:', viewBox, '->', correctedViewBox);
                        }
                    });
                }
            });
        });
    });
    
    // Iniciar observação quando DOM estiver pronto
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            observer.observe(document.body, {
                childList: true,
                subtree: true
            });
        });
    } else {
        observer.observe(document.body, {
            childList: true,
            subtree: true
        });
    }
    
    console.log('SVG error prevention script loaded');
})();