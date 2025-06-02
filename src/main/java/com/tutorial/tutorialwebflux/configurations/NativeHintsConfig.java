package com.tutorial.tutorialwebflux.configurations;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import com.tutorial.tutorialwebflux.messages.MessageBeneficiariesResponse;
import com.tutorial.tutorialwebflux.messages.MessageContactResponse;
import com.tutorial.tutorialwebflux.messages.MessageFileResponse;
import com.tutorial.tutorialwebflux.models.BeneficiaryEntity;
import com.tutorial.tutorialwebflux.models.BussinessFormatConfigEntity;
import com.tutorial.tutorialwebflux.models.ContactEntity;

@RegisterReflectionForBinding({
    MessageBeneficiariesResponse.class,
    MessageContactResponse.class,
    MessageFileResponse.class,
    BeneficiaryEntity.class,
    BussinessFormatConfigEntity.class,
    ContactEntity.class
})
public class NativeHintsConfig {
}
