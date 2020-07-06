package com.natsuyami.project.nwa.controller;

import com.natsuyami.project.nwa.config.dto.NwaLoginCredentialDto;
import com.natsuyami.project.nwa.service.NwaLoginService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/login", produces = "application/json")
@RestController
public class NwaLoginController {
  private static final Logger LOGGER = LoggerFactory.getLogger(NwaLoginController.class);

  @Autowired
  NwaLoginService nwaLoginService;

  @PostMapping
  @ApiOperation(value = "encrypt details, ecrpytion mode used in contents", response = String.class)
  public Object login(@RequestBody NwaLoginCredentialDto credentialDto) throws Exception {
    LOGGER.info("Initialized login controller credentialDto={{}}", credentialDto);

    return nwaLoginService.login(credentialDto);
  }

  @PostMapping("/encrypt")
  @ApiOperation(value = "encrypt details, ecrpytion mode used in contents", response = String.class)
  public Object encrypt(@RequestParam String data) throws Exception {
    LOGGER.info("Initialized encrypt controller data={{}}", data);

    return nwaLoginService.encryptData(data);
  }
}
