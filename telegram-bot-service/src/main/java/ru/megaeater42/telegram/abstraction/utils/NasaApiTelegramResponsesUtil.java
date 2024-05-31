package ru.megaeater42.telegram.abstraction.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.megaeater42.dto.ApodDTO;
import ru.megaeater42.telegram.abstraction.TelegramResponse;

import java.util.ArrayList;
import java.util.List;

public class NasaApiTelegramResponsesUtil {
    public static List<TelegramResponse> getRandomApodsTelegramResponse(List<ApodDTO.Response.Inner> apods, Long chatId) {
        List<TelegramResponse> answer = new ArrayList<>();
        for (ApodDTO.Response.Inner apod : apods) {
            if (apod.getMediaType().equals("image")) {
                if (apod.getUrl().endsWith("gif")) {
                    answer.add(
                            new TelegramResponse(
                                    SendAnimation.builder()
                                            .chatId(chatId)
                                            .animation(new InputFile(apod.getUrl()))
                                            .caption("*" + apod.getTitle() + "*" + "\n" + apod.getDate())
                                            .parseMode("Markdown")
                                            .build()
                            )
                    );
                } else {
                    answer.add(
                            new TelegramResponse(
                                    SendPhoto.builder()
                                            .chatId(chatId)
                                            .photo(new InputFile(apod.getUrl()))
                                            .caption("*" + apod.getTitle() + "*" + "\n" + apod.getDate())
                                            .parseMode("Markdown")
                                            .build()
                            )
                    );
                }
            } else {
                if (apod.getMediaType().equals("video")) {
                    answer.add(
                            new TelegramResponse(
                                    SendVideo.builder()
                                            .chatId(chatId)
                                            .video(new InputFile(apod.getThumbnailUrl()))
                                            .caption("*" + apod.getTitle() + "*" + "\n" + apod.getDate())
                                            .parseMode("Markdown")
                                            .build()
                            )
                    );
                }
            }
            answer.add(
                    new TelegramResponse(
                            SendMessage.builder()
                                    .chatId(chatId)
                                    .text(
                                            "Описание:\n" + apod.getExplanation() + "\n" +
                                                    ((apod.getCopyright() != null)? "Copyright: " + apod.getCopyright() : "") +
                                                    ((apod.getHdUrl() != null)? "\nHD-URL: " + apod.getHdUrl() : "")
                                    )
                                    .build()
                    )
            );
        }
        return answer;
    }
}
