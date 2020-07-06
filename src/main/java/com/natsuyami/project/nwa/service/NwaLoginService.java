package com.natsuyami.project.nwa.service;

import com.natsuyami.project.nwa.common.constant.NwaContentType;
import com.natsuyami.project.nwa.common.dto.NwaTokenDto;
import com.natsuyami.project.nwa.common.encrypt.NwaContentEncyption;
import com.natsuyami.project.nwa.common.http.NwaRestTemplate;
import com.natsuyami.project.nwa.config.dto.NwaLoginCredentialDto;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.BodyInserters;

public class NwaLoginService {

  private static final Logger LOGGER = LoggerFactory.getLogger(NwaLoginService.class);

  @Value("${encryption.public.key}")
  private String publicKey;

  @Autowired
  private NwaRestTemplate nwaRestTemplate;

  public NwaTokenDto login(NwaLoginCredentialDto credentialDto) {

    BodyInserters.FormInserter bodyParam = nwaRestTemplate.createToken("nwa-signup-app", "ed0c6e08-9b3b-4f73-8be4-2505ad72059d", "user", "User!23");

    NwaTokenDto token = nwaRestTemplate.post("http://localhost:8080/auth/realms/NWASpringBoot/protocol/openid-connect/token", bodyParam, NwaContentType.URL_ENCODED, null, NwaTokenDto.class);

    return token;
  }

  public String encryptData(String data) throws Exception {
    String encrpyted = Base64.getDecoder().encode(NwaContentEncyption.encrypt(data, publicKey));
    return encrpyted;
  }
}
