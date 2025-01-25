package br.com.lucanicoleti.todolist.user;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

//Modificadores
//public: A classe pode ser acessada de qualquer outro pacote
//private: A classe só pode ser acessada dentro da própria classe
//protected: A classe só pode ser acessada dentro do mesmo pacote ou por subclasses
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    // String: Texto
    // Integer: Número inteiro
    // Double: Número decimal
    // Float: Número decimal
    // char: Caractere
    // date: Data
    // void: Vazio
    // As informacoes iram vir dentro do body da requisicao
    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null) {
            // Mensagem de erro
            // Status code 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
        }

        var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);
    }
}
