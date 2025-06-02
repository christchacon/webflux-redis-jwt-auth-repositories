package com.tutorial.tutorialwebflux.models;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
//@RegisterReflectionForBinding(BeneficiaryEntity.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@org.springframework.data.relational.core.mapping.Table(name = "tbl_beneficiaries", schema = "test")
public class BeneficiaryEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @org.springframework.data.annotation.Id
    @org.springframework.data.relational.core.mapping.Column(value="id")
    private Long id;

    @org.springframework.data.relational.core.mapping.Column(value="rut")
    private String rut;

    @org.springframework.data.relational.core.mapping.Column(value="names")
    private String names;

    @org.springframework.data.relational.core.mapping.Column(value="last_names")
    private String lastNames;

    @org.springframework.data.relational.core.mapping.Column(value="age")
    private int age;

    @org.springframework.data.relational.core.mapping.Column(value="bank")
    private String bank;

    @org.springframework.data.relational.core.mapping.Column(value="account_type")
    private String accountType;

    @org.springframework.data.relational.core.mapping.Column(value="account_number")
    private String accountNumber;

    @org.springframework.data.relational.core.mapping.Column(value="amount")
    private BigDecimal amount;

    public BeneficiaryEntity() {
    }
    @JsonProperty
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @JsonProperty
    public String getRut() {
        return rut;
    }
    public void setRut(String rut) {
        this.rut = rut;
    }
    @JsonProperty
    public String getNames() {
        return names;
    }
    public void setNames(String names) {
        this.names = names;
    }
    @JsonProperty
    public String getLastNames() {
        return lastNames;
    }
    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }
    @JsonProperty
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    @JsonProperty
    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }
    @JsonProperty
    public String getAccountType() {
        return accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    @JsonProperty
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    @JsonProperty
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal bigDecimal) {
        this.amount = bigDecimal;
    }
    @Override
    public String toString() {
        return "Beneficiary [rut=" + rut + ", names=" + names + ", lastNames=" + lastNames + ", age=" + age + ", bank="
                + bank + ", accountType=" + accountType + ", accountNumber=" + accountNumber + ", amount=" + amount
                + "]";
    }

}
