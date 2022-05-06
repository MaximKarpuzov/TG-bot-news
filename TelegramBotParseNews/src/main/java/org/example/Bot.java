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
        //������� ������ ������� ���������� � ���������� ������ ���������
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true); //��������� ������
        replyKeyboardMarkup.setOneTimeKeyboard(false); //�������� ����� �������������

        //������� ������ � ������ ������
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        //������� ���� ��� ������ � ��������� ��� � ������
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        //��������� ���� ������ � ������� "��������" ��� ���
        keyboardRow.add(new KeyboardButton("����� ��������� �������"));
        keyboardRow.add(new KeyboardButton("������� ����"));
        //��������� ���� � ����� ����� ������ � ������� ������
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(message != null && message.hasText()){
            switch (message.getText()){
                case "/help":
                    sendMsg(message, "�������: \n������� ����\n����� ��������� �������");
                    break;
                case "������� ����":
                    Date current = new Date();
                    sendMsg(message, current.toString());
                    break;
                case "����� ��������� �������":
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
                    sendMsg(message, "������� ���������� ������� /help ");
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