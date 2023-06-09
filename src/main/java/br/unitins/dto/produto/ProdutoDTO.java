package br.unitins.dto.produto;

import jakarta.validation.constraints.NotBlank;

public record ProdutoDTO(

        @NotBlank(message = "O campo nome deve ser informado.") 
        String nome,
        
        String descricao,
        Double preco,
        Integer estoque

) {

}