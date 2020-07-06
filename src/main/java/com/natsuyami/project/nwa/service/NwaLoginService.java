package com.natsuyami.project.nwa.service;

import com.natsuyami.project.nwa.common.constant.NwaContentType;
import com.natsuyami.project.nwa.common.dto.NwaTokenDto;
import com.natsuyami.project.nwa.common.encrypt.NwaContentEncyption;
import com.natsuyami.project.nwa.common.encrypt.NwaPasswordEncrypt;
import com.natsuyami.project.nwa.common.http.NwaRestTemplate;
import com.natsuyami.project.nwa.config.dto.NwaLoginCredentialDto;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;

@Service
public class NwaLoginService {

  private static final Logger LOGGER = LoggerFactory.getLogger(NwaLoginService.class);

  @Value("${encryption.public.key}")
  private String publicKey;

  @Value("${encryption.private.key}")
  private String privateKey;

  @Value("${keycloak.resource}")
  private String clientId;

  @Value("${keycloak.credentials.secret}")
  private String clientSecret;

  private NwaRestTemplate nwaRestTemplate;

  public NwaTokenDto login(NwaLoginCredentialDto credentialDto) throws Exception {

    credentialDto.setUsername(NwaContentEncyption.decrypt(credentialDto.getUsername(), privateKey));
    credentialDto.setPasscode(NwaContentEncyption.decrypt(credentialDto.getPasscode(), privateKey));
    credentialDto.setPassword(NwaContentEncyption.decrypt(credentialDto.getPassword(), privateKey));

    String[] encryptedPass = NwaPasswordEncrypt.originalEncryption(credentialDto.getPassword(),
            credentialDto.getPasscode());
    BodyInserters.FormInserter bodyParam = nwaRestTemplate.createToken(clientId, clientSecret, credentialDto.getUsername(), encryptedPass[0].concat(".").concat(encryptedPass[1]));

    NwaTokenDto token = nwaRestTemplate.post("http://localhost:8080/auth/realms/NWASpringBoot/protocol/openid-connect/token", bodyParam, NwaContentType.URL_ENCODED, null, NwaTokenDto.class);

    return token;
  }

  public String encryptData(String data) throws Exception {
    String encrpyted = NwaContentEncyption.encrypt(data, publicKey);
    return encrpyted;
  }
}
