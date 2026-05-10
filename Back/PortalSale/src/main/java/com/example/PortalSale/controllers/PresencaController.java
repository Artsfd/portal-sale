package com.example.PortalSale.controllers;

import com.example.PortalSale.dto.ValidacaoCodigoRequest;
import com.example.PortalSale.models.PresencaEvento;
import com.example.PortalSale.security.ApplicationUserDetails;
import com.example.PortalSale.services.PresencaService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/presenca")
@CrossOrigin(origins = "*")
public class PresencaController {

    private final PresencaService presencaService;

    public PresencaController(PresencaService presencaService) {
        this.presencaService = presencaService;
    }

    @PostMapping("/checkin/solicitar-codigo/{eventoId}")
    public ResponseEntity<?> solicitarCodigoCheckin(@PathVariable Long eventoId,
                                                    @AuthenticationPrincipal ApplicationUserDetails userDetails,
                                                    @RequestBody(required = false) ValidacaoCodigoRequest requestBody,
                                                    HttpServletRequest request) {
        String email = requestBody != null ? requestBody.getEmail() : null;
        if (userDetails == null && (email == null || email.isBlank())) {
            return ResponseEntity.status(401).body("Não autorizado. Informe o e-mail cadastrado para solicitar o código.");
        }

        String codigo = presencaService.solicitarCodigoCheckin(eventoId,
                userDetails != null ? userDetails.getId() : null,
                email,
                request.getRemoteAddr(),
                request.getHeader("User-Agent"));

        return ResponseEntity.ok(java.util.Map.of("codigo", codigo));
    }

    @PostMapping("/checkin/validar")
    public ResponseEntity<?> validarCheckin(@AuthenticationPrincipal ApplicationUserDetails userDetails,
                                            @RequestBody ValidacaoCodigoRequest request,
                                            HttpServletRequest httpRequest) {
        if (userDetails == null && (request.getEmail() == null || request.getEmail().isBlank())) {
            return ResponseEntity.status(401).body("Não autorizado. Informe o e-mail cadastrado para validar o código.");
        }
        try {
            PresencaEvento presenca = presencaService.validarCheckin(
                    request.getEventoId(),
                    userDetails != null ? userDetails.getId() : null,
                    request.getEmail(),
                    request.getCodigo(),
                    request.getLatitude(),
                    request.getLongitude(),
                    httpRequest.getRemoteAddr(),
                    httpRequest.getHeader("User-Agent")
            );
            return ResponseEntity.ok(presenca);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(java.util.Map.of("erro", ex.getMessage()));
        }
    }

    @PostMapping("/checkout/solicitar-codigo/{eventoId}")
    public ResponseEntity<?> solicitarCodigoCheckout(@PathVariable Long eventoId,
                                                     @AuthenticationPrincipal ApplicationUserDetails userDetails,
                                                     HttpServletRequest request) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Não autorizado");
        }

        String codigo = presencaService.solicitarCodigoCheckout(eventoId, userDetails.getId(), null, request.getRemoteAddr(), request.getHeader("User-Agent"));
        return ResponseEntity.ok(java.util.Map.of("codigo", codigo));
    }

    @PostMapping("/checkout/validar")
    public ResponseEntity<?> validarCheckout(@AuthenticationPrincipal ApplicationUserDetails userDetails,
                                             @RequestBody ValidacaoCodigoRequest request,
                                             HttpServletRequest httpRequest) {
        if (userDetails == null) {
            return ResponseEntity.status(481).body("Não autorizado");
        }
        try {
            PresencaEvento presenca = presencaService.validarCheckout(
                    request.getEventoId(),
                    userDetails.getId(),
                    null,
                    request.getCodigo(),
                    request.getLatitude(),
                    request.getLongitude(),
                    httpRequest.getRemoteAddr(),
                    httpRequest.getHeader("User-Agent")
            );
            return ResponseEntity.ok(presenca);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(java.util.Map.of("erro", ex.getMessage()));
        }
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<PresencaEvento>> listarPresentes(@PathVariable Long eventoId) {
        return ResponseEntity.ok(presencaService.listarPresencas(eventoId));
    }
}
