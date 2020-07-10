package com.natsuyami.project.nwa.service;

import com.natsuyami.project.nwa.common.constant.NwaContentType;
import com.natsuyami.project.nwa.common.dto.NwaTokenDto;
import com.natsuyami.project.nwa.common.encrypt.NwaContentEncyption;
import com.natsuyami.project.nwa.common.encrypt.NwaPasswordEncrypt;
import com.natsuyami.project.nwa.common.http.NwaRestTemplate;
import com.natsuyami.project.nwa.config.dto.NwaLoginCredentialDto;
import com.natsuyami.project.nwa.model.NwaUserModel;
import com.natsuyami.project.nwa.repository.NwaUserRepository;
import io.netty.util.internal.StringUtil;
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

  @Autowired
  private NwaRestTemplate nwaRestTemplate;

  @Autowired
  private NwaUserRepository nwaUserRepository;

  public NwaTokenDto login(NwaLoginCredentialDto credentialDto) throws Exception {
    LOGGER.info("Initialized login with credentials username={{}}, passcode={{}}", credentialDto.getUsername(), credentialDto.getPasscode());

    credentialDto.setUsername(NwaContentEncyption.decrypt(credentialDto.getUsername(), privateKey));
    credentialDto.setPasscode(NwaContentEncyption.decrypt(credentialDto.getPasscode(), privateKey));

    String[] hashVal = NwaPasswordEncrypt.originalEncryption("",
            credentialDto.getPasscode());

    NwaUserModel userModel = nwaUserRepository.findByUsername(credentialDto.getUsername());
    String password = NwaPasswordEncrypt.decrypt(hashVal[1], userModel.getPassphrase());

    if (StringUtil.isNullOrEmpty(password)) {
      throw new Exception("Incorrect Password and Passcode");
    }

    String kcPass = password.concat(".").concat(hashVal[1]);
    //incorrect validation of password, requires to get password from database to get secret/salt
    LOGGER.info("Create request data for clientId={{}}, clientSecret={{}}, username={{}}, password={{}}", clientId, clientSecret, credentialDto.getUsername(), kcPass);
    BodyInserters.FormInserter bodyParam = nwaRestTemplate.kcTokenCred(clientId, clientSecret, credentialDto.getUsername(), kcPass);

    NwaTokenDto token = nwaRestTemplate.post("http://localhost:8080/auth/realms/NWASpringBoot/protocol/openid-connect/token", bodyParam, NwaContentType.URL_ENCODED, null, NwaTokenDto.class);

    return token;
  }

  public String encryptData(String data) throws Exception {
    String encrpyted = NwaContentEncyption.encrypt(data, publicKey);
    return encrpyted;
  }
}
