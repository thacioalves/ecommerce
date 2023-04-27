package br.unitins.dto.endereco;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.unitins.model.Cidade;
import br.unitins.model.Endereco;

public record EnderecoResponseDTO(

        Long id,
        String rua,
        String bairro,

        @Size(max = 2, message = "O numero deve posssuir 14 caracteres.")
        String numero,

        String complemento,

        @NotBlank(message = "O campo cep deve ser informado.")
        @Size(max = 8, message = "O cep deve posssuir 8 caracteres.")
        String cep,
        Cidade cidade

) {
    public EnderecoResponseDTO(Endereco endereco){
        this(endereco.getId(), endereco.getRua(), endereco.getBairro(), endereco.getNumero(),
        endereco.getComplemento(), endereco.getCep(), endereco.getCidade()
        );
    }
}