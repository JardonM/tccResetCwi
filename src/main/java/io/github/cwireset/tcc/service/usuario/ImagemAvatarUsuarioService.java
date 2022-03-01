package io.github.cwireset.tcc.service.usuario;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
@FeignClient(name = "usuarios", url = "https://some-random-api.ml/img/dog")
public interface ImagemAvatarUsuarioService {

    @RequestMapping(method = RequestMethod.GET)
    String getImagemAvatar();
}
