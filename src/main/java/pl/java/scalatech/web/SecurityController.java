package pl.java.scalatech.web;


import java.security.Principal;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SecurityController {

    @RequestMapping("secContext")
    public ResponseEntity<Map<String, Object>> secContext(Model model) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        model.addAttribute("principal", authentication.getName());

        return new ResponseEntity<>(model.asMap(), HttpStatus.OK);

    }

    @RequestMapping("principal")
    public ResponseEntity<String> principal(Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("++++    {}", auth.getAuthorities());
        return new ResponseEntity<>(principal.getName(), HttpStatus.OK);

    }
}
