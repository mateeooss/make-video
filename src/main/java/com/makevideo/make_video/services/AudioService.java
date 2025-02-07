package com.makevideo.make_video.services;

import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import com.makevideo.make_video.models.google.TextToSpeech;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class AudioService {
    @Value("${google.apikey}")
    private String googleApiKey;

    public void textToSpeech(TextToSpeech textToSpeech){
        // Caminho para o arquivo credentials.json
        String credentialsPath = "src/main/resources/googleCredentials/snappy-photon-447902-c6-f933c797ef90.json"; // Caminho relativo ou absoluto

        // Variável efetivamente final para as credenciais
        final GoogleCredentials credentials;
        TextToSpeechSettings settings;
        try {
            credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
            // Configura o cliente do Text-to-Speech
            settings = TextToSpeechSettings.newBuilder()
                    .setCredentialsProvider(() -> credentials) // Agora a variável é efetivamente final
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar credenciais", e);
        }

        // Instancia o cliente
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
            // Define o texto de entrada

            SynthesisInput input =
                textToSpeech.SSML() ? SynthesisInput.newBuilder().setSsml(textToSpeech.text()).build()
                                    : SynthesisInput.newBuilder().setText(textToSpeech.text()).build();


            // Configura a voz
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(textToSpeech.languageCode())
                    .setName(textToSpeech.modelName()) // Escolha a voz desejada
                    .build();

            // Configura o áudio
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3) // Formato do áudio
                    .setSpeakingRate(textToSpeech.voiceSpeed())
                    .setPitch(textToSpeech.voicePitch())
                    .build();

            // Faz a requisição
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Salva o áudio em um arquivo
            ByteString audioContents = response.getAudioContent();
            try (FileOutputStream out = new FileOutputStream("output.mp3")) {
                out.write(audioContents.toByteArray());
                System.out.println("Áudio salvo em output.mp3");
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao sintetizar fala", e);
        }
    }
}