package com.tutorial.tutorialwebflux.messages;

import java.io.Serializable;
import java.util.List;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tutorial.tutorialwebflux.models.BeneficiaryEntity;

@Component
//@RegisterReflectionForBinding(MessageBeneficiariesResponse.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageBeneficiariesResponse implements Serializable {
    
    private BeneficiaryEntity beneficiaryEntity;
    private String message;
	private String status;
	private List<BeneficiaryEntity> lBeneficiaryEntities;
    private Integer totalBatch;

    @JsonProperty
    public BeneficiaryEntity getBeneficiaryEntity() {
        return beneficiaryEntity;
    }

    public void setBeneficiaryEntity(BeneficiaryEntity beneficiaryEntity) {
        this.beneficiaryEntity = beneficiaryEntity;
    }
    
    @JsonProperty
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty
    public List<BeneficiaryEntity> getlBeneficiaryEntities() {
        return lBeneficiaryEntities;
    }

    public void setlBeneficiaryEntities(List<BeneficiaryEntity> lBeneficiaryEntities) {
        this.lBeneficiaryEntities = lBeneficiaryEntities;
    }

    @JsonProperty
    public Integer getTotalBatch() {
        return totalBatch;
    }

    public void setTotalBatch(Integer totalBatch) {
        this.totalBatch = totalBatch;
    }

}
