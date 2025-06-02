package com.tutorial.tutorialwebflux.messages;

import java.io.Serializable;
import java.util.List;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
//@RegisterReflectionForBinding(MessageFileResponse.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageFileResponse implements Serializable {
    
    private String message;
	private String status;
	private List<String> lines;
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
    public List<String> getLines() {
        return lines;
    }
    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}
