package org.example;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public void sendMsg(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            initKeyboard(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void initKeyboard(SendMessage sendMessage){
        //Создаем объект будущей клавиатуры и выставляем нужные настройки
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true); //подгоняем размер
        replyKeyboardMarkup.setOneTimeKeyboard(false); //скрываем после использования

        //Создаем список с рядами кнопок
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        //Создаем один ряд кнопок и добавляем его в список
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        //Добавляем одну кнопку с текстом "Просвяти" наш ряд
        keyboardRow.add(new KeyboardButton("Вывод последней новости"));
        keyboardRow.add(new KeyboardButton("Текущая дата"));
        //добавляем лист с одним рядом кнопок в главный объект
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(message != null && message.hasText()){
            switch (message.getText()){
                case "/help":
                    sendMsg(message, "Команды: \nТекущая дата\nВывод последней новости");
                    break;
                case "Текущая дата":
                    Date current = new Date();
                    sendMsg(message, current.toString());
                    break;
                case "Вывод последней новости":
                    List<Article> artlist = new ArrayList<>();
                    List<Article> list = null;
                    try {
                        list = NewsParser.artList(artlist);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    sendMsg(message, list.get(0).toString());
                    break;
                default:
                    sendMsg(message, "Введите правильную команду /help ");
            }
        }
    }


    @Override
    public String getBotUsername() {
        return "ParseNewsTestBot";
    }

    @Override
    public String getBotToken() {
        return "5393391293:AAHQ7YSoAcQq7Y1NtI4j-wbBm39dX0CtcCk";
    }
}