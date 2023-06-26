package com.example.serverside.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.tcp.SslProvider;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Slf4j
@Component
public class sererConf {
    public SslContext buildSslContextForReactorClientHttpConnector() {
        SslContext sslContext = null;

        KeyStore keyStore;

        try{

            keyStore = KeyStore.getInstance("jks");
            ClassPathResource classPathResource = new ClassPathResource("server.jks");
            InputStream inputStream = classPathResource.getInputStream();
            keyStore.load(inputStream,"password".toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "password".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            sslContext = SslContextBuilder.forClient()
                    .keyManager(keyManagerFactory)
                    .trustManager(trustManagerFactory)
                    .build();


        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException e) {
            log.error("Exception while building SSL context for reactor web client: ",e);
        }
        return sslContext;
    }


    @Bean
    public WebClient WebClient() {
        SslProvider sslProvider = SslProvider.builder()
                .sslContext(buildSslContextForReactorClientHttpConnector()).build();

        reactor.netty.http.client.HttpClient httpClient = reactor.netty.http.client.HttpClient.create()
                .secure(sslProvider);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();

    }
}
