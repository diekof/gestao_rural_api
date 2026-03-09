package com.seuprojeto.agro.crop.mapper;

import com.seuprojeto.agro.crop.domain.Crop;
import com.seuprojeto.agro.crop.dto.CropCreateRequest;
import com.seuprojeto.agro.crop.dto.CropResponse;
import com.seuprojeto.agro.crop.dto.CropUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class CropMapper {

    public Crop toEntity(CropCreateRequest request) {
        Crop crop = new Crop();
        apply(crop, request.nome(), request.categoria(), request.cicloMedioDias(), request.produtividadeEsperada(),
                request.unidadeProdutividade(), request.descricao());
        return crop;
    }

    public void updateEntity(Crop crop, CropUpdateRequest request) {
        apply(crop, request.nome(), request.categoria(), request.cicloMedioDias(), request.produtividadeEsperada(),
                request.unidadeProdutividade(), request.descricao());
    }

    private void apply(Crop crop, String nome, String categoria, Integer cicloMedioDias, java.math.BigDecimal produtividadeEsperada,
                       String unidadeProdutividade, String descricao) {
        crop.setNome(nome);
        crop.setCategoria(categoria);
        crop.setCicloMedioDias(cicloMedioDias);
        crop.setProdutividadeEsperada(produtividadeEsperada);
        crop.setUnidadeProdutividade(unidadeProdutividade);
        crop.setDescricao(descricao);
    }

    public CropResponse toResponse(Crop crop) {
        return new CropResponse(crop.getId(), crop.getTenantId(), crop.getNome(), crop.getCategoria(), crop.getCicloMedioDias(),
                crop.getProdutividadeEsperada(), crop.getUnidadeProdutividade(), crop.getDescricao(), crop.getStatus(),
                crop.getCreatedAt(), crop.getUpdatedAt(), crop.getCreatedBy(), crop.getUpdatedBy());
    }
}
