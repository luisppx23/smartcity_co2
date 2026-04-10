package pt.upskill.smart_city_co2.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.PasswordResetToken;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.models.SignUpModel;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.MunicipioRepository;
import pt.upskill.smart_city_co2.repositories.PasswordResetTokenRepository;
import pt.upskill.smart_city_co2.repositories.UserRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CidadaoRepository cidadaoRepository;

    @Autowired
    MunicipioRepository municipioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordResetTokenRepository tokenRepository;

    @Autowired
    EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final SecureRandom secureRandom = new SecureRandom();

    public User register(SignUpModel signUpModel) {

        // Verificar email
        if (userRepository.findByEmail(signUpModel.getEmail()).isPresent()) {
            throw new RuntimeException("Email já está em uso");
        }

        // Verificar username
        if (userRepository.findByUsername(signUpModel.getUsername()).isPresent()) {
            throw new RuntimeException("Username já está em uso");
        }

        String encodedPassword = passwordEncoder.encode(signUpModel.getPassword());

        if ("cidadao".equals(signUpModel.getTipo())) {

            Municipio municipio = null;
            if (signUpModel.getMunicipioId() != null) {
                municipio = municipioRepository.findById(signUpModel.getMunicipioId())
                        .orElseThrow(() -> new RuntimeException("Município não encontrado"));
            }

            Cidadao cidadao = new Cidadao(
                    signUpModel.getFirstName(),
                    signUpModel.getLastName(),
                    signUpModel.getUsername(),
                    LocalDateTime.now(),
                    signUpModel.getEmail(),
                    encodedPassword,
                    signUpModel.getNif(),
                    signUpModel.getTipo(),
                    true,
                    signUpModel.getContacto(),
                    signUpModel.getMorada()
            );

            // Associar ao município
            cidadao.setMunicipio(municipio);

            // Salvar o cidadão
            Cidadao savedCidadao = cidadaoRepository.save(cidadao);

            // Adicionar à lista do município (se existir)
            if (municipio != null) {
                if (municipio.getListaDeCidadaos() == null) {
                    municipio.setListaDeCidadaos(new ArrayList<>());
                }
                municipio.getListaDeCidadaos().add(savedCidadao);
                municipioRepository.save(municipio);
            }

            return savedCidadao;
        }

        return null;
    }

    public User getUser(String username) {
        return userRepository.getUserByUsername(username);
    }

    public User validateLogin(String username, String password) {
        User user = getUser(username);
        if (user == null || user.getId() == null) {
            return null;
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        return user;
    }

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }
        String username = auth.getPrincipal().toString();
        return getUser(username);
    }

    public boolean verificarDadosRecuperacao(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email).isPresent();
    }

    @Transactional
    public String gerarCodigoRecuperacao(String username, String email) {
        User user = userRepository.findByUsernameAndEmail(username, email)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        // Remover tokens antigos do mesmo user
        PasswordResetToken existingToken = tokenRepository.findByUser(user).orElse(null);
        if (existingToken != null) {
            logger.info("Removendo token existente para user: {}", username);
            tokenRepository.delete(existingToken);
            tokenRepository.flush(); // Forçar delete imediato
        }

        // Gerar código aleatório de 6 dígitos
        String codigo = String.format("%06d", secureRandom.nextInt(1000000));

        logger.info("Gerando novo código para user: {} - Código: {}", username, codigo);

        // Criar novo token com expiração de 15 minutos
        PasswordResetToken resetToken = new PasswordResetToken(
                codigo,
                user,
                LocalDateTime.now().plusMinutes(15)
        );

        tokenRepository.save(resetToken);
        tokenRepository.flush(); // Forçar save imediato

        // Enviar email com o código
        try {
            emailService.enviarCodigoRecuperacao(user.getEmail(), codigo, user.getUsername());
            logger.info("Email enviado com sucesso para: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Erro ao enviar email: {}", e.getMessage());
            // Fallback: mostrar no console para debugging
            System.out.println("=========================================");
            System.out.println("CÓDIGO DE RECUPERAÇÃO PARA: " + username);
            System.out.println("CÓDIGO: " + codigo);
            System.out.println("EMAIL DO UTILIZADOR: " + user.getEmail());
            System.out.println("=========================================");
        }

        return codigo;
    }

    @Transactional
    public boolean validarCodigoRecuperacao(String username, String codigo) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        PasswordResetToken resetToken = tokenRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Nenhum pedido de recuperação encontrado. Solicite um novo código."));

        if (resetToken.isUsed()) {
            throw new RuntimeException("Este código já foi utilizado. Solicite um novo código.");
        }

        if (resetToken.isExpired()) {
            throw new RuntimeException("Código expirado. Solicite um novo código.");
        }

        if (!resetToken.getToken().equals(codigo)) {
            throw new RuntimeException("Código inválido. Verifique e tente novamente.");
        }

        return true;
    }

    @Transactional
    public void atualizarPasswordComCodigo(String username, String codigo, String novaPassword) {
        // Validar código antes de atualizar
        validarCodigoRecuperacao(username, codigo);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        // Atualizar password
        user.setPassword(passwordEncoder.encode(novaPassword));
        userRepository.save(user);

        // Marcar token como usado
        PasswordResetToken resetToken = tokenRepository.findByUser(user).orElse(null);
        if (resetToken != null) {
            resetToken.setUsed(true);
            tokenRepository.save(resetToken);
        }
    }

    // Method original mantido para compatibilidade (sem código de verificação)
    public void atualizarPassword(String username, String novaPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        user.setPassword(passwordEncoder.encode(novaPassword));
        userRepository.save(user);
    }
}